package ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation;

import java.time.LocalTime;

/**
 * Represents a collection of time slots within a specified time range.
 * Each time slot has a fixed size and can be allocated with a specific value.
 */
public class TimeSlots {
    private LocalTime dayStartTime;
    private LocalTime dayEndTime;
    private TimeSlotValue[] timeSlots;
    private int remainingMinutes;
    private int slotSize;
    private int slotCount;

    /**
     * Constructs a TimeSlots object with the specified start time, end time, and slot size.
     *
     * @param dayStartTime the start time of the time slots
     * @param dayEndTime   the end time of the time slots
     * @param slotSize  the size of each time slot in minutes
     */
    public TimeSlots(LocalTime dayStartTime, LocalTime dayEndTime, int slotSize) {
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
        this.slotSize = slotSize;
        int startMinutes = dayStartTime.getHour() * 60 + dayStartTime.getMinute();
        int endMinutes = dayEndTime.getHour() * 60 + dayEndTime.getMinute();
        int totalMinutes = endMinutes - startMinutes;
        this.slotCount = totalMinutes / slotSize;
        this.timeSlots = new TimeSlotValue[slotCount];
        this.remainingMinutes = totalMinutes;

        for (int i = 0; i < timeSlots.length; i++) {
            timeSlots[i] = TimeSlotValue.FREE;
        }
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
        int adjust = 1;
        if (startTime.equals(endTime)) adjust = 0;

        int dayStartMinutes = dayStartTime.getHour() * 60 + dayStartTime.getMinute();
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();

        int startSlot = Math.max((startMinutes - dayStartMinutes) / slotSize, 0);
        int endSlot = Math.min(((endMinutes - dayStartMinutes) / slotSize) - adjust, timeSlots.length);

        if (startSlot >= endSlot) {
            timeSlots[startSlot] = content;
            remainingMinutes -= slotSize;
        } else {
            for (int i = startSlot; i < endSlot; i++) {
                timeSlots[i] = content;
                remainingMinutes -= slotSize;
            }
        }
        
    }

    /**
     * Returns the start time of the specified slot.
     *
     * @param slot the slot index
     * @return the start time of the slot
     */
    public LocalTime getStartTime(int slot) {
        int minutesOffset = slot * slotSize; // Minuten relativ zu Startzeit 08:00
        return dayStartTime.plusMinutes(minutesOffset);
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

        int startSlot = Math.max((startMinutes - dayStartTime.getHour() * 60) / slotSize, 0);
        int endSlot = Math.min((endMinutes - dayStartTime.getHour() * 60) / slotSize, timeSlots.length);

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
                int minutesOffset = i * slotSize; // Minuten relativ zu Startzeit 08:00
                return dayStartTime.plusMinutes(minutesOffset);
            }
        }
        return dayEndTime.plusMinutes(slotSize); // Falls kein freier Slot vorhanden ist
    }

    /**
     * Returns the number of free minutes available after the specified start time.
     *
     * @param startTime the start time to check from
     * @return the number of free minutes available
     */
    public int getFreeMinutesAfter(LocalTime startTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int startMinutesOffset = startMinutes - dayStartTime.getHour() * 60;
        int startSlot = startMinutesOffset / slotSize;

        int freeMinutes = 0;

        for (int i = startSlot; i < timeSlots.length; i++) {
            if (timeSlots[i] == TimeSlotValue.FREE) {
                freeMinutes += slotSize; // Freie Minuten summieren
            } else {
                break; // Abbrechen, sobald ein belegter Slot erreicht wird
            }
        }

        System.out.println("Calculated free minutes: " + freeMinutes);
        return freeMinutes;
    }



}
