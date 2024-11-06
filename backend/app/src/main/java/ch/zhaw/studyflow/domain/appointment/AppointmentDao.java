package ch.zhaw.studyflow.domain.appointment;

import java.util.List;

/**
 * Interface for managing appointment data.
 */
public interface AppointmentDao {

    /**
     * Saves an appointment.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @param appointment the appointment to save
     * @return the saved appointment
     */
    Appointment save(long userId, long calendarId, Appointment appointment);

    /**
     * Reads all appointments for a user and calendar.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return a list of appointments
     */
    List<Appointment> readAll(long userId, long calendarId);

    /**
     * Reads a specific appointment for a user and calendar.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @param appointmentId the ID of the appointment
     * @return the appointment, or null if not found
     */
    Appointment read(long userId, long calendarId, long appointmentId);

    /**
     * Deletes a specific appointment for a user and calendar.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @param appointmentId the ID of the appointment
     */
    void delete(long userId, long calendarId, long appointmentId);

    /**
     * Updates an appointment.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @param appointment the appointment to update
     * @return the updated appointment
     */
    Appointment update(long userId, long calendarId, Appointment appointment);
}