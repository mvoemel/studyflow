package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapReadableBodyContentFactoryTest {

    @Test
    void testCreateWithValidMimeType() {
        ServiceCollection services = Mockito.mock(ServiceCollection.class);
        ReadableBodyContentInstanceFactory instanceFactory = Mockito.mock(ReadableBodyContentInstanceFactory.class);
        ReadableBodyContent readableBodyContent = Mockito.mock(ReadableBodyContent.class);

        when(instanceFactory.create(anyString(), any(ServiceCollection.class), anyMap(), any(InputStream.class)))
                .thenReturn(readableBodyContent);

        Map<String, ReadableBodyContentInstanceFactory> factories = Map.of("application/json", instanceFactory);
        MapReadableBodyContentFactory factory = new MapReadableBodyContentFactory(services, factories);

        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());
        ReadableBodyContent result = factory.create("application/json", Map.of(), inputStream);

        assertNotNull(result);
        assertEquals(readableBodyContent, result);
    }

    @Test
    void testCreateWithInvalidMimeType() {
        ServiceCollection services = Mockito.mock(ServiceCollection.class);
        Map<String, ReadableBodyContentInstanceFactory> factories = Map.of();
        MapReadableBodyContentFactory factory = new MapReadableBodyContentFactory(services, factories);

        InputStream inputStream = new ByteArrayInputStream("{}".getBytes());

        assertThrows(IllegalArgumentException.class, () -> factory.create("application/xml", Map.of(), inputStream));
    }
}