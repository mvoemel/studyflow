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
        Grade mathGrade = createGrade(-1, "Math", 85, 3.5, 1);
        gradeDao.create(mathGrade);
        Grade scienceGrade = createGrade(-1, "Science", 85, 3.5, 1);
        gradeDao.create(scienceGrade);

        Grade updatedMathGrade = createGrade(mathGrade.getId(), "Mathematics", 95, 4.5, 1);
        List<Grade> newGrades = new ArrayList<>();
        newGrades.add(updatedMathGrade);
        newGrades.add(scienceGrade);

        gradeDao.updateByDegree(1, newGrades);

        Grade updatedGrade1 = gradeDao.read(mathGrade.getId());
        assertEquals("Mathematics", updatedGrade1.getName());
        assertEquals(95, updatedGrade1.getPercentage());
        assertEquals(4.5, updatedGrade1.getValue());

        Grade existingGrade2 = gradeDao.read(scienceGrade.getId());
        assertNotNull(existingGrade2);
        assertEquals("Science", existingGrade2.getName());
        assertEquals(85, existingGrade2.getPercentage());
        assertEquals(3.5, existingGrade2.getValue());
    }

    @Test
    void testAddNewGrade() {
        Grade exsitingGrade = createGrade(-1, "Math", 90, 4.0, 1);
        Grade additionalGrade = createGrade(-1, "History", 80, 3.0, 1);
        gradeDao.create(exsitingGrade);

        List<Grade> updateGrades = new ArrayList<>();
        updateGrades.add(additionalGrade);
        updateGrades.add(exsitingGrade);

        gradeDao.updateByDegree(1, updateGrades);

        List<Grade> gradesFromDatabase = gradeDao.readByModule(1);

        assertEquals(2, gradesFromDatabase.size());
        Grade updatedExistingGrade = gradesFromDatabase.stream().filter(e -> e.getId()==exsitingGrade.getId()).findFirst().orElse(null);
        assertNotNull(updatedExistingGrade);
        assertEquals("Math", updatedExistingGrade.getName());
        assertEquals(90, updatedExistingGrade.getPercentage());
        assertEquals(4.0, updatedExistingGrade.getValue());

        Grade addedGrade = gradesFromDatabase.stream().filter(e -> e.getId()==additionalGrade.getId()).findFirst().orElse(null);
        assertNotNull(addedGrade);
        assertEquals("History", addedGrade.getName());
        assertEquals(80, addedGrade.getPercentage());
        assertEquals(3.0, addedGrade.getValue());
    }

    @Test
    void updateAndRemoveOldGrades() {
        Grade existingMathGrade = createGrade(-1, "Math", 90, 4.0, 1);
        Grade existingScienceGrade = createGrade(-1, "Science", 85, 3.5, 1);
        gradeDao.create(existingMathGrade);
        gradeDao.create(existingScienceGrade);

        Grade updatedMathGrade = createGrade(existingMathGrade.getId(), "Mathematics", 95, 4.5, 1);
        List<Grade> newGrades = new ArrayList<>();
        newGrades.add(updatedMathGrade);

        gradeDao.updateByDegree(1, newGrades);

        Grade deletedScienceGrade = gradeDao.read(existingScienceGrade.getId());
        assertNull(deletedScienceGrade);

        List<Grade> remainingGrades = gradeDao.readByModule(1);
        assertEquals(1, remainingGrades.size());
        assertEquals("Mathematics", remainingGrades.get(0).getName());
    }
}