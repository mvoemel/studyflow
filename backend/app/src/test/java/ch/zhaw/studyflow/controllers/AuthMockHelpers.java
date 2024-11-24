package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.Principal;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.makeUnautherizedResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthMockHelpers {
    public static void configureFailingAuthHandler(AuthenticationHandler authHandler) {
        when(authHandler.handleIfAuthenticated(any(), any())).then(invocation -> {
            HttpRequest request = invocation.getArgument(0);
            return request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED);
        });
    }

    public static void configureSuccessfulAuthHandler(AuthenticationHandler authHandler, Map<Claim<?>, Object> claims) {
        when(authHandler.handleIfAuthenticated(any(), any())).then(a -> {
            Function<Principal, HttpResponse> handler = a.getArgument(1);
            return handler.apply(makePrincipal(claims));
        });
    }

    @SuppressWarnings("unchecked")
    public static Principal makePrincipal(Map<Claim<?>, Object> claims) {
        Principal principal = mock(Principal.class);

        when(principal.getClaim(any(Claim.class))).then(a -> {
            Claim<?> claim = a.getArgument(0);
            return Optional.ofNullable(claims.get(claim));
        });

        when(principal.getClaims()).thenReturn(claims.keySet());

        doAnswer(invocation -> {
            Claim<?> claim = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            claims.put(claim, value);
            return null;
        }).when(principal).addClaim(any(Claim.class), any());

        return principal;
    }

}
