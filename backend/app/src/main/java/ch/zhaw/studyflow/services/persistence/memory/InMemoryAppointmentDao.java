package ch.zhaw.studyflow.services.persistence.memory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;

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
    public Appointment read(long calendarId, long appointmentId) {
        return appointments.stream()
                .filter(a -> a.getCalendarId() == calendarId && a.getId() == appointmentId)
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
    public void delete(long appointmentId) {
        appointments.removeIf(a -> a.getId() == appointmentId);
    }

    @Override
    public void update(Appointment appointment) {
        delete(appointment.getId());
        appointments.add(appointment);
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, LocalDate from, LocalDate to) {
        return appointments.stream()
                .filter(appointment -> appointment.getCalendarId() == calendarId)
                .filter(appointment -> !appointment.getStartTime().toLocalDate().isBefore(from))
                .filter(appointment -> appointment.getEndTime().toLocalDate().isBefore(to))
                .toList();
    }
}