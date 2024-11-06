package ch.zhaw.studyflow.domain.appointment;

import ch.zhaw.studyflow.domain.appointment.Appointment;
import ch.zhaw.studyflow.domain.appointment.AppointmentDao;
import ch.zhaw.studyflow.domain.appointment.AppointmentManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppointmentManagerTest {

    private AppointmentManager appointmentManager;
    private AppointmentDao appointmentDao;

    @BeforeEach
    void setUp() {
        appointmentDao = mock(AppointmentDao.class);
        appointmentManager = new AppointmentManager(appointmentDao);
    }

    @Test
    void testCreate() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L);
        when(appointmentDao.create(appointment)).thenReturn(appointment);

        Appointment createdAppointment = appointmentManager.create(appointment);
        assertEquals(appointment, createdAppointment);
        verify(appointmentDao).create(appointment);
    }

    @Test
    void testRead() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L);
        when(appointmentDao.read(1L, 1L)).thenReturn(appointment);

        Appointment readAppointment = appointmentManager.read(1L, 1L);
        assertEquals(appointment, readAppointment);
        verify(appointmentDao).read(1L, 1L);
    }

    @Test
    void testReadAllBy() {
        Appointment appointment1 = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L);
        Appointment appointment2 = new Appointment(2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3), 1L);
        Date from = new Date();
        Date to = new Date();
        when(appointmentDao.readAllBy(1L, from, to)).thenReturn(List.of(appointment1, appointment2));

        List<Appointment> appointments = appointmentManager.readAllBy(1L, from, to);
        assertEquals(2, appointments.size());
        assertTrue(appointments.contains(appointment1));
        assertTrue(appointments.contains(appointment2));
        verify(appointmentDao).readAllBy(1L, from, to);
    }

    @Test
    void testDelete() {
        doNothing().when(appointmentDao).delete(1L);

        appointmentManager.delete(1L);
        verify(appointmentDao).delete(1L);
    }

    @Test
    void testUpdate() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 1L);
        when(appointmentDao.update(appointment)).thenReturn(appointment);

        Appointment updatedAppointment = appointmentManager.update(appointment);
        assertEquals(appointment, updatedAppointment);
        verify(appointmentDao).update(appointment);
    }
}