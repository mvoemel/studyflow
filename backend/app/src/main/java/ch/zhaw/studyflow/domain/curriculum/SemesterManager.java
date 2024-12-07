package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;
import java.util.Optional;

public interface SemesterManager {

    void createSemester(Semester semester, long degreeId, long userId);
    List<Semester> getSemestersForStudent(long userId);
    List<Semester> getSemestersForDegree(long degreeId);
    Optional<Semester> getSemesterById(long semesterId);
    void updateSemester(Semester semester);
    void deleteSemester(long semesterId);

}
