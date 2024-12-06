package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

/**
 * Data access object for reading, writing and updating degrees from and to a persistent storage.
 */
public interface DegreeDao {
    /**
     * Creates a new degree.
     * @param degree the degree to create
     */
    void create(Degree degree);

    /**
     * Reads a specific degree.
     * @param degreeId The id of the degree
     * @return the degree, or null if not found
     */
    Degree read(long degreeId);

    /**
     * Reads all degrees.
     * @return a list of all degrees
     */
    List<Degree> readAllByStudent(long degreeId);

    /**
     * Updates a degree.
     * @param degree the degree to update
     */
    void update(Degree degree);

    /**
     * Deletes a specific degree.
     * @param id The id of the degree
     */
    void delete(long id);
}
