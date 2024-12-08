package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        StudyAllocation allocation = mock(StudyAllocation.class);
        when(allocation.getMinutes()).thenReturn(120);
        studyDay.addStudyAllocation(allocation);
        studyDay.calculateMinutes();
        assertEquals(120, studyDay.getMinutes());
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
    public void testCompareTo() {
        BasicStudyDay otherStudyDay = new BasicStudyDay(date, startTime, endTime);
        StudyAllocation allocation = mock(StudyAllocation.class);
        when(allocation.getMinutes()).thenReturn(120);
        otherStudyDay.addStudyAllocation(allocation);
        otherStudyDay.calculateMinutes();
        assertTrue(studyDay.compareTo(otherStudyDay) > 0);
    }
}


