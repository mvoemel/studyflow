package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContentFactory;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SunRootHttpHandlerTest {
    /**
     * If the SunRootHttpHandler encounters an unknown MIME type, it should return a 415 Unsupported Media Type response
     * instead of throwing an exception.
     */
    @Test
    void testUnknownMediaType() {
        ReadableBodyContentFactory factory = mock(ReadableBodyContentFactory.class);
        when(factory.create(any(), any(), any())).thenThrow(new IllegalArgumentException());

        ServiceCollection serviceCollection = mock(ServiceCollection.class);
        when(serviceCollection.getRequiredService(ReadableBodyContentFactory.class))
                .thenReturn(factory);
        RouteTrie routeTrie = mock(RouteTrie.class);
        when(routeTrie.retrieve(any(), any()))
                .thenReturn(Optional.of(new Tuple<>(null, List.of())));

        HttpExchange exchange = mock(HttpExchange.class);

        when(exchange.getRequestMethod()).thenReturn("POST");
        Headers headers = mock(Headers.class);
        when(headers.get("Content-Type")).thenReturn(List.of("text/aac"));
        when(exchange.getRequestHeaders()).thenReturn(headers);
        when(exchange.getRequestURI()).thenReturn(URI.create("http://test.ch/user/current/1"));
        SunRootHttpHandler handler = new SunRootHttpHandler(serviceCollection, routeTrie, null);
        assertDoesNotThrow(() -> handler.handle(exchange));
        ArgumentCaptor<Integer> responseCodeCaptor = ArgumentCaptor.forClass(Integer.class);
        try {
            verify(exchange, atLeastOnce()).sendResponseHeaders(responseCodeCaptor.capture(), anyLong());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(415, responseCodeCaptor.getValue());
    }
}
