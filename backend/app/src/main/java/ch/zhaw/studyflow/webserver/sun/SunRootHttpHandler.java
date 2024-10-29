package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.controllers.routing.SegmentType;
import ch.zhaw.studyflow.webserver.http.*;
import ch.zhaw.studyflow.webserver.http.contents.BodyContent;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContentFactory;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.http.cookies.HashMapCookieContainer;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The SunRootHttpHandler is an implementation of the HttpHandler interface provided by the Sun HTTP server.
 * It is responsible for handling incoming HTTP requests and routing them to the appropriate endpoint.
 */
public class SunRootHttpHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(SunRootHttpHandler.class.getName());

    private final ServiceCollection serviceCollection;
    private final ReadableBodyContentFactory bodyContentRegistry;
    private final RouteTrie routeTrie;
    private final RequestProcessor invoker;


    public SunRootHttpHandler(ServiceCollection serviceCollection, RouteTrie routeTrie, RequestProcessor endpointInvoker) {
        this.serviceCollection      = serviceCollection;
        this.bodyContentRegistry    = serviceCollection.getRequiredService(ReadableBodyContentFactory.class);
        this.routeTrie              = routeTrie;
        this.invoker                = endpointInvoker;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Optional<Tuple<EndpointMetadata, List<String>>> possibleRoutingResult = routeTrie.retrieve(
                HttpMethod.valueOf(exchange.getRequestMethod()),
                List.of(exchange.getRequestURI().getPath().split("/"))
        );

        try {
            if (possibleRoutingResult.isPresent()) {
                handleRoutedRequest(exchange, possibleRoutingResult.get());
            } else {
                exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), -1);
            }
        } catch (Exception e) {
            /*
             * This 'global' is the final fail-safe for any exception that might have occurred during the processing
             * and has not been caught by the individual components.
             * In the case of an exception, the server will respond with a 500 Internal Server Error and log the exception.
             */
            LOGGER.log(
                    Level.SEVERE,
                    "An exception occurred while processing a request to '%s'"
                            .formatted(exchange.getRequestURI()),
                    e
            );
            exchange.sendResponseHeaders(HttpStatusCode.INTERNAL_SERVER_ERROR.getCode(), -1);
        } finally {
            exchange.close();
        }
    }

    /**
     * Handles a routed request by constructing the request context and request processing pipeline.
     *
     * @param exchange      The HTTP exchange in question.
     * @param routingResult The routing result containing the endpoint metadata and the captured values.
     * @throws IOException If an I/O error occurs.
     */
    private void handleRoutedRequest(final HttpExchange exchange, final Tuple<EndpointMetadata, List<String>> routingResult) throws IOException {
        final SunHttpRequest request = new SunHttpRequest(
                exchange,
                createRequestBody(exchange),
                createCookieContainer(exchange)
        );

        final RequestContext context = new SunHttpRequestContext(
                routingResult.value1(),
                request,
                buildCaptureContainer(routingResult.value1(), routingResult.value2())
        );

        final HttpResponse response = invoker.process(context);

        BodyContent content = response.getResponseBody();
        final long responseLength = content == null ? -1 : content.getContentLength();
        final boolean sendContent = responseLength != -1;

        if (sendContent) {
            final String contentTypeHeaderValue = content.getContentTypeHeader();
            if (contentTypeHeaderValue != null) {
                exchange.getResponseHeaders().add("Content-Type", contentTypeHeaderValue);
            }
        }

        for (var cookie : response.getCookies().asCollection()) {
            exchange.getResponseHeaders().add("Set-Cookie", cookie.toHeaderFormat());
        }

        // From here onwards, headers MUST NOT be modified!
        // With the invocation of sendResponseHeaders, the response headers are written to the output stream and
        // the transmission of the response body can begin.
        exchange.sendResponseHeaders(response.getStatusCode().getCode(), responseLength);
        if (sendContent) {
            response.getResponseBody().write(exchange.getResponseBody());
        }
    }

    /**
     * Creates a ReadableBodyContent instance from the request body based on the Content-Type header.
     *
     * @param exchange The HTTP exchange.
     * @return The ReadableBodyContent instance or null if no request body is present.
     */
    private ReadableBodyContent createRequestBody(HttpExchange exchange) {
        List<String> strings = exchange.getRequestHeaders().get("Content-Type");
        if (strings == null || strings.isEmpty()) {
            return null;
        }

        Tuple<String, Map<String, String>> contentType = parseContentType(strings.getFirst());
        return bodyContentRegistry.create(
                contentType.value1(),
                contentType.value2(),
                exchange.getRequestBody()
        );
    }

    /**
     * Builds a CaptureContainer from the given capture values and the endpoint metadata.
     *
     * @param endpointMetadata The endpoint metadata.
     * @param captureValues    The capture values.
     * @return The CaptureContainer.
     */
    private CaptureContainer buildCaptureContainer(EndpointMetadata endpointMetadata, List<String> captureValues) {
        List<RouteSegment> captureSegments = endpointMetadata.route().segments().stream()
                .filter(e -> e.is(SegmentType.CAPTURE))
                .toList();
        Map<String, String> entries = new HashMap<>();
        for (int i = 0; i < captureSegments.size(); i++) {
            entries.put(captureSegments.get(i).value(), captureValues.get(i));
        }
        return new MapCaptureContainer(entries);
    }

    /**
     * Parses a content type string into a type and a map of properties.
     * @param contentType The content type string.
     * @return A tuple containing the type and the properties.
     */
    private Tuple<String, Map<String, String>> parseContentType(String contentType) {
        Objects.requireNonNull(contentType);

        String[] parts = contentType.split(";");
        String type = parts[0];
        Map<String, String> properties = new HashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String[] property = parts[i].split("=");
            properties.put(property[0].trim(), property[1].trim());
        }
        return new Tuple<>(type, properties);
    }

    /**
     * Creates a cookie container from the cookies present in the request.
     * @param exchange The HTTP exchange to create the cookie container for.
     * @return The cookie container.
     */
    private CookieContainer createCookieContainer(HttpExchange exchange) {
        HashMapCookieContainer cookieContainer = new HashMapCookieContainer();
        List<String> rawCookies = exchange.getRequestHeaders().get("Cookie");
        if (rawCookies != null) {
            for (String rawCookie : rawCookies) {
                Optional<Cookie> cookie = Cookie.readFromHeader(rawCookie);
                cookie.ifPresent(cookieContainer::set);
            }
        }
        return cookieContainer;
    }
}
