package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.controllers.deo.ModuleDeo;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import ch.zhaw.studyflow.webserver.http.HttpStatusCode;
import ch.zhaw.studyflow.webserver.http.contents.JsonContent;
import ch.zhaw.studyflow.webserver.http.contents.WritableBodyContent;
import ch.zhaw.studyflow.webserver.http.pipeline.RequestContext;
import ch.zhaw.studyflow.webserver.security.authentication.AuthenticationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static ch.zhaw.studyflow.controllers.AuthMockHelpers.configureSuccessfulAuthHandler;
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
    void testAddModule() {
        configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        final ModuleDeo module = new ModuleDeo();
        module.setId(-1);
        module.setName("Test");
        module.setEcts(4);
        module.setDegreeId(0);
        module.setSemesterId(1);
        module.setDescription("Test");
        module.setSemesterId(1);
        module.setTime(4);
        module.setUnderstanding(4);
        module.setComplexity(4);

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(ModuleDeo.class, module));
        final RequestContext context = makeRequestContext(request);

        final HttpResponse response = moduleController.addModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = captureResponseCode(response);
        assertEquals(HttpStatusCode.CREATED, statusCodeCapture.getValue());

        verify(moduleManager, never()).create(any(Module.class), eq(1L), eq(0L), eq(0L));
    }

    @Test
    void testGetModule() {
        Module module = new Module();
        when(moduleManager.read(anyLong())).thenReturn(module);

        configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of("moduleId", "1"));

        final HttpResponse response = moduleController.getModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = captureResponseCode(response);
        final ArgumentCaptor<WritableBodyContent> responseBodyCapture = captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBodyCapture.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());
    }

    @Test
    void testGetModules() {
        Module module = new Module();
        when(moduleManager.getModules(anyLong())).thenReturn(List.of(module));

        configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of());

        final HttpResponse response = moduleController.getModules(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = captureResponseCode(response);
        final ArgumentCaptor<WritableBodyContent> responseBodyCapture = captureResponseBody(response);

        assertInstanceOf(JsonContent.class, responseBodyCapture.getValue());
        assertEquals(HttpStatusCode.OK, statusCodeCapture.getValue());
    }

    @Test
    void testDeleteModule() {
        configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        final HttpRequest request = makeHttpRequest();
        final RequestContext context = makeRequestContext(request, Map.of("moduleId", "1"));

        final HttpResponse response = moduleController.deleteModule(context);

        final ArgumentCaptor<HttpStatusCode> statusCodeCapture = captureResponseCode(response);

        assertEquals(HttpStatusCode.NO_CONTENT, statusCodeCapture.getValue());
    }

    @Test
    void testUpdateModule() {
        final ModuleDeo moduleDeo = new ModuleDeo();
        moduleDeo.setName("Test");
        moduleDeo.setId(1L);
        moduleDeo.setSemesterId(1L);
        moduleDeo.setDegreeId(1L);
        moduleDeo.setDescription("Test");
        moduleDeo.setEcts(1);
        moduleDeo.setUnderstanding(1);
        moduleDeo.setComplexity(1);
        moduleDeo.setTime(1);

        final Module module = testModule();

        configureSuccessfulAuthHandler(authenticationHandler, AuthMockHelpers.getDefaultClaims());

        when(moduleManager.getModule(1L)).thenReturn(Optional.of(module));

        final HttpRequest request = makeHttpRequest(makeJsonRequestBody(ModuleDeo.class, moduleDeo));

        moduleController.updateModule(makeRequestContext(request, Map.of("id", "1")));

        verify(moduleManager, times(1)).update(module);

        assertEquals(moduleDeo.getName(), module.getName());
        assertEquals(moduleDeo.getDescription(), module.getDescription());
        assertEquals(moduleDeo.getEcts(), module.getECTS());
    }

    @ParameterizedTest
    @MethodSource("provideTargets")
    void testAuthorizationTest(Tuple<String, Function<Tuple<ModuleController, RequestContext>, HttpResponse>> target) {
        HttpRequest request = makeHttpRequest(makeJsonRequestBody(Semester.class, new Semester()));
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

    private static Module testModule() {
        Module module = new Module();
        module.setName("Test");
        module.setSemesterId(1);
        module.setDescription("Test");
        module.setUnderstanding(1);
        module.setComplexity(1);
        module.setTime(1);
        return module;
    }
}
