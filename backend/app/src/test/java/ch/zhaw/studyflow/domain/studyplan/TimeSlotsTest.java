package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlotValue;
import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlots;

class TimeSlotsTest {

    private TimeSlots timeSlots;

    @BeforeEach
    void setUp() {
        timeSlots = new TimeSlots(LocalTime.of(8, 0), LocalTime.of(18, 0), 5);
    }

    //clear the time slots
    void clearTimeSlots() {
        for (int i = 0; i < timeSlots.getTimeSlots().length; i++) {
            timeSlots.getTimeSlots()[i] = TimeSlotValue.FREE;
        }
    }

    @Test
    void testGetTimeSlots() {
        TimeSlotValue[] slots = timeSlots.getTimeSlots();
        for (TimeSlotValue slot : slots) {
            assertEquals(TimeSlotValue.FREE, slot);
        }
    }

    @Test
    void testGetRemainingMinutes() {
        assertEquals(600, timeSlots.getRemainingMinutes());
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30));
        assertEquals(570, timeSlots.getRemainingMinutes());
    }

    @Test
    void testGetSlotSize() {
        assertEquals(5, timeSlots.getSlotSize());
    }

    @Test
    void testGetSlotCount() {
        assertEquals(120, timeSlots.getSlotCount());
    }

    @Test
    void testSetTimeSlot() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertFalse(timeSlots.isFree(LocalTime.of(9, 0), LocalTime.of(10, 0)));

        //test if start time is not in the time slots
        clearTimeSlots();
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(7, 0), LocalTime.of(9, 0));
        assertTrue(timeSlots.isSlotValue(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(9, 0)));
        assertTrue(timeSlots.isFree(LocalTime.of(9, 0), LocalTime.of(10, 0)));

        //test if end time is not in the time slots
        clearTimeSlots();
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(17, 0), LocalTime.of(19, 0));
        assertTrue(timeSlots.isSlotValue(TimeSlotValue.STUDY, LocalTime.of(17, 0), LocalTime.of(18, 0)));
        assertTrue(timeSlots.isFree(LocalTime.of(16, 0), LocalTime.of(17, 0)));

        //test if start and end time are not in the time slots
        clearTimeSlots();
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(7, 0), LocalTime.of(19, 0));
        assertTrue(timeSlots.isSlotValue(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(18, 0)));

        clearTimeSlots();
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(5,0), LocalTime.of(7,0));
        assertTrue(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(18, 0)));
    }

    @Test
    void testGetStartTime() {
        assertEquals(LocalTime.of(8, 0), timeSlots.getStartTime(0));
        assertEquals(LocalTime.of(8, 5), timeSlots.getStartTime(1));
    }

    @Test
    void testIsFree() {
        assertTrue(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(9, 0)));
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30));
        assertFalse(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(9, 0)));
    }

    @Test
    void testGetEarliestFree() {
        assertEquals(LocalTime.of(8, 0), timeSlots.getEarliestFree());
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30));
        assertEquals(LocalTime.of(8, 30), timeSlots.getEarliestFree());
    }

    @Test
    void testGetFreeMinutesAfter() {
        assertEquals(600, timeSlots.getFreeMinutesAfter(LocalTime.of(8, 0)));
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30));
        assertEquals(0, timeSlots.getFreeMinutesAfter(LocalTime.of(8, 0)));
        assertEquals(570, timeSlots.getFreeMinutesAfter(LocalTime.of(8, 30)));
    }

    @Test   
    void testIsSlotValue() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30));
        assertTrue(timeSlots.isSlotValue(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(8, 30)));
        assertFalse(timeSlots.isSlotValue(TimeSlotValue.FREE, LocalTime.of(8, 0), LocalTime.of(8, 30)));
    }
}

