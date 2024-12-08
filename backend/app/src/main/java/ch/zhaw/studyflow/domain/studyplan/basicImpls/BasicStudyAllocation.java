package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Basic implementation of the StudyAllocation interface.
 * This class represents the allocation of study time with a start and end time.
 */
public class BasicStudyAllocation implements StudyAllocation {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * Constructs a BasicStudyAllocation with the specified start and end times.
     *
     * @param startTime the start time of the study allocation
     * @param endTime   the end time of the study allocation
     */
    public BasicStudyAllocation(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Constructs a BasicStudyAllocation with the specified start and end times on a specific date.
     *
     * @param start the start time of the study allocation
     * @param end   the end time of the study allocation
     * @param date  the date of the study allocation
     */
    public BasicStudyAllocation(LocalTime start, LocalTime end, LocalDate date) {
        this.startTime = LocalDateTime.of(date, start);
        this.endTime = LocalDateTime.of(date, end);
    }

    /**
     * Returns the start time of the study allocation.
     *
     * @return the start time
     */
    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the start time of the study allocation.
     *
     * @param startTime the new start time
     */
    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the end time of the study allocation.
     *
     * @return the end time
     */
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Sets the end time of the study allocation.
     *
     * @param endTime the new end time
     */
    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the number of minutes between the start and end times.
     *
     * @return the number of minutes
     */
    @Override
    public int getMinutes() {
        return (int) (endTime.toLocalTime().toSecondOfDay() - startTime.toLocalTime().toSecondOfDay()) / 60;
    }
}