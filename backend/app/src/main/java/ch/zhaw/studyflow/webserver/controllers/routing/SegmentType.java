package ch.zhaw.studyflow.webserver.controllers.routing;

/**
 * Represents the type of segment in a route.
 * For more info see {@link RestRoute}
 */
public enum SegmentType {
    /**
     * Represents a static segment.
     */
    STATIC,

    /**
     * Represents a dynamic segment.
     */
    CAPTURE
}
