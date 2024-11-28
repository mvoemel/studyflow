package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

public interface SemesterManager {

    void createSemester(Semester semester);
    List<Semester> getSemestersForStudent(long userId);
    Semester getSemesterById(long semesterId);
    void updateSemester(Semester semester);
    void deleteSemester(long semesterId);

}
