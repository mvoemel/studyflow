package ch.zhaw.studyflow.services.persistance.memory;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.services.persistance.AppointmentDao;

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
    public void create(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        long newId = appointments.size() + 1;
        appointment.setId(newId);
        appointments.add(appointment);
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
    public void update(Appointment appointment) {
        delete(appointment.getId());
        appointments.add(appointment);
    }
}