package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

public interface ModuleManager {
    void create(Module module, long semesterId);

    Module read(long moduleId);

    void delete(long id);

    List<Module> getModules(long userId, long degreeId, long semesterId);

    Module getModule(long moduleId);

    void update(Module module);
}
