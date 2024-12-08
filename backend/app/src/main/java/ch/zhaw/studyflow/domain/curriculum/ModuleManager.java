package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;
import java.util.Optional;

public interface ModuleManager {
    void create(Module module, long semesterId, long degreeId, long userId);

    Module read(long moduleId);

    void delete(long id);

    List<Module> getModules(long userId);

    Optional<Module> getModule(long moduleId);

    List<Module> getModulesBySemester(long semesterId);

    void update(Module module);
}
