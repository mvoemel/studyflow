package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.grade.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryGradeDaoTest {

    private InMemoryGradeDao gradeDao;

    @BeforeEach
    void setUp() {
        gradeDao = new InMemoryGradeDao();
    }

    private Grade createGrade(long id, String name, int percentage, double value, long belongsTo) {
        Grade grade = new Grade();
        grade.setId(id);
        grade.setName(name);
        grade.setPercentage(percentage);
        grade.setValue(value);
        grade.setBelongsTo(belongsTo);
        return grade;
    }

    @Test
    void testUpdateExistingGrade() {
        gradeDao.create(createGrade(1, "Math", 90, 4.0, 1));

        Grade newGrade1 = createGrade(1, "Mathematics", 95, 4.5, 1);
        List<Grade> newGrades = new ArrayList<>();
        newGrades.add(newGrade1);

        gradeDao.updateByDegree(1, newGrades);

        Grade updatedGrade1 = gradeDao.read(1);
        assertEquals("Mathematics", updatedGrade1.getName());
        assertEquals(95, updatedGrade1.getPercentage());
        assertEquals(4.5, updatedGrade1.getValue());
    }

    @Test
    void testAddNewGrade() {
        gradeDao.create(createGrade(1, "Math", 90, 4.0, 1));

        Grade newGrade2 = createGrade(0, "History", 80, 3.0, 1);
        List<Grade> newGrades = new ArrayList<>();
        newGrades.add(newGrade2);

        gradeDao.updateByDegree(1, newGrades);

        Grade updatedGrade2 = gradeDao.read(newGrade2.getId());
        assertNotNull(updatedGrade2);
        assertEquals("History", updatedGrade2.getName());
        assertEquals(80, updatedGrade2.getPercentage());
        assertEquals(3.0, updatedGrade2.getValue());
    }

    @Test
    void testRemoveOldGrade() {
        gradeDao.create(createGrade(1, "Math", 90, 4.0, 1));
        gradeDao.create(createGrade(2, "Science", 85, 3.5, 1));

        Grade newGrade1 = createGrade(1, "Mathematics", 95, 4.5, 1);
        List<Grade> newGrades = new ArrayList<>();
        newGrades.add(newGrade1);

        gradeDao.updateByDegree(1, newGrades);

        Grade deletedGrade2 = gradeDao.read(2);
        assertNull(deletedGrade2);
    }
}