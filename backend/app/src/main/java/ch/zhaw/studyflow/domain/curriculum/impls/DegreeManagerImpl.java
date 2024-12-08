package ch.zhaw.studyflow.domain.curriculum.impls;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import ch.zhaw.studyflow.utils.Validation;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Objects;

public class DegreeManagerImpl implements DegreeManager {
    private final StudentManager studentManager;
    private final DegreeDao degreeDao;
    private final SemesterDao semesterDao;


    public DegreeManagerImpl(StudentManager studentManager, DegreeDao degreeDao, SemesterDao semesterDao) {
        this.studentManager = studentManager;
        this.degreeDao      = degreeDao;
        this.semesterDao    = semesterDao;
    }

    @Override
    public void createDegree(long studentId, Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() != -1) {
            throw new IllegalArgumentException("Degree should not have an ID set before creation");
        }

        if (Validation.isNullOrEmpty(degree.getName())) {
            throw new IllegalArgumentException("Degree name must be set");
        }

        degree.setOwnerId(studentId);
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

        if (degree.getId() < 0) {
            throw new IllegalArgumentException("Degree should have an ID set before updating");
        }

        Degree degreeFromDatabase = degreeDao.read(degree.getId());
        if (degreeFromDatabase != null) {
            degreeFromDatabase.setName(degree.getName());
            degreeFromDatabase.setDescription(degree.getDescription());
            degreeFromDatabase.setActiveSemesterId(degree.getActiveSemesterId());
            degreeFromDatabase.setOwnerId(degree.getOwnerId());
            degreeDao.update(degreeFromDatabase);
        }
    }

    @Override
    public void deleteDegree(long degreeId) {
        if (degreeId < 0) {
            throw new IllegalArgumentException("Degree ID must be positive");
        }

        Degree degree = degreeDao.read(degreeId);
        if (degree != null) {
            semesterDao.getSemestersForDegree(degreeId).forEach(semester -> semesterDao.deleteSemester(semester.getId()));
            degreeDao.delete(degreeId);

            List<Degree> degreesForStudent = getDegreesForStudent(degree.getOwnerId());
            final long result = degreesForStudent.stream()
                    .mapToLong(Degree::getId)
                    .max()
                    .orElse(-1L);

            studentManager.getSettings(degree.getOwnerId())
                    .ifPresent(settings -> {
                        if (settings.getActiveDegree() == degreeId) {
                            settings.setActiveDegree(result);
                            studentManager.updateSettings(settings);
                        }
                    });
        }
    }
}
