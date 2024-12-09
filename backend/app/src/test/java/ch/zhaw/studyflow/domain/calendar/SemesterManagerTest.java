package ch.zhaw.studyflow.domain.calendar;

import ch.zhaw.studyflow.domain.curriculum.DegreeManager;
import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.domain.curriculum.SemesterManager;
import ch.zhaw.studyflow.domain.curriculum.impls.SemesterManagerImpl;
import ch.zhaw.studyflow.services.persistence.ModuleDao;
import ch.zhaw.studyflow.services.persistence.SemesterDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        Semester semester = new Semester();
        semester.setName("Test Semester");
        semester.setDescription("Test Description");
        semester.setDegreeId(1L);
        semester.setUserId(1L);

        semesterManager.createSemester(semester, semester.getDegreeId(), semester.getUserId());

        verify(semesterDao).createSemester(semester, semester.getDegreeId(), semester.getUserId());
    }

    @Test
    void updateSemester() {
        Semester persistedSemester = new Semester();
        persistedSemester.setId(1L);
        persistedSemester.setName("Test Semester");
        persistedSemester.setDescription("Test Description");
        persistedSemester.setDegreeId(1L);
        persistedSemester.setUserId(1L);
        when(semesterDao.read(1L)).thenReturn(persistedSemester);

        if ()

        semesterManager.updateSemester(persistedSemester);

        verify(semesterDao).update(persistedSemester);
    }

    @Test
    void testReadAllByStudent() {
        verify(semesterDao).readAllByStudent(1L);
    }
}
