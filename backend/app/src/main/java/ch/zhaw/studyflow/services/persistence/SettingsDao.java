package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.student.Settings;

/**
 * Data access object for reading, writing and updating student settings from and to a persistent storage.
 */
public interface SettingsDao {
    /**
     * Writes a new settings object to the persistent storage and assigns an ID to it.
     * @param studentSettings The settings to create
     */
    void create(Settings studentSettings);

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
     * Deletes a specific settings for a student.
     * @param settingsId The id of the settings
     */
    void delete(long settingsId);
}
