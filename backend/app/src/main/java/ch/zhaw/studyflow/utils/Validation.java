package ch.zhaw.studyflow.utils;

public class Validation {
    private Validation() {
        /* Class should not be instantiated */
    }

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isEmpty();
    }
}
