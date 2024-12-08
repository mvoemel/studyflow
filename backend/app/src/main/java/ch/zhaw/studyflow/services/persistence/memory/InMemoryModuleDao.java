
package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryModuleDao implements ModuleDao {
    private final Map<Long, Module> modules;
    private final Map<Long, Long> moduleToSemester;
    private final Map<Long, Long> semesterToDegree;
    private final Map<Long, Long> degreeToUser;
    private AtomicInteger idCounter;

    public InMemoryModuleDao() {
        modules = new HashMap<>();
        moduleToSemester = new HashMap<>();
        semesterToDegree = new HashMap<>();
        degreeToUser = new HashMap<>();
        idCounter = new AtomicInteger();
    }

    @Override
    public void create(long studentId, long semesterId, long degreeId, Module module) {
        Objects.requireNonNull(module, "Module cannot be null");
        module.setId(idCounter.getAndIncrement());
        modules.put(module.getId(), module);
        moduleToSemester.put(module.getId(), semesterId);
        semesterToDegree.put(semesterId, degreeId);
        degreeToUser.put(degreeId, studentId);
    }

    @Override
    public Module read(long moduleId) {
        return modules.get(moduleId);
    }

    @Override
    public void delete(long moduleId) {
        modules.remove(moduleId);
        moduleToSemester.remove(moduleId);
    }

    @Override
    public Module update(Module module) {
        Objects.requireNonNull(module, "Module cannot be null");
        modules.put(module.getId(), module);
        return module;
    }

    @Override
    public List<Module> readAllByStudent(long studentId) {
        List<Module> userModules = new ArrayList<>();
        degreeToUser.forEach((degreeId, id) -> {
            if (id == studentId) {
                semesterToDegree.forEach((semesterId, dId) -> {
                    if (Objects.equals(dId, degreeId)) {
                        moduleToSemester.forEach((moduleId, sId) -> {
                            if (Objects.equals(sId, semesterId)) {
                                userModules.add(modules.get(moduleId));
                            }
                        });
                    }
                });
            }
        });
        return userModules;
    }


    @Override
    public List<Module> readAllBySemester(long semesterId) {
        List<Module> modulesForSemester = new ArrayList<>();
        moduleToSemester.forEach((moduleId, sId) -> {
            if(sId == semesterId) {
                modulesForSemester.add(modules.get(moduleId));
            }
        });
        return modulesForSemester;
    }
}