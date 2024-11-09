package ch.zhaw.studyflow.webserver.security.principal;

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
    public static final Claim<Boolean> AUTHENTICATED = new Claim<>("authenticated", Boolean.class);

    /**
     * The user id claim is used to store the user id of a user.
     */
    public static final Claim<Long> USER_ID = new Claim<>("id", Long.class);
}
