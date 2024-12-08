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
        timeSlots = new TimeSlots(LocalTime.of(8, 0), LocalTime.of(18, 0), 60);
    }

    @Test
    void testSetTimeSlot() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertEquals(TimeSlotValue.STUDY, timeSlots.getTimeSlots()[1], "The slot at 09:00 should be occupied.");
        assertEquals(TimeSlotValue.STUDY, timeSlots.getTimeSlots()[2], "The slot at 10:00 should be occupied.");
        assertEquals(TimeSlotValue.FREE, timeSlots.getTimeSlots()[0], "The slot at 08:00 should be free.");
        assertEquals(TimeSlotValue.FREE, timeSlots.getTimeSlots()[3], "The slot at 11:00 should be free.");
        assertEquals(480, timeSlots.getRemainingMinutes(), "There should be 480 remaining minutes.");
    }

    @Test
    void testIsFree() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(9, 0), LocalTime.of(11, 0));
        assertFalse(timeSlots.isFree(LocalTime.of(9, 0), LocalTime.of(11, 0)), "The time from 09:00 to 11:00 should be occupied.");
        assertTrue(timeSlots.isFree(LocalTime.of(8, 0), LocalTime.of(9, 0)), "The time from 08:00 to 09:00 should be free.");
    }

    @Test
    void testGetEarliestFree() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(8, 0), LocalTime.of(9, 0));
        assertEquals(LocalTime.of(9, 0), timeSlots.getEarliestFree(), "The earliest free time should be at 09:00.");
    }

    @Test
    void testGetFreeMinutesAfter() {
        timeSlots.setTimeSlot(TimeSlotValue.STUDY, LocalTime.of(9, 0), LocalTime.of(10, 0));
        assertEquals(60, timeSlots.getFreeMinutesAfter(LocalTime.of(8, 0)), "There should be 60 free minutes after 08:00.");
    }

    @Test
    void testSlotInitialization() {
        for (int i = 0; i < timeSlots.getTimeSlots().length; i++) {
            System.out.println("Slot " + i + ": " + timeSlots.getTimeSlots()[i]);
            assertEquals(TimeSlotValue.FREE, timeSlots.getTimeSlots()[i], "Slot " + i + " should be FREE.");
        }
    }
}
