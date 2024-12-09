package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;
import java.util.Optional;

/**
 * Interface for the module manager to mutate and query modules in the domain.
 */
public interface ModuleManager {
    /**
     * Creates a module with the specified semester ID, degree ID, and user ID.
     *
     * @param module The module to create
     * @param semesterId The ID of the semester
     * @param degreeId The ID of the degree
     * @param userId The ID of the user
     */
    void create(Module module, long semesterId, long degreeId, long userId);

    /**
     * Reads a module with the specified ID.
     *
     * @param moduleId The ID of the module
     * @return The module with the specified ID
     */
    Module read(long moduleId);

    /**
     * Deletes a module with the specified ID.
     *
     * @param id The ID of the module
     */
    void delete(long id);

    /**
     * Gets all modules.
     *
     * @return A list of all modules
     */
    List<Module> getModules(long userId);

    /**
     * Gets a module with the specified ID.
     *
     * @param moduleId The ID of the module
     * @return The module with the specified ID
     */
    Optional<Module> getModule(long moduleId);

    /**
     * Gets all modules with the specified semester ID.
     *
     * @param semesterId The ID of the semester
     * @return A list of modules with the specified semester ID
     */
    List<Module> getModulesBySemester(long semesterId);

    /**
     * Updates a module.
     *
     * @param module The module to update
     */
    void update(Module module);
}
