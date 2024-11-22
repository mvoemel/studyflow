package ch.zhaw.studyflow.domain.calendar;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 *
 * In-memory implementation of the AppointmentDao interface.
 */
public class InMemoryAppointmentDao implements AppointmentDao {
    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

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
    public List<Appointment> readAllBy(long calendarId, LocalDate from, LocalDate to) {
        // We use !isBefore(from) since this includes the from and isBefore(to) since it
        // excludes the to date.
        return appointments.stream()
                .filter(appointment -> appointment.getCalendarId() == calendarId &&
                        !appointment.getStartTime().isBefore(to.atStartOfDay()) &&
                        appointment.getEndTime().isBefore(from.atStartOfDay()))
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