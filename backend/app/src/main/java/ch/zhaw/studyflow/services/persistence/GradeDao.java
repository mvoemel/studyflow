// GradeDao.java
package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.grade.Grade;

import java.util.List;

/**
 * Data Access Object for grades.
 */
public interface GradeDao {

    /**
     * Creates a new grade.
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
     * Reads grades by degree ID.
     *
     * @param degreeId the degree ID.
     * @return the list of grades.
     */
    List<Grade> readByDegree(long degreeId);

    /**
     * Updates an existing grade.
     *
     * @param grade the grade to update.
     */
    void update(Grade grade);

    /**
     * Updates grades by degree ID.
     *
     * @param degreeId the degree ID.
     * @param grades the list of grades to update.
     */
    void updateByDegree(long degreeId, List<Grade> grades);

    /**
     * Deletes a grade by its ID.
     *
     * @param gradeId the grade ID.
     */
    void delete(long gradeId);
}