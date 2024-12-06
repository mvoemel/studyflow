package ch.zhaw.studyflow.webserver.controllers.routing;

/**
 * Represents the type of segment in a route.
 */
public enum SegmentType {
    /**
     * A static segment is a segment that is not a parameter.
     */
    STATIC,
    /**
     * A parameter segment is a segment that is a parameter and is extracted from the path and passed
     * to the handler.
     */
    CAPTURE
}
