package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDateTime;

/**
 * Represents an allocation of study time within a study plan.
 * This interface provides methods to get and set the start and end times of the study allocation,
 * as well as to get the total number of minutes allocated.
 */
public interface StudyAllocation {

    /**
     * Returns the start time of the study allocation.
     *
     * @return the start time
     */
    public LocalDateTime getStartTime();

    /**
     * Sets the start time of the study allocation.
     *
     * @param startTime the new start time
     */
    public void setStartTime(LocalDateTime startTime);

    /**
     * Returns the end time of the study allocation.
     *
     * @return the end time
     */
    public LocalDateTime getEndTime();

    /**
     * Sets the end time of the study allocation.
     *
     * @param endTime the new end time
     */
    public void setEndTime(LocalDateTime endTime);

    /**
     * Returns the total number of minutes allocated for study.
     *
     * @return the total minutes
     */
    public int getMinutes();
}