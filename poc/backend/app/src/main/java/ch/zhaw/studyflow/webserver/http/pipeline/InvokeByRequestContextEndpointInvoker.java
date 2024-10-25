package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvokeByRequestContextEndpointInvoker implements RequestProcessor {
    private static final Logger LOGGER = Logger.getLogger(InvokeByRequestContextEndpointInvoker.class.getName());

    @Override
    public HttpResponse process(RequestContext context) {
        HttpResponse response = null;

        final EndpointMetadata target = context.getTarget();
        try {
            Constructor<?> emptyConstructor = target.controller().getConstructor();
            Object controller = emptyConstructor.newInstance();

            response = (HttpResponse) target.endpoint().invoke(controller, context);
        } catch (NoSuchMethodException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "The endpoint '%s' does not have a default constructor".formatted(target.endpoint().getName()),
                    e
            );
        } catch (InvocationTargetException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "An exception occurred while invoking the endpoint '%s'".formatted(target.endpoint().getName()),
                    e.getCause()
            );
        } catch (InstantiationException e) {
            LOGGER.log(
                    Level.SEVERE,
                    "The endpoint '%s' could not be instantiated".formatted(target.endpoint().getName()),
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
        if (context.getResponse() == null) {
            context.setResponse(response);
        }
        return response;
    }

    private static HttpResponse createInternalServerErrorResponse(HttpRequest request) {
        return request.createResponse()
                .setStatusCode(HttpStatusCode.INTERNAL_SERVER_ERROR);
    }
}
