package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.function.Function;

/**
 * An authentication handler that checks if a claim is present and has a specific value.
 * If the claim is present and has the specific value, the handler function is called; otherwise, a 401 response is returned.
 */
public class JwtBasedAuthenticationHandler implements AuthenticationHandler {
    private final PrincipalProvider principalProvider;
    private final long tokenLifetime;


    public JwtBasedAuthenticationHandler(PrincipalProvider principalProvider, TemporalAmount tokenLifetime) {
        this.principalProvider = principalProvider;
        this.tokenLifetime      = tokenLifetime.get(ChronoUnit.SECONDS);
    }


    @Override
    public HttpResponse handleIfAuthenticated(HttpRequest request, Function<Principal, HttpResponse> handler) {
        final Principal principal = principalProvider.getPrincipal(request);

        HttpResponse response;
        if (isAuthenticated(principal)) {
            response = handler.apply(principal);
        } else {
            response = request.createResponse()
                    .setStatusCode(HttpStatusCode.UNAUTHORIZED);
            principal.clearClaims();
            principalProvider.clearPrincipal(response);
        }
        final Optional<Long> expires = principal.getClaim(CommonClaims.EXPIRES);
        if (expires.isPresent()) {
            principal.addClaim(CommonClaims.EXPIRES, Instant.now().getEpochSecond() + tokenLifetime);
            principalProvider.setPrincipal(response, principal);
        }
        return response;
    }

    @Override
    public HttpResponse handleIfUnauthenticated(HttpRequest request, Function<Principal, HttpResponse> handler) {
        return handleRequest(
                request,
                principal -> request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED),
                handler
        );
    }

    @Override
    public HttpResponse handleRequest(HttpRequest request, Function<Principal, HttpResponse> handler, Function<Principal, HttpResponse> unauthenticatedHandler) {
        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = isAuthenticated(principal)
                ? handler.apply(principal)
                : unauthenticatedHandler.apply(principal);

        final Optional<Long> expires = principal.getClaim(CommonClaims.EXPIRES);
        if (expires.isPresent()) {
            principal.addClaim(CommonClaims.EXPIRES, Instant.now().getEpochSecond() + tokenLifetime);
            principalProvider.setPrincipal(response, principal);
        }
        return response;
    }

    private boolean isAuthenticated(Principal principal) {
        return principal.getClaim(CommonClaims.EXPIRES)
                .map(expires -> Instant.now().getEpochSecond() < expires)
                .orElse(false);
    }
}
