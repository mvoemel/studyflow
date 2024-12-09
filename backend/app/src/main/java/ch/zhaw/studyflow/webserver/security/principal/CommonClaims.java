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
     * The email claim is used to store the email..
     */
    public static final Claim<String> EMAIL = new Claim<>("email", String.class);

    /**
     * The settings claim is used to store the settings id.
     */
    public static final Claim<Long> SETTINGS_ID = new Claim<>("settingsId", Long.class);

    /**
     * The user name claim is used to store the user name of a user.
     */
    public static final Claim<Long> EXPIRES = new Claim<>("exp", Long.class);
}
