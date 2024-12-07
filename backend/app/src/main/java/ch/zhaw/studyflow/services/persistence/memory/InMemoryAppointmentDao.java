package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * In-memory implementation of the AppointmentDao interface.
 */
public class InMemoryAppointmentDao implements AppointmentDao {
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final List<Appointment> appointments = new ArrayList<>();

    @Override
    public void create(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment cannot be null");
        }
        long newId = idGenerator.getAndIncrement();
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
    public List<Appointment> readAllBy(long calendarId) {
        // We use !isBefore(from) since this includes the from and isBefore(to) since it
        // excludes the to date.
        return appointments.stream()
                .filter(appointment -> appointment.getCalendarId() == calendarId)
                .toList();
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