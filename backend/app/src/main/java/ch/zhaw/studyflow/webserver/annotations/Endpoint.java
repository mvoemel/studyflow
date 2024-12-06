package ch.zhaw.studyflow.webserver.annotations;

import ch.zhaw.studyflow.webserver.http.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The {@link Endpoint} annotation is used to mark a method as an endpoint.
 * It contains the HTTP method that the endpoint is listening to.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    /**
     * Returns the HTTP method that the endpoint is listening to.
     * @return The HTTP method that the endpoint is listening to.
     */
    HttpMethod method();
}
