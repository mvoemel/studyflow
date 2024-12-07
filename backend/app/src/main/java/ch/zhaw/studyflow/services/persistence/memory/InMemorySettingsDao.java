package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.services.persistence.SettingsDao;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySettingsDao implements SettingsDao {
    private AtomicLong idCounter;
    private HashMap<Long, Settings> settingsById;


    public InMemorySettingsDao() {
        this.idCounter      = new AtomicLong(0);
        this.settingsById   = new HashMap<>();
    }


    @Override
    public void create(Settings studentSettings) {
        studentSettings.setId(idCounter.getAndIncrement());
        settingsById.put(idCounter.getAndIncrement(), studentSettings);
    }

    @Override
    public void update(Settings studentSettings) {
        Settings longSettingsTuple = settingsById.get(studentSettings.getId());
        if (longSettingsTuple == null) {
            throw new IllegalArgumentException("Settings not found");
        }
        settingsById.put(studentSettings.getId(), studentSettings);
    }

    @Override
    public Settings read(long settingsId) {
        Settings longSettingsTuple = settingsById.get(settingsId);
        if (longSettingsTuple == null) {
            throw new IllegalArgumentException("Settings not found");
        }
        return longSettingsTuple;
    }

    @Override
    public void delete(long settingsId) {
        settingsById.remove(settingsId);
    }
}
