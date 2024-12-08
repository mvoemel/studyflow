package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

import java.util.List;
import java.util.Optional;

/**
 * Data access object for reading, writing and updating semesters from and to a persistent storage.
 */
public interface SemesterDao {
    /**
     * Writes a new semester to the persistent storage and assigns an ID to it.
     * @param semester the semester to create
     * @param degreeId the ID of the degree
     * @param studentId the ID of the student
     */
    void createSemester(Semester semester, long degreeId, long studentId);

    /**
     * Reads a specific semester by its ID.
     * @param semesterId the ID of the semester
     * @return the semester, or null if not found
     */
    Optional<Semester> read(long semesterId);

    /**
     * Reads all semesters for a student from the persistent storage.
     * @param studentId the ID of the student
     * @return a list of semesters for the student
     */
    List<Semester> readAllByStudent(long studentId);

    /**
     * Reads all semesters for a degree from the persistent storage.
     * @param degreeId the ID of the degree
     * @return a list of semesters for the degree
     */
    List<Semester> readAllByDegree(long degreeId);

    /**
     * Updates a semester in the persistent storage.
     * @param semester the semester to update
     */
    void update(Semester semester);

    /**
     * Deletes a semester from the persistent storage.
     * @param semesterId the ID of the semester to delete
     */
    void delete(long semesterId);

    /**
     * Reads all semesters for a degree from the persistent storage.
     * @param degreeId the ID of the degree
     * @return a list of semesters for the degree
     */
    List<Semester> readByDegreeId(long degreeId);

}
