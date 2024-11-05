package ch.zhaw.studyflow.webserver.security.authentication.jwt;

/**
 * The hash algorithm that is used to sign the JWT.
 * This enum is used to determine the algorithm that is used to sign the JWT and map it to the Java name of the algorithm.
 */
public enum JwtHashAlgorithm {
    HS256("HmacSHA256"),
    HS512("HmacSHA512");

    private final String javaName;

    JwtHashAlgorithm(String javaMacName) {
        this.javaName   = javaMacName;
    }

    public String getMacName() {
        return javaName;
    }
}
