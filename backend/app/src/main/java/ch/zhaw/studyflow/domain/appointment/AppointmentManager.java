package ch.zhaw.studyflow.domain.appointment;

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
    public Appointment save(long userId, long calendarId, Appointment appointment) {
        return appointmentDao.save(userId, calendarId, appointment);
    }

    @Override
    public List<Appointment> readAll(long userId, long calendarId) {
        return appointmentDao.readAll(userId, calendarId);
    }

    @Override
    public Appointment read(long userId, long calendarId, long appointmentId) {
        return appointmentDao.read(userId, calendarId, appointmentId);
    }

    @Override
    public Appointment update(long userId, long calendarId, Appointment appointment) {
        return appointmentDao.update(userId, calendarId, appointment);
    }

    @Override
    public void delete(long userId, long calendarId, long appointmentId) {
        appointmentDao.delete(userId, calendarId, appointmentId);
    }
}