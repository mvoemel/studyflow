package ch.zhaw.studyflow.webserver.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The {@link Route} annotation is used to declare the route of a controller or endpoint.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Route {
    /**
     * The path used to access the controller or endpoint.
     * @return The path.
     */
    String path();
}
