package ch.zhaw.studyflow.utils;

public class Result<T> {
    private final T value;
    private final Throwable throwable;

    private Result(T value, Throwable message) {
        this.value = value;
        this.throwable = message;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Throwable error) {
        return new Result<>(null, error);
    }

    public T getValue() {
        return value;
    }

    public boolean isSuccess() {
        return throwable == null;
    }

    public Throwable getError() {
        return throwable;
    }
}
