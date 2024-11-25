package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryModuleDao implements ModuleDao {
    private final HashMap<Long,HashMap<Long,Module>> modules = new HashMap<>();

    @Override
    public void create(Module module,long semesterId) {
        Objects.requireNonNull(module, "Module cannot be null");
        HashMap<Long,Module> semesterModules = new HashMap<>();
        semesterModules.put(module.getId(),module);
        modules.put(semesterId,semesterModules);
    }

    @Override
    public Module read(long moduleId) {
        return modules.values().stream()
                .flatMap(map -> map.values().stream())
                .filter(module -> module.getId() == moduleId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        modules.values().forEach(map -> map.remove(id));
    }

    @Override
    public Module update(Module module) {
        modules.values().forEach(map -> map.put(module.getId(),module));
        return module;
    }

    @Override
    public List<Module> getModules(long userId, long degreeId, long semesterId) {
        return new ArrayList<>(modules.get(semesterId).values());
    }

    @Override
    public Module getModule(long moduleId) {
        return modules.values().stream()
                .flatMap(map -> map.values().stream())
                .filter(module -> module.getId() == moduleId)
                .findFirst()
                .orElse(null);
    }
}