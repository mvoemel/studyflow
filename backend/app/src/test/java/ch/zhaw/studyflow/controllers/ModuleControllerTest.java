package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.ReadableBodyContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.zhaw.studyflow.controllers.HttpMockHelpers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ModuleControllerTest {

    private ModuleController moduleController;
    private ModuleManager moduleManager;
    private AuthenticationHandler authenticationHandler;


    @BeforeEach
    void setUp() {
        moduleManager = mock(ModuleManager.class);
        authenticationHandler = mock(AuthenticationHandler.class);

        moduleController = new ModuleController(moduleManager, authenticationHandler);
    }

    @Test
    void testGetModule() {
        Module module = new Module();
        when(moduleManager.read(anyLong())).thenReturn(module);

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of("moduleId", "1"));

        final HttpResponse response = moduleController.getModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = HttpMockHelpers.captureResponseCode(response);
        final ArgumentCaptor<WritableBodyContent> responseBodyCapture = HttpMockHelpers.captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBodyCapture.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());

    }

    @Test
    void testGetModules() {
        Module module = new Module();
        when(moduleManager.getModules(anyLong(), anyLong(), anyLong())).thenReturn(List.of(module));

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of("userId", "1", "degreeId", "1", "semesterId", "1"));

        final HttpResponse response = moduleController.getModules(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = HttpMockHelpers.captureResponseCode(response);
        final ArgumentCaptor<WritableBodyContent> responseBodyCapture = HttpMockHelpers.captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBodyCapture.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());
    }

    @Test
    void testDeleteModule() {
        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of("moduleId", "1"));

        final HttpResponse response = moduleController.deleteModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = HttpMockHelpers.captureResponseCode(response);

        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCapture.getValue());
    }

    @Test
    void testUpdateModule() {
        Module module = new Module();

        AuthMockHelpers.configureSuccessfulAuthHandler(authenticationHandler, Map.of(
                CommonClaims.USER_ID, 1,
                CommonClaims.AUTHENTICATED, true
        ));

        final ReadableBodyContent bodyContent = HttpMockHelpers.makeJsonRequestBody(Module.class, module);
        final HttpRequest request = HttpMockHelpers.makeHttpRequest(bodyContent);
        final RequestContext context = HttpMockHelpers.makeRequestContext(request, Map.of());

        final HttpResponse response = moduleController.updateModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = HttpMockHelpers.captureResponseCode(response);

        verify(moduleManager, times(1)).update(module);
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());
    }

    @ParameterizedTest
    @MethodSource("provideTargets")
    void testGetModulesUnauthorized(Tuple<String, Function<Tuple<ModuleController, RequestContext>, HttpResponse>> target) {
        HttpRequest request = makeHttpRequest();
        AuthMockHelpers.configureFailingAuthHandler(authenticationHandler);

        HttpResponse response = target.value2().apply(new Tuple<>(moduleController, makeRequestContext(request)));

        ArgumentCaptor<HttpStatusCode> responseStatusCode = captureResponseCode(response);
        assertEquals(HttpStatusCode.UNAUTHORIZED, responseStatusCode.getValue());
    }

    public static Stream<Tuple<String, Function<Tuple<ModuleController, RequestContext>, HttpResponse>>> provideTargets() {
        return Stream.of(
                makeTarget("addModule", ctrl -> ctrl::addModule),
                makeTarget("getModules", ctrl -> ctrl::getModules),
                makeTarget("getModule", ctrl -> ctrl::getModule),
                makeTarget("updateModule", ctrl -> ctrl::updateModule),
                makeTarget("deleteModule", ctrl -> ctrl::deleteModule)
        );
    }

    private static <T> Tuple<String, Function<Tuple<T, RequestContext>, HttpResponse>> makeTarget(String name, Function<T, Function<RequestContext, HttpResponse>> targetInvoker) {
        return new Tuple<>(
                name,
                argument -> targetInvoker.apply(argument.value1()).apply(argument.value2())
        );
    }
}
