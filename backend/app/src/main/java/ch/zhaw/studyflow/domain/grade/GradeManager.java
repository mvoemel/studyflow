package ch.zhaw.studyflow.domain.grade;

import java.util.List;

/**
 * Interface for managing grades in the domain.
 */
public interface GradeManager {

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
     * Deletes a grade by its ID.
     *
     * @param gradeId the grade ID.
     */
    void delete(long gradeId);

    /**
     * Updates an existing grade.
     *
     * @param grade the grade to update.
     * @return the updated grade.
     */
    Grade update(Grade grade);

    /**
     * Reads grades by module ID.
     *
     * @param moduleId the module ID.
     * @return the list of grades.
     */
    List<Grade> readByModule(long moduleId);

    /**
     * Reads grades by student ID.
     *
     * @param studentId the student ID.
     * @return the list of grades.
     */
    List<Grade> readByStudent(long studentId);
}