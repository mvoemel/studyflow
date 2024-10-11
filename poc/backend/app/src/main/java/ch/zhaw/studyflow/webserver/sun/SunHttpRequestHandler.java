package ch.zhaw.studyflow.webserver.sun;

import ch.zhaw.studyflow.webserver.cookies.Cookie;
import ch.zhaw.studyflow.webserver.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.cookies.HashMapCookieContainer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class SunHttpRequestHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        CookieContainer container = createCookieContainerFrom(exchange.getRequestHeaders());
        SunHttpRequest request = new SunHttpRequest(exchange, container);


    }

    private CookieContainer createCookieContainerFrom(Headers headers) {
        HashMapCookieContainer container = new HashMapCookieContainer();
        headers.get("cookie").stream()
                .map(content -> content.split(";"))
                .map(parts -> {
                    String[] keyValue = parts[0].split("=");
                    return new Cookie(keyValue[0], keyValue[1]);
                })
                .forEach(container::set);
        return container;
    }
}
