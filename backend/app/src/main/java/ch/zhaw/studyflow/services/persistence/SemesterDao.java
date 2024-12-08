package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

import java.util.List;
import java.util.Optional;

/**
 * Data access object for reading, writing and updating semesters from and to a persistent storage.
 */
public interface SemesterDao {
    void createSemester(Semester semester, long degreeId, long studentId);
    Optional<Semester> read(long semesterId);
    List<Semester> readAllByStudent(long studentId);
    List<Semester> readAllByDegree(long degreeId);
    void update(Semester semester);
    void delete(long semesterId);
    List<Semester> readByDegreeId(long degreeId);

}
