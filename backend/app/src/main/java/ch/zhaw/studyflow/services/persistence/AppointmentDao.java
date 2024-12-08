package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.calendar.Appointment;

import java.util.List;

/**
 * Data access object for reading, writing and updating appointment from and to a persistent storage.
 */
public interface AppointmentDao {

    /**
     * Writes a new appointment to the persistent storage and assigns an ID to it.
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