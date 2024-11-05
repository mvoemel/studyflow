package ch.zhaw.studyflow.webserver.security.authentication.jwt;

import java.time.Duration;

/**
 * Options for the JwtPrincipalProvider.
 * All options are required.
 */
public class JwtPrincipalProviderOptions {
    private final String cookieName;
    private final JwtHashAlgorithm hashAlgorithm;
    private final String secret;
    private final Duration expiresAfter;


    public JwtPrincipalProviderOptions(String cookieName, JwtHashAlgorithm hashAlgorithm, String secret, Duration expiresAfter) {
        this.cookieName     = cookieName;
        this.hashAlgorithm  = hashAlgorithm;
        this.secret         = secret;
        this.expiresAfter   = expiresAfter;
    }


    /**
     * The name of the cookie that will be used to store the JWT.
     * @return The cookie name.
     */
    public String getCookieName() {
        return cookieName;
    }

    /**
     * The hash algorithm that will be used to sign the JWT.
     */
    public JwtHashAlgorithm getHashAlgorithm() {
        return hashAlgorithm;
    }

    /**
     * The secret that will be used to sign the JWT.
     */
    public String getSecret() {
        return secret;
    }

    /**
     * The time a jwt token is considered valid in seconds.
     * @return The expiration time in seconds.
     */
    public Duration getExpiresAfter() {
        return expiresAfter;
    }
}
