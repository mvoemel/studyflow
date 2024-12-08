package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;

/**
 * Data access object for reading, writing and updating modules from and to a persistent storage.
 */
public interface ModuleDao {

    void create(Module module, long semesterId, long degreeId, long studentId);

    Module read(long moduleId);

    void delete(long moduleId);

    Module update(Module module);

    List<Module> readAllByStudent(long studentId);

    List<Module> readAllBySemester(long semesterId);
}
