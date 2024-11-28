package ch.zhaw.studyflow.webserver.security.principal;

import ch.zhaw.studyflow.controllers.HttpMockHelpers;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.cookies.Cookie;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.security.principal.impls.PrincipalImpl;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtHashAlgorithm;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtPrincipalProvider;
import ch.zhaw.studyflow.webserver.security.principal.jwt.JwtPrincipalProviderOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtPrincipalProviderTest {
    private PrincipalProvider jwtPrincipalProvider;

    @BeforeEach
    void beforeEach() {
        jwtPrincipalProvider = new JwtPrincipalProvider(new JwtPrincipalProviderOptions(
                "jwt",
                JwtHashAlgorithm.HS512,
                "secret",
                Duration.ofHours(1)
        ), List.of(CommonClaims.EXPIRES, CommonClaims.USER_ID));
    }

    @Test
    void testGetPrincipal() {
        final HttpRequest request = HttpMockHelpers.makeHttpRequest();

        final Instant expireDateTime = Instant.now().plus(Duration.ofHours(1));
        final HashMap<String, Cookie> cookies = HttpMockHelpers.addCookieContainer(request);
        cookies.put("jwt", new Cookie("jwt", makeFakeJwt(expireDateTime)));

        final Principal principal = jwtPrincipalProvider.getPrincipal(request);
        Assertions.assertNotNull(principal);

        final Optional<Long> id         = principal.getClaim(CommonClaims.USER_ID);
        final Optional<Long> expires    = principal.getClaim(CommonClaims.EXPIRES);
        assertTrue(id.isPresent());
        assertTrue(expires.isPresent());
        assertEquals(1234L, id.get());
        assertEquals(expireDateTime.getEpochSecond(), expires.get());
    }

    @Test
    void testGetPrincipalExpired() {
        final HttpRequest request = HttpMockHelpers.makeHttpRequest();

        final Instant expireDateTime = Instant.now().minus(Duration.ofHours(1));
        final HashMap<String, Cookie> cookies = HttpMockHelpers.addCookieContainer(request);
        cookies.put("jwt", new Cookie("jwt", makeFakeJwt(expireDateTime)));

        final Principal principal = jwtPrincipalProvider.getPrincipal(request);
        Assertions.assertNotNull(principal);

        final Optional<Long> id         = principal.getClaim(CommonClaims.USER_ID);
        final Optional<Long> expires    = principal.getClaim(CommonClaims.EXPIRES);
        assertTrue(id.isEmpty());
        assertTrue(expires.isEmpty());
        assertTrue(principal.getClaims().isEmpty());
    }

    @Test
    void testSetPrincipal() {
        final HttpResponse response = HttpMockHelpers.makeHttpResponse();

        final Principal principal = new PrincipalImpl();
        principal.addClaim(CommonClaims.USER_ID, 1234L);

        final HashMap<String, Cookie> cookies = HttpMockHelpers.addCookieContainer(response);
        jwtPrincipalProvider.setPrincipal(response, principal);

        final CookieContainer cookieContainer = response.getCookies();
        verify(cookieContainer).set(any(Cookie.class));
        assertEquals(1, cookies.size());
        assertEquals("jwt", cookies.get("jwt").getName());

        final String value = cookies.get("jwt").getValue();
        Assertions.assertNotNull(value);

        final String[] splits = value.split("\\.");
        assertEquals(3, splits.length);
        try {
            final String header = new String(Base64.getUrlDecoder().decode(splits[0]));
            final String payload = new String(Base64.getUrlDecoder().decode(splits[1]));
            assertEquals("{\"alg\":\"HS512\",\"typ\":\"JWT\"}", header);
            assertEquals("{\"id\":1234}", payload);

            final String token = calculateSignature(splits[0], splits[1], "secret");
            assertEquals(splits[2], token);

        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    private static String makeFakeJwt(Instant expires) {
        Base64.Encoder urlEncoder = Base64.getUrlEncoder().withoutPadding();
        final String header = urlEncoder.encodeToString("{\"alg\":\"HS512\",\"typ\":\"JWT\"}".getBytes());
        final String payload = urlEncoder.encodeToString("{\"id\":1234, \"exp\":\"%s\"}".formatted(
                expires.getEpochSecond()
        ).getBytes());
        return header + "." + payload + "." + calculateSignature(header, payload, "secret");
    }

    private static String calculateSignature(String header, String claims, String secret) {
        try {
            Mac mac = Mac.getInstance(JwtHashAlgorithm.HS512.getMacName());
            mac.init(new SecretKeySpec("secret".getBytes(), JwtHashAlgorithm.HS512.getMacName()));
            mac.update(header.getBytes());
            mac.update(claims.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(secret.getBytes()));
        } catch (Exception e) {
            Assertions.fail(e);
        }
        return null;
    }
}
