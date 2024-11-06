package ch.zhaw.studyflow.domain.appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the AppointmentDao interface.
 * This class provides methods to perform CRUD operations on Appointment objects
 * stored in memory.
 */
public class MemoryAppointmentDao implements AppointmentDao {
    private final Map<Long, List<Appointment>> userAppointments = new HashMap<>();

    @Override
    public Appointment save(long userId, Appointment appointment) {
        userAppointments.computeIfAbsent(userId, k -> new ArrayList<>()).add(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> readAll(long userId) {
        return userAppointments.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public Appointment read(long userId, long appointmentId) {
        return userAppointments.getOrDefault(userId, new ArrayList<>())
                .stream()
                .filter(appointment -> appointment.getId() == appointmentId)
                .findFirst()
                .orElse(null);
    }

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

    @Override
    public void delete(long userId, long appointmentId) {
        List<Appointment> appointments = userAppointments.get(userId);
        if (appointments != null) {
            appointments.removeIf(appointment -> appointment.getId() == appointmentId);
        }
    }
}