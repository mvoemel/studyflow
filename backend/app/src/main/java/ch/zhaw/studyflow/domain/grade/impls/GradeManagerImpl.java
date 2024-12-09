package ch.zhaw.studyflow.domain.grade.impls;

import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.domain.grade.GradeManager;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.List;

/**
 * Implementation of GradeManager.
 */
public class GradeManagerImpl implements GradeManager {
    private final GradeDao gradeDao;

    public GradeManagerImpl(GradeDao gradeDao) {
        this.gradeDao = gradeDao;
    }

    @Override
    public void create(Grade grade) {
        gradeDao.create(grade);
    }

    @Override
    public Grade read(long gradeId) {
        return gradeDao.read(gradeId);
    }

    @Override
    public void delete(long gradeId) {
        gradeDao.delete(gradeId);
    }

    @Override
    public Grade update(Grade grade) {
        gradeDao.update(grade);
        return grade;
    }

    @Override
    public List<Grade> getGradesByModule(long moduleId) {
        return gradeDao.readByModule(moduleId);
    }

    @Override
    public void updateGradesByModule(long moduleId, List<Grade> grades) {
        gradeDao.updateByModule(moduleId, grades);
    }
}