package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoutingTreeBuilder {
    private TreeNode root = new TreeNode(null);

    public void append(final EndpointMetadata segmentedRoute) {
        RouteSegmentOld first = normalizeStartSegment(segmentedRoute.segments().getFirst());

        if(first.isCapture()) {
            throw new IllegalArgumentException("First segment must not be a capture segment");
        }


    }

    private RouteSegmentOld normalizeStartSegment(RouteSegmentOld segment) {
        String value = segment.value();
        if (!value.startsWith("/")) {
            value = "/" + value;
        }
        return new RouteSegmentOld(false, value);
    }

    private static class TreeNode {
        private final RouteSegmentOld segment;
        private final List<TreeNode> children;

        public TreeNode(RouteSegmentOld segment) {
            this.segment    = segment;
            this.children   = new ArrayList<>();
        }

        private RouteSegmentOld getSegment() {
            return segment;
        }

        private List<TreeNode> getChildren() {
            return Collections.unmodifiableList(children);
        }
    }
}
