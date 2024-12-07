package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * In-memory implementation of GradeDao.
 */
public class InMemoryGradeDao implements GradeDao {
    private final Map<Long, Grade> persistedGrades = new HashMap<>();
    private long currentId = 1;

    @Override
    public void create(Grade grade) {
        grade.setId(currentId++);
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
    public void updateByModule(long moduleId, List<Grade> grades) {

        List<Grade> oldGrades = readByModule(moduleId);
        for (Grade grade : grades) {
            persistedGrades.put(grade.getId(), grade);
            if (persistedGrades.containsKey(grade.getId())) {
                oldGrades.removeIf(e -> e.getId() == grade.getId());
            }
        }
        for (Grade grade : oldGrades) {
            persistedGrades.remove(grade.getId());
        }
    }

    @Override
    public void delete(long gradeId) {
        persistedGrades.remove(gradeId);
    }
}