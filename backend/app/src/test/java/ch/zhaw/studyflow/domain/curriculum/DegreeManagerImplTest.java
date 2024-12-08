package ch.zhaw.studyflow.domain.curriculum;

import ch.zhaw.studyflow.domain.curriculum.impls.DegreeManagerImpl;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DegreeManagerImplTest {
    private DegreeDao degreeDao;
    private SemesterDao semesterDao;
    private DegreeManagerImpl degreeManager;

    @BeforeEach
    void beforeEach() {
        degreeDao = mock(DegreeDao.class);
        semesterDao = mock(SemesterDao.class);
        degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
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
        verify(degreeDao, times(1)).update(updatedDegree);

        assertEquals(1, existingDegree.getId());
        assertEquals("New Name", existingDegree.getName());
        assertEquals("New Description", existingDegree.getDescription());
        assertEquals(6, existingDegree.getActiveSemesterId());
        assertEquals(5, existingDegree.getOwnerId());
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
