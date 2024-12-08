package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleDao {

    void create(Module module, long semesterId, long degreeId, long studentId);

    ch.zhaw.studyflow.domain.curriculum.Module read(long moduleId);

    void delete(long id);

    Module update(Module module);

    List<Module> getModules(long userId);

    Optional<Module> getModule(long moduleId);

    Optional<Module> getModuleByName(String name);
    List<Module> readBySemesterId(long semesterId);
}
