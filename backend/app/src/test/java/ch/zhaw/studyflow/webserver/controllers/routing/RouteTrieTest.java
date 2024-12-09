package ch.zhaw.studyflow.webserver.controllers.routing;

import ch.zhaw.studyflow.webserver.http.HttpMethod;
import ch.zhaw.studyflow.utils.Tuple;
import ch.zhaw.studyflow.webserver.controllers.EndpointMetadata;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RouteTrieTest {
    @Test
    void testEmptyRouteMapToRoot() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("");

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

    @Test
    void testUnfinishedRouteMatch() {
        RouteTrie routeTrie = new RouteTrie();
        final EndpointMetadata endpointA = createEndpoint(HttpMethod.GET, "/api/v1/user/{test}");

        routeTrie.insert(endpointA);
        Optional<Tuple<EndpointMetadata, List<String>>> result = routeTrie.retrieve(HttpMethod.GET, List.of("api", "v1", "user", "1234", "degrees"));
        assertTrue(result.isEmpty());
    }

    @Test
    void testCorrectRouteMatch() {
        RouteTrie routeTrie = new RouteTrie();
        final EndpointMetadata incorrectEndpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/{test}");
        final EndpointMetadata correctEndpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/{test}/degrees");

        routeTrie.insert(incorrectEndpoint);
        routeTrie.insert(correctEndpoint);
        Optional<Tuple<EndpointMetadata, List<String>>> result = routeTrie.retrieve(HttpMethod.GET, List.of("api", "v1", "user", "1234", "degrees"));
        assertTrue(result.isPresent());
        final Tuple<EndpointMetadata, List<String>> endpoint = result.get();
        assertEquals(correctEndpoint, endpoint.value1());
        assertEquals("degrees", endpoint.value1().route().segments().getLast().value());
    }

    @Test
    void testInsertAndRetrieveStaticRoute() {
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
    void testInsertAndRetrieveDynamicRoute() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/{id}");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("api", "v1", "user", "1234");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(1, retrieveResult.get().value2().size());
        assertEquals("1234", retrieveResult.get().value2().get(0));
    }

    @Test
    void testInsertAndRetrieveMultipleDynamicSegments() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/{userId}/games/{gameId}");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        final List<String> path = List.of("api", "v1", "user", "1234", "games", "666");

        Optional<Tuple<EndpointMetadata, List<String>>> retrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(retrieveResult.isPresent());
        assertEquals(endpoint, retrieveResult.get().value1());
        assertEquals(2, retrieveResult.get().value2().size());
        assertEquals("1234", retrieveResult.get().value2().get(0));
        assertEquals("666", retrieveResult.get().value2().get(1));
    }

    @Test
    void testInsertAndRetrieveWithDifferentHttpMethods() {
        final EndpointMetadata getEndpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/current");
        final EndpointMetadata postEndpoint = createEndpoint(HttpMethod.POST, "/api/v1/user/current");

        RouteTrie trie = new RouteTrie();
        trie.insert(getEndpoint);
        trie.insert(postEndpoint);

        final List<String> path = List.of("api", "v1", "user", "current");

        Optional<Tuple<EndpointMetadata, List<String>>> getRetrieveResult = trie.retrieve(HttpMethod.GET, path);
        assertTrue(getRetrieveResult.isPresent());
        assertEquals(getEndpoint, getRetrieveResult.get().value1());

        Optional<Tuple<EndpointMetadata, List<String>>> postRetrieveResult = trie.retrieve(HttpMethod.POST, path);
        assertTrue(postRetrieveResult.isPresent());
        assertEquals(postEndpoint, postRetrieveResult.get().value1());
    }

    @Test
    void testInsertDuplicateRouteThrowsException() {
        final EndpointMetadata endpoint = createEndpoint(HttpMethod.GET, "/api/v1/user/current");

        RouteTrie trie = new RouteTrie();
        trie.insert(endpoint);

        assertThrows(UnsupportedOperationException.class, () -> trie.insert(endpoint));
    }

    private static EndpointMetadata createEndpoint(HttpMethod method, String route) {
        return new EndpointMetadata(
                null,
                method,
                RestRoute.of(route),
                null
        );
    }
}