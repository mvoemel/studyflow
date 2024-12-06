package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

import java.util.List;
import java.util.Optional;

/**
 * Data access object for reading, writing and updating semester from and to a persistent storage.
 */
public interface SemesterDao {
    /**
     * Creates a new semester.
     * @param semester The semester to create
     * @param degreeId The id of the degree
     * @param userId The id of the user
     */
    void create(Semester semester, long degreeId, long userId);

    /**
     * Reads all semesters for a student.
     * @param studentId The id of the student
     * @return a list of all semesters
     */
    List<Semester> readAllByStudent(long studentId);

    /**
     * Reads all semesters for a degree.
     * @param degreeId The id of the degree
     * @return a list of all semesters
     */
    List<Semester> readAllByDegree(long degreeId);

    /**
     * Reads a specific semester.
     * @param semesterId The id of the semester
     * @return the semester, or null if not found
     */
    Optional<Semester> readSemesterById(long semesterId);

    /**
     * Updates a semester.
     * @param semester The semester to update
     */
    void update(Semester semester);

    /**
     * Deletes a specific semester.
     * @param semesterId The id of the semester
     */
    void deleteSemester(long semesterId);

}
