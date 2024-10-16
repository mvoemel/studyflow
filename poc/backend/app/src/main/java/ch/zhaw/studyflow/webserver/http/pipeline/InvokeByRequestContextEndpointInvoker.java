package ch.zhaw.studyflow.webserver.http.pipeline;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class InvokeByRequestContextEndpointInvoker implements RequestProcessor {
    @Override
    public HttpResponse process(RequestContext context) {
        final EndpointMetadata target = context.getTarget();
        try {
            Constructor<?> emptyConstructor = target.controller().getConstructor();
            Object controller = emptyConstructor.newInstance();

            HttpResponse response = (HttpResponse) target.endpoint().invoke(controller, context);
            if (context.getResponse() == null) {
                context.setResponse(response);
            }
            return response;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
