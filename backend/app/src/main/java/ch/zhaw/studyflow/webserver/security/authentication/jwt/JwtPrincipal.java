package ch.zhaw.studyflow.webserver.security.authentication.jwt;

import ch.zhaw.studyflow.webserver.security.authentication.Principal;
import ch.zhaw.studyflow.webserver.security.authentication.claims.Claim;

import java.util.Optional;
import java.util.Set;

public class JwtPrincipal implements Principal {

    @Override
    public Set<Claim<?>> getClaims() {
        return Set.of();
    }

    @Override
    public <T> Optional<T> getClaim(Claim<T> claim) {
        return Optional.empty();
    }

    @Override
    public <T> void addClaim(Claim<T> claim, T value) {

    }
}
