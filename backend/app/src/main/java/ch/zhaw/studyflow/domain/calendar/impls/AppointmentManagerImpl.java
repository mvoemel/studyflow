package ch.zhaw.studyflow.domain.calendar.impls;

import java.time.LocalDate;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;

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
    public List<Appointment> readAllBy(long calendarId) {
        return appointmentDao.readAllBy(calendarId);
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, LocalDate start, LocalDate end) {
        return appointmentDao.readAllBy(calendarId, start, end);
    }

    @Override
    public void delete(long id) {
        appointmentDao.delete(id);
    }

    @Override
    public void update(Appointment appointment) {
        Appointment appointmentToUpdate = appointmentDao.read(appointment.getCalendarId(), appointment.getId());

        if (appointmentToUpdate != null) {
            appointmentToUpdate.setTitle(appointment.getTitle());
            appointmentToUpdate.setDescription(appointment.getDescription());
            appointmentToUpdate.setStartTime(appointment.getStartTime());
            appointmentToUpdate.setEndTime(appointment.getEndTime());
            appointmentDao.update(appointmentToUpdate);
        }
    }
}