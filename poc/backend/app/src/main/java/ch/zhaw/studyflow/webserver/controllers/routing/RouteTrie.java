package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

public class RouteTrie {
    private RouteTrieNode root;

    public RouteTrie() {
        this.root   = new RouteTrieNode(null);
    }

    public void insert(EndpointMetadata endpoint) {
        RouteTrieNode current = getHttpMethodRoot(endpoint.method());

        for (RouteSegment segment : endpoint.route().segments()) {
            final BiPredicate<RouteSegment, RouteSegment> matchPredicate
                    = segment.is(SegmentType.CAPTURE) ? RouteTrie::captureSegmentMatch : RouteTrie::staticSegmentMatch;

            boolean foundMatch = false;
            final List<RouteTrieNode> children = current.getChildren();
            for (int idx = 0; idx < children.size(); idx++) {
                if (matchPredicate.test(children.get(idx).getSegment(), segment)) {
                    current = children.get(idx);
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                final RouteTrieNode newNode = new RouteTrieNode(segment);
                children.add(newNode);
                current = newNode;
            }
        }

        current.setEndpoint(endpoint);
    }

    private RouteTrieNode getHttpMethodRoot(HttpMethod method) {
        return root.children.stream().filter(e -> e.getSegment().value().equalsIgnoreCase(method.methodName())).findFirst()
                .orElseGet(() -> {
                    final RouteTrieNode newNode = new RouteTrieNode(new RouteSegment(SegmentType.STATIC, method.methodName()));
                    root.getChildren().add(newNode);
                    return newNode;
                });
    }


    public Optional<Tuple<EndpointMetadata, List<String>>> retrieve(HttpMethod method, List<String> routeSegments) {
        RouteTrieNode current = getHttpMethodRoot(method);
        final List<String> captures = new ArrayList<>();

        boolean foundMatch;
        for (String segment : routeSegments) {
            foundMatch = false;

            for (RouteTrieNode child : current.getChildren()) {
                RouteSegment childSegment = child.getSegment();

                if (childSegment.is(SegmentType.STATIC) && childSegment.value().equalsIgnoreCase(segment)) {
                    current = child;
                    foundMatch = true;
                    break;
                } else if (childSegment.is(SegmentType.CAPTURE)) {
                    captures.add(segment);
                    current = child;
                    foundMatch = true;
                    break;
                }
            }

            if (!foundMatch) {
                break;
            }
        }

        Optional<Tuple<EndpointMetadata, List<String>>> result;
        if (current.getEndpoint() == null) {
            result = Optional.empty();
        } else {
            result = Optional.of(Tuple.of(current.getEndpoint(), captures));
        }
        return result;
    }

    private static boolean captureSegmentMatch(RouteSegment current, RouteSegment toInsert) {
        return current.is(SegmentType.CAPTURE);
    }

    private static boolean staticSegmentMatch(RouteSegment current, RouteSegment toInsert) {
        return current.is(SegmentType.STATIC) && current.value().equalsIgnoreCase(toInsert.value());
    }

    private static final class RouteTrieNode {
        private final RouteSegment segment;
        private final List<RouteTrieNode> children;
        private EndpointMetadata endpoint;


        public RouteTrieNode(RouteSegment segment) {
            this.segment    = segment;
            this.children   = new ArrayList<>();
        }

        public RouteSegment getSegment() {
            return segment;
        }

        public List<RouteTrieNode> getChildren() {
            return children;
        }

        public EndpointMetadata getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(EndpointMetadata endpoint) {
            if (this.endpoint != null) {
                throw new UnsupportedOperationException("After an endpoint is set, it cannot be changed.");
            }
            this.endpoint = endpoint;
        }
    }
}
