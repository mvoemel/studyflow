package ch.zhaw.studyflow.webserver.security.authentication.jwt;

import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.CommonClaims;
import ch.zhaw.studyflow.webserver.security.authentication.Principal;
import ch.zhaw.studyflow.webserver.security.authentication.PrincipalProvider;
import ch.zhaw.studyflow.webserver.security.authentication.claims.Claim;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JwtPrincipalProvider extends PrincipalProvider {
    private static final Logger LOGGER = Logger.getLogger(JwtPrincipalProvider.class.getName());

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String cookieName;


    public JwtPrincipalProvider(List<Claim<?>> knownClaims, String cookieName) {
        super(knownClaims);

        this.cookieName     = cookieName;
        this.objectMapper   = new ObjectMapper();
    }


    @Override
    public Principal getPrincipal(RequestContext requestContext) {
        Optional<Cookie> jwt = requestContext.getRequest().getCookies().get(cookieName);
        JwtPrincipal result = new JwtPrincipal();
        if (jwt.isPresent()) {
            try {
                JsonNode rootNode = objectMapper.readTree(jwt.get().getValue());

                for (Claim<?> knownClaim : getKnownClaims()) {
                    String fieldName = knownClaim.getName();
                    JsonNode node = rootNode.get(fieldName);
                    if (node != null) {
                        handleClaim(result, node, knownClaim);
                    }
                }
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "Failed to parse JWT cookie", e);
            }
        }
        return result;
    }

    @Override
    public void setPrincipal(RequestContext requestContext, Principal principal) {
        String jwt = writePrincipalTostring(principal);
        if (jwt != null) {
            requestContext.getRequest().createResponse().getCookies().set(new Cookie(cookieName, jwt));
        }

    }

    private String writePrincipalTostring(Principal principal) {
        try {
            StringWriter writer = new StringWriter();
            JsonGenerator generator = objectMapper.createGenerator(writer);

            generator.writeStartObject();
            for (Claim<?> knownClaim : principal.getClaims()) {
                Optional<?> claim = principal.getClaim(knownClaim);

                assert claim.isPresent();
                // write all claims to the writer using the claims name as the field name
                generator.writeFieldName(knownClaim.getName());
                generator.writeObject(claim.get());
            }
            generator.writeEndObject();
            generator.close();
            return writer.toString();
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
     * @param principal The principal to add the claim to
     * @param node The JSON node to parse
     * @param claim The claim to add
     * @param <T> The type of the claim
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
