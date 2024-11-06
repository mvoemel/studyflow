package ch.zhaw.studyflow.domain.appointment;

import java.util.List;

public interface AppointmentDao {
    /**
     * Saves an appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointment the appointment to save
     * @return the saved appointment
     */
    Appointment save(long userId, Appointment appointment);

    /**
     * Reads all appointments for a user.
     *
     * @param userId the ID of the user
     * @return a list of appointments
     */
    List<Appointment> readAll(long userId);

    /**
     * Reads a specific appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointmentId the ID of the appointment
     * @return the appointment, or null if not found
     */
    Appointment read(long userId, long appointmentId);

    /**
     * Updates an appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointment the appointment to update
     * @return the updated appointment
     */
    Appointment update(long userId, Appointment appointment);

    /**
     * Deletes a specific appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointmentId the ID of the appointment
     */
    void delete(long userId, long appointmentId);
}