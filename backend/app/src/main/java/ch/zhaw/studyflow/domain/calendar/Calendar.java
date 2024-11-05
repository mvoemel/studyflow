package ch.zhaw.studyflow.domain.calendar;

import com.fasterxml.jackson.annotation.JsonGetter;

public class Calendar {
    private long id;
    private String name;

    public Calendar() {}

    public Calendar(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }
}