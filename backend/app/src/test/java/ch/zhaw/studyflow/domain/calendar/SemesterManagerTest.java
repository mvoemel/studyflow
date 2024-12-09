package ch.zhaw.studyflow.domain.calendar;

import ch.zhaw.studyflow.domain.curriculum.*;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.impls.SemesterManagerImpl;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SemesterManagerTest {
    private SemesterManager semesterManager;
    private DegreeManager degreeManager;
    private SemesterDao semesterDao;
    private ModuleDao moduleDao;

    @BeforeEach
    void beforeEach() {
        degreeManager   = mock(DegreeManager.class);
        moduleDao       = mock(ModuleDao.class);
        semesterDao     = mock(SemesterDao.class);
        semesterManager = new SemesterManagerImpl(degreeManager, semesterDao, moduleDao);
    }

    @Test
    void testCreate() {
        final Semester semester = new Semester();
        semester.setName("Test Semester");
        semester.setDescription("Test Description");
        semester.setDegreeId(1L);
        semester.setUserId(1L);

        semesterManager.createSemester(semester, semester.getDegreeId(), semester.getUserId());

        verify(semesterDao).createSemester(semester, semester.getDegreeId(), semester.getUserId());
    }

    @Test
    void testUpdateSemester() {
        final Semester persistedSemester = new Semester();
        persistedSemester.setId(1L);
        persistedSemester.setName("Test Semester");
        persistedSemester.setDescription("Test Description");
        persistedSemester.setDegreeId(1L);
        persistedSemester.setUserId(1L);
        when(semesterDao.read(1L)).thenReturn(persistedSemester);

        final Semester changesToPersistedSemester = new Semester();
        changesToPersistedSemester.setId(1L);
        changesToPersistedSemester.setName("Test Semester Updated");
        changesToPersistedSemester.setDescription("Test Description Updated");
        changesToPersistedSemester.setDegreeId(2L);
        changesToPersistedSemester.setUserId(3L);

        semesterManager.updateSemester(changesToPersistedSemester);
        verify(semesterDao).read(1L);
        verify(semesterDao).update(persistedSemester);

        assertEquals("Test Semester Updated", persistedSemester.getName());
        assertEquals("Test Description Updated", persistedSemester.getDescription());
    }

    @Test
    void testUpdateUnknownSemester() {
        final Semester changesToPersistedSemester = new Semester();
        changesToPersistedSemester.setId(1L);
        changesToPersistedSemester.setName("Test Semester Updated");

        semesterManager.updateSemester(changesToPersistedSemester);
        verify(semesterDao).read(1L);
        verify(semesterDao, never()).update(any());
    }

    @Test
    void testDeleteSemester() {
        final long semesterId = 1L;
        final Semester semester = new Semester();
        semester.setId(semesterId);
        semester.setDegreeId(1L);

        List<Module> modules = List.of(
                new Module(1, "Module 1", 1),
                new Module(2, "Module 2", 1)
        );

        final Degree degree = new Degree();
        degree.setId(1L);
        degree.setActiveSemesterId(1L);

        when(moduleDao.readAllBySemester(semesterId)).thenReturn(modules);
        when(semesterDao.read(semesterId)).thenReturn(semester);
        when(degreeManager.getDegree(1L)).thenReturn(degree);
        when(moduleDao.readAllBySemester(1L)).thenReturn(modules);

        semesterManager.deleteSemester(semesterId);

        verify(semesterDao).delete(semesterId);
        verify(moduleDao).readAllBySemester(semesterId);
        verify(moduleDao, times(2)).delete(anyLong());
        verify(degreeManager).updateDegree(degree);

        assertEquals(-1, degree.getActiveSemesterId());
    }

    @Test
    void testDeleteSemesterAndSetNewActiveSemester() {
        final Semester oldSemester = new Semester();
        oldSemester.setId(1L);
        oldSemester.setDegreeId(1L);

        final Semester newActiveSemester = new Semester();
        newActiveSemester.setId(0L);
        newActiveSemester.setDegreeId(1L);

        final List<Module> modules = List.of(
                new Module(1, "Module 1", 1),
                new Module(2, "Module 2", 1)
        );

        final List<Semester> semesters = List.of(
                newActiveSemester
        );

        final Degree degree = new Degree();
        degree.setId(1L);
        degree.setActiveSemesterId(1L);

        when(moduleDao.readAllBySemester(oldSemester.getId())).thenReturn(modules);
        when(semesterDao.read(oldSemester.getId())).thenReturn(oldSemester);
        when(semesterDao.readAllByDegree(1L)).thenReturn(semesters);
        when(degreeManager.getDegree(1L)).thenReturn(degree);
        when(moduleDao.readAllBySemester(1L)).thenReturn(modules);

        semesterManager.deleteSemester(oldSemester.getId());

        verify(semesterDao).delete(oldSemester.getId());
        verify(moduleDao).readAllBySemester(oldSemester.getId());
        verify(moduleDao, times(2)).delete(anyLong());
        verify(degreeManager).updateDegree(degree);

        assertEquals(0L, degree.getActiveSemesterId());
    }

    @Test
    void testDeleteSemesterWithNegativeId() {
        final long semesterId = -1L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            semesterManager.deleteSemester(semesterId);
        });

        assertEquals("Semester ID must be positive", exception.getMessage());
        verify(semesterDao, never()).delete(anyLong());
        verify(moduleDao, never()).readAllBySemester(anyLong());
        verify(degreeManager, never()).updateDegree(any(Degree.class));
    }

    @Test
    void testGetSemesterForStudent() {
        semesterManager.getSemestersForStudent(1L);
        verify(semesterDao).readAllByStudent(1L);
    }

    @Test
    void testGetSemesterForDegree() {
        semesterManager.getSemestersForDegree(1L);
        verify(semesterDao).readAllByDegree(1L);
    }

}
