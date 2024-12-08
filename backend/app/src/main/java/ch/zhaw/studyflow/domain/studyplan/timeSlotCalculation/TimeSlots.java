package ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation;

import java.time.LocalTime;

public class TimeSlots {
    private TimeSlotContent[] timeSlots;
    private int remainingMinutes;
    private int slotSize;

    public TimeSlots(LocalTime startTime, LocalTime endTime, int slotSize) {
        this.slotSize = slotSize;
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int totalMinutes = endMinutes - startMinutes;
        int slots = totalMinutes / slotSize;
        this.timeSlots = new TimeSlotContent[slots];
        this.remainingMinutes = totalMinutes;
    }

    public TimeSlotContent[] getTimeSlots() {
        return timeSlots;
    }

    public int getRemainingMinutes() {
        return remainingMinutes;
    }

    public int getSlotSize() {
        return slotSize;
    }

    public void setTimeSlot(TimeSlotContent content, LocalTime startTime, LocalTime endTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int startSlot = startMinutes / slotSize;
        int endSlot = endMinutes / slotSize;
        for (int i = startSlot; i < endSlot; i++) {
            timeSlots[i] = content;
        }
        remainingMinutes -= (endMinutes - startMinutes);
    }

    public LocalTime getStartTime(int slot) {
        return LocalTime.of(slot * slotSize / 60, slot * slotSize % 60);
    }

    public boolean isFree(LocalTime startTime, LocalTime endTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int endMinutes = endTime.getHour() * 60 + endTime.getMinute();
        int startSlot = startMinutes / slotSize;
        int endSlot = endMinutes / slotSize;
        for (int i = startSlot; i < endSlot; i++) {
            if (timeSlots[i] != TimeSlotContent.FREE) {
                return false;
            }
        }
        return true;
    }

    public LocalTime getEarliestFree() {
        for (int i = 0; i < timeSlots.length; i++) {
            if (timeSlots[i] == TimeSlotContent.FREE) {
                return getStartTime(i);
            }
        }
        return null;
    }

    public int getFreeMinutesAfter(LocalTime startTime) {
        int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
        int startSlot = startMinutes / slotSize;
        for (int i = startSlot; i < timeSlots.length; i++) {
            if (timeSlots[i] != TimeSlotContent.FREE) {
                return i * slotSize - startMinutes;
            }
        }
        return 0;
    }

    
}
