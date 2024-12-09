package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.List;

public class SemesterManagerImpl implements SemesterManager {
    private final DegreeManager degreeManager;
    private final SemesterDao semesterDao;
    private final ModuleDao moduleDao;

    public SemesterManagerImpl(DegreeManager degreeManager, SemesterDao semesterDao, ModuleDao moduleDao) {
        this.degreeManager  = degreeManager;
        this.semesterDao    = semesterDao;
        this.moduleDao      = moduleDao;
    }

    @Override
    public void createSemester(Semester semester, long degreeId, long userId) {
        semesterDao.createSemester(semester, degreeId, userId);
    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        return semesterDao.readAllByStudent(userId);
    }

    @Override
    public List<Semester> getSemestersForDegree(long degreeId) {
        return semesterDao.readAllByDegree(degreeId);
    }

    @Override
    public Semester getSemesterById(long semesterId) {
        return semesterDao.read(semesterId);
    }

    @Override
    public void updateSemester(Semester semester) {
        Semester semesterToUpdate = semesterDao.read(semester.getId());
        if (semesterToUpdate != null) {
            semesterToUpdate.setName(semester.getName());
            semesterToUpdate.setDescription(semester.getDescription());
            semesterDao.update(semesterToUpdate);
        }
    }

    @Override
    public void deleteSemester(long semesterId) {
        if (semesterId < 0) {
            throw new IllegalArgumentException("Semester ID must be positive");
        }

        final Semester semester = getSemesterById(semesterId);
        if (semester != null) {
            semesterDao.delete(semesterId);
            moduleDao.readAllBySemester(semesterId).forEach(module -> moduleDao.delete(module.getId()));

            Degree degree = degreeManager.getDegree(semester.getDegreeId());
            if (degree != null) {
                final long newActiveSemester = getSemestersForDegree(degree.getId())
                        .stream()
                        .mapToLong(Semester::getId)
                        .max()
                        .orElse(-1);
                degree.setActiveSemesterId(newActiveSemester);
                degreeManager.updateDegree(degree);
            }
        }
    }

}
