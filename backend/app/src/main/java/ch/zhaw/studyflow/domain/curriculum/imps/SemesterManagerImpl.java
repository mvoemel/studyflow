package ch.zhaw.studyflow.domain.curriculum.imps;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;

import java.util.List;

public class SemesterManagerImpl implements SemesterManager {
    @Override
    public void createSemester(Semester semester) {

    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        return List.of();
    }

    @Override
    public Semester getSemesterById(long semesterId) {
        return null;
    }

    @Override
    public void updateSemester(Semester semester) {

    }

    @Override
    public void deleteSemester(long semesterId) {

    }
}
