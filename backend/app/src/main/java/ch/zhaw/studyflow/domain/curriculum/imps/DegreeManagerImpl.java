package ch.zhaw.studyflow.domain.curriculum.imps;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;

import java.util.List;

public class DegreeManagerImpl implements DegreeManager {

    @Override
    public void createDegreeFor(long userId, Degree degree) {

    }

    @Override
    public List<Degree> getDegreesForUser(long usersId) {
        return List.of();
    }

    @Override
    public void createDegree(Degree degree) {

    }

    @Override
    public List<Degree> getDegreesForStudent(long userId) {
        return List.of();
    }

    @Override
    public Degree getDegree(long degreeId) {
        return null;
    }

    @Override
    public void updateDegree(Degree degree) {

    }

    @Override
    public void deleteDegree(long degreeId) {

    }
}
