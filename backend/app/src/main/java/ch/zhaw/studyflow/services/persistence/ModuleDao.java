package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;

public interface ModuleDao {

    void create(ch.zhaw.studyflow.domain.curriculum.Module module);

    ch.zhaw.studyflow.domain.curriculum.Module read(long moduleId);

    void delete(long userId, long id);

    ch.zhaw.studyflow.domain.curriculum.Module update(ch.zhaw.studyflow.domain.curriculum.Module module);

    List<ch.zhaw.studyflow.domain.curriculum.Module> getModules(long userId, long degreeId, long semesterId);

    Module getModule(long userId, long degreeId, long semesterId, long moduleId);
}
