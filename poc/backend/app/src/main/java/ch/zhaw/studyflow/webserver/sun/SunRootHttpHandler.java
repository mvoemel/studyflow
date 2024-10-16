package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.controllers.routing.SegmentType;
import ch.zhaw.studyflow.webserver.http.*;
import ch.zhaw.studyflow.webserver.http.contents.BodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class SunRootHttpHandler implements HttpHandler {
    private final RouteTrie routeTrie;
    private final RequestProcessor invoker;


    public SunRootHttpHandler(RouteTrie routeTrie, RequestProcessor endpointInvoker) {
        this.routeTrie = routeTrie;
        this.invoker = endpointInvoker;
    }


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final Optional<Tuple<EndpointMetadata, List<String>>> possibleRoutingResult = routeTrie.retrieve(
                HttpMethod.valueOf(exchange.getRequestMethod()),
                List.of(exchange.getRequestURI().getPath().split("/"))
        );

        if (possibleRoutingResult.isPresent()) {
            handleRoutedRequest(exchange, possibleRoutingResult.get());
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), -1);
        }
        exchange.close();
    }

    private void handleRoutedRequest(final HttpExchange exchange, final Tuple<EndpointMetadata, List<String>> routingResult) throws IOException {
        SunHttpRequest request = SunHttpRequest.fromExchange(exchange);
        RequestContext context = new SunHttpRequestContext(
                routingResult.value1(),
                request,
                buildCaptureContainer(routingResult.value1(), routingResult.value2()));

        invoker.process(context);
        HttpResponse response = context.getResponse();

        BodyContent content = response.getResponseContent();
        final long responseLength = content == null ? -1 : content.getContentLength();
        final boolean sendContent = responseLength != -1;

        if (sendContent) {
            final String contentTypeHeaderValue = content.getContentHeader();
            if (contentTypeHeaderValue != null) {
                exchange.getResponseHeaders().add("Content-Type", contentTypeHeaderValue);
            }
        }

        for (var cookie : response.getCookies().asCollection()) {
            exchange.getResponseHeaders().add("Set-Cookie", cookie.toHeaderFormat());
        }

        // From here onwards, headers must not be modified!
        exchange.sendResponseHeaders(response.getStatusCode().getCode(), responseLength);
        if (sendContent) {
            response.getResponseContent().writeTo(context.getResponse(), exchange.getResponseBody());
        }
    }

    private static CaptureContainer buildCaptureContainer(EndpointMetadata endpointMetadata, List<String> captureValues) {
        List<RouteSegment> captureSegments = endpointMetadata.route().segments().stream()
                .filter(e -> e.is(SegmentType.CAPTURE))
                .toList();
        List<Tuple<String, String>> tuples = new ArrayList<>(captureValues.size());
        for (int i = 0; i < captureSegments.size(); i++) {
            tuples.add(new Tuple<>(captureSegments.get(i).value(), captureValues.get(i)));
        }
        return new TupleListCaptureContainer(tuples);
    }
}
