package ch.zhaw.studyflow.domain.studyplan;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;

/**
 * Interface for managing study plans.
 * This interface provides a method to create a study plan with the specified parameters and user ID.
 */
public interface StudyplanManager {

    /**
     * Creates a study plan with the given parameters and user ID.
     *
     * @param parameters the parameters for the study plan
     * @param userId     the ID of the user creating the study plan
     * @return the ID of the created study plan
     */
    Long createStudyplan(StudyplanParameters parameters, long userId);
}