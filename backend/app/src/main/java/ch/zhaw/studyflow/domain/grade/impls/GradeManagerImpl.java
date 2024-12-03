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

    public GradeManagerImpl(ModuleManager moduleManager, GradeDao gradeDao) {
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
    public List<Grade> readByModule(long moduleId) {
        return gradeDao.readByModule(moduleId);
    }

    @Override
    public void updateByModule(long degreeId, List<Grade> grades) {
        gradeDao.updateByDegree(degreeId, grades);
    }
}