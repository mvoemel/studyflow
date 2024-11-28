package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.utils.Validation;
import ch.zhaw.studyflow.webserver.security.principal.CommonClaims;
import ch.zhaw.studyflow.webserver.security.principal.Principal;

import java.util.List;
import java.util.Objects;

public class DegreeManagerImpl implements DegreeManager {
    private final DegreeDao degreeDao;


    public DegreeManagerImpl(DegreeDao degreeDao) {
        this.degreeDao  = degreeDao;
    }


    @Override
    public void createDegreeFor(long userId, Degree degree) {

    }

    @Override
    public List<Degree> getDegreesForUser(long usersId) {
        return List.of();
    }

    @Override
    public void createDegree(Principal actor, Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() != -1) {
            throw new IllegalArgumentException("Degree should not have an ID set before creation");
        }

        if (degree.getOwnerId() == -1) {
            degree.setOwnerId(actor.getClaim(CommonClaims.USER_ID).orElseThrow());
        }

        if (Validation.isNullOrEmpty(degree.getName())) {
            throw new IllegalArgumentException("Degree name must be set");
        }
        degreeDao.create(degree);
    }

    @Override
    public List<Degree> getDegreesForStudent(long studentId) {
        if (studentId < 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        return degreeDao.readAllByStudent(studentId);
    }

    @Override
    public Degree getDegree(long degreeId) {
        if (degreeId < 0) {
            throw new IllegalArgumentException("Degree ID must be positive");
        }
        return degreeDao.read(degreeId);
    }

    @Override
    public void updateDegree(Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() == 0) {
            throw new IllegalArgumentException("Degree should have an ID set before updating");
        }

        Degree oldDegree = degreeDao.read(degree.getId());
        if (oldDegree.getOwnerId() != degree.getOwnerId()) {
            throw new IllegalArgumentException("Degree owner cannot be changed");
        }
        degreeDao.update(degree);
    }

    @Override
    public void deleteDegree(long degreeId) {
        if (degreeId < 0) {
            throw new IllegalArgumentException("Degree ID must be positive");
        }
        degreeDao.delete(degreeId);
    }
}
