package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.grade.Grade;

import java.util.List;

/**
 * Data access object for reading, writing and updating students from and to a persistent storage.
 */
public interface GradeDao {

    /**
     * Writes a new grade to the persistent storage and assigns an ID to it.
     *
     * @param grade the grade to create.
     */
    void create(Grade grade);

    /**
     * Reads a grade by its ID.
     *
     * @param gradeId the grade ID.
     * @return the grade.
     */
    Grade read(long gradeId);

    /**
     * Reads grades by module ID.
     *
     * @param moduleId the module ID.
     * @return the list of grades.
     */
    List<Grade> readByModule(long moduleId);

    /**
     * Updates an existing grade.
     *
     * @param grade the grade to update.
     */
    void update(Grade grade);

    /**
     * Updates grades by degree ID.
     *
     * @param moduleId the degree ID.
     * @param grades the list of grades to update.
     */
    void updateByModule(long moduleId, List<Grade> grades);

    /**
     * Deletes a grade by its ID.
     *
     * @param gradeId the grade ID.
     */
    void delete(long gradeId);
}