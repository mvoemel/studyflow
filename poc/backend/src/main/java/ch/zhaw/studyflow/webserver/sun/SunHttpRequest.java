package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.CookieContainer;
import ch.zhaw.studyflow.webserver.HttpRequest;
import ch.zhaw.studyflow.webserver.HttpResponse;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.Charset;

public class SunHttpRequest implements HttpRequest {
    private final HttpExchange exchange;
    private final CookieContainer cookieContainer;


    public SunHttpRequest(final HttpExchange exchange, final CookieContainer cookieContainer) {
        this.cookieContainer    = cookieContainer;
        this.exchange           = exchange;
    }

    public HttpExchange getUnderlyingExchange() {
        return exchange;
    }


    @Override
    public Charset getRequestCharset() {
        return null;
    }

    @Override
    public HttpResponse createResponse() {
        return null;
    }
}
