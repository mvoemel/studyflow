package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.BodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.HttpRequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
            final Tuple<EndpointMetadata, List<String>> result = possibleRoutingResult.get();
            SunHttpRequest request = SunHttpRequest.fromExchange(exchange);
            RequestContext context = new HttpRequestContext(result.value1(), request);

            invoker.process(context);
            HttpResponse response = context.getResponse();

            BodyContent content = response.getResponseContent();
            final long responseLength = content != null ? content.getContentLength() : -1;
            final boolean sendContent = responseLength != -1;

            if (sendContent) {
                exchange.getResponseHeaders().add("Content-Type", content.getMimeType());
            }

            // From here onwards, headers must not be modified!
            exchange.sendResponseHeaders(response.getStatusCode().getCode(), responseLength);
            if (sendContent) {
                response.getResponseContent().writeTo(context.getResponse(), exchange.getResponseBody());
            }
        } else {
            exchange.sendResponseHeaders(HttpStatusCode.NOT_FOUND.getCode(), -1);
        }
        exchange.close();
    }
}
