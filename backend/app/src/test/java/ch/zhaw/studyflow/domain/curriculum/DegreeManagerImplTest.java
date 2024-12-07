package ch.zhaw.studyflow.domain.curriculum;

import ch.zhaw.studyflow.domain.curriculum.impls.DegreeManagerImpl;
import ch.zhaw.studyflow.services.persistence.DegreeDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DegreeManagerImplTest {
    @Test
    void testCreateDegree() {
        DegreeDao degreeDao = mock(DegreeDao.class);
        SemesterDao semesterDao = mock(SemesterDao.class);
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        Degree degree = new Degree();
        degree.setName("Test Degree");
        degree.setDescription("Test Description");
        degreeManager.createDegree(1L, degree);
        verify(degreeDao, times(1)).create(degree);
    }

    @Test
    void throwIfDegreeIdAlreadySet() {
        DegreeDao degreeDao = mock(DegreeDao.class);
        SemesterDao semesterDao = mock(SemesterDao.class);
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        Degree degree = new Degree();
        degree.setId(1);
        degree.setOwnerId(1);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.createDegree(1L, degree));
    }

    @Test
    void throwsIfGetDegreeIdIsNegative() {
        DegreeDao degreeDao = mock(DegreeDao.class);
        SemesterDao semesterDao = mock(SemesterDao.class);
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegree(-1));
    }

    @Test
    void throwsIfDeleteDegreeIdIsNegative() {
        DegreeDao degreeDao = mock(DegreeDao.class);
        SemesterDao semesterDao = mock(SemesterDao.class);
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.deleteDegree(-1));
    }

    @Test
    void throwsIfGetDegreesForStudentIdIsNegative() {
        DegreeDao degreeDao = mock(DegreeDao.class);
        SemesterDao semesterDao = mock(SemesterDao.class);
        DegreeManager degreeManager = new DegreeManagerImpl(degreeDao, semesterDao);
        assertThrows(IllegalArgumentException.class, () -> degreeManager.getDegreesForStudent(-1));
    }
}
