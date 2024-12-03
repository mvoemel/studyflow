package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;
import ch.zhaw.studyflow.domain.grade.Grade;

public class ModuleGrade {
    private long id;
    private String name;
    private List<Grade> grades;
    private int ects;

    public ModuleGrade(long id, String name, List<Grade> grades) {
        this(id, name, grades, 0);
    }

    public ModuleGrade(long id, String name, List<Grade> grades, int ects) {
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
    public int getEtcs() {
        return ects;
    }

    @JsonSetter("ects")
    public void setEtcs(int etcs) {
        this.ects = etcs;
    }
}