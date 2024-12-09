package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.studyflow.domain.studyplan.ModuleAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;

/**
 * Basic implementation of the ModuleAllocation interface.
 * This class represents the allocation of study time to a specific module.
 */
public class BasicModuleAllocation implements ModuleAllocation {
    private final long moduleId;
    private final long percentage;
    private final long targetMinutes;
    private long allocatedMinutes;
    private final List<StudyDay> studyDays;

    /**
     * Constructs a BasicModuleAllocation with the specified module ID, percentage, and target minutes.
     *
     * @param moduleId      the ID of the module
     * @param percentage    the percentage of the total study time allocated to this module
     * @param targetMinutes the target number of minutes to be allocated to this module
     */
    public BasicModuleAllocation(Long moduleId, Long percentage, long targetMinutes) {
        this.moduleId = moduleId;
        this.percentage = percentage;
        this.targetMinutes = targetMinutes;
        this.allocatedMinutes = 0;
        this.studyDays = new ArrayList<>();
    }

    /**
     * Returns the ID of the module.
     *
     * @return the module ID
     */
    @Override
    public long getModuleId() {
        return moduleId;
    }

    /**
     * Returns the percentage of the total study time allocated to this module.
     *
     * @return the percentage
     */
    @Override
    public long getPercentage() {
        return percentage;
    }

    /**
     * Returns the target number of minutes to be allocated to this module.
     *
     * @return the target minutes
     */
    @Override
    public long getTargetMinutes() {
        return targetMinutes;
    }

    /**
     * Returns the number of minutes that have been allocated to this module so far.
     *
     * @return the allocated minutes
     */
    @Override
    public long getAllocatedMinutes() {
        return allocatedMinutes;
    }

    /**
     * Returns the number of minutes remaining to be allocated to this module.
     *
     * @return the remaining minutes
     */
    @Override
    public long getRemainingMinutes() {
        return targetMinutes - allocatedMinutes;
    }

    /**
     * Allocates study time to this module for a given study day.
     *
     * @param day the study day to allocate
     */
    @Override
    public void allocate(StudyDay day) {
        studyDays.add(day);
        if (this.getRemainingMinutes() >= day.getMinutes()) {
            allocatedMinutes += day.getMinutes();
        } else {
            allocatedMinutes += this.getRemainingMinutes();
        }
    }

    /**
     * Compares this module allocation with another based on the remaining minutes.
     *
     * @param other the other module allocation to compare to
     * @return a negative integer, zero, or a positive integer as this module allocation
     *         has less than, equal to, or greater remaining minutes than the other
     */
    @Override
    public int compareTo(ModuleAllocation other) {
        return Long.compare(other.getRemainingMinutes(), this.getRemainingMinutes());
    }

    /**
     * Returns the list of study days allocated to this module.
     *
     * @return the list of study days
     */
    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }
}