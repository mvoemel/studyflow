package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

import java.util.List;

public interface SemesterDao {
    void createSemester(Semester semester);
    List<Semester> getSemestersForStudent(long userId);
    Semester getSemesterById(long semesterId);
    void updateSemester(Semester semester);
    void deleteSemester(long semesterId);
}
