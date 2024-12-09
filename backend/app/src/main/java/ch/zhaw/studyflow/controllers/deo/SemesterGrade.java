package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;

/**
 * The SemesterGrade structure is used to send the client the students grade in an easy to navigate structure.
 * The structure is leaned on the ui's representation of the grades.
 */
public class SemesterGrade {
    private long id;
    private String name;
    private List<ModuleGrade> modules;

    public SemesterGrade() {}

    public SemesterGrade(long id, String name, List<ModuleGrade> modules) {
        this.id = id;
        this.name = name;
        this.modules = modules;
    }

    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonGetter("modules")
    public List<ModuleGrade> getModules() {
        return modules;
    }

    @JsonSetter("modules")
    public void setModules(List<ModuleGrade> modules) {
        this.modules = modules;
    }
}