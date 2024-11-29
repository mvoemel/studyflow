package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of GradeDao.
 */
public class InMemoryGradeDao implements GradeDao {
    private final Map<Long, Grade> grades = new HashMap<>();
    private final Map<Long, List<Grade>> degrees = new HashMap<>();
    private long currentId = 1;

    @Override
    public void create(Grade grade) {
        grade.setId(currentId++);
        grades.put(grade.getId(), grade);
    }

    @Override
    public Grade read(long gradeId) {
        return grades.get(gradeId);
    }

    @Override
    public List<Grade> readByModule(long moduleId) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades.values()) {
            if (grade.getBelongsTo() == moduleId) {
                result.add(grade);
            }
        }
        return result;
    }

    @Override
    public List<Grade> readByStudent(long studentId) {
        List<Grade> result = new ArrayList<>();
        for (Grade grade : grades.values()) {
            if (grade.getBelongsTo() == studentId) {
                result.add(grade);
            }
        }
        return result;
    }

    @Override
    public List<Grade> readByDegree(long degreeId) {
        return degrees.get(degreeId);
    }

    @Override
    public void update(Grade grade) {
        grades.put(grade.getId(), grade);
    }

    @Override
    public void updateByDegree(long degreeId, List<Grade> grades) {
        degrees.put(degreeId, grades);
    }

    @Override
    public void delete(long gradeId) {
        grades.remove(gradeId);
    }
}