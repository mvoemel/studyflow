package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteSegment;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.http.pipeline.HttpRequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class SunRootHttpHandler implements HttpHandler {
    private RouteTrie routeTrie;
    private RequestProcessor invoker;


    public SunRootHttpHandler(RouteTrie routeTrie, RequestProcessor endpointInvoker) {
        this.routeTrie  = routeTrie;
        this.invoker    = endpointInvoker;
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
            exchange.sendResponseHeaders(exchange.getResponseCode(), 0);
            exchange.close();
        }

    }
}
