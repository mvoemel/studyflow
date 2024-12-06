package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * A trie data structure for storing and retrieving routes.
 * The trie is used during routing to find the correct endpoint for a given route.
 */
public class RouteTrie {
    private final RouteTrieNode root;

    /**
     * Creates a new route trie.
     */
    public RouteTrie() {
        this.root   = new RouteTrieNode(null);
    }

    /**
     * Inserts a new endpoint into the trie.
     * The endpoint is inserted into the trie based on the route and the http method. If an endpoint already exists for
     * the given route and http method, the new endpoint replaces the old one.
     * @param endpoint The endpoint to insert.
     */
    public void insert(EndpointMetadata endpoint) {
        Objects.requireNonNull(endpoint);

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
                if (segment.is(SegmentType.CAPTURE)) {
                    children.addLast(newNode);
                } else {
                    children.addFirst(newNode);
                }
                current = newNode;
            }
        }

        current.setEndpoint(endpoint);
    }

    /**
     * Retrieves the endpoint for the given route and http method.
     * @param method The http method of the route.
     * @param routeSegments The segments of the route.
     * @return An optional containing the endpoint and the captures if the route was found, an empty optional otherwise.
     */
    public Optional<Tuple<EndpointMetadata, List<String>>> retrieve(HttpMethod method, List<String> routeSegments) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(routeSegments);

        if (routeSegments.getFirst().isEmpty()) {
            routeSegments = routeSegments.subList(1, routeSegments.size());
        }

        RouteTrieNode current = getHttpMethodRoot(method);
        final List<String> captures = new ArrayList<>();

        int processedCounter = 0;
        boolean foundMatch;
        Iterator<String> iterator = routeSegments.iterator();
        while (iterator.hasNext()) {
            String segment = iterator.next();
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
            processedCounter++;
        }


        Optional<Tuple<EndpointMetadata, List<String>>> result;
        if (current.getEndpoint() == null || processedCounter != routeSegments.size()) {
            result = Optional.empty();
        } else {
            result = Optional.of(Tuple.of(current.getEndpoint(), captures));
        }
        return result;
    }


    /**
     * Retrieves the root node for the given http method.
     * @param method The http method.
     * @return The root node for the given http method. If the node does not exist, it is created.
     */
    private RouteTrieNode getHttpMethodRoot(HttpMethod method) {
        return root.children.stream()
                .filter(e -> e.getSegment().value().equalsIgnoreCase(method.methodName()))
                .findFirst()
                .orElseGet(() -> {
                    final RouteTrieNode newNode = new RouteTrieNode(new RouteSegment(SegmentType.STATIC, method.methodName()));
                    root.getChildren().add(newNode);
                    return newNode;
                });
    }

    /**
     * Checks if the current segment is a capture segment.
     * @param current The current segment.
     * @param toInsert The segment to insert.
     * @return true if the current segment is a capture segment and the toInsert segment is a static segment, false otherwise.
     */
    private static boolean captureSegmentMatch(RouteSegment current, RouteSegment toInsert) {
        return current.is(SegmentType.CAPTURE);
    }

    /**
     * Checks if the current segment is a static segment and the toInsert segment has the same value.
     * @param current The current segment.
     * @param toInsert The segment to insert.
     * @return true if the current segment is a static segment and the toInsert segment has the same value, false otherwise.
     */
    private static boolean staticSegmentMatch(RouteSegment current, RouteSegment toInsert) {
        return current.is(SegmentType.STATIC) && current.value().equalsIgnoreCase(toInsert.value());
    }

    /**
     * Represents a node in the route trie.
     * A node contains the segment it represents, a list of children and an endpoint (optional).
     */
    private static final class RouteTrieNode {
        private final RouteSegment segment;
        private final List<RouteTrieNode> children;
        private EndpointMetadata endpoint;


        public RouteTrieNode(RouteSegment segment) {
            this.segment    = segment;
            this.children   = new ArrayList<>();
        }

        /**
         * Gets the segment of the node.
         * @return The segment of the node.
         */
        public RouteSegment getSegment() {
            return segment;
        }

        /**
         * Gets the children of the node.
         * @return The children of the node.
         */
        public List<RouteTrieNode> getChildren() {
            return children;
        }

        /**
         * Gets the endpoint of the node.
         * @return The endpoint of the node.
         */
        public EndpointMetadata getEndpoint() {
            return endpoint;
        }

        /**
         * Sets the endpoint of the node.
         * @param endpoint The endpoint to set.
         */
        public void setEndpoint(EndpointMetadata endpoint) {
            if (this.endpoint != null) {
                throw new UnsupportedOperationException("After an endpoint is set, it cannot be changed.");
            }
            this.endpoint = endpoint;
        }
    }
}
