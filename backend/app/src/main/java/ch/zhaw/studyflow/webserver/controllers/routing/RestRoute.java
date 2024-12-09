package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.utils.Tuple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a route in a RESTful API.
 * A route consists of segments, which can be either static or capture segments. The content of capture segments in
 * an incoming request are stored in a {@link ch.zhaw.studyflow.webserver.http.CaptureContainer} and passed to the handler.
 */
public record RestRoute(List<RouteSegment> segments) {
    public RestRoute {
        segments = Collections.unmodifiableList(segments);
    }

    /**
     * Parses a route string into a {@link RestRoute}. A route string consists of segments, which can be either static
     * or capture segments.
     * Statics segments are fixed parts of the route, while capture segments are placeholders for dynamic parts of the route.
     * The syntax for a capture segment is '{<name>}', where <name> is the name of the capture used to access it in the
     * handler (using the {@link ch.zhaw.studyflow.webserver.http.CaptureContainer}).
     * @param route the route string.
     * @return the parsed route.
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
