package ch.zhaw.studyflow.webserver;

/**
 * A simple tuple class to hold two values.
 * @param value1 The first value
 * @param value2 The second value
 * @param <V1> The type of the first value
 * @param <V2> The type of the second value
 */
public record Tuple<V1, V2>(V1 value1, V2 value2) {
    public static <V1, V2> Tuple<V1, V2> of(V1 value1, V2 value2) {
        return new Tuple<>(value1, value2);
    }
}
