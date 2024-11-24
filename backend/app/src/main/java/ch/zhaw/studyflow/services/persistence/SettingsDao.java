package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.student.Settings;

/**
 * Data access object for reading, writing and updating student settings from and to a persistent storage.
 */
public interface SettingsDao {
    /**
     * Creates a new settings for a student.
     * @param userId The id of the student
     * @param studentSettings The settings to create
     */
    void create(long userId, Settings studentSettings);

    /**
     * Updates the settings for a student.
     * @param studentSettings The settings to update
     */
    void update(Settings studentSettings);

    /**
     * Reads a specific settings for a student.
     * @param settingsId The id of the settings
     * @return the settings, or null if not found
     */
    Settings read(long settingsId);

    /**
     * Reads the settings for a user.
     * @param userId The id of the user
     * @return the settings, or null if not found
     */
    Settings readByUserId(long userId);

    /**
     * Deletes a specific settings for a student.
     * @param settingsId The id of the settings
     */
    void delete(long settingsId);
}
