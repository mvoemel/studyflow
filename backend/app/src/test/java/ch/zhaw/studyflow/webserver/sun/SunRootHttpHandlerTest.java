package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContentFactory;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

class SunRootHttpHandlerTest {
    /**
     * If the SunRootHttpHandler encounters an unknown MIME type, it should return a 415 Unsupported Media Type response
     * instead of throwing an exception.
     */
    @Test
    void testUnknownMediaType() {
        ReadableBodyContentFactory factory = Mockito.mock(ReadableBodyContentFactory.class);
        Mockito.when(factory.create(any(), any(), any())).thenThrow(new IllegalArgumentException());

        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        Mockito.when(serviceCollection.getRequiredService(ReadableBodyContentFactory.class))
                .thenReturn(factory);
        RouteTrie routeTrie = Mockito.mock(RouteTrie.class);
        Mockito.when(routeTrie.retrieve(any(), any()))
                .thenReturn(Optional.of(new Tuple<>(null, List.of())));

        HttpExchange exchange = Mockito.mock(HttpExchange.class);
        Mockito.when(exchange.getRequestMethod()).thenReturn("POST");
        Mockito.when(exchange.getRequestHeaders()).thenReturn(Mockito.mock(com.sun.net.httpserver.Headers.class));
        Mockito.when(exchange.getRequestURI()).thenReturn(URI.create("http://test.ch/user/current/1"));
        SunRootHttpHandler handler = new SunRootHttpHandler(serviceCollection, routeTrie, null);
        Assertions.assertDoesNotThrow(() -> handler.handle(exchange));
    }
}
