package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.Tuple;

import java.util.ArrayList;
import java.util.List;

public record RouteSegment(SegmentType type, String value) {
    public boolean is(SegmentType type) {
        return this.type == type;
    }
}
