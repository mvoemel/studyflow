package ch.zhaw.studyflow.domain.calendar.impls;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;

import java.time.LocalDate;
import java.util.List;

/**
 * Manages appointments for users.
 */
public class AppointmentManagerImpl implements AppointmentManager {
    private final AppointmentDao appointmentDao;

    /**
     * Constructs an AppointmentManager with the specified AppointmentDao.
     *
     * @param appointmentDao the AppointmentDao to use
     */
    public AppointmentManagerImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public void create(Appointment appointment) {
        appointmentDao.create(appointment);
    }

    @Override
    public Appointment read(long calendarId, long id) {
        return appointmentDao.read(calendarId, id);
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, LocalDate from, LocalDate to) {
        return appointmentDao.readAllBy(calendarId, from, to);
    }

    @Override
    public void delete(long id) {
        appointmentDao.delete(id);
    }

    @Override
    public void update(Appointment appointment) {
        appointmentDao.update(appointment);
    }
}