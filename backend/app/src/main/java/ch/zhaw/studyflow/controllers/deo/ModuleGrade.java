package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.List;
import ch.zhaw.studyflow.domain.grade.Grade;

public class ModuleGrade {
    private long id;
    private String name;
    private List<Grade> grades;

    public ModuleGrade() {}

    public ModuleGrade(long id, String name, List<Grade> grades) {
        this.id = id;
        this.name = name;
        this.grades = grades;
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
}