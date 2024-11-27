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
    private long understandingValue;
    private long timeValue;
    private long importanceValue;

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

    @JsonGetter("understandingValue")
    public long getUnderstandingValue() {
        return understandingValue;
    }

    @JsonSetter("understandingValue")
    public void setUnderstandingValue(long understandingValue) {
        this.understandingValue = understandingValue;
    }

    @JsonGetter("timeValue")
    public long getTimeValue() {
        return timeValue;
    }

    @JsonSetter("timeValue")
    public void setTimeValue(long timeValue) {
        this.timeValue = timeValue;
    }

    @JsonGetter("importanceValue")
    public long getImportanceValue() {
        return importanceValue;
    }

    @JsonSetter("importanceValue")
    public void setImportanceValue(long importanceValue) {
        this.importanceValue = importanceValue;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() && description != null && !description.isEmpty() && understandingValue >= 0 && timeValue >= 0 && importanceValue >= 0;
    }

}