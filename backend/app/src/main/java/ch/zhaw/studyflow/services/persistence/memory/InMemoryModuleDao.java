
package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.*;

public class InMemoryModuleDao implements ModuleDao {
    private final Map<Long, Map<Long, Map<Long, List<Module>>>> modules = new HashMap<>();

    @Override
    public void create(Module module, long semesterId, long degreeId, long userId) {
        Objects.requireNonNull(module, "Module cannot be null");
        modules
                .computeIfAbsent(userId, k -> new HashMap<>())
                .computeIfAbsent(degreeId, k -> new HashMap<>())
                .computeIfAbsent(semesterId, k -> new ArrayList<>())
                .add(module);
    }

    @Override
    public Module read(long moduleId) {
        return modules.values().stream()
                .flatMap(degreeMap -> degreeMap.values().stream())
                .flatMap(semesterMap -> semesterMap.values().stream())
                .flatMap(Collection::stream)
                .filter(module -> module.getId() == moduleId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(long id) {
        modules.values().forEach(degreeMap ->
                degreeMap.values().forEach(semesterMap ->
                        semesterMap.values().forEach(moduleList ->
                                moduleList.removeIf(module -> module.getId() == id)
                        )
                )
        );
    }

    @Override
    public Module update(Module module) {
        Objects.requireNonNull(module, "Module cannot be null");
        long moduleId = module.getId();
        modules.values().forEach(degreeMap ->
                degreeMap.values().forEach(semesterMap ->
                        semesterMap.values().forEach(moduleList -> {
                            for (int i = 0; i < moduleList.size(); i++) {
                                if (moduleList.get(i).getId() == moduleId) {
                                    moduleList.set(i, module);
                                    return;
                                }
                            }
                        })
                )
        );
        return module;
    }

    @Override
    public List<Module> getModules(long userId) {
        return modules.getOrDefault(userId, Collections.emptyMap())
                .values().stream()
                .flatMap(degreeMap -> degreeMap.values().stream())
                .flatMap(Collection::stream)
                .toList();
    }

    @Override
    public Optional<Module> getModule(long moduleId) {
        return modules.values().stream()
                .flatMap(degreeMap -> degreeMap.values().stream())
                .flatMap(semesterMap -> semesterMap.values().stream())
                .flatMap(Collection::stream)
                .filter(module -> module.getId() == moduleId)
                .findFirst();
    }

    @Override
    public Optional<Module> getModuleByName(String name) {
        return modules.values().stream()
                .flatMap(degreeMap -> degreeMap.values().stream())
                .flatMap(semesterMap -> semesterMap.values().stream())
                .flatMap(Collection::stream)
                .filter(module -> module.getName().equals(name))
                .findFirst();
    }
}