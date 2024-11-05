package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.http.query.QueryParameters;
import ch.zhaw.studyflow.webserver.http.query.QueryParametersImpl;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.Charset;
import java.util.*;

public class SunHttpRequest implements HttpRequest {
    private final HttpExchange exchange;
    private final CookieContainer cookieContainer;
    private Charset requestCharset;
    private ReadableBodyContent requestBody;

    public SunHttpRequest(final HttpExchange exchange, final ReadableBodyContent requestBody, final CookieContainer cookieContainer) {
        this.requestBody = requestBody;
        this.cookieContainer = cookieContainer;
        this.exchange = exchange;
    }

    public HttpExchange getUnderlyingExchange() {
        return exchange;
    }

    @Override
    public CookieContainer getCookies() {
        return cookieContainer;
    }

    @Override
    public Optional<ReadableBodyContent> getRequestBody() {
        return Optional.ofNullable(requestBody);
    }

    @Override
    public HttpResponse createResponse() {
        return new SunHttpResponse(this);
    }

    @Override
    public QueryParameters getQueryParameters() {
        String query = exchange.getRequestURI().getQuery();
        return new QueryParametersImpl(query);
    }
}