package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
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
    public void update(Grade grade) {
        persistedGrades.put(grade.getId(), grade);
    }

    @Override
    public void updateByModule(long degreeId, List<Grade> grades) {
        Map<Long, Grade> oldGrades = readByModule(degreeId).stream().collect(Collectors.toMap(Grade::getId, Function.identity()));

        for (Grade grade : grades) {
            if (oldGrades.containsKey(grade.getId())) {
                Grade existingGrade = persistedGrades.get(grade.getId());
                existingGrade.setName(grade.getName());
                existingGrade.setPercentage(grade.getPercentage());
                existingGrade.setValue(grade.getValue());
                oldGrades.remove(grade.getId());
            } else {
                grade.setId(idCounter.getAndIncrement());
                grade.setBelongsTo(degreeId);
                persistedGrades.put(grade.getId(), grade);
            }
        }

        for (Grade grade : oldGrades.values()) {
            persistedGrades.remove(grade.getId());
        }
    }

    @Override
    public void delete(long gradeId) {
        persistedGrades.remove(gradeId);
    }
}