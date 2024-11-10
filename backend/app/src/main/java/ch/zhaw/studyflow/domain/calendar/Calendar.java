package ch.zhaw.studyflow.domain.calendar;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a calendar.
 */
public class Calendar {
    private long id;
    private String name;
    private long ownerId;

    /**
     * Default constructor.
     */
    public Calendar() {}

    /**
     * Constructs a calendar with the specified ID, name, and owner ID.
     *
     * @param id the ID of the calendar
     * @param name the name of the calendar
     * @param ownerId the ID of the owner
     */
    public Calendar(long id, String name, long ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
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
     * Gets the owner ID of the calendar.
     *
     * @return the owner ID of the calendar
     */
    @JsonGetter("ownerId")
    public long getOwnerId() {
        return ownerId;
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

    /**
     * Sets the owner ID of the calendar.
     *
     * @param ownerId the new owner ID of the calendar
     */
    @JsonSetter("ownerId")
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * Gets the calendar ID.
     *
     * @return the calendar ID
     */
    public long getCalendarId() {
        return id;
    }

    /**
     * Sets the calendar ID.
     *
     * @param calendarId the new calendar ID
     */
    public void setCalendarId(long calendarId) {
        this.id = calendarId;
    }
}