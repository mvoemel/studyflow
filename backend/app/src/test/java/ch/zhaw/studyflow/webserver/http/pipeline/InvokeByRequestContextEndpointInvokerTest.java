package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.controllers.ControllerFactory;
import ch.zhaw.studyflow.webserver.controllers.ControllerMetadata;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InvokeByRequestContextEndpointInvokerTest {

    private ServiceCollection serviceCollection;
    private InvokeByRequestContextEndpointInvoker invoker;
    private RequestContext context;
    private EndpointMetadata endpointMetadata;
    private HttpRequest request;
    private HttpResponse response;
    private ControllerFactory controllerFactory;
    private ControllerMetadata controllerMetadata;

    @BeforeEach
    void setUp() {
        serviceCollection = mock(ServiceCollection.class);
        invoker = new InvokeByRequestContextEndpointInvoker(serviceCollection);
        context = mock(RequestContext.class);
        endpointMetadata = mock(EndpointMetadata.class);
        request = mock(HttpRequest.class);
        response = mock(HttpResponse.class);
        controllerFactory = mock(ControllerFactory.class);
        controllerMetadata = mock(ControllerMetadata.class);

        when(context.getTarget()).thenReturn(endpointMetadata);
        when(context.getRequest()).thenReturn(request);
        when(request.createResponse()).thenReturn(response);
        when(endpointMetadata.controller()).thenReturn(controllerMetadata);
        when(controllerMetadata.factory()).thenReturn(controllerFactory);
    }

    @Test
    void testProcessSuccess() throws Exception {
        MockController controller = new MockController();
        Method endpointMethod = MockController.class.getMethod("handleRequest", RequestContext.class);
        when(controllerFactory.create(serviceCollection)).thenReturn(controller);
        when(endpointMetadata.endpoint()).thenReturn(endpointMethod);
        when(endpointMethod.invoke(controller, context)).thenReturn(response);

        HttpResponse result = invoker.process(context);

        assertEquals(response, result);
    }


    // TODO: Add tests for negative scenarios

    static class MockController {
        public HttpResponse handleRequest(RequestContext context) {
            return context.getRequest().createResponse();
        }

        public HttpResponse handleRequestWithInvocationTargetException(RequestContext context) throws InvocationTargetException {
            throw new InvocationTargetException(new RuntimeException("Invocation target exception"));
        }

        public HttpResponse handleRequestWithIllegalAccessException(RequestContext context) throws IllegalAccessException {
            throw new IllegalAccessException("Illegal access exception");
        }
    }
}