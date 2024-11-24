package ch.zhaw.studyflow.domain.grade;


import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.List;

/**
 * Manages grades in the system.
 */
public class GradeManager {
    private final GradeDao gradeDao;

    /**
     * Constructs a GradeManager with the specified GradeDao.
     *
     * @param gradeDao the GradeDao to use for data access.
     */
    public GradeManager(GradeDao gradeDao) {
        this.gradeDao = gradeDao;
    }

    /**
     * Creates a new grade.
     *
     * @param grade the grade to create.
     */
    public void create(Grade grade) {
        gradeDao.create(grade);
    }

    /**
     * Reads a grade by its ID.
     *
     * @param gradeId the grade ID.
     * @return the grade.
     */
    public Grade read(long gradeId) {
        return gradeDao.read(gradeId);
    }

    /**
     * Deletes a grade by its ID.
     *
     * @param gradeId the grade ID.
     */
    public void delete(long gradeId) {
        gradeDao.delete(gradeId);
    }

    /**
     * Updates an existing grade.
     *
     * @param grade the grade to update.
     * @return the updated grade.
     */
    public Grade update(Grade grade) {
        gradeDao.update(grade);
        return grade;
    }

    /**
     * Reads grades by module ID.
     *
     * @param moduleId the module ID.
     * @return the list of grades.
     */
    public List<Grade> readByModule(long moduleId) {
        return gradeDao.readByModule(moduleId);
    }

    /**
     * Reads grades by student ID.
     *
     * @param studentId the student ID.
     * @return the list of grades.
     */
    public List<Grade> readByStudent(long studentId) {
        return gradeDao.readByStudent(studentId);
    }
}

