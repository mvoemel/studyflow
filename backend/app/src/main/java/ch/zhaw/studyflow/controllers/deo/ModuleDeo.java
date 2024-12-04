package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ModuleDeo {
    private long id;
    private String name;
    private String description;
    private long degreeId;
    private long semesterId;
    private long ects;
    private long understanding;
    private long time;
    private long complexity;

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

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonGetter("degreeId")
    public long getDegreeId() {
        return degreeId;
    }

    @JsonSetter("degreeId")
    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
    }

    @JsonGetter("semesterId")
    public long getSemesterId() {
        return semesterId;
    }

    @JsonSetter("semesterId")
    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }

    @JsonGetter("ects")
    public long getEcts() {
        return ects;
    }

    @JsonSetter("ects")
    public void setEcts(long ects) {
        this.ects = ects;
    }

    @JsonGetter("understanding")
    public long getUnderstanding() {
        return understanding;
    }

    @JsonSetter("understanding")
    public void setUnderstanding(long understanding) {
        this.understanding = understanding;
    }

    @JsonGetter("time")
    public long getTime() {
        return time;
    }

    @JsonSetter("time")
    public void setTime(long time) {
        this.time = time;
    }

    @JsonGetter("complexity")
    public long getComplexity() {
        return complexity;
    }

    @JsonSetter("complexity")
    public void setComplexity(long complexity) {
        this.complexity = complexity;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() && ects >= 0 && understanding >= 0 && time >= 0 && complexity >= 0;
    }

}