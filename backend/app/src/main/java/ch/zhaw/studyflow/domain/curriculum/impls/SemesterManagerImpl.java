package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.List;
import java.util.Optional;

public class SemesterManagerImpl implements SemesterManager {

    private final SemesterDao SemesterDao;

    public SemesterManagerImpl(SemesterDao semesterDao) {
        this.SemesterDao = semesterDao;
    }

    @Override
    public void createSemester(Semester semester, long degreeId, long userId) {
        SemesterDao.createSemester(semester, degreeId, userId);
    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        return SemesterDao.getSemestersForStudent(userId);
    }

    @Override
    public Optional<Semester> getSemesterById(long semesterId) {
        return SemesterDao.getSemesterById(semesterId);
    }

    @Override
    public void updateSemester(Semester semester) {
        SemesterDao.updateSemester(semester);
    }

    @Override
    public void deleteSemester(long semesterId) {
        SemesterDao.deleteSemester(semesterId);
    }

}
