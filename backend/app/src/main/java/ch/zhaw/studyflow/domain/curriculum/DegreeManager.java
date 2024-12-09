package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

public interface DegreeManager {
    /**
     * Creates a degree for the specified user.
     * @param userId The users id
     * @param degree The degree model to store in the database.
     */
    void createDegree(long userId, Degree degree);

    /**
     * Returns a list of degrees for the specified user.
     * @param userId The users id
     * @return A list of degree models
     */
    List<Degree> getDegreesForStudent(long userId);

    /**
     * Returns the degree for the specified id.
     * @param degreeId The degree id
     * @return The degree model
     */
    Degree getDegree(long degreeId);

    /**
     * Writes the passed degree to the persistant storage.
     * @param degree The degree to store
     */
    void updateDegree(Degree degree);

    /**
     * Deletes the degree with the specified id.
     * @param degreeId The degree id
     */
    void deleteDegree(long degreeId);
}
