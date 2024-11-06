package ch.zhaw.studyflow.domain.appointment;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * In-memory implementation of the AppointmentDao interface.
 */
public class MemoryAppointmentDao implements AppointmentDao {
    private final List<Appointment> appointments = new ArrayList<>();

    @Override
    public Appointment create(Appointment appointment) {
        appointments.add(appointment);
        return appointment;
    }

    @Override
    public Appointment read(long calendarId, long id) {
        return appointments.stream()
                .filter(a -> a.getCalendarId() == calendarId && a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, Date from, Date to) {
        return appointments.stream()
                .filter(a -> a.getCalendarId() == calendarId &&
                        !a.getStartTime().isBefore(from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()) &&
                        !a.getEndTime().isAfter(to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        appointments.removeIf(a -> a.getId() == id);
    }

    @Override
    public Appointment update(Appointment appointment) {
        delete(appointment.getId());
        appointments.add(appointment);
        return appointment;
    }
}