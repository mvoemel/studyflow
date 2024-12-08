package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlotValue;
import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlots;


public class TimeSlotsTest {
    private TimeSlots timeSlots;

    @BeforeEach
    public void setUp() {
        timeSlots = new TimeSlots(LocalTime.of(8, 0), LocalTime.of(12, 0), 30);
    }

    @Test
    public void testGetSlotCount() {
        assertEquals(8, timeSlots.getSlotCount());
    }

    @Test
    public void testGetRemainingMinutes() {
        assertEquals(240, timeSlots.getRemainingMinutes());
    }

    @Test
    public void testSetTimeSlot() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(TimeSlotValue.STUDY, timeSlots.getTimeSlots()[2]);
        assertEquals(TimeSlotValue.STUDY, timeSlots.getTimeSlots()[3]);
    }

    @Test
    public void testIsFree() {
        assertTrue(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(9, 0)));
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertFalse(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(9, 0)));
    }

    @Test
    public void testGetEarliestFree() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals(LocalTime.of(9, 0), timeSlots.getEarliestFree());
    }

    @Test
    public void testGetFreeMinutesAfter() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals(180, timeSlots.getFreeMinutesAfter(LocalTime.of(8, 0)));
    }
}


