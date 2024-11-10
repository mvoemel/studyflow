package ch.zhaw.studyflow.domain.calendar;

import java.util.Date;
import java.util.List;

/**
 * Manages appointments for users.
 */
public class AppointmentManager implements AppointmentDao {
    private final AppointmentDao appointmentDao;

    /**
     * Constructs an AppointmentManager with the specified AppointmentDao.
     *
     * @param appointmentDao the AppointmentDao to use
     */
    public AppointmentManager(AppointmentDao appointmentDao) {
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
    public List<Appointment> readAllBy(long calendarId, Date from, Date to) {
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