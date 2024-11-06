package ch.zhaw.studyflow.domain.appointment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentManagerTest {

    private AppointmentManager appointmentManager;

    @BeforeEach
    void setUp() {
        appointmentManager = new AppointmentManager();
    }

    @Test
    void testSave() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Appointment savedAppointment = appointmentManager.save(1L, appointment);
        assertEquals(appointment, savedAppointment);
    }

    @Test
    void testReadAll() {
        Appointment appointment1 = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Appointment appointment2 = new Appointment(2, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusHours(3));
        appointmentManager.save(1L, appointment1);
        appointmentManager.save(1L, appointment2);

        List<Appointment> appointments = appointmentManager.readAll(1L);
        assertEquals(2, appointments.size());
        assertTrue(appointments.contains(appointment1));
        assertTrue(appointments.contains(appointment2));
    }

    @Test
    void testRead() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        appointmentManager.save(1L, appointment);

        Appointment readAppointment = appointmentManager.read(1L, 1L);
        assertEquals(appointment, readAppointment);
    }

    @Test
    void testUpdate() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        appointmentManager.save(1L, appointment);

        Appointment updatedAppointment = new Appointment(1, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
        Appointment result = appointmentManager.update(1L, updatedAppointment);
        assertEquals(updatedAppointment, result);
    }

    @Test
    void testDelete() {
        Appointment appointment = new Appointment(1, LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        appointmentManager.save(1L, appointment);

        appointmentManager.delete(1L, 1L);
        assertNull(appointmentManager.read(1L, 1L));
    }
}