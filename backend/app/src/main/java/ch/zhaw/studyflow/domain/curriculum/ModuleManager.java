package ch.zhaw.studyflow.domain.curriculum;

import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.List;

/**
 * Manages modules in the curriculum.
 */
public class ModuleManager {

    private final ModuleDao moduleDao;

    /**
     * Constructs a ModuleManager with the specified ModuleDao.
     *
     * @param moduleDao the data access object for modules
     */
    public ModuleManager(ModuleDao moduleDao) {
        this.moduleDao = moduleDao;
    }

    /**
     * Creates a new module.
     *
     * @param module the module to create
     */
    public void create(Module module) {
        moduleDao.create(module);
    }

    /**
     * Reads a module by user ID and module ID.
     *
     * @param moduleId the ID of the module
     * @return the module
     */
    public Module read(long moduleId) {
        return moduleDao.read(moduleId);
    }

    /**
     * Deletes a module by user ID and module ID.
     *
     * @param userId the ID of the user
     * @param id the ID of the module
     */
    public void delete(long userId, long id) {
        moduleDao.delete(userId, id);
    }

    /**
     * Gets a list of modules by user ID, degree ID, and semester ID.
     *
     * @param userId the ID of the user
     * @param degreeId the ID of the degree
     * @param semesterId the ID of the semester
     * @return the list of modules
     */
    public List<Module> getModules(long userId, long degreeId, long semesterId) {
        return moduleDao.getModules(userId, degreeId, semesterId);
    }

    /**
     * Gets a module by user ID, degree ID, semester ID, and module ID.
     *
     * @param userId the ID of the user
     * @param degreeId the ID of the degree
     * @param semesterId the ID of the semester
     * @param moduleId the ID of the module
     * @return the module
     */
    public Module getModule(long userId, long degreeId, long semesterId, long moduleId) {
        return moduleDao.getModule(userId, degreeId, semesterId, moduleId);
    }

    /**
     * Updates a module.
     *
     * @param module the module to update
     */
    public void update(Module module) {
        moduleDao.update(module);
    }
}