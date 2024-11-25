package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;

public interface ModuleDao {

    void create(Module module,long semesterId);

    ch.zhaw.studyflow.domain.curriculum.Module read(long moduleId);

    void delete(long id);

    ch.zhaw.studyflow.domain.curriculum.Module update(Module module);

    List<ch.zhaw.studyflow.domain.curriculum.Module> getModules(long userId, long degreeId, long semesterId);

    Module getModule(long moduleId);
}
