package ch.zhaw.studyflow.services.persistance;

import java.util.List;

public interface ModuleDao {
    void create(Module module);
    Module read(long id);
    List<Module> readAllBy(long id);
    void update(Module module);
}
