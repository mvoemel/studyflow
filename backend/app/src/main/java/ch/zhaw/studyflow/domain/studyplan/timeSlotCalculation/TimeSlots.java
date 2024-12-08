package ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation;

import java.time.LocalTime;

/**
 * Represents a collection of time slots within a specified time range.
 * Each time slot has a fixed size and can be allocated with a specific value.
 */
public class TimeSlots {
    private TimeSlotValue[] timeSlots;
    private int remainingMinutes;
    private int slotSize;
    private int slotCount;

    /**
     * Constructs a TimeSlots object with the specified start time, end time, and slot size.
     *
     * @param startTime the start time of the time slots
     * @param endTime   the end time of the time slots
     * @param slotSize  the size of each time slot in minutes
     */
    public TimeSlots(LocalTime startTime, LocalTime endTime, int slotSize) {
        this.slotSize = slotSize;
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int totalMinutes = endMinutes - startMinutes;
        this.slotCount = totalMinutes / slotSize;
        this.timeSlots = new TimeSlotValue[slotCount];
        this.remainingMinutes = totalMinutes;
    }

    /**
     * Returns the array of time slots.
     *
     * @return the array of time slots
     */
    public TimeSlotValue[] getTimeSlots() {
        return timeSlots;
    }

    /**
     * Returns the remaining minutes available in the time slots.
     *
     * @return the remaining minutes
     */
    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    /**
     * Returns the size of each time slot in minutes.
     *
     * @return the size of each time slot
     */
    public int getSlotSize() {
        return slotSize;
    }

    /**
     * Returns the total number of time slots.
     *
     * @return the total number of time slots
     */
    public int getSlotCount() {
        return slotCount;
    }

    /**
     * Sets the value of the time slots within the specified start and end times.
     *
     * @param content   the value to set for the time slots
     * @param startTime the start time of the range to set
     * @param endTime   the end time of the range to set
     */
    public void setTimeSlot(TimeSlotValue content, LocalTime startTime, LocalTime endTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int startSlot = startMinutes / slotSize;
        int endSlot = endMinutes / slotSize;
        for (int i = startSlot; i < endSlot; i++) {
            timeSlots[i] = content;
        }
        remainingMinutes -= (endMinutes - startMinutes);
    }

    /**
     * Returns the start time of the specified slot.
     *
     * @param slot the slot index
     * @return the start time of the slot
     */
    public LocalTime getStartTime(int slot) {
        return LocalTime.of(slot * slotSize / 60, slot * slotSize % 60);
    }

    /**
     * Checks if the time slots within the specified start and end times are free.
     *
     * @param startTime the start time of the range to check
     * @param endTime   the end time of the range to check
     * @return true if the time slots are free, false otherwise
     */
    public boolean isFree(LocalTime startTime, LocalTime endTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int startSlot = startMinutes / slotSize;
        int endSlot = endMinutes / slotSize;
        for (int i = startSlot; i < endSlot; i++) {
            if (timeSlots[i] != TimeSlotValue.FREE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the earliest free time slot.
     *
     * @return the start time of the earliest free time slot, or null if none are free
     */
    public LocalTime getEarliestFree() {
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i] == TimeSlotValue.FREE) {
                return getStartTime(i);
            }
        }
        return null;
    }

    /**
     * Returns the number of free minutes available after the specified start time.
     *
     * @param startTime the start time to check from
     * @return the number of free minutes available
     */
    public int getFreeMinutesAfter(LocalTime startTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int startSlot = startMinutes / slotSize;
        for (int i = startSlot; i < timeSlots.length; i++) {
            if (timeSlots[i] != TimeSlotValue.FREE) {
                return i * slotSize - startMinutes;
            }
        }
        return 0;
    }

    
}