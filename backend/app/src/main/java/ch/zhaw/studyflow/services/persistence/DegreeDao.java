package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

/**
 * Data access object for reading, writing and updating degrees from and to a persistent storage.
 */
public interface DegreeDao {
    /**
     * Writes a new degree to the persistent storage and assigns an ID to it.
     * @param degree
     */
    void create(Degree degree);
    Degree read(long degreeId);
    List<Degree> readAllByStudent(long studentId);
    void update(Degree degree);
    void delete(long degreeId);
}
