package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.services.ServiceCollection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class JsonContent implements BodyContent {
    public static final String MIME_TYPE_JSON = "application/json";

    private final String mimeType;

    protected JsonContent(String mimeType) {
        Objects.requireNonNull(mimeType);

        this.mimeType = mimeType;
    }

    @Override
    public String getContentTypeHeader() {
        return "";
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    public static <T> WritableBodyContent writableOf(T content) {
        return new WritableJsonContent(MIME_TYPE_JSON, content);
    }

    public static ReadableBodyContent writableOf(InputStream inputStream) {
        return new ReadableJsonContent(MIME_TYPE_JSON, inputStream);
    }

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

        }
    }

    private static class ReadableJsonContent extends JsonContent implements ReadableBodyContent {
        private final InputStream inputStream;
        private String valueRead;

        public ReadableJsonContent(String mimeType, InputStream inputStream) {
            super(mimeType);
            this.inputStream = inputStream;
        }

        @Override
        public <T> T read(Class<T> valueType) throws IOException {
            if (!valueType.equals(String.class)) {
                throw new IllegalArgumentException("Cannot read content as " + valueType.getSimpleName());
            }
            return null;
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
