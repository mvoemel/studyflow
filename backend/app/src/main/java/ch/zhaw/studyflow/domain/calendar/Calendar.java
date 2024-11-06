package ch.zhaw.studyflow.domain.calendar;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a calendar.
 */
public class Calendar {
    private long id;
    private String name;

    /**
     * Default constructor.
     */
    public Calendar() {}

    /**
     * Constructs a calendar with the specified ID and name.
     *
     * @param id the ID of the calendar
     * @param name the name of the calendar
     */
    public Calendar(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the calendar.
     *
     * @return the ID of the calendar
     */
    @JsonGetter("id")
    public long getId() {
        return id;
    }

    /**
     * Gets the name of the calendar.
     *
     * @return the name of the calendar
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the ID of the calendar.
     *
     * @param id the new ID of the calendar
     */
    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Sets the name of the calendar.
     *
     * @param name the new name of the calendar
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }
}