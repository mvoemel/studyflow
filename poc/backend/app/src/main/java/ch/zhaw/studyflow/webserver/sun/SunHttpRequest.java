package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.http.cookies.HashMapCookieContainer;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

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


    public static SunHttpRequest fromExchange(HttpExchange exchange) {
        return new SunHttpRequest(exchange, createCookieContainer(exchange));
    }

    private static CookieContainer createCookieContainer(HttpExchange exchange) {
        HashMapCookieContainer cookieContainer = new HashMapCookieContainer();
        List<String> rawCookies = exchange.getRequestHeaders().get("Cookie");
        if (rawCookies != null) {
            for (String rawCookie : rawCookies) {
                Optional<Cookie> cookie = Cookie.readFromHeader(rawCookie);
                cookie.ifPresent(cookieContainer::set);
            }
        }
        return cookieContainer;
    }
}
