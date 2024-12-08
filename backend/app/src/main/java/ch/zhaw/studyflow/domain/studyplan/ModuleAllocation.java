package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;

/**
 * Represents a module allocation in the study plan.
 */

public interface ModuleAllocation extends Comparable<ModuleAllocation> {

    /**
     * Gets the ID of the module.
     *
     * @return the ID of the module
     */
    long getModuleId();

    /**
     * Gets the percentage of the module allocation.
     *
     * @return the percentage of the module allocation
     */
    long getPercentage();

    /**
     * Gets the target minutes of the module allocation.
     *
     * @return the target minutes of the module allocation
     */
    long getTargetMinutes();

    /**
     * Gets the allocated minutes of the module allocation.
     *
     * @return the allocated minutes of the module allocation
     */
    long getAllocatedMinutes();

    /**
     * Gets the remaining minutes of the module allocation.
     *
     * @return the remaining minutes of the module allocation
     */
    long getRemainingMinutes();

    /**
     * Allocates the specified study day to the module allocation.
     *
     * @param day the study day to allocate
     */
    void allocate(StudyDay day);

    /**
     * Gets the study days of the module allocation.
     *
     * @return the study days of the module allocation
     */
    List<StudyDay> getStudyDays();

    /**
     * Compares this module allocation to the specified module allocation.
     *
     * @param other the module allocation to compare to
     * @return a negative integer, zero, or a positive integer if this module allocation is less than, equal to, or greater than the specified module allocation
     */
    @Override
    int compareTo(ModuleAllocation other);
}
