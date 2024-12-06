package ch.zhaw.studyflow.webserver.controllers.routing;

/**
 * Represents a segment of a route.
 * A segment has a type and a value. The type is used to determine if the segment is a static or capture segment.
 * If the segment is a capture segment, the value contains the name of the capture. Otherwise, the value contains the
 * static value of the segment (e.g. "users").
 */
public record RouteSegment(SegmentType type, String value) {

    /**
     * Checks if the segment is of the given type.
     * @param type The type to check.
     * @return true if the segment is of the given type, false otherwise.
     */
    public boolean is(SegmentType type) {
        return this.type == type;
    }
}
