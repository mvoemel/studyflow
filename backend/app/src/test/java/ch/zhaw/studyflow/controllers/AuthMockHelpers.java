package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.Claim;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthMockHelpers {
    public static void configureFailingAuthHandler(AuthenticationHandler authHandler) {
        configureFailingAuthHandler(authHandler, getDefaultClaims());
    }

    public static Principal configureFailingAuthHandler(AuthenticationHandler authHandler, Map<Claim<?>, Object> claims) {
        Principal principal = makePrincipal(claims);
        when(authHandler.handleIfAuthenticated(any(), any())).then(invocation -> {
            HttpRequest request = invocation.getArgument(0);
            return request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED);
        });

        when(authHandler.handleIfUnauthenticated(any(), any())).then(invocation -> {
            HttpRequest request = invocation.getArgument(0);
            return request.createResponse().setStatusCode(HttpStatusCode.UNAUTHORIZED);
        });

        when(authHandler.handleRequest(any(), any(), any())).then(invocation -> {
            Function<Principal, HttpResponse> unauthenticatedHandler = invocation.getArgument(2);
            return unauthenticatedHandler.apply(principal);
        });
        return principal;
    }

    public static Principal configureSuccessfulAuthHandler(AuthenticationHandler authHandler, Map<Claim<?>, Object> claims) {
        final Principal principal = makePrincipal(claims);
        when(authHandler.handleIfAuthenticated(any(), any())).then(a -> {
            Function<Principal, HttpResponse> handler = a.getArgument(1);
            return handler.apply(principal);
        });

        when(authHandler.handleIfUnauthenticated(any(), any())).then(a -> {
            Function<Principal, HttpResponse> handler = a.getArgument(1);
            return handler.apply(principal);
        });

        when(authHandler.handleRequest(any(), any(), any())).then(a -> {
            Function<Principal, HttpResponse> handler = a.getArgument(1);
            return handler.apply(principal);
        });
        return principal;
    }

    public static HashMap<Claim<?>, Object> getDefaultClaims() {
        HashMap<Claim<?>, Object> claims = new HashMap<>();
        claims.put(CommonClaims.USER_ID, 1L);
        claims.put(CommonClaims.EXPIRES, Instant.now().getEpochSecond() + 3600);
        return claims;
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

        doAnswer(invocation -> {
            Claim<?> claim = invocation.getArgument(0);
            claims.remove(claim);
            return null;
        }).when(principal).removeClaim(any(Claim.class));

        doAnswer(invocation -> {
            claims.clear();
            return null;
        }).when(principal).clearClaims();
        return principal;
    }

}
