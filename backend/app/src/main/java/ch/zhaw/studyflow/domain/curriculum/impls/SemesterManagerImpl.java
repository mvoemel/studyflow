package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.List;
import java.util.Optional;

public class SemesterManagerImpl implements SemesterManager {

    private final SemesterDao semesterDao;
    private final ModuleDao moduleDao;

    public SemesterManagerImpl(SemesterDao semesterDao, ModuleDao moduleDao) {
        this.semesterDao = semesterDao;
        this.moduleDao = moduleDao;
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
        return semesterDao.getSemestersForDegree(degreeId);
    }

    @Override
    public Optional<Semester> getSemesterById(long semesterId) {
        return semesterDao.getSemesterById(semesterId);
    }

    @Override
    public void updateSemester(Semester semester) {
        semesterDao.getSemesterById(semester.getId())
                .ifPresent(semesterToUpdate -> {
                    semesterToUpdate.setName(semester.getName());
                    semesterToUpdate.setDescription(semester.getDescription());
                    this.semesterDao.updateSemester(semesterToUpdate);
                }
        );
    }

    @Override
    public void deleteSemester(long semesterId) {
        semesterDao.deleteSemester(semesterId);
        moduleDao.readBySemesterId(semesterId).forEach(module -> moduleDao.delete(module.getId()));
    }

}
