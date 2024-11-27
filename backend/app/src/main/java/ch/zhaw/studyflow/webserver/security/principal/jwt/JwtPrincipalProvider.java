package ch.zhaw.studyflow.webserver.security.principal.jwt;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.impls.PrincipalImpl;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.text.html.Option;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A PrincipalProvider that uses JWTs to store the principal.
 */
public class JwtPrincipalProvider extends PrincipalProvider {
    private static final Logger LOGGER = Logger.getLogger(JwtPrincipalProvider.class.getName());

    private final JwtPrincipalProviderOptions options;
    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final SecretKeySpec keySpec;
    private final ObjectMapper objectMapper;
    private final String precomputedHeader;

    /**
     * Creates a new JwtPrincipalProvider.
     * @param options The options for the provider.
     * @param knownClaims The known claims that the provider should look for in the JWT.
     */
    public JwtPrincipalProvider(JwtPrincipalProviderOptions options, List<Claim<?>> knownClaims) {
        super(knownClaims);

        this.options            = options;
        this.base64Encoder      = Base64.getUrlEncoder().withoutPadding();
        this.base64Decoder      = Base64.getUrlDecoder();
        this.keySpec            = new SecretKeySpec(options.getSecret().getBytes(), options.getHashAlgorithm().getMacName());
        this.objectMapper       = new ObjectMapper();
        this.precomputedHeader  = buildPrecomputedHeader();
    }


    @Override
    public Principal getPrincipal(HttpRequest request) {
        Objects.requireNonNull(request, "Request must not be null");

        Optional<Cookie> jwt = request.getCookies().get(options.getCookieName());
        PrincipalImpl principal = new PrincipalImpl();
        if (jwt.isEmpty()) {
            return principal;
        }

        String[] parts = jwt.get().getValue().split("\\.");
        if (parts.length == 3) {
            if (!validateTokenBySignature(parts[0], parts[1], parts[2])) {
                LOGGER.log(Level.WARNING, "Failed to validate token, invalid signature");
                return principal;
            }

            try {
                JsonNode rootNode = objectMapper.readTree(base64Decoder.decode(parts[1]));

                for (Claim<?> knownClaim : getKnownClaims()) {
                    String fieldName = knownClaim.getName();
                    JsonNode node = rootNode.get(fieldName);
                    if (node != null) {
                        handleClaim(principal, node, knownClaim);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to parse JWT cookie", e);
            }

            if (!isExpirationDateValid(principal)) {
                LOGGER.log(Level.WARNING, "Token expired");
                principal.clearClaims();
            }
        }
        return principal;
    }

    @Override
    public void setPrincipal(HttpResponse response, Principal principal) {
        Objects.requireNonNull(response, "Response must not be null");
        Objects.requireNonNull(principal, "Principal must not be null");

        final String claims = buildClaimsPart(principal);

        byte[] signature = calculateSignature(precomputedHeader, claims);
        if (signature != null) {
            Cookie cookie = new Cookie(
                    options.getCookieName(),
                    precomputedHeader + "." + claims + "." + base64Encoder.encodeToString(signature)
            );
            cookie.setExpires(LocalDateTime.now().plus(options.getExpiresAfter()));
            response.getCookies().set(cookie);
        }
    }

    @Override
    public void clearPrincipal(HttpResponse response) {
        Objects.requireNonNull(response, "Response must not be null");
        response.getCookies().remove(options.getCookieName());
        Cookie cookie = new Cookie(options.getCookieName(), "");
        cookie.setExpires(LocalDateTime.MIN);
        response.getCookies().set(cookie);
    }

    /**
     * Builds the precomputed header part of the JWT (contains the algorithm and type).
     * @return The precomputed header.
     */
    private String buildPrecomputedHeader() {
        return base64Encoder.encodeToString(("{\"alg\":\"" + options.getHashAlgorithm().getJwtName() + "\",\"typ\":\"JWT\"}").getBytes());
    }


    /**
     * Validates a principal.
     * @param principal The principal to validate.
     * @return True if the principal is valid, false otherwise.
     */
    private boolean isExpirationDateValid(Principal principal) {

        return principal.getClaim(CommonClaims.EXPIRES)
                .map(expires -> Instant.now().getEpochSecond() < expires)
                .orElse(true);
    }

    /**
     * Validates a token by its signature.
     * @param settings The settings part of the token.
     * @param claims The claims part of the token.
     * @param signature The signature part of the token.
     * @return True if the token is valid, false otherwise.
     */
    private boolean validateTokenBySignature(String settings, String claims, String signature) {
        byte[] hash = calculateSignature(settings, claims);

        boolean result = false;
        if (hash != null) {
            result = Arrays.equals(base64Decoder.decode(signature), hash);
        }
        return result;
    }

    /**
     * Calculates the signature of a token.
     * @param settings The settings part of the token.
     * @param claims The claims part of the token.
     * @return The signature of the token.
     */
    private byte[] calculateSignature(String settings, String claims) {
        byte[] result = null;
        try {
            // TODO: Check if a Mac instanced can be reused / cached.
            Mac mac = Mac.getInstance(options.getHashAlgorithm().getMacName());
            mac.init(keySpec);
            mac.update(settings.getBytes());
            mac.update(claims.getBytes());
            result = mac.doFinal(options.getSecret().getBytes());
        } catch (Exception e) {
            /*
             * Since we check the algorithm availability in the constructor, this should never happen.
             */
            LOGGER.log(Level.SEVERE, "Failed to validate token", e);
        }
        return result;
    }

    private String buildClaimsPart(Principal principal) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try (JsonGenerator generator = objectMapper.createGenerator(buffer)) {
            generator.writeStartObject();
            for (Claim<?> knownClaim : principal.getClaims()) {
                Optional<?> claim = principal.getClaim(knownClaim);
                // Since we are iterating over the claims,
                // we should be safe to believe that the claim is present
                assert claim.isPresent();

                generator.writeFieldName(knownClaim.getName());
                generator.writeObject(claim.get());
            }
            generator.writeEndObject();
            generator.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write principal to JSON", e);
        }
        return base64Encoder.encodeToString(buffer.toByteArray());
    }

    /**
     * Try to parse a claim from a JSON node and add it to the principal.
     * This is a type safety workaround. In the context of getPrincipal, there is no way to tell the compiler that the
     * value read from the JSON node (claim.valueType()) is of the same type as the value type of the claim (T) since
     * we don't have a generic T in the getPrincipal method. This leads to problems when adding the claim to the principal.
     * By passing Claim&lt;T&gt; as an argument, we can "tell" the compiler that the value read from the JSON node is of the
     * same type as the value type of the claim and thus add it to the principal without any warnings.
     *
     * @param principal The principal to add the claim to
     * @param node      The JSON node to parse
     * @param claim     The claim to add
     * @param <T>       The type of the claim
     */
    private <T> void handleClaim(Principal principal, JsonNode node, Claim<T> claim) {
        try {
            T value = objectMapper.treeToValue(node, claim.valueType());
            principal.addClaim(claim, value);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, "Failed to parse claim", e);
        }
    }
}
