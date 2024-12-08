package ch.zhaw.studyflow.domain.studyplan;

import ch.zhaw.studyflow.domain.calendar.Appointment;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a study day within a study plan.
 * This interface provides methods to manage the date, minutes, appointments, and study allocations for the study day.
 */
public interface StudyDay extends Comparable<StudyDay> {

    /**
     * Returns the date of the study day.
     *
     * @return the date of the study day
     */
    LocalDate getDate();

    /**
     * Sets the date of the study day.
     *
     * @param date the new date of the study day
     */
    void setDate(LocalDate date);

    /**
     * Returns the total number of minutes available for study on this day.
     *
     * @return the total minutes available for study
     */
    long getMinutes();

    /**
     * Calculates the total number of minutes available for study on this day.
     */
    void calculateMinutes();

    /**
     * Returns the list of appointments scheduled for this study day.
     *
     * @return the list of appointments
     */
    List<Appointment> getAppointments();

    /**
     * Adds an appointment to this study day.
     *
     * @param appointment the appointment to add
     */
    void addAppointment(Appointment appointment);

    /**
     * Removes an appointment from this study day.
     *
     * @param appointment the appointment to remove
     */
    void removeAppointment(Appointment appointment);

    /**
     * Returns the list of study allocations for this study day.
     *
     * @return the list of study allocations
     */
    List<StudyAllocation> getStudyAllocations();

    /**
     * Adds a study allocation to this study day.
     *
     * @param studyAllocation the study allocation to add
     */
    void addStudyAllocation(StudyAllocation studyAllocation);

    /**
     * Removes a study allocation from this study day.
     *
     * @param studyAllocation the study allocation to remove
     */
    void removeStudyAllocation(StudyAllocation studyAllocation);

    /**
     * Calculates the study allocations for this study day.
     */
    void calculateStudyAllocations();

    /**
     * Compares this study day with another study day based on their dates.
     *
     * @param other the other study day to compare to
     * @return a negative integer, zero, or a positive integer as this study day is earlier than, equal to, or later than the specified study day
     */
    @Override
    int compareTo(StudyDay other);
}