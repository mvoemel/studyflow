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