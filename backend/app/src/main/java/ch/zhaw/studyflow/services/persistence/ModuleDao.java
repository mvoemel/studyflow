package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.util.List;

/**
 * Data access object for reading, writing and updating modules from and to a persistent storage.
 */
public interface ModuleDao {

    /**
     * Writes a new module to the persistent storage and assigns an ID to it.
     * @param studentId the ID of the student
     * @param semesterId the ID of the semester
     * @param degreeId the ID of the degree
     * @param module the module to create
     */
    void create(long studentId, long semesterId, long degreeId, Module module);

    /**
     * Reads a module by its ID.
     * @param moduleId the module ID
     * @return the module
     */
    Module read(long moduleId);

    /**
     * Deletes a module by its ID.
     * @param moduleId the modules ID
     */
    void delete(long moduleId);

    /**
     * Updates a module.
     * @param module the module to update
     * @return the updated module
     */
    Module update(Module module);

    /**
     * Gets all modules for a specific student.
     * @param studentId the ID of the student
     * @return a list of modules for the student
     */
    List<Module> readAllByStudent(long studentId);

    /**
     * Gets all modules for a specific semester.
     * @param semesterId the ID of the semester
     * @return a list of modules for the semester
     */
    List<Module> readAllBySemester(long semesterId);
}
