package ch.zhaw.studyflow.webserver.http.contents;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public abstract class TextBasedContent implements BodyContent {
    private final Charset charset;
    private final String mimeType;

    protected TextBasedContent(String mimeType) {
        this(StandardCharsets.UTF_8, mimeType);
    }

    protected TextBasedContent(final Charset charset, final String mimeType) {
        Objects.requireNonNull(charset);
        Objects.requireNonNull(mimeType);

        this.charset    = charset;
        this.mimeType   = mimeType;
    }


    @Override
    public String getMimeType() {
        return mimeType;
    }

    @Override
    public String getContentHeader() {
        return "Content-Type: %s; charset=%s"
                .formatted(mimeType, charset.name());
    }
}
