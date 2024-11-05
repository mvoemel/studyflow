package ch.zhaw.studyflow.webserver.security.authentication.jwt;

public class JwtPrincipalProviderOptions {
    private final String cookieName;
    private final String hsAlgorithm;
    private final String secret;
    private final long expirationTime;


    public JwtPrincipalProviderOptions(String cookieName, String hsAlgorithm, String secret, long expirationTime) {
        this.cookieName     = cookieName;
        this.hsAlgorithm    = hsAlgorithm;
        this.secret         = secret;
        this.expirationTime = expirationTime;
    }


    public String getCookieName() {
        return cookieName;
    }

    public String getHsAlgorithm() {
        return hsAlgorithm;
    }

    public String getSecret() {
        return secret;
    }

    public long getExpirationTime() {
        return expirationTime;
    }
}
