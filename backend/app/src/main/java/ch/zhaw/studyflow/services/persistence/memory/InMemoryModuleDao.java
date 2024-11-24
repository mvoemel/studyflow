package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryModuleDao implements ModuleDao {
    private final List<Module> modules = new ArrayList<>();

    @Override
    public void create(Module module) {
        Objects.requireNonNull(module, "Module cannot be null");
        modules.add(module);
    }

    @Override
    public Module read(long moduleId) {
        return modules.stream()
                .filter(module -> module.getId() == moduleId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(long userId, long id) {
        modules.removeIf(module -> module.getId() == id && module.getId() == userId);
    }

    @Override
    public Module update(Module module) {
        delete(module.getId(), module.getId());
        modules.add(module);
        return module;
    }

    @Override
    public List<Module> getModules(long userId, long degreeId, long semesterId) {
        return null;
    }

    @Override
    public Module getModule(long userId, long degreeId, long semesterId, long moduleId) {
        return null;
    }
}