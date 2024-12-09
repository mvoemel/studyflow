package ch.zhaw.studyflow.webserver.controllers.routing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


import static org.junit.jupiter.api.Assertions.*;

class RestRouteTest {

    @Test
    void testEmptyRouteSegmentation() {
        RestRoute segment = RestRoute.of("/");
        assertEquals(0, segment.segments().size());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/v1/user/current/",
            "api/v1/user/current",
            "/api/v1/user/current"
    })
    void testStaticRouteSegmentation(String route) {
        RestRoute segment = RestRoute.of(route);
        assertEquals(4, segment.segments().size());

        for (RouteSegment routeSegment : segment.segments()) {
            assertEquals(SegmentType.STATIC, routeSegment.type());
        }

        assertEquals("api", segment.segments().get(0).value());
        assertEquals("v1", segment.segments().get(1).value());
        assertEquals("user", segment.segments().get(2).value());
        assertEquals("current", segment.segments().get(3).value());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/api/v1/user//"
    })
    void throwOnInvalidRoutes(String route) {
        assertThrows(IllegalArgumentException.class, () -> RestRoute.of(route));
    }

    @Test
    void throwOnUnmatchedCapture() {
        assertThrows(IllegalArgumentException.class, () -> RestRoute.of("/api/v1/user/{id"));
    }

    @Test
    void throwOnEmptyCapture() {
        assertThrows(IllegalArgumentException.class, () -> RestRoute.of("/api/v1/user/{}"));
    }

    @Test
    void testSingleCaptureRouteSegmentation() {
        RestRoute segment = RestRoute.of("/api/v1/user/{id}");
        assertEquals(4, segment.segments().size());

        assertEquals(SegmentType.STATIC, segment.segments().get(0).type());
        assertEquals(SegmentType.STATIC, segment.segments().get(1).type());
        assertEquals(SegmentType.STATIC, segment.segments().get(2).type());
        assertEquals(SegmentType.CAPTURE, segment.segments().get(3).type());

        assertEquals("api", segment.segments().get(0).value());
        assertEquals("v1", segment.segments().get(1).value());
        assertEquals("user", segment.segments().get(2).value());
        assertEquals("id", segment.segments().get(3).value());
    }
}