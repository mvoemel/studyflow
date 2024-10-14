package ch.zhaw.studyflow.webserver.controllers.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record RouteSegmentOld(boolean isCapture, String value) {
    public static List<RouteSegmentOld> segmentize(String route) {
        int indexOfOpenBrace = route.indexOf("{");

        List<RouteSegmentOld> routeSegments = new ArrayList<>();
        if (indexOfOpenBrace != -1) {
            int indexOfCloseBrace = -1;

            do {
                indexOfCloseBrace++;
                routeSegments.add(new RouteSegmentOld(false, route.substring(indexOfCloseBrace, indexOfOpenBrace)));
                indexOfCloseBrace = route.indexOf("}", indexOfOpenBrace);

                String braceContent = route.substring(indexOfOpenBrace + 1, indexOfCloseBrace);

                routeSegments.add(new RouteSegmentOld(true, braceContent));
                indexOfOpenBrace = route.indexOf("{", indexOfCloseBrace);

            } while (indexOfOpenBrace != -1);

            if (indexOfCloseBrace != route.length() - 1) {
                routeSegments.add(new RouteSegmentOld(false, route.substring(indexOfCloseBrace + 1)));
            }
        } else {
            routeSegments.add(new RouteSegmentOld(false, route));
        }
        return Collections.unmodifiableList(routeSegments);
    }
}