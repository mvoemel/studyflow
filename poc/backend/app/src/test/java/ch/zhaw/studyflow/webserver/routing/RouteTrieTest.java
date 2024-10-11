package ch.zhaw.studyflow.webserver.routing;

import ch.zhaw.studyflow.webserver.HttpMethod;
import ch.zhaw.studyflow.webserver.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import ch.zhaw.studyflow.webserver.controllers.routing.RestRoute;
import ch.zhaw.studyflow.webserver.controllers.routing.RouteTrie;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RouteTrieTest {
    @Test
    void testEmptyRouteMapToRoot() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("/");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(0, retrieveResult.get().value2().size());
    }

    @Test
    void testDynamicSegmentCapture() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/{id}");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("api", "v1", "user", "1234");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(1, retrieveResult.get().value2().size());
        assertEquals("1234", retrieveResult.get().value2().getFirst());
    }

    @Test
    void testStaticSegmentMatch() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/current");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("api", "v1", "user", "current");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(0, retrieveResult.get().value2().size());
    }

    @Test
    void testMultipleDynamicSegmentCaptures() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET,"/api/v1/user/{userId}/games/{gameId}");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("api", "v1", "user", "1234", "games", "666");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(2, retrieveResult.get().value2().size());
        assertEquals("1234", retrieveResult.get().value2().getFirst());
        assertEquals("666", retrieveResult.get().value2().getLast());
    }

    @Test
    void testHttpMethodRouteSeparation() {
        final EndpointMetadata getEndpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/current");
        final EndpointMetadata deleteEndpoint = createEndpoint(HttpMethod.DELETE, "/api/v1/user/current");

        RouteTrie trie = new RouteTrie();
        trie.insert(getEndpoint);
        trie.insert(deleteEndpoint);

        final List<String> path = List.of("api", "v1", "user", "current");

        Optional<Tuple<EndpointMetadata, List<String>>> getRetrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(getRetrieveResult.isPresent());
        assertEquals(getEndpoint, getRetrieveResult.get().value1());
        assertEquals(0, getRetrieveResult.get().value2().size());

        Optional<Tuple<EndpointMetadata, List<String>>> deleteRetrieveResult = trie.retrieve(HttpMethod.DELETE, path);
        assertTrue(deleteRetrieveResult.isPresent());
        assertEquals(deleteEndpoint, deleteRetrieveResult.get().value1());
        assertEquals(0, deleteRetrieveResult.get().value2().size());
    }

    @Test
    void testThrowOnReSettingEndpoint() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/current");

        RouteTrie routeTrie = new RouteTrie();
        routeTrie.insert(endpoint);

        assertThrows(UnsupportedOperationException.class, () -> routeTrie.insert(endpoint));
    }

    private static EndpointMetadata createEndpoint(HttpMethod method, String route) {
        Objects.requireNonNull(method);
        Objects.requireNonNull(route);

        return new EndpointMetadata(
                method,
                RestRoute.of(route),
                null,
                null
        );
    }
}
