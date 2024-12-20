package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;
import ch.zhaw.studyflow.domain.grade.Grade;

/**
 * The ModuleGrade structure is used to represent a modules grade. It's used by itself and in a tree like structure
 * in combination with the {@link SemesterGrade} structure.
 */
public class ModuleGrade {
    private long id;
    private String name;
    private List<Grade> grades;
    private long ects;

    public ModuleGrade() {}

    public ModuleGrade(long id, String name, List<Grade> grades) {
        this(id, name, grades, 0);
    }

    public ModuleGrade(long id, String name, List<Grade> grades, long ects) {
        this.id = id;
        this.name = name;
        this.grades = grades;
        this.ects = ects;
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

    @JsonGetter("grades")
    public List<Grade> getGrades() {
        return grades;
    }

    @JsonSetter("grades")
    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @JsonGetter("ects")
    public long getEtcs() {
        return ects;
    }

    @JsonSetter("ects")
    public void setEtcs(int etcs) {
        this.ects = etcs;
    }
}