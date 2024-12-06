package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.util.List;
import java.util.Optional;

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
    public void create(Module module,long semesterId, long degreeId, long userId) {
        moduleDao.create(module,semesterId,degreeId,userId);
    }

    /**
     * Reads a module by user ID and module ID.
     *
     * @param moduleId the ID of the module
     * @return the module
     */
    public Module read(long moduleId) {
        if (moduleId < 0) {
            throw new IllegalArgumentException("Module ID must be greater than 0.");
        }
        return moduleDao.readById(moduleId).orElse(null);
    }

    /**
     * Deletes a module by user ID and module ID.
     *
     * @param moduleId the ID of the module
     */
    public void delete(long moduleId) {
        if (moduleId < 0) {
            throw new IllegalArgumentException("Module ID must be greater than 0.");
        }
        moduleDao.delete(moduleId);
    }

    /**
     * Gets a list of modules by user ID, degree ID, and semester ID.
     *
     * @param userId the ID of the user
     * @return the list of modules
     */
    public List<Module> getModules(long userId) {
        return moduleDao.readAllByStudent(userId);
    }

    /**
     * Gets a module by user ID, degree ID, semester ID, and module ID.
     *
     * @param moduleId the ID of the module
     * @return the module
     */
    public Optional<Module> getModule(long moduleId) {
        return moduleDao.readById(moduleId);
    }

    /**
     * Gets a module by name.
     *
     * @param name the name of the module
     * @return the module
     */
    public Optional<Module> getModuleByName(String name) {
        return moduleDao.readByName(name);
    }

    @Override
    public List<Module> getModulesBySemester(long semesterId) {
        return moduleDao.readAllBySemester(semesterId);
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