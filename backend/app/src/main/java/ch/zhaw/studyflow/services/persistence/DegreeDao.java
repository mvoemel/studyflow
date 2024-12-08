package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

/**
 * Data access object for reading, writing and updating degrees from and to a persistent storage.
 */
public interface DegreeDao {
    /**
     * Writes a new degree to the persistent storage and assigns an ID to it.
     * @param degree the degree to create
     */
    void create(Degree degree);

    /**
     * Reads a degree from the persistent storage.
     * @param degreeId the ID of the degree to read
     * @return the degree, or null if not found
     */
    Degree read(long degreeId);

    /**
     * Reads all degrees for a student from the persistent storage.
     * @param studentId the ID of the student
     * @return a list of degrees for the student
     */
    List<Degree> readAllByStudent(long studentId);

    /**
     * Updates a degree in the persistent storage.
     * @param degree the degree to update
     */
    void update(Degree degree);

    /**
     * Deletes a degree from the persistent storage.
     * @param degreeId the ID of the degree to delete
     */
    void delete(long degreeId);
}
