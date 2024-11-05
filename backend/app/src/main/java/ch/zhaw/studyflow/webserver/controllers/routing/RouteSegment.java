package ch.zhaw.studyflow.webserver.controllers.routing;

public record RouteSegment(SegmentType type, String value) {
    public boolean is(SegmentType type) {
        return this.type == type;
    }
}
