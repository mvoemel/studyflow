package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.cookies.CookieContainer;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtHashAlgorithm;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtPrincipalProvider;
import ch.zhaw.studyflow.webserver.security.authentication.jwt.JwtPrincipalProviderOptions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtPrincipalProviderTest {
    private static final JwtPrincipalProviderOptions ALWAYS_EXPIRED = new JwtPrincipalProviderOptions(
            "token",
            JwtHashAlgorithm.HS256,
            "helloworld",
            Duration.ofMillis(1)
    );

    private static final JwtPrincipalProviderOptions DEFAULT_OPTIONS = new JwtPrincipalProviderOptions(
            "token",
            JwtHashAlgorithm.HS256,
            "helloworld",
            Duration.ofMillis(1)
    );

    private static final List<Claim<?>> WELL_KNOWN_CLAIMS = List.of(CommonClaims.AUTHENTICATED, CommonClaims.USER_ID);

    @Test
    void testExpiredCookie() {
        JwtPrincipalProvider jwtPrincipalProvider = new JwtPrincipalProvider(ALWAYS_EXPIRED, WELL_KNOWN_CLAIMS);
        Principal principal = jwtPrincipalProvider.getPrincipal(null);
    }


    private HttpRequest fakeRequest(PrincipalProvider provider, JwtPrincipalProviderOptions options, String payload) {
        CookieContainer cookieContainer = mock(CookieContainer.class);
        HttpRequest request = mock(HttpRequest.class);
        when(request.getCookies()).thenReturn(cookieContainer);
    }
}
