package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a JSON content in an HTTP request or response.
 */
public class JsonContent implements BodyContent {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static final String MIME_TYPE_JSON = "application/json";

    private final String mimeType;

    protected JsonContent(String mimeType) {
        Objects.requireNonNull(mimeType);

        this.mimeType = mimeType;
    }

    @Override
    public String getContentTypeHeader() {
        return "Content-Type: %s; charset=utf-8".formatted(mimeType);
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    /**
     * Creates a writable JSON content from the given content.
     * @param content The content to write.
     * @return The writable JSON content.
     * @param <T> The type of the content.
     */
    public static <T> WritableBodyContent writableOf(T content) {
        return new WritableJsonContent(MIME_TYPE_JSON, content);
    }

    /**
     * Creates a readable JSON content from the given input stream.
     * @param mimeType The MIME type of the content.
     * @param serviceCollection The service collection to inject into the content.
     * @param properties The properties of the content.
     * @param inputStream The input stream to read from.
     * @return The readable JSON content.
     */
    public static ReadableBodyContent readableOf(
            String mimeType,
            ServiceCollection serviceCollection,
            Map<String, String> properties,
            InputStream inputStream
    ) {
        return new ReadableJsonContent(mimeType, inputStream);
    }

    private static class WritableJsonContent extends JsonContent implements WritableBodyContent {
        private final Object content;

        public WritableJsonContent(String mimeType, Object content) {
            super(mimeType);
            this.content = content;
        }

        @Override
        public void write(OutputStream outputStream) throws IOException {
            OBJECT_MAPPER.writeValue(outputStream, content);
        }
    }

    private static class ReadableJsonContent extends JsonContent implements ReadableBodyContent {
        private final InputStream inputStream;
        private boolean hasBeenRead;
        private Object valueRead;

        public ReadableJsonContent(String mimeType, InputStream inputStream) {
            super(mimeType);
            this.inputStream = inputStream;
        }

        @Override
        public <T> T read(Class<T> valueType) throws IOException {
            T result;
            if (hasBeenRead) {
                if (valueType.isInstance(valueRead)) {
                    result = valueType.cast(valueRead);
                } else {
                    throw new IllegalArgumentException("Cannot read content as " + valueType.getSimpleName());
                }
            } else {
                hasBeenRead = true;
                valueRead = result = OBJECT_MAPPER.readValue(inputStream, valueType);
            }
            return result;
        }

        @Override
        public <T> Optional<T> tryRead(Class<T> valueType) {
            try {
                return Optional.of(read(valueType));
            } catch (IOException e) {
                return Optional.empty();
            }
        }
    }
}
