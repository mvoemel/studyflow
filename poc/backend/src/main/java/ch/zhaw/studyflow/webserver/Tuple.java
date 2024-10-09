package ch.zhaw.studyflow.webserver;

public record Tuple<V1, V2>(V1 value1, V2 value2) {
    public static <V1, V2> Tuple<V1, V2> of(V1 value1, V2 value2) {
        return new Tuple<>(value1, value2);
    }
}
