package ch.zhaw.studyflow.webserver.security.authentication.jwt;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.security.authentication.Principal;
import ch.zhaw.studyflow.webserver.security.authentication.impls.PrincipalImpl;
import ch.zhaw.studyflow.webserver.security.authentication.PrincipalProvider;
import ch.zhaw.studyflow.webserver.security.authentication.Claim;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringWriter;
import java.security.Provider;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtPrincipalProvider extends PrincipalProvider {
    private static final Logger LOGGER = Logger.getLogger(JwtPrincipalProvider.class.getName());

    private final JwtPrincipalProviderOptions options;
    private final Base64.Encoder base64Encoder;
    private final Base64.Decoder base64Decoder;
    private final SecretKeySpec keySpec;
    private final ObjectMapper objectMapper;
    private final String precomputedHeader;


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
        Optional<Cookie> jwt = request.getCookies().get(options.getCookieName());
        PrincipalImpl result = new PrincipalImpl();
        if (jwt.isEmpty()) {
            return result;
        }

        final Cookie cookie = jwt.get();
        if (!validateTokenLifetime(cookie.getExpires())) {
            LOGGER.log(Level.FINE, "Failed to validate token, expired");
            return result;
        }

        String[] parts = jwt.get().getValue().split("\\.");
        if (parts.length == 3) {
            if (!validateTokenBySignature(parts[0], parts[1], parts[2])) {
                LOGGER.log(Level.WARNING, "Failed to validate token, invalid signature");
                return result;
            }

            try {
                JsonNode rootNode = objectMapper.readTree(base64Decoder.decode(parts[1]));

                for (Claim<?> knownClaim : getKnownClaims()) {
                    String fieldName = knownClaim.getName();
                    JsonNode node = rootNode.get(fieldName);
                    if (node != null) {
                        handleClaim(result, node, knownClaim);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to parse JWT cookie", e);
            }
        }
        return result;
    }

    @Override
    public void setPrincipal(HttpResponse response, Principal principal) {
        final String claims = buildClaimsPart(principal);

        byte[] signature = calculateSignature(precomputedHeader, claims);
        if (signature != null) {
            Cookie cookie = new Cookie(options.getCookieName(), precomputedHeader + "." + claims + "." + new String(signature));
            cookie.setExpires(LocalDateTime.now().plus(options.getExpiresAfter()));
            response.getCookies().set(options.getCookieName(), precomputedHeader + "." + claims + "." + base64Encoder.encodeToString(signature));
        }
    }

    private String buildPrecomputedHeader() {
        return base64Encoder.encodeToString(("{\"alg\":\"" + options.getHashAlgorithm().getJwtName() + "\",\"typ\":\"JWT\"}").getBytes());
    }


    private boolean validateTokenLifetime(LocalDateTime expirationDate) {
        boolean result = true;
        if (expirationDate != null) {
            result = expirationDate.isAfter(LocalDateTime.now());
        }
        return result;
    }

    private boolean validateTokenBySignature(String settings, String claims, String signature) {
        boolean result = false;
        byte[] hash = calculateSignature(settings, claims);
        if (hash != null) {
            result = Arrays.equals(base64Decoder.decode(signature), hash);
        }
        return result;
    }

    private byte[] calculateSignature(String settings, String claims) {
        try {
            Mac mac = Mac.getInstance(options.getHashAlgorithm().getMacName());
            mac.init(keySpec);
            mac.update(settings.getBytes());
            mac.update(claims.getBytes());
            return mac.doFinal(options.getSecret().getBytes());
        } catch (Exception e) {
            /*
             * Since we check the algorithm availability in the constructor, this should never happen.
             */
            LOGGER.log(Level.SEVERE, "Failed to validate token", e);
        }
        return null;
    }

    private String buildClaimsPart(Principal principal) {
        StringWriter writer = new StringWriter();
        try (JsonGenerator generator = objectMapper.createGenerator(writer)) {
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
            return base64Encoder.encodeToString(writer.toString().getBytes());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to write principal to JSON", e);
        }
        return null;
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