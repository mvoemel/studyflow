package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlotValue;
import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlots;

/**
 * Basic implementation of the StudyDay interface.
 * This class represents a study day with a date, start time, end time, and study allocations.
 */
public class BasicStudyDay implements StudyDay {
    private final int LUNCH_BREAK = 60;
    private final int SHORT_DAY_MINUTES = 240;
    private final int LUNCH_BREAK_SHORT = 30;
    private final int LUNCH_BREAK_CLASH = 45;
    private final LocalTime EARLY_MIDDAY = LocalTime.of(12, 00);
    private final LocalTime LATE_MIDDAY = LocalTime.of(14, 00);
    private final int MAX_MINUTES_PER_BLOCK = 120;
    private final int MIN_MINUTES_PER_BLOCK = 30;
    private final int MIN_MINUTES_PER_BLOCK_WITH_BREAK = 60;
    private final int BLOCK_BREAK = 10;
    private final int APPOINTMENT_BUFFER = 15;
    private final int SLOT_SIZE = 5;
    

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private long minutes;
    private List<Appointment> appointments;
    private TimeSlots timeSlots;
    
    private List<StudyAllocation> studyAllocations; 

    /**
     * Constructs a BasicStudyDay with the specified date, start time, and end time.
     *
     * @param date      the date of the study day
     * @param startTime the start time of the study day
     * @param endTime   the end time of the study day
     */
    public BasicStudyDay(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.timeSlots = new TimeSlots(startTime, endTime, SLOT_SIZE);
        this.studyAllocations = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    /**
     * Returns the date of the study day.
     *
     * @return the date
     */
    @Override
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date of the study day.
     *
     * @param date the new date
     */
    @Override
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the total number of minutes allocated for study on this day.
     *
     * @return the total minutes
     */
    @Override
    public long getMinutes() {
        return minutes;
    }

    /**
     * Calculates the total number of minutes allocated for study on this day.
     */
    @Override
    public void calculateMinutes() {
        minutes = 0;
        for (StudyAllocation studyAllocation : studyAllocations) {
            minutes += studyAllocation.getMinutes();
        }
    }

    /**
     * Returns the list of appointments for this study day.
     *
     * @return the list of appointments
     */
    @Override
    public List<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Adds an appointment to this study day.
     *
     * @param appointment the appointment to add
     */
    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * Removes an appointment from this study day.
     *
     * @param appointment the appointment to remove
     */
    @Override
    public void removeAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    /**
     * Returns the list of study allocations for this study day.
     *
     * @return the list of study allocations
     */
    @Override
    public List<StudyAllocation> getStudyAllocations() {
        return studyAllocations;
    }

    /**
     * Adds a study allocation to this study day.
     *
     * @param studyAllocation the study allocation to add
     */
    @Override
    public void addStudyAllocation(StudyAllocation studyAllocation) {
        studyAllocations.add(studyAllocation);
    }

    /**
     * Removes a study allocation from this study day.
     *
     * @param studyAllocation the study allocation to remove
     */
    @Override
    public void removeStudyAllocation(StudyAllocation studyAllocation) {
        if (studyAllocations.contains(studyAllocation))
        studyAllocations.remove(studyAllocation);
    }

    /**
     * Calculates the study allocations for this study day based on the available time slots.
     * Process:
        1. Using startTime and endTime create an array of time slots (e.g. 5 minutes each)
        2. Check if there are appointments between start and end time, if yes add a break of 15 minutes before and after the appointment and mark the time slots as taken 
        3. Standard lunch break: 60 minutes between 11:30 and 14:30, ideally in the middle of the day
        - figure out middle of the day (e.g. 12:30) and distribute lunch around this time
        - if any appointments are clash with the lunch break, move the lunch break to a different time (right before or after the appointment, such that it is as close to intended time as possible, only 45 minutes)
        - if day starts after 12:00 or ends before 14:00, or total time of studyDay <4h reduce lunch break to 30 minutes and place in middle of day (unless clashes with appointment, then place before or after and reduce to 15 minutes)
        4. Create studyAllocations for remaining time slots
     */
    @Override
    public void calculateStudyAllocations() {         
        
        //mark appointments in TimeSlots
        for (Appointment appointment : appointments) {
            timeSlots.setTimeSlot(TimeSlotValue.APPOINTMENT, appointment.getStartTime().toLocalTime(), appointment.getEndTime().toLocalTime());
            timeSlots.setTimeSlot(TimeSlotValue.BREAK, appointment.getStartTime().toLocalTime().minusMinutes(15), appointment.getStartTime().toLocalTime());
        }
        
        //calculate lunch break
        int lunchBreak = 60;
        LocalTime midDay = timeSlots.getStartTime(timeSlots.getSlotCount() / 2);

        //determine duration and placement of lunch break
        if (timeSlots.getRemainingMinutes() < 240 || startTime.isAfter(LocalTime.of(12, 0)) || endTime.isBefore(LocalTime.of(14, 0))) {
            lunchBreak = 30;
        } else {
            if (midDay.isBefore(LocalTime.of(12, 00))) {
                midDay = LocalTime.of(12, 00);
            } else if (midDay.isAfter(LocalTime.of(14, 00))) {
                midDay = LocalTime.of(14, 00);
            }
        }

        //check if lunch break clashes with appointments
        if(!timeSlots.isFree(midDay.minusMinutes(lunchBreak / 2), midDay.plusMinutes(lunchBreak / 2))) {
            lunchBreak = 45;
            //move lunch break to before or after appointment
            boolean moved = false;
            int moveBy = SLOT_SIZE;
            while (!moved && midDay.minusMinutes(lunchBreak / 2 - moveBy - 60).isAfter(startTime) && midDay.plusMinutes(lunchBreak / 2 + moveBy + 60).isBefore(endTime)) {
                if (timeSlots.isFree(midDay.minusMinutes(lunchBreak / 2 - moveBy), midDay.plusMinutes(lunchBreak / 2 - moveBy))) {
                    midDay = midDay.minusMinutes(moveBy);
                    moved = true;
                } else if (timeSlots.isFree(midDay.minusMinutes(lunchBreak / 2 + moveBy), midDay.plusMinutes(lunchBreak / 2 + moveBy))) {
                    midDay = midDay.plusMinutes(moveBy);
                    moved = true;
                } else {
                    moveBy += SLOT_SIZE;
                    if (moveBy > 90) lunchBreak = 30;
                }
            }
        }

        //mark lunch break in TimeSlots
        timeSlots.setTimeSlot(TimeSlotValue.BREAK, midDay.minusMinutes(lunchBreak / 2), midDay.plusMinutes(lunchBreak / 2));

        //create studyAllocations for remaining time slots
        while (timeSlots.getRemainingMinutes() >= 30) {
            LocalTime start = timeSlots.getEarliestFree();
            if (start.isAfter(endTime)) {
                break;
            }
            System.out.println("Start: " + start);
            int availableMinutes = timeSlots.getFreeMinutesAfter(startTime);
            System.out.println("Available minutes: " + availableMinutes);
            
            if (availableMinutes < 30) {
                timeSlots.setTimeSlot(TimeSlotValue.BREAK, start, start.plusMinutes(availableMinutes));
            } else if (availableMinutes < 120) {
                timeSlots.setTimeSlot(TimeSlotValue.STUDY, start, start.plusMinutes(availableMinutes));
                studyAllocations.add(new BasicStudyAllocation(start, start.plusMinutes(availableMinutes), date));
            } else {
                int nrOfBlocks = availableMinutes / 120 + 1;
                int blockLength = (((availableMinutes - nrOfBlocks * 10) / nrOfBlocks)/SLOT_SIZE)*SLOT_SIZE;
                for(int i = 0; i < nrOfBlocks; i++) {
                    timeSlots.setTimeSlot(TimeSlotValue.STUDY, start.plusMinutes(i * (blockLength + 10)), start.plusMinutes((i + 1) * blockLength + i * 10));
                    timeSlots.setTimeSlot(TimeSlotValue.BREAK, start.plusMinutes((i + 1) * blockLength + i * 10), start.plusMinutes((i + 1) * blockLength + (i + 1) * 10));
                    studyAllocations.add(new BasicStudyAllocation(start.plusMinutes(i * (blockLength + 10)), start.plusMinutes((i + 1) * blockLength + i * 10), date));
                }
            }
            System.out.println("Remaining minutes: " + timeSlots.getRemainingMinutes());
        }

    }

    /**
     * Compares this study day with another based on the total minutes allocated for study.
     *
     * @param other the other study day to compare to
     * @return a negative integer, zero, or a positive integer as this study day
     *         has less than, equal to, or greater total minutes than the other
     */
    @Override
    public int compareTo(StudyDay other) {
        return Long.compare(other.getMinutes(), this.getMinutes());
    }
}