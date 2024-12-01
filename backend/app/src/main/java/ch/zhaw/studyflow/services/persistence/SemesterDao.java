package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

import java.util.List;
import java.util.Optional;

public interface SemesterDao {
    void createSemester(Semester semester, long degreeId, long userId);
    List<Semester> getSemestersForStudent(long userId);
    Optional<Semester> getSemesterById(long semesterId);
    void updateSemester(Semester semester);
    void deleteSemester(long semesterId);
    List<Semester> readByDegreeId(long degreeId);

}
