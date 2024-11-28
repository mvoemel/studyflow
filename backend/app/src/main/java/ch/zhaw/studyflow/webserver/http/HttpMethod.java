package ch.zhaw.studyflow.webserver.http;

import javax.sound.midi.Patch;

public enum HttpMethod {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    DELETE("DELETE"),
    PATCH("PATCH");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public String methodName() {
        return method;
    }
}
