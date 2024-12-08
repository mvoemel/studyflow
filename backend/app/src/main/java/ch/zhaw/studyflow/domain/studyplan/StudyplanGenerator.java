package ch.zhaw.studyflow.domain.studyplan;

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
    Long generateStudyplan();
}