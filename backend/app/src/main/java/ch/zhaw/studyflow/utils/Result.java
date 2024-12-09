package ch.zhaw.studyflow.utils;

/**
 * Represents a result of an operation that can either be successful or failed.
 * @param <T> the type of the value.
 */
public class Result<T> {
    private final T value;
    private final Throwable throwable;

    private Result(T value, Throwable message) {
        this.value = value;
        this.throwable = message;
    }

    /**
     * Creates a new success result with the given value.
     * @param value the value of the result.
     * @param <T> the type of the value.
     * @return the success result.
     */
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    /**
     * Creates a new failure result with the given error.
     * @param error the error of the result.
     * @param <T> the type of the value.
     * @return the failure result.
     */
    public static <T> Result<T> failure(Throwable error) {
        return new Result<>(null, error);
    }

    /**
     * Returns the value of the result.
     * @return the value of the result.
     */
    public T getValue() {
        return value;
    }

    /**
     * Returns whether the result is successful.
     * @return whether the result is successful.
     */
    public boolean isSuccess() {
        return throwable == null;
    }

    /**
     * Returns the error of the result.
     * @return the error of the result.
     */
    public Throwable getError() {
        return throwable;
    }
}
