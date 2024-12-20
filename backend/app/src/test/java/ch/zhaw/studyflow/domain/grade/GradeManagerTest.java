package ch.zhaw.studyflow.domain.grade;

import ch.zhaw.studyflow.domain.grade.impls.GradeManagerImpl;
import ch.zhaw.studyflow.services.persistence.GradeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GradeManagerTest {

    private GradeDao gradeDao;
    private GradeManagerImpl gradeManager;


    @BeforeEach
    void setUp() {
        gradeDao        = mock(GradeDao.class);
        gradeManager    = new GradeManagerImpl(gradeDao);
    }


    @Test
    void testCreate() {
        Grade grade = new Grade();
        gradeManager.create(grade);
        verify(gradeDao, times(1)).create(grade);
    }

    @Test
    void testRead() {
        Grade grade = new Grade();
        when(gradeDao.read(1L)).thenReturn(grade);
        Grade result = gradeManager.read(1L);
        assertEquals(grade, result);
    }

    @Test
    void testDelete() {
        gradeManager.delete(1L);
        verify(gradeDao, times(1)).delete(1L);
    }

    @Test
    void testUpdate() {
        Grade grade = new Grade();
        gradeManager.update(grade);
        verify(gradeDao, times(1)).update(grade);
    }

    @Test
    void testReadByModule() {
        List<Grade> grades = List.of(new Grade());
        when(gradeDao.readByModule(1L)).thenReturn(grades);
        List<Grade> result = gradeManager.getGradesByModule(1L);
        assertEquals(grades, result);
    }

    @Test
    void testUpdateByDegree() {
        long degreeId = 1L;
        List<Grade> grades = List.of(new Grade(1L, "Test", 0.5, 50.0, 1L), new Grade(2L, "Test2", 0.5, 50.0, 1L));

        gradeManager.updateGradesByModule(degreeId, grades);

        verify(gradeDao, times(1)).updateByModule(degreeId, grades);
    }
}