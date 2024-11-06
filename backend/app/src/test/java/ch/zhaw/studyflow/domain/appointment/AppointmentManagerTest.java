package ch.zhaw.studyflow.domain.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
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
    void testSave() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentDao.save(1L, 1L, appointment)).thenReturn(appointment);

        Appointment savedAppointment = appointmentManager.save(1L, 1L, appointment);
        assertEquals(appointment, savedAppointment);
        verify(appointmentDao).save(1L, 1L, appointment);
    }

    @Test
    void testReadAll() {
        Appointment appointment1 = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Appointment appointment2 = new Appointment(2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));
        when(appointmentDao.readAll(1L, 1L)).thenReturn(List.of(appointment1, appointment2));

        List<Appointment> appointments = appointmentManager.readAll(1L, 1L);
        assertEquals(2, appointments.size());
        assertTrue(appointments.contains(appointment1));
        assertTrue(appointments.contains(appointment2));
        verify(appointmentDao).readAll(1L, 1L);
    }

    @Test
    void testRead() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentDao.read(1L, 1L, 1L)).thenReturn(appointment);

        Appointment readAppointment = appointmentManager.read(1L, 1L, 1L);
        assertEquals(appointment, readAppointment);
        verify(appointmentDao).read(1L, 1L, 1L);
    }

    @Test
    void testUpdate() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        when(appointmentDao.update(1L, 1L, appointment)).thenReturn(appointment);

        Appointment updatedAppointment = appointmentManager.update(1L, 1L, appointment);
        assertEquals(appointment, updatedAppointment);
        verify(appointmentDao).update(1L, 1L, appointment);
    }

    @Test
    void testDelete() {
        doNothing().when(appointmentDao).delete(1L, 1L, 1L);

        appointmentManager.delete(1L, 1L, 1L);
        verify(appointmentDao).delete(1L, 1L, 1L);
    }
}