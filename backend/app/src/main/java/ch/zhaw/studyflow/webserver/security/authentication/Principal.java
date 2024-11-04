package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.security.authentication.claims.Claim;

import java.util.Optional;
import java.util.Set;

public interface Principal {
    Set<Claim<?>> getClaims();

    <T> Optional<T> getClaim(Claim<T> claim);
    <T> void addClaim(Claim<T> claim, T value);
}
