package ch.zhaw.studyflow.webserver.security.principal.jwt;

import java.security.Provider;
import java.security.Security;
import java.time.Duration;
import java.util.Objects;

/**
 * Options for the JwtPrincipalProvider.
 */
public class JwtPrincipalProviderOptions {
    private final String cookieName;
    private final JwtHashAlgorithm hashAlgorithm;
    private final String secret;
    private final Duration expiresAfter;

    /**
     * Creates a new instance of JwtPrincipalProviderOptions.
     * @param cookieName The name of the cookie that will be used to store the JWT.
     * @param hashAlgorithm The hash algorithm that will be used to sign the JWT.
     * @param secret The secret that will be used to sign the JWT.
     * @param expiresAfter The time a jwt token is considered valid in seconds.
     */
    public JwtPrincipalProviderOptions(String cookieName, JwtHashAlgorithm hashAlgorithm, String secret, Duration expiresAfter) {
        Objects.requireNonNull(cookieName, "cookieName must not be null");
        Objects.requireNonNull(hashAlgorithm, "hashAlgorithm must not be null");
        Objects.requireNonNull(secret, "secret must not be null");
        Objects.requireNonNull(expiresAfter, "expiresAfter must not be null");
        checkAlgorithmAvailability(hashAlgorithm);

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


    /**
     * Checks if the provided algorithm is available.
     * @param algorithm The algorithm to check.
     */
    private void checkAlgorithmAvailability(JwtHashAlgorithm algorithm) {
        // check if the algorithm provided in name is available
        for (Provider provider : Security.getProviders()) {
            if (provider.getService("Mac", algorithm.getMacName()) != null) {
                return;
            }
        }
        throw new IllegalArgumentException("Algorithm not available: " + algorithm.getMacName());
    }
}
