package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.List;
import java.util.Optional;

public class SemesterManagerImpl implements SemesterManager {

    private final SemesterDao semesterDao;

    public SemesterManagerImpl(SemesterDao semesterDao) {
        this.semesterDao = semesterDao;
    }

    @Override
    public void createSemester(Semester semester, long degreeId, long userId) {
        semesterDao.createSemester(semester, degreeId, userId);
    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        return semesterDao.getSemestersForStudent(userId);
    }

    @Override
    public List<Semester> getSemestersForDegree(long degreeId) {
        return semesterDao.getSemestersForStudent(degreeId);
    }

    @Override
    public Optional<Semester> getSemesterById(long semesterId) {
        return semesterDao.getSemesterById(semesterId);
    }

    @Override
    public void updateSemester(Semester semester) {
        semesterDao.updateSemester(semester);
    }

    @Override
    public void deleteSemester(long semesterId) {
        semesterDao.deleteSemester(semesterId);
    }

}
