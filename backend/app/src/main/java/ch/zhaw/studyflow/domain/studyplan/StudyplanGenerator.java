package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;

/**
 * Interface for generating a study plan.
 * This interface provides a method to generate a study plan and return its ID.
 */
public interface StudyplanGenerator {

    /**
     * Generates a study plan.
     *
     * @return the ID of the generated study plan
     */
    long generateStudyplan();

    /**
     * Creates a calendar for the study plan.
     *
     * @return the ID of the created calendar
     */
    long createCalendar();

    /**
     * Creates appointments for the study plan.
     *
     * @param studyplanCalendarId the ID of the study plan calendar
     * @param moduleAllocations   the module allocations
     */
    void createAppointments(long studyplanCalendarId, List<ModuleAllocation> moduleAllocations);
}