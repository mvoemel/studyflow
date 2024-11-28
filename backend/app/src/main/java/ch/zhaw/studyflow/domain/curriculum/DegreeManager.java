package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

public interface DegreeManager {
    /**
     * Creates the degree for the specified user.
     * @param userId The users id
     * @param degree The degree model to store in the database.
     */
    void createDegreeFor(long userId, Degree degree);

    /**
     * Fetches all available degrees for the user specified.
     * @param usersId The users id
     * @return a list of degrees if available; otherwise an empty list.
     */
    List<Degree> getDegreesForUser(long usersId);
}
