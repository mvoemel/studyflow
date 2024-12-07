package ch.zhaw.studyflow.domain.calendar;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.LocalDateTime;

/**
 * Represents an appointment.
 */
public class Appointment {
    private long id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long calendarId;

    //TODO: add title and description

    /**
     * Default constructor.
     */
    public Appointment() {
        this.id = -1;
    }

    /**
     * Constructs an appointment with the specified details.
     *
     * @param id the ID of the appointment
     * @param startTime the start time of the appointment
     * @param endTime the end time of the appointment
     * @param calendarId the ID of the calendar
     */
    public Appointment(long id, LocalDateTime startTime, LocalDateTime endTime, long calendarId, String title, String description) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarId = calendarId;
        this.title = title;
        this.description = description;
    }

    /**
     * Gets the name of the appointment.
     *
     * @return the name of the appointment
     */
    @JsonGetter("title")
    public String getTitle() {
        return title;
    }

    /**
     * Sets the name of the appointment.
     *
     * @param title the new name of the appointment
     */
    @JsonSetter("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the appointment.
     *
     * @return the description of the appointment
     */
    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the appointment.
     *
     * @param description the new description of the appointment
     */
    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the ID of the appointment.
     *
     * @return the ID of the appointment
     */
    @JsonGetter("id")
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the appointment.
     *
     * @param id the new ID of the appointment
     */
    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the start time of the appointment.
     *
     * @return the start time of the appointment
     */
    @JsonGetter("startDateTime")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the appointment.
     *
     * @param startTime the new start time of the appointment
     */
    @JsonSetter("startDateTime")
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the appointment.
     *
     * @return the end time of the appointment
     */
    @JsonGetter("endDateTime")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the appointment.
     *
     * @param endTime the new end time of the appointment
     */
    @JsonSetter("endDateTime")
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Gets the calendar ID of the appointment.
     *
     * @return the calendar ID of the appointment
     */
    @JsonGetter("calendarId")
    public long getCalendarId() {
        return calendarId;
    }

    /**
     * Sets the calendar ID of the appointment.
     *
     * @param calendarId the new calendar ID of the appointment
     */
    @JsonSetter("calendarId")
    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

}