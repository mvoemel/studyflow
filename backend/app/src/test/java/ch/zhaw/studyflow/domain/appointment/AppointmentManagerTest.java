package ch.zhaw.studyflow.domain.appointment;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;
import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentManagerTest {

    private AppointmentManagerImpl appointmentManager;
    private AppointmentDao appointmentDao;

    @BeforeEach
    void setUp() {
        appointmentDao = mock(AppointmentDao.class);
        appointmentManager = new AppointmentManagerImpl(appointmentDao);
    }

    @Test
    void testCreate() {
        final Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, "Test", "Test");

        doNothing().when(appointmentDao).create(appointment);

        appointmentManager.create(appointment);
        verify(appointmentDao).create(appointment);
    }

    @Test
    void testRead() {
        final Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, "Test", "Test");

        when(appointmentDao.read(1L, 1L)).thenReturn(appointment);

        Appointment readAppointment = appointmentManager.read(1L, 1L);
        assertEquals(appointment, readAppointment);
        verify(appointmentDao).read(1L, 1L);
    }

    @Test
    void testReadAllBy() {
        final Appointment appointment1 = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L, "Test", "Test");
        final Appointment appointment2 = new Appointment(2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), 1L, "Test", "Test");

        when(appointmentDao.readAllBy(1L)).thenReturn(List.of(appointment1, appointment2));

        List<Appointment> appointments = appointmentManager.readAllBy(1L);
        assertEquals(2, appointments.size());
        assertTrue(appointments.contains(appointment1));
        assertTrue(appointments.contains(appointment2));
        verify(appointmentDao).readAllBy(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(appointmentDao).delete(1L);

        appointmentManager.delete(1L);
        verify(appointmentDao).delete(1L);
    }

    @Test
    void testUpdate() {
        final Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1, "Test", "Test");
        final Appointment updatesToApply = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), 27, "1234", "4321");

        when(appointmentDao.read(27, 1)).thenReturn(appointment);


        appointmentManager.update(updatesToApply);
        verify(appointmentDao).read(27, 1);
        verify(appointmentDao).update(appointment);

        /* Assert mutable properties */
        assertEquals(updatesToApply.getTitle(), appointment.getTitle());
        assertEquals(updatesToApply.getDescription(), appointment.getDescription());
        assertEquals(updatesToApply.getStartTime(), appointment.getStartTime());
        assertEquals(updatesToApply.getEndTime(), appointment.getEndTime());

        /* Assert immutable properties */
        assertEquals(1, appointment.getId());
        assertEquals(1, appointment.getCalendarId());
    }
}