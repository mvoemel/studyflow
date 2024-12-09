package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the content of a text body.
 * Use {@link #writableOf(String)} to create a writable text content or
 * {@link #readableOf(String, ServiceCollection, Map, InputStream)} to create a readable text content.
 */
public class TextContent implements BodyContent {
    public static final String MIME_TEXT_PLAIN = "text/plain";

    private final String contentType;
    private final Charset charset;

    protected TextContent(String contentType, Charset charset) {
        Objects.requireNonNull(contentType);
        Objects.requireNonNull(charset);

        this.contentType    = contentType;
        this.charset        = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    @Override
    public String getContentTypeHeader() {
        return "Content-Type: " + contentType + (charset != null ? "; charset=" + charset.name().toLowerCase() : "");
    }

    @Override
    public long getContentLength() {
        return 0;
    }


    public static WritableBodyContent writableOf(String content) {
        return new WritableTextContent(MIME_TEXT_PLAIN, StandardCharsets.UTF_8, content);
    }

    public static ReadableBodyContent readableOf(String mimeType, ServiceCollection serviceCollection, Map<String, String> properties, InputStream inputStream) {
        return new ReadableTextContent(mimeType, StandardCharsets.UTF_8, inputStream);
    }

    private static class ReadableTextContent extends TextContent implements ReadableBodyContent {
        private final InputStream inputStream;
        private boolean hasBeenRead;
        private String valueRead;


        public ReadableTextContent(String contentType, Charset charset, InputStream inputStream) {
            super(contentType, charset);
            this.inputStream = inputStream;
        }


        @Override
        public <T> T read(Class<T> valueType) throws IOException {
            if (!valueType.equals(String.class)) {
                throw new IllegalArgumentException("Cannot read content as " + valueType.getSimpleName());
            }
            if (!hasBeenRead) {
                hasBeenRead = true;
                valueRead = readInternal();
            }
            return valueType.cast(valueRead);
        }

        @Override
        public <T> Optional<T> tryRead(Class<T> valueType) {
            try {
                return Optional.of(read(valueType));
            } catch (IOException e) {
                return Optional.empty();
            }
        }

        private String readInternal() throws IOException {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            inputStream.transferTo(buffer);
            return buffer.toString(getCharset());
        }
    }

    private static class WritableTextContent extends TextContent implements WritableBodyContent {
        private final Charset charset;
        private final String content;


        public WritableTextContent(String contentType, Charset charset, String content) {
            super(contentType, charset);
            this.charset    = charset;
            this.content    = content;
        }


        @Override
        public void write(OutputStream outputStream) throws IOException {
            try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, charset)) {
                writer.write(content);
            }
        }
    }
}
