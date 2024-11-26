package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.function.Function;

/**
 * An authentication handler that checks if a claim is present and has a specific value.
 * If the claim is present and has the specific value, the handler function is called; otherwise, a 401 response is returned.
 */
public class JwtBasedAuthenticationHandler implements AuthenticationHandler {
    private final PrincipalProvider principalProvider;
    private final TemporalAmount tokenLifetime;


    public JwtBasedAuthenticationHandler(PrincipalProvider principalProvider, TemporalAmount tokenLifetime) {
        this.principalProvider = principalProvider;
        this.tokenLifetime = tokenLifetime;
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
            principalProvider.clearPrincipal(response);
        }
        return response;
    }

    @Override
    public HttpResponse handleIfUnauthenticated(HttpRequest request, Function<Principal, HttpResponse> handler) {
        return handleIfUnauthenticated(
                request,
                handler,
                principal -> request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED)
        );
    }

    @Override
    public HttpResponse handleIfUnauthenticated(HttpRequest request, Function<Principal, HttpResponse> handler, Function<Principal, HttpResponse> unauthenticatedHandler) {
        final Principal principal = principalProvider.getPrincipal(request);
        HttpResponse response = isAuthenticated(principal)
                ? handler.apply(principal)
                : unauthenticatedHandler.apply(principal);

        final Optional<LocalDateTime> expires = principal.getClaim(CommonClaims.EXPIRES);
        if (expires.isPresent()) {
            principal.addClaim(CommonClaims.EXPIRES, LocalDateTime.now(ZoneOffset.UTC).plus(tokenLifetime));
        }
        principalProvider.setPrincipal(response, principal);
        return response;
    }

    private boolean isAuthenticated(Principal principal) {
        return principal.getClaim(CommonClaims.EXPIRES)
                .map(expires -> expires.isAfter(LocalDateTime.now(ZoneOffset.UTC)))
                .orElse(false);
    }
}
