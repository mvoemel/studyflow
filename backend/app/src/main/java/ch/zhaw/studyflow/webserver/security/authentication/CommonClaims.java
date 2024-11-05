package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.security.authentication.claims.BooleanClaim;
import ch.zhaw.studyflow.webserver.security.authentication.claims.IntegerClaim;

/**
 * Common claims that are used in the application.
 */
public final class CommonClaims {
    private CommonClaims() {
        /* no instances */
    }

    /**
     * The authenticated claim is used to determine if a user is authenticated.
     */
    public static final Claim<Boolean> AUTHENTICATED = new BooleanClaim("authenticated");

    /**
     * The user id claim is used to store the user id of a user.
     */
    public static final Claim<Integer> USERID = new IntegerClaim("id");
}
