package ch.zhaw.studyflow.webserver.security.principal.jwt;

/**
 * The hash algorithm that is used to sign the JWT.
 * This enum is used to determine the algorithm that is used to sign the JWT and map it to the Java name of the algorithm.
 */
public enum JwtHashAlgorithm {
    HS256("HS256", "HmacSHA256"),
    HS512("HS512", "HmacSHA512");


    private final String jwtName;
    private final String javaName;


    JwtHashAlgorithm(String jwtName, String javaMacName) {
        this.jwtName    = jwtName;
        this.javaName   = javaMacName;
    }


    /**
     * Returns the name of the algorithm as it is used in the JWT.
     * @return the name of the algorithm as it is used in the JWT
     */
    public String getJwtName() {
        return jwtName;
    }

    /**
     * Returns the name of the algorithm as it is used in Java.
     * @return the name of the algorithm as it is used in Java.
     */
    public String getMacName() {
        return javaName;
    }
}
