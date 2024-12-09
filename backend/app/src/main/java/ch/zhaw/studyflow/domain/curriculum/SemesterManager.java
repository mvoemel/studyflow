package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

/**
 * Interface for managing semesters.
 * This interface provides methods to create, get, update, and delete semesters.
 */
public interface SemesterManager {
    /**
     * Creates a new semester for a degree.
     *
     * @param semester The semester to create
     * @param degreeId The ID of the degree
     * @param userId   The ID of the user
     */
    void createSemester(Semester semester, long degreeId, long userId);

    /**
     * Gets the semesters for a student.
     *
     * @param userId The ID of the student
     * @return The semesters for the student
     */
    List<Semester> getSemestersForStudent(long userId);

    /**
     * Gets the semesters for a degree.
     *
     * @param degreeId The ID of the degree
     * @return The semesters for the degree
     */
    List<Semester> getSemestersForDegree(long degreeId);

    /**
     * Gets the semester with the given ID.
     *
     * @param semesterId The ID of the semester
     * @return The semester with the given ID
     */
    Semester getSemesterById(long semesterId);

    /**
     * Updates the semester with the given ID.
     *
     * @param semester The semester to update
     */
    void updateSemester(Semester semester);

    /**
     * Deletes the semester with the given ID.
     *
     * @param semesterId The ID of the semester
     */
    void deleteSemester(long semesterId);
}
