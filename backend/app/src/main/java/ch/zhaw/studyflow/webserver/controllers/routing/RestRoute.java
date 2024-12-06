package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.utils.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a route in a REST API.
 * A route consists of segments that are either static or capture segments. Capture segments are used to capture parts
 * of the route. Static segments are fixed parts of the route.
 */
public record RestRoute(List<RouteSegment> segments) {
    public RestRoute {
        segments = Collections.unmodifiableList(segments);
    }

    /**
     * Parses the passed route into a REST route.
     * Routes consist of segments that are either static or capture segments.
     * Capture segments are enclosed in curly braces and are used to capture a part of the route.
     * Static segments are fixed parts of the route.
     *
     * @param route The route string to parse.
     * @return The parsed REST route.
     */
    public static RestRoute of(String route) {
        final char[] routeChars = route.toCharArray();

        final List<RouteSegment> segments = new ArrayList<>();

        int idx = 0;
        while (idx < routeChars.length && routeChars[idx] == '/') {
            idx++;
        }

        int start = idx;
        while (idx < routeChars.length) {
            final char current = routeChars[idx];
            switch (current) {
                case '{':
                    final Tuple<Integer, RouteSegment> captureSegment = parseCaptureSegment(routeChars, idx);
                    segments.add(captureSegment.value2());
                    idx = captureSegment.value1();
                    start = idx;
                    break;
                case '/':
                    if (idx - start <= 0)
                        throw new IllegalArgumentException("Empty segment encountered: '%s'".formatted(new String(routeChars)));
                    segments.add(new RouteSegment(SegmentType.STATIC, new String(routeChars, start, idx - start)));
                    start = ++idx;
                    break;
                default:
                    idx++;
                    break;
            }
        }
        if (start != idx) {
            segments.add(new RouteSegment(SegmentType.STATIC, new String(routeChars, start, idx - start)));
        }

        return new RestRoute(segments);
    }

    private static Tuple<Integer, RouteSegment> parseCaptureSegment(char[] routeChars, int idx) {
        int startIdx = idx++;

        boolean isCaptureCompleted = false;
        while (idx < routeChars.length && !isCaptureCompleted) {
            if (routeChars[idx] == '}') {
                isCaptureCompleted = 0 <= idx - 1 && routeChars[idx - 1] != '\\';
            }
            idx++;
        }

        if (!isCaptureCompleted) {
            throw new IllegalArgumentException("Unclosed capture segment encountered: '%s'".formatted(new String(routeChars)));
        }

        if (idx - startIdx == 2) {
            throw new IllegalArgumentException("Capture segment must contain a name '{<name>}'.");
        }

        if (idx < routeChars.length && routeChars[idx] != '/') {
            throw new IllegalArgumentException("Capture segment must be followed by a '/' (end of segment).");
        }

        return Tuple.of(idx + 1, new RouteSegment(SegmentType.CAPTURE, new String(routeChars, startIdx + 1, idx - startIdx - 2)));
    }
}
