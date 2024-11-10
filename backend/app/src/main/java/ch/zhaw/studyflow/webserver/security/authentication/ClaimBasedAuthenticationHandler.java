package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;

import java.util.Optional;
import java.util.function.Function;

/**
 * An authentication handler that checks if a claim is present and has a specific value.
 * If the claim is present and has the specific value, the handler function is called; otherwise, a 401 response is returned.
 */
public class ClaimBasedAuthenticationHandler implements AuthenticationHandler {
    private final PrincipalProvider principalProvider;
    private final Claim<Boolean> requiredClaim;


    public ClaimBasedAuthenticationHandler(PrincipalProvider principalProvider, Claim<Boolean> requiredClaim) {
        this.principalProvider  = principalProvider;
        this.requiredClaim      = requiredClaim;
    }


    @Override
    public HttpResponse check(HttpRequest request, Function<Principal, HttpResponse> handler) {
        Principal principal = principalProvider.getPrincipal(request);
        Optional<Boolean> claim = principal.getClaim(requiredClaim);
        if (claim.isPresent() && Boolean.TRUE.equals(claim.get())) {
            return handler.apply(principal);
        }
        return request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED);
    }
}
