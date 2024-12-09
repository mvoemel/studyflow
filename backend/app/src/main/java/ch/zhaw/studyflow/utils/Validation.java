package ch.zhaw.studyflow.utils;

/**
 * Provides utility methods for the validation of values.
 */
public class Validation {
    private Validation() {
        /* Class should not be instantiated */
    }

    /**
     * Checks if the given value is null or empty.
     * @param value the value to check.
     * @return true if the value is null or empty, false otherwise.
     */
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
