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
     * @return the list of modules
     */
    public List<Module> getModules(long userId) {
        return moduleDao.getModules(userId);
    }

    /**
     * Gets a module by user ID, degree ID, semester ID, and module ID.
     *
     * @param moduleId the ID of the module
     * @return the module
     */
    public Optional<Module> getModule(long moduleId) {
        return moduleDao.getModule(moduleId);
    }

    /**
     * Gets a module by name.
     *
     * @param name the name of the module
     * @return the module
     */
    public Optional<Module> getModuleByName(String name) {
        return moduleDao.getModuleByName(name);
    }

    @Override
    public List<Module> getModulesBySemester(long semesterId) {
        return moduleDao.readBySemesterId(semesterId);
    }

    /**
     * Updates a module.
     *
     * @param module the module to update
     */
    public void update(Module module) {
        Module moduleToUpdate = moduleDao.read(module.getId());
        if (moduleToUpdate != null) {
            moduleToUpdate.setName(module.getName());
            moduleToUpdate.setDescription(module.getDescription());
            moduleToUpdate.setECTS(module.getECTS());
            moduleToUpdate.setComplexity(module.getComplexity());
            moduleToUpdate.setTime(module.getTime());
            moduleToUpdate.setUnderstanding(module.getUnderstanding());
            moduleDao.update(moduleToUpdate);
        }
    }
}