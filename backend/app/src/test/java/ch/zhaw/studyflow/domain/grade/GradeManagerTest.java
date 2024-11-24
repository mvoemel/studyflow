package ch.zhaw.studyflow.domain.grade;

import ch.zhaw.studyflow.domain.grade.impls.GradeManagerImpl;
import ch.zhaw.studyflow.services.persistence.GradeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GradeManagerTest {

    private GradeManagerImpl gradeManager;
    private GradeDao gradeDao;

    @BeforeEach
    void setUp() {
        gradeDao = mock(GradeDao.class);
        gradeManager = new GradeManagerImpl(gradeDao);
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
        List<Grade> result = gradeManager.readByModule(1L);
        assertEquals(grades, result);
    }

    @Test
    void testReadByStudent() {
        List<Grade> grades = List.of(new Grade());
        when(gradeDao.readByStudent(1L)).thenReturn(grades);
        List<Grade> result = gradeManager.readByStudent(1L);
        assertEquals(grades, result);
    }
}