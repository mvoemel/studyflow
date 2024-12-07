package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SemesterDeo {

    private long id;
    private String name;
    private String description;
    private long degreeId;
    private long userId;
    private long calendarId;


    public SemesterDeo() {
        this.id         = -1;
        this.calendarId = -1;
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

    @JsonGetter("userId")
    public long getUserId() {
        return userId;
    }

    @JsonSetter("userId")
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @JsonGetter("calendarId")
    public long getCalendarId() {
        return calendarId;
    }

    @JsonGetter("calendarId")
    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() && degreeId > -1 && userId > -1;
    }

}
