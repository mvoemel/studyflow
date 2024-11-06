package ch.zhaw.studyflow.domain.appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages appointments for users.
 */
public class AppointmentManager implements AppointmentDao {
    private final Map<Long, List<Appointment>> userAppointments = new HashMap<>();

    /**
     * Saves an appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointment the appointment to save
     * @return the saved appointment
     */
    @Override
    public Appointment save(long userId, Appointment appointment) {
        userAppointments.computeIfAbsent(userId, k -> new ArrayList<>()).add(appointment);
        return appointment;
    }

    /**
     * Reads all appointments for a user.
     *
     * @param userId the ID of the user
     * @return a list of appointments
     */
    @Override
    public List<Appointment> readAll(long userId) {
        return userAppointments.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Reads a specific appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointmentId the ID of the appointment
     * @return the appointment, or null if not found
     */
    @Override
    public Appointment read(long userId, long appointmentId) {
        return userAppointments.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(appointment -> appointment.getId() == appointmentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointment the appointment to update
     * @return the updated appointment, or null if not found
     */
    @Override
    public Appointment update(long userId, Appointment appointment) {
        List<Appointment> appointments = userAppointments.get(userId);
        if (appointments != null) {
            for (int i = 0; i < appointments.size(); i++) {
                if (appointments.get(i).getId() == appointment.getId()) {
                    appointments.set(i, appointment);
                    return appointment;
                }
            }
        }
        return null;
    }

    /**
     * Deletes a specific appointment for a user.
     *
     * @param userId the ID of the user
     * @param appointmentId the ID of the appointment
     */
    @Override
    public void delete(long userId, long appointmentId) {
        List<Appointment> appointments = userAppointments.get(userId);
        if (appointments != null) {
            appointments.removeIf(appointment -> appointment.getId() == appointmentId);
        }
    }
}