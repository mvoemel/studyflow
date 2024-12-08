package ch.zhaw.studyflow.domain.student;

import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.student.impls.StudentManagerImpl;
import ch.zhaw.studyflow.services.persistence.SettingsDao;
import ch.zhaw.studyflow.services.persistence.StudentDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StudentManagerTest {
    private StudentManager studentManager;
    private CalendarManager calendarManager;
    private StudentDao studentDao;
    private SettingsDao settingsDao;

    @BeforeEach
    public void beforeEach() {
        calendarManager = mock(CalendarManager.class);
        studentDao = mock(StudentDao.class);
        settingsDao = mock(SettingsDao.class);
        studentManager = new StudentManagerImpl(calendarManager, studentDao, settingsDao);
    }

    @Test
    void testRegistration() {
        final Student student = makeFakeStudent();

        doAnswer(invocation -> {
            final Student studentToCreate = invocation.getArgument(0);
            studentToCreate.setId(1);
            return studentToCreate;
        }).when(studentDao).create(any(Student.class));

        doAnswer(invocation -> {
            final Settings settings = invocation.getArgument(0);
            settings.setId(1L);
            return settings;
        }).when(settingsDao).create(any(Settings.class));

        doAnswer(invocation -> {
            final Calendar calendar = invocation.getArgument(0);
            calendar.setId(1);
            return null;
        }).when(calendarManager).create(any(Calendar.class));

        final Optional<Student> register = studentManager.register(student);

        verify(studentDao).create(student);
        verify(settingsDao).create(any(Settings.class));
        verify(calendarManager).create(any(Calendar.class));
    }

    @Test
    void testLogin() {
        final Student student = makeFakeStudent();

        when(studentDao.readByEMail("test@test.test")).thenReturn(student);

        final Optional<Student> loginResult = studentManager.login(student.getEmail(), "password");

        assertTrue(loginResult.isPresent());
        assertEquals(student, loginResult.get());
    }

    @Test
    void testInvalidLogin() {
        final Student student = makeFakeStudent();

        when(studentDao.readByEMail("test@test.test")).thenReturn(student);

        final Optional<Student> loginResult = studentManager.login(student.getEmail(), "wrongPassword");

        assertTrue(loginResult.isEmpty());
    }

    @Test
    void testGetStudent() {
        final Student student = makeFakeStudent();
        student.setId(1);

        when(studentDao.read(1)).thenReturn(student);

        final Optional<Student> studentResult = studentManager.getStudent(1);

        assertTrue(studentResult.isPresent());
        assertEquals(student, studentResult.get());
        verify(studentDao).read(1);
    }

    @Test
    void testGetStudentByEmail() {
        final Student student = makeFakeStudent();

        when(studentDao.readByEMail(student.getEmail())).thenReturn(student);

        final Optional<Student> studentResult = studentManager.getStudentByEmail(student.getEmail());

        assertTrue(studentResult.isPresent());
        assertEquals(student, studentResult.get());
        verify(studentDao).readByEMail(student.getEmail());
    }

    @Test
    void testGetSettings() {
        final Settings settings = new Settings();
        settings.setGlobalCalendarId(42);
        settings.setId(1);

        when(settingsDao.read(1L)).thenReturn(settings);

        final Optional<Settings> settingsResult = studentManager.getSettings(1L);

        assertTrue(settingsResult.isPresent());
        assertEquals(settings, settingsResult.get());
        verify(settingsDao).read(1);
    }


    private static Student makeFakeStudent() {
        final Student student = new Student();
        student.setEmail("test@test.test");
        student.setFirstname("Firstname");
        student.setLastname("Lastname");
        student.setPassword("password");
        return student;
    }
}
