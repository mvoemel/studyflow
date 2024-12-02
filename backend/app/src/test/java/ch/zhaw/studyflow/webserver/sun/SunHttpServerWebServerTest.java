package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.controllers.ControllerRegistry;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestProcessor;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

class SunHttpServerWebServerTest {

    private InetSocketAddress address;
    private ServiceCollection serviceCollection;
    private ControllerRegistry controllerRegistry;
    private RouteTrie routeTrie;
    private RequestProcessor invoker;
    private SunHttpServerWebServer webServer;
    private HttpServer httpServer;

    @BeforeEach
    void setUp() throws Exception {
        address = new InetSocketAddress(8080);
        serviceCollection = mock(ServiceCollection.class);
        controllerRegistry = mock(ControllerRegistry.class);
        routeTrie = mock(RouteTrie.class);
        invoker = mock(RequestProcessor.class);
        httpServer = mock(HttpServer.class);

        webServer = new SunHttpServerWebServer(address, serviceCollection, controllerRegistry, routeTrie, invoker);
    }

    @Test
    void testStart() throws Exception {
        try (MockedStatic<HttpServer> mockedHttpServer = mockStatic(HttpServer.class)) {
            mockedHttpServer.when(() -> HttpServer.create(address, 0)).thenReturn(httpServer);

            webServer.start();

            verify(httpServer).createContext(eq("/"), any(SunRootHttpHandler.class));
            verify(httpServer).setExecutor(any());
            verify(httpServer).start();
        }
    }

}