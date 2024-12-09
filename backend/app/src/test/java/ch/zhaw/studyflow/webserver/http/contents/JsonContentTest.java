package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonContentTest {

    @Test
    void testWritableJsonContent() throws Exception {
        String content = "test content";
        WritableBodyContent writableContent = JsonContent.writableOf(content);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writableContent.write(outputStream);

        String result = outputStream.toString();
        assertEquals("\"test content\"", result);
    }

    @Test
    void testReadableJsonContent() throws Exception {
        String json = "{\"key\":\"value\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = JsonContent.readableOf(
                JsonContent.MIME_TYPE_JSON,
                serviceCollection,
                Map.of(),
                inputStream
        );

        Map<String, String> result = readableContent.read(Map.class);
        assertEquals("value", result.get("key"));
    }

    @Test
    void testReadableJsonContentTryRead() {
        String json = "{\"key\":\"value\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = JsonContent.readableOf(
                JsonContent.MIME_TYPE_JSON,
                serviceCollection,
                Map.of(),
                inputStream
        );

        var result = readableContent.tryRead(Map.class);
        assertTrue(result.isPresent());
        assertEquals("value", result.get().get("key"));
    }

    @Test
    void testReadableJsonContentInvalidType() {
        String json = "{\"key\":\"value\"}";
        InputStream inputStream = new ByteArrayInputStream(json.getBytes());
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = JsonContent.readableOf(
                JsonContent.MIME_TYPE_JSON,
                serviceCollection,
                Map.of(),
                inputStream
        );

        assertThrows(com.fasterxml.jackson.databind.exc.MismatchedInputException.class, () -> readableContent.read(Integer.class));
    }
}