package ch.zhaw.studyflow.webserver.security.authentication;

import ch.zhaw.studyflow.controllers.AuthMockHelpers;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import ch.zhaw.studyflow.webserver.security.principal.PrincipalProvider;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class JwtBasedAuthenticationHandlerTest {
    @Test
    void testHandleIfAuthenticated() {
        final HttpRequest request = makeHttpRequest();

        final Principal fakePrincipal = AuthMockHelpers.makePrincipal(AuthMockHelpers.getDefaultClaims());

        PrincipalProvider principalProvider = mock(PrincipalProvider.class);
        when(principalProvider.getPrincipal(request)).thenReturn(fakePrincipal);

        JwtBasedAuthenticationHandler jwtBasedAuthenticationHandler = new JwtBasedAuthenticationHandler(
                principalProvider,
                Duration.ofHours(1).plusSeconds(60)
        );

        Instant timeBeforeRequest = Instant.now();
        final HttpResponse response = jwtBasedAuthenticationHandler.handleIfAuthenticated(
                request,
                principal -> request.createResponse()
        );

        ArgumentCaptor<Long> expiresCaptor = ArgumentCaptor.forClass(Long.class);

        verify(principalProvider).getPrincipal(request);
        verify(principalProvider).setPrincipal(response, fakePrincipal);
        verify(fakePrincipal).addClaim(eq(CommonClaims.EXPIRES), expiresCaptor.capture());

        assertTrue(expiresCaptor.getValue() - timeBeforeRequest.getEpochSecond() > 3600);
    }

    @Test
    void testHandleIfUnauthenticatedButAuthenticated() {
        final HttpRequest request = makeHttpRequest();

        final Principal fakePrincipal = AuthMockHelpers.makePrincipal(AuthMockHelpers.getDefaultClaims());

        PrincipalProvider principalProvider = mock(PrincipalProvider.class);
        when(principalProvider.getPrincipal(request)).thenReturn(fakePrincipal);

        JwtBasedAuthenticationHandler jwtBasedAuthenticationHandler = new JwtBasedAuthenticationHandler(
                principalProvider,
                Duration.ofHours(1).plusSeconds(60)
        );

        final HttpResponse response = jwtBasedAuthenticationHandler.handleIfUnauthenticated(
                request,
                principal -> request.createResponse().setStatusCode(HttpStatusCode.OK)
        );

        final ArgumentCaptor<HttpStatusCode> statusCodeArgumentCaptor = captureResponseCode(response);
        verify(principalProvider).getPrincipal(request);
        assertEquals(HttpStatusCode.UNAUTHORIZED, statusCodeArgumentCaptor.getValue());
    }
}
