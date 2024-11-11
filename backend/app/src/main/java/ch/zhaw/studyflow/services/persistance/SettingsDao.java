package ch.zhaw.studyflow.services.persistance;

import ch.zhaw.studyflow.domain.student.Settings;

public interface SettingsDao {
    void create(long userId, Settings studentSettings);
    void update(Settings studentSettings);
    Settings read(long settingsId);
    Settings readByUserId(long userId);
    void delete(long settingsId);
}
