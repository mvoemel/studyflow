package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.security.principal.Principal;

import java.util.function.Function;

/**
 * An authentication handler may be used to simplify the authentication process.
 * It is used to check if a request is authenticated and if so, it will call the handler function.
 */
public interface AuthenticationHandler {
    /**
     * Checks if the request is authenticated and calls the handler function if so.
     * If a request is considered authenticated, depends on the implementation of the AuthenticationHandler.
     * @param request The request to check.
     * @param handler The handler function to call if the request is authenticated.
     * @return The response of the handler function or a response with status code 401 if the request is not authenticated.
     */
    HttpResponse handleIfAuthenticated(HttpRequest request, Function<Principal, HttpResponse> handler);

    /**
     * Checks if the request is not authenticated and calls the handler function if so.
     * If a request is considered not authenticated, depends on the implementation of the AuthenticationHandler.
     * @param request The request to check.
     * @param handler The handler function to call if the request is not authenticated.
     * @return The response of the handler function or a response with status code 401 if the request is authenticated.
     */
    HttpResponse handleIfUnauthenticated(HttpRequest request, Function<Principal, HttpResponse> handler);

    /**
     * Checks if the request is authenticated and calls the handler function if so.
     * @param request The request to check.
     * @param handler The handler function to call if the request is authenticated.
     * @param unauthenticatedHandler The handler function to call if the request is not authenticated.
     * @return The response of the handler function or a response with status code 401 if the request is not authenticated.
     */
    HttpResponse handleIfUnauthenticated(HttpRequest request, Function<Principal, HttpResponse> handler, Function<Principal, HttpResponse> unauthenticatedHandler);
}
