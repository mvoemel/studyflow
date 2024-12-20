package ch.zhaw.studyflow.domain.curriculum;

import ch.zhaw.studyflow.domain.curriculum.impls.DegreeManagerImpl;
import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DegreeManagerImplTest {
    private StudentManager studentManager;
    private DegreeDao degreeDao;
    private SemesterDao semesterDao;
    private DegreeManager degreeManager;

    @BeforeEach
    void beforeEach() {
        studentManager  = mock(StudentManager.class);
        degreeDao       = mock(DegreeDao.class);
        semesterDao     = mock(SemesterDao.class);
        degreeManager   = new DegreeManagerImpl(studentManager, degreeDao, semesterDao);
    }

    @Test
    void testCreateDegree() {
        Degree degree = new Degree();
        degree.setName("Test Degree");
        degree.setDescription("Test Description");
        degreeManager.createDegree(1L, degree);
        verify(degreeDao, times(1)).create(degree);
        assertEquals(1, degree.getOwnerId());
    }

    @Test
    void testUpdateDegree() {
        Degree existingDegree = new Degree();
        existingDegree.setId(1);
        existingDegree.setName("Test Degree");
        existingDegree.setDescription("Test Description");
        existingDegree.setActiveSemesterId(5);
        existingDegree.setOwnerId(5);
        when(degreeDao.read(1)).thenReturn(existingDegree);

        Degree updatedDegree = new Degree();
        updatedDegree.setId(existingDegree.getId());
        updatedDegree.setName("New Name");
        updatedDegree.setDescription("New Description");
        updatedDegree.setActiveSemesterId(6);
        updatedDegree.setOwnerId(1);
        degreeManager.updateDegree(updatedDegree);
        verify(degreeDao, times(1)).update(existingDegree);

        assertEquals(1, existingDegree.getId());
        assertEquals("New Name", existingDegree.getName());
        assertEquals("New Description", existingDegree.getDescription());
        assertEquals(6, existingDegree.getActiveSemesterId());
    }

    @Test
    void testDeleteDegree() {
        Settings settings = new Settings();
        settings.setId(1);
        settings.setActiveDegree(1);
        when(studentManager.getSettings(1L)).thenReturn(Optional.of(settings));

        Degree degree = new Degree();
        degree.setId(1);
        degree.setOwnerId(1);
        when(degreeDao.read(1)).thenReturn(degree);
        degreeManager.deleteDegree(1);
        verify(degreeDao, times(1)).delete(1);

        assertEquals(-1, settings.getActiveDegree());
    }

    @Test
    void throwIfDegreeIdAlreadySet() {
        Degree degree = new Degree();
        degree.setId(1);
        degree.setOwnerId(1);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.createDegree(1L, degree));
    }

    @Test
    void throwsIfGetDegreeIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegree(-1));
    }

    @Test
    void throwsIfDeleteDegreeIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> degreeManager.deleteDegree(-1));
    }

    @Test
    void throwsIfGetDegreesForStudentIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegreesForStudent(-1));
    }
}
