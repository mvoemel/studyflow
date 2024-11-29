package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleDao {

    void create(Module module,long semesterId, long degreeId, long userId);

    ch.zhaw.studyflow.domain.curriculum.Module read(long moduleId);

    void delete(long id);

    ch.zhaw.studyflow.domain.curriculum.Module update(Module module);

    List<Module> getModules(long userId);

    Optional<Module> getModule(long moduleId);

    Optional<Module> getModuleByName(String name);
}
