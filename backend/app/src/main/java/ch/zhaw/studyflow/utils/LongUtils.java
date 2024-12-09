package ch.zhaw.studyflow.utils;

import java.util.Optional;

/**
 * Utility class for longs.
 */
public class LongUtils {
    private LongUtils() {
        /* Class should not be instantiated */
    }
    /**
     * Tries to parse a string to a long.
     * If the string is not a valid long, an empty optional is returned.
     * @param value The string to parse
     * @return An optional containing the parsed long or an empty optional
     */
    public static Optional<Long> tryParseLong(String value) {
        try {
            return Optional.of(Long.parseLong(value));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}