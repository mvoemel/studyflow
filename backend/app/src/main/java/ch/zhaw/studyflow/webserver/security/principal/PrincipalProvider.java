package ch.zhaw.studyflow.webserver.security.principal;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;

import java.util.List;

/**
 * A PrincipalProvider is responsible for extracting a Principal from a request and setting a Principal in a response.
 */
public abstract class PrincipalProvider {
    private final List<Claim<?>> knownClaims;


    protected PrincipalProvider(List<Claim<?>> knownClaims) {
        this.knownClaims = knownClaims;
    }


    public List<Claim<?>> getKnownClaims() {
        return knownClaims;
    }

    /**
     * Extracts a principal from a request.
     * @param request The request to extract the principal from.
     * @return The principal that was extracted from the request.
     */
    public abstract Principal getPrincipal(HttpRequest request);

    /**
     * Sets a principal in a response.
     * @param response The response to set the principal in.
     * @param principal The principal to set in the response.
     */
    public abstract void setPrincipal(HttpResponse response, Principal principal);
}
