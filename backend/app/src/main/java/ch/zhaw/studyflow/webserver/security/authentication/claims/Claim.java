package ch.zhaw.studyflow.webserver.security.authentication.claims;

import java.util.function.Function;

public abstract class Claim<T> {
    private final String name;
    private final Class<T> valueType;
    private final Function<String, T> fromString;
    private final Function<T, String> toString;

    protected Claim(String name, Class<T> valueType, Function<String, T> fromString, Function<T, String> toString) {
        this.name       = name;
        this.valueType  = valueType;

        this.fromString = fromString;
        this.toString   = toString;
    }


    public String getName() {
        return name;
    }

    public Class<T> valueType() {
        return valueType;
    }

    public T fromString(String value) {
        return fromString.apply(value);
    }

    public String toString(T value) {
        return toString.apply(value);
    }
}
