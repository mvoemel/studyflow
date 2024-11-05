package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.claims.Claim;

import java.util.List;

public abstract class PrincipalProvider {
    private final List<Claim<?>> knownClaims;


    protected PrincipalProvider(List<Claim<?>> knownClaims) {
        this.knownClaims = knownClaims;
    }


    public List<Claim<?>> getKnownClaims() {
        return knownClaims;
    }

    public abstract Principal getPrincipal(HttpRequest request);
    public abstract void setPrincipal(HttpResponse response, Principal principal);
}
