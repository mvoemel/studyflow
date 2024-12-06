package ch.zhaw.studyflow.webserver.security.principal.impls;

import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.Principal;

import java.util.*;

/***
 * A generic principal implementation that stores claims in a map.
 */
public class PrincipalImpl implements Principal {
    private final HashMap<Claim<?>, Object> claims;

    /**
     * Creates a new principal with no claims.
     */
    public PrincipalImpl() {
        this(Collections.emptyMap());
    }

    /**
     * Creates a new principal with the given claims.
     * The constructor creates a *copy* of the passed map.
     * @param claims the claims to add to the principal
     */
    public PrincipalImpl(Map<Claim<?>, Object> claims) {
        this.claims = new HashMap<>(claims);
    }


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

    @Override
    public <T> void removeClaim(Claim<T> claim) {
        claims.remove(claim);
    }

    @Override
    public void clearClaims() {
        claims.clear();
    }
}
