package ch.zhaw.studyflow.webserver.security.authentication;

import java.util.Optional;
import java.util.Set;

/**
 * A Principal represents a user or entity can be authenticated by the server.
 * It may contain claims that represent the user's identity or attributes of the user.
 */
public interface Principal {
    /**
     * Returns all claims that are associated with this principal.
     * @return a set of claims
     */
    Set<Claim<?>> getClaims();

    /**
     * Returns the value of a specific claim.
     * @param claim the claim to get the value of
     * @param <T> the type of the claim
     * @return the value of the claim, or an empty optional if the claim is not present
     */
    <T> Optional<T> getClaim(Claim<T> claim);

    /**
     * Adds a claim to the principal.
     * @param claim the claim to add
     * @param value the value of the claim
     * @param <T> the type of the claim
     */
    <T> void addClaim(Claim<T> claim, T value);
}
