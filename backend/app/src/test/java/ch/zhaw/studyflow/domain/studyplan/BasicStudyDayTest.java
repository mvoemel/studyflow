package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyDay;



public class BasicStudyDayTest {
    private BasicStudyDay studyDay;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    @BeforeEach
    public void setUp() {
        date = LocalDate.of(2023, 10, 10);
        startTime = LocalTime.of(9, 0);
        endTime = LocalTime.of(17, 0);
        studyDay = new BasicStudyDay(date, startTime, endTime);
    }

    @Test
    public void testGetDate() {
        assertEquals(date, studyDay.getDate());
    }

    @Test
    public void testSetDate() {
        LocalDate newDate = LocalDate.of(2023, 10, 11);
        studyDay.setDate(newDate);
        assertEquals(newDate, studyDay.getDate());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(0, studyDay.getMinutes());
    }

    @Test
    public void testCalculateMinutes() {
        StudyAllocation allocation1 = mock(StudyAllocation.class);
        StudyAllocation allocation2 = mock(StudyAllocation.class);
        when(allocation1.getMinutes()).thenReturn(120);
        when(allocation2.getMinutes()).thenReturn(60);
        studyDay.addStudyAllocation(allocation1);
        studyDay.addStudyAllocation(allocation2);
        studyDay.calculateMinutes();
        assertEquals(180, studyDay.getMinutes());
    }

    @Test
    public void testGetAppointments() {
        assertTrue(studyDay.getAppointments().isEmpty());
        Appointment appointment1 = mock(Appointment.class);
        Appointment appointment2 = mock(Appointment.class);
        studyDay.addAppointment(appointment1);
        studyDay.addAppointment(appointment2);
        assertTrue(studyDay.getAppointments().contains(appointment1));
        assertTrue(studyDay.getAppointments().contains(appointment2));
    }

    @Test
    public void testAddAppointment() {
        Appointment appointment = mock(Appointment.class);
        studyDay.addAppointment(appointment);
        assertTrue(studyDay.getAppointments().contains(appointment));
    }

    @Test
    public void testRemoveAppointment() {
        Appointment appointment = mock(Appointment.class);
        studyDay.addAppointment(appointment);
        studyDay.removeAppointment(appointment);
        assertFalse(studyDay.getAppointments().contains(appointment));
    }

    @Test
    public void testGetStudyAllocations() {
        assertTrue(studyDay.getStudyAllocations().isEmpty());
        StudyAllocation allocation1 = mock(StudyAllocation.class);
        StudyAllocation allocation2 = mock(StudyAllocation.class);
        studyDay.addStudyAllocation(allocation1);
        studyDay.addStudyAllocation(allocation2);
        assertTrue(studyDay.getStudyAllocations().contains(allocation1));
        assertTrue(studyDay.getStudyAllocations().contains(allocation2));
    }

    @Test
    public void testAddStudyAllocation() {
        StudyAllocation allocation = mock(StudyAllocation.class);
        studyDay.addStudyAllocation(allocation);
        assertTrue(studyDay.getStudyAllocations().contains(allocation));
    }

    @Test
    public void testRemoveStudyAllocation() {
        StudyAllocation allocation = mock(StudyAllocation.class);
        studyDay.addStudyAllocation(allocation);
        studyDay.removeStudyAllocation(allocation);
        assertFalse(studyDay.getStudyAllocations().contains(allocation));
    }

    @Test
    public void testCalculateStudyAllocations() {
        Appointment appointment = mock(Appointment.class);
        when(appointment.getStartTime()).thenReturn(LocalDateTime.of(date, LocalTime.of(12, 0)));
        when(appointment.getEndTime()).thenReturn(LocalDateTime.of(date, LocalTime.of(13, 0)));
        studyDay.addAppointment(appointment);
        studyDay.calculateStudyAllocations();
        List<StudyAllocation> allocations = studyDay.getStudyAllocations();
        assertFalse(allocations.isEmpty());
    }

    @Test
    public void testCompareTo() {
        BasicStudyDay otherDay = new BasicStudyDay(date, startTime, endTime);
        StudyAllocation allocation1 = mock(StudyAllocation.class);
        StudyAllocation allocation2 = mock(StudyAllocation.class);
        when(allocation1.getMinutes()).thenReturn(120);
        when(allocation2.getMinutes()).thenReturn(60);
        studyDay.addStudyAllocation(allocation1);
        studyDay.calculateMinutes();
        otherDay.addStudyAllocation(allocation2);
        otherDay.calculateMinutes();
        assertTrue(studyDay.compareTo(otherDay) < 0);
    }
}



