package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.services.ServiceCollection;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Invokes an endpoint by using the request context.
 * This class acts as a terminal of a request processing pipeline and invokes the endpoint specified in the request context.
 */
public class InvokeByRequestContextEndpointInvoker implements RequestProcessor {
    private static final Logger LOGGER = Logger.getLogger(InvokeByRequestContextEndpointInvoker.class.getName());

    private final ServiceCollection serviceCollection;


    public InvokeByRequestContextEndpointInvoker(ServiceCollection serviceCollection) {
        this.serviceCollection = serviceCollection;
    }


    @Override
    public HttpResponse process(RequestContext context) {
        HttpResponse response = null;

        final EndpointMetadata target = context.getTarget();
        try {
            Object controller = target.controller().factory().create(serviceCollection);
            response = (HttpResponse) target.endpoint().invoke(controller, context);
        } catch (InvocationTargetException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "An exception occurred while invoking the endpoint '%s'".formatted(target.endpoint().getName()),
                    e
            );
        } catch (IllegalAccessException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "The endpoint '%s' could not be accessed".formatted(target.endpoint().getName()),
                    e
            );
        } finally {
            if (response == null) {
                response = createInternalServerErrorResponse(context.getRequest());
            }
        }
        return response;
    }

    private static HttpResponse createInternalServerErrorResponse(HttpRequest request) {
        return request.createResponse()
                .setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
}
