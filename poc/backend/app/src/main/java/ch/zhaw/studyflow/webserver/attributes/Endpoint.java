package ch.zhaw.studyflow.webserver.attributes;

import ch.zhaw.studyflow.webserver.http.HttpMethod;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Endpoint {
    HttpMethod method();
}
