package ch.zhaw.studyflow.webserver.security.authentication.jwt;

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
