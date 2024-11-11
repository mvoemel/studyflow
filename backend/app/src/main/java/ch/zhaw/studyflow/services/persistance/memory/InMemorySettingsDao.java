package ch.zhaw.studyflow.services.persistance.memory;

import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.services.persistance.SettingsDao;
import ch.zhaw.studyflow.utils.Tuple;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySettingsDao implements SettingsDao {
    private AtomicLong idCounter = new AtomicLong(0);
    private HashMap<Long, Tuple<Long, Settings>> settingsById;


    public InMemorySettingsDao() {
        this.settingsById = new HashMap<>();
    }


    @Override
    public void create(long userId, Settings studentSettings) {
        settingsById.put(idCounter.getAndIncrement(), new Tuple<>(userId, studentSettings));

    }

    @Override
    public void update(Settings studentSettings) {
        Tuple<Long, Settings> longSettingsTuple = settingsById.get(studentSettings.getId());
        if (longSettingsTuple == null) {
            throw new IllegalArgumentException("Settings not found");
        }
        settingsById.put(studentSettings.getId(), new Tuple<>(longSettingsTuple.value1(), studentSettings));
    }

    @Override
    public Settings read(long settingsId) {
        Tuple<Long, Settings> longSettingsTuple = settingsById.get(settingsId);
        if (longSettingsTuple == null) {
            throw new IllegalArgumentException("Settings not found");
        }
        return longSettingsTuple.value2();
    }

    @Override
    public Settings readByUserId(long userId) {
        return settingsById.values().stream()
                .filter(tuple -> tuple.value1().equals(userId))
                .map(Tuple::value2)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Settings not found"));
    }

    @Override
    public void delete(long settingsId) {
        settingsById.remove(settingsId);
    }
}
