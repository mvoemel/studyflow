package ch.zhaw.studyflow.webserver.http.contents;

import ch.zhaw.studyflow.webserver.http.HttpRequest;
import ch.zhaw.studyflow.webserver.http.HttpResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JsonContent<T> extends TextBasedContent {
    public static final String APPLICATION_JSON = "application/json";

    private static final ObjectMapper JSON_OBJECT_MAPPER = new ObjectMapper();
    private final Class<T> valueType;
    private T value;


    public JsonContent(Class<T> type) {
        super(StandardCharsets.UTF_8, APPLICATION_JSON);
        this.valueType = type;
    }

    public JsonContent(Class<T> type, T value) {
        this(type);
        this.value = value;
    }


    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public void writeTo(HttpResponse request, OutputStream output) throws IOException {
        JSON_OBJECT_MAPPER.writeValue(output, value);
    }

    @Override
    public void readFrom(HttpRequest request, InputStream input) throws IOException {
        JSON_OBJECT_MAPPER.readValue(input, valueType);
    }
}
