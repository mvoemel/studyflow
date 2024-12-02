package ch.zhaw.studyflow.domain.curriculum.impls;

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
}
