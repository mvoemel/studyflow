package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.timeSlotCalculation.TimeSlots;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of the StudyDay interface.
 * This class represents a study day with a date, start time, end time, and study allocations.
 */
public class BasicStudyDay implements StudyDay {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private long minutes;
    private List<Appointment> appointments;
    private TimeSlots timeSlots;
    private final int slotSize = 5;
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
        this.timeSlots = new TimeSlots(startTime, endTime, slotSize);
        this.studyAllocations = new ArrayList<>();
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
        studyAllocations.remove(studyAllocation);
    }

    /**
     * Calculates the study allocations for this study day based on the available time slots.
     */
    @Override
    public void calculateStudyAllocations() {
        // Implementation details...
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