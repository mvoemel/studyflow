package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.List;

/**
 * Manages modules in the curriculum.
 */
public class ModuleManagerImpl implements ModuleManager {

    private final ModuleDao moduleDao;

    /**
     * Constructs a ModuleManager with the specified ModuleDao.
     *
     * @param moduleDao the data access object for modules
     */
    public ModuleManagerImpl(ModuleDao moduleDao) {
        this.moduleDao = moduleDao;
    }

    /**
     * Creates a new module.
     *
     * @param module the module to create
     */
    public void create(Module module,long semesterId) {
        moduleDao.create(module,semesterId);
    }

    /**
     * Reads a module by user ID and module ID.
     *
     * @param moduleId the ID of the module
     * @return the module
     */
    public ch.zhaw.studyflow.domain.curriculum.Module read(long moduleId) {
        return moduleDao.read(moduleId);
    }

    /**
     * Deletes a module by user ID and module ID.
     *
     * @param id the ID of the module
     */
    public void delete(long id) {
        moduleDao.delete(id);
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
     * @param moduleId the ID of the module
     * @return the module
     */
    public Module getModule(long moduleId) {
        return moduleDao.getModule(moduleId);
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