package ch.zhaw.studyflow.webserver.controllers.routing;

/**
 * Represents a segment of a route.
 * A segment can be a static segment or a dynamic segment.
 * @param type The type of the segment.
 * @param value The value of the segment.
 */
public record RouteSegment(SegmentType type, String value) {
    public boolean is(SegmentType type) {
        return this.type == type;
    }
}
