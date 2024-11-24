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
    public static final Claim<Integer> USER_ID = new Claim<>("id", Integer.class);

    /**
     * The degree id claim is used to store the degree id of a user.
     */
    public static final Claim<Integer> DEGREE_ID = new Claim<>("degreeId", Integer.class);

    /**
     * The semester id claim is used to store the semester id of a user.
     */
    public static final Claim<Integer> SEMESTER_ID = new Claim<>("semesterId", Integer.class);

    /**
     * The module id claim is used to store the module id of a user.
     */
    public static final Claim<Integer> MODULE_ID = new Claim<>("moduleId", Integer.class);
}
