package ch.zhaw.studyflow.domain.appointment;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.LocalDateTime;

/**
 * Represents an appointment.
 */
public class Appointment {
    private long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long calendarId;

    /**
     * Default constructor.
     */
    public Appointment() {}

    /**
     * Constructs an appointment with the specified details.
     *
     * @param id the ID of the appointment
     * @param startTime the start time of the appointment
     * @param endTime the end time of the appointment
     * @param calendarId the ID of the calendar
     */
    public Appointment(long id, LocalDateTime startTime, LocalDateTime endTime, long calendarId) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarId = calendarId;
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
    @JsonGetter("startTime")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the appointment.
     *
     * @param startTime the new start time of the appointment
     */
    @JsonSetter("startTime")
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Gets the end time of the appointment.
     *
     * @return the end time of the appointment
     */
    @JsonGetter("endTime")
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the appointment.
     *
     * @param endTime the new end time of the appointment
     */
    @JsonSetter("endTime")
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

    /**
     * Gets the appointment ID.
     *
     * @return the appointment ID
     */
    public long getAppointmentId() {
        return id;
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentId the new appointment ID
     */
    public void setAppointmentId(long appointmentId) {
        this.id = appointmentId;
    }
}