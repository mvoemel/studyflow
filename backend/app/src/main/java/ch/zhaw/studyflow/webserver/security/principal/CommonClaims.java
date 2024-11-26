package ch.zhaw.studyflow.webserver.security.principal;

import java.time.LocalDateTime;

/**
 * Common claims that are used in the application.
 */
public final class CommonClaims {
    private CommonClaims() {
        /* no instances */
    }

    /**
     * The user id claim is used to store the user id of a user.
     */
    public static final Claim<Long> USER_ID = new Claim<>("id", Long.class);

    /**
     * The user name claim is used to store the user name of a user.
     */
    public static final Claim<LocalDateTime> EXPIRES = new Claim<>("exp", LocalDateTime.class);
}
