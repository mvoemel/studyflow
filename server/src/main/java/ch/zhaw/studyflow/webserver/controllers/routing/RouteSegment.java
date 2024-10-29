package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.Tuple;

import java.util.ArrayList;
import java.util.List;

public sealed interface RouteSegment permits CaptureSegment, LiteralRouteSegment, StaticRouteSegment {
    static List<RouteSegment> segmentize(String route) {
        final char[] routeChars = route.toCharArray();

        final List<RouteSegment> segments = new ArrayList<>();

        int idx = 0;
        while (idx < routeChars.length && routeChars[idx] == '/') {
            idx++;
        }

        do {
            final char current = routeChars[idx];
            switch (current) {
                case '{':
                    final Tuple<Integer, RouteSegment> captureSegment = parseCaptureSegment(routeChars, idx);
                    segments.add(captureSegment.value2());
                    idx = captureSegment.value1();
                    break;
                case '/':
                    break;
                default:

                    break;
            }
        } while (true);
    }

    private static Tuple<Integer, RouteSegment> parseCaptureSegment(char[] routeChars, int idx) {
        int startIdx = idx++;

        boolean isCaptureCompleted = false;
        do {
            if (routeChars[idx] == '}') {
                isCaptureCompleted = 0 <= idx - 1 && routeChars[idx - 1] != '\\';
            }
            idx++;
        } while (!isCaptureCompleted);
        return Tuple.of(idx, new CaptureSegment(new String(routeChars, startIdx, idx - startIdx)));
    }
}
