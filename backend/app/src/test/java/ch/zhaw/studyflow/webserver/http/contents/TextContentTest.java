package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextContentTest {

    @Test
    void testWritableTextContent() throws Exception {
        String content = "test content";
        WritableBodyContent writableContent = TextContent.writableOf(content);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        writableContent.write(outputStream);

        String result = outputStream.toString(StandardCharsets.UTF_8);
        assertEquals("test content", result);
    }

    @Test
    void testReadableTextContent() throws Exception {
        String text = "test content";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = TextContent.readableOf(
                TextContent.MIME_TEXT_PLAIN,
                serviceCollection,
                Map.of(),
                inputStream
        );

        String result = readableContent.read(String.class);
        assertEquals("test content", result);
    }

    @Test
    void testReadableTextContentTryRead() {
        String text = "test content";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = TextContent.readableOf(
                TextContent.MIME_TEXT_PLAIN,
                serviceCollection,
                Map.of(),
                inputStream
        );

        var result = readableContent.tryRead(String.class);
        assertTrue(result.isPresent());
        assertEquals("test content", result.get());
    }

    @Test
    void testReadableTextContentInvalidType() {
        String text = "test content";
        InputStream inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        ServiceCollection serviceCollection = Mockito.mock(ServiceCollection.class);
        ReadableBodyContent readableContent = TextContent.readableOf(
                TextContent.MIME_TEXT_PLAIN,
                serviceCollection,
                Map.of(),
                inputStream
        );

        assertThrows(IllegalArgumentException.class, () -> readableContent.read(Integer.class));
    }
}