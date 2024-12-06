package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;
import java.util.Optional;

public interface ModuleDao {
    void create(Module module,long semesterId, long degreeId, long userId);
    void delete(long id);
    void update(Module module);
    List<Module> readAllByStudent(long studentId);
    Optional<Module> readById(long moduleId);
    Optional<Module> readByName(String name);
    List<Module> readAllBySemester(long semesterId);
}
