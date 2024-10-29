package ch.zhaw.studyflow.services;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MapServiceCollectionTest {
    @Test
    void testGetRequiredService() {
        ServiceCollection serviceCollection = new MapServiceCollection(Map.of(String.class, sc -> "test"));
        String service = serviceCollection.getRequiredService(String.class);
        assert(service.equals("test"));
    }

    @Test
    void testGetService() {
        ServiceCollection serviceCollection = new MapServiceCollection(Map.of(String.class, sc -> "test"));
        Optional<String> service = serviceCollection.getService(String.class);
        assertTrue(service.isPresent());
        assertEquals("test", service.get());
    }

    @Test
    void testGetRequiredServiceMissing() {
        ServiceCollection serviceCollection = new MapServiceCollection(Map.of());
        assertThrows(IllegalArgumentException.class, () -> serviceCollection.getRequiredService(String.class));
    }

    @Test
    void testGetServiceMissing() {
        ServiceCollection serviceCollection = new MapServiceCollection(Map.of());
        assertFalse(serviceCollection.getService(String.class).isPresent());
    }

    @Test
    void testSingeltonFactory() {
        ServiceCollection builder = new MapServiceCollectionBuilder()
                .registerSingelton(Object.class, sc -> new Object())
                .build();
        Object service1 = builder.getRequiredService(Object.class);
        Object service2 = builder.getRequiredService(Object.class);
        assertEquals(service1, service2);
    }

    @Test
    void testIncorrectServiceType() {
        ServiceCollection serviceCollection = new MapServiceCollection(Map.of(String.class, sc -> new Object()));
        assertThrows(IllegalArgumentException.class, () -> serviceCollection.getRequiredService(String.class));
    }
}
