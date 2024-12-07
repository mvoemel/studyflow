package ch.zhaw.studyflow.domain.curriculum;

import ch.zhaw.studyflow.domain.curriculum.impls.DegreeManagerImpl;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DegreeManagerImplTest {
    private DegreeDao degreeDao;
    private SemesterDao semesterDao;

    @BeforeEach
    void beforeEach() {
        degreeDao = mock(DegreeDao.class);
        semesterDao = mock(SemesterDao.class);
    }

    @Test
    void testCreateDegree() {
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        Degree degree = new Degree();
        degree.setName("Test Degree");
        degree.setDescription("Test Description");
        degreeManager.createDegree(1L, degree);
        verify(degreeDao, times(1)).create(degree);
        verify(degree, times(1)).setOwnerId(1);
    }

    @Test
    void throwIfDegreeIdAlreadySet() {
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        Degree degree = new Degree();
        degree.setId(1);
        degree.setOwnerId(1);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.createDegree(1L, degree));
    }

    @Test
    void throwsIfGetDegreeIdIsNegative() {
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegree(-1));
    }

    @Test
    void throwsIfDeleteDegreeIdIsNegative() {
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.deleteDegree(-1));
    }

    @Test
    void throwsIfGetDegreesForStudentIdIsNegative() {
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegreesForStudent(-1));
    }
}
