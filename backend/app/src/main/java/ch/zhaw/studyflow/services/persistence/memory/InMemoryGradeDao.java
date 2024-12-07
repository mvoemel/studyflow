package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * In-memory implementation of GradeDao.
 */
public class InMemoryGradeDao implements GradeDao {
    private final Map<Long, Grade> persistedGrades = new HashMap<>();
    private final AtomicInteger idCounter;

    public InMemoryGradeDao() {
        this.idCounter  = new AtomicInteger();
    }

    @Override
    public void create(Grade grade) {
        grade.setId(idCounter.getAndIncrement());
        persistedGrades.put(grade.getId(), grade);
    }

    @Override
    public Grade read(long gradeId) {
        return persistedGrades.get(gradeId);
    }

    @Override
    public List<Grade> readByModule(long moduleId) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : persistedGrades.values()) {
            if (grade.getBelongsTo() == moduleId) {
                result.add(grade);
            }
        }
        return result;
    }

    @Override
    public List<Grade> readByDegree(long degreeId) {
        return persistedGrades.values().stream()
                .filter(e -> e.getBelongsTo() == degreeId)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Grade grade) {
        persistedGrades.put(grade.getId(), grade);
    }

    @Override
    public void updateByDegree(long degreeId, List<Grade> grades) {
        List<Grade> oldGrades = readByDegree(degreeId);

        for (Grade grade : grades) {
            if (persistedGrades.containsKey(grade.getId())) {
                Grade existingGrade = persistedGrades.get(grade.getId());
                existingGrade.setName(grade.getName());
                existingGrade.setPercentage(grade.getPercentage());
                existingGrade.setValue(grade.getValue());
                oldGrades.removeIf(e -> e.getId() == grade.getId());
            } else {
                grade.setId(generateNewId());
                persistedGrades.put(grade.getId(), grade);
            }
        }

        for (Grade grade : oldGrades) {
            persistedGrades.remove(grade.getId());
        }
    }

    private long generateNewId() {
        return idCounter.getAndIncrement();
    }

    @Override
    public void delete(long gradeId) {
        persistedGrades.remove(gradeId);
    }
}