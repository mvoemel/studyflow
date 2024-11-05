package ch.zhaw.studyflow.webserver.security.authentication.impls;

import ch.zhaw.studyflow.webserver.security.authentication.Claim;
import ch.zhaw.studyflow.webserver.security.authentication.Principal;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class PrincipalImpl implements Principal {
    private HashMap<Claim<?>, Object> claims = new HashMap<>();

    @Override
    public Set<Claim<?>> getClaims() {
        return claims.keySet();
    }

    @Override
    public <T> Optional<T> getClaim(Claim<T> claim) {
        return Optional.ofNullable((T) claims.get(claim));
    }

    @Override
    public <T> void addClaim(Claim<T> claim, T value) {
        claims.put(claim, value);
    }
}
