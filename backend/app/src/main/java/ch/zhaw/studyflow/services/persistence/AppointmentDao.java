package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.calendar.Appointment;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface for managing appointment data.
 */
public interface AppointmentDao {

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
     * Deletes a specific appointment by appointment ID.
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