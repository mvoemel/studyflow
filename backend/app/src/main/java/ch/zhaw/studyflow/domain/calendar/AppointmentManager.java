package ch.zhaw.studyflow.domain.calendar;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for managing appointments in the domain.
 */
public interface AppointmentManager {
    /**
     * Creates a new appointment.
     *
     * @param appointment the appointment to create
     */
    void create(Appointment appointment);

    /**
     * Reads a specific appointment by calendar ID and appointment ID.
     *
     * @param calendarId the ID of the calendar
     * @param id the ID of the appointment
     * @return the appointment, or null if not found
     */
    Appointment read(long calendarId, long id);

    /**
     * Reads all appointments for a calendar within a date range.
     *
     * @param calendarId the ID of the calendar
     * @return a list of appointments
     */
    List<Appointment> readAllBy(long calendarId);

    /**
     * Reads all appointments for a calendar within a date range.
     *
     * @param calendarId the ID of the calendar
     * @param start the start date
     * @param end the end date
     * @return a list of appointments
     */
    List<Appointment> readAllBy(long calendarId, LocalDate start, LocalDate end);

    /**
     * Deletes a specific appointment by its ID.
     *
     * @param id the ID of the appointment
     */
    void delete(long id);

    /**
     * Updates an appointment.
     *
     * @param appointment the appointment to update
     */
    void update(Appointment appointment);
}
