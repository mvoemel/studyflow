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

        when(studentDao.readStudentByEmail("test@test.test")).thenReturn(student);

        final Optional<Student> loginResult = studentManager.login(student.getEmail(), "password");

        assertTrue(loginResult.isPresent());
        assertEquals(student, loginResult.get());
    }

    @Test
    void testInvalidLogin() {
        final Student student = makeFakeStudent();

        when(studentDao.readStudentByEmail("test@test.test")).thenReturn(student);

        final Optional<Student> loginResult = studentManager.login(student.getEmail(), "wrongPassword");

        assertTrue(loginResult.isEmpty());
    }

    @Test
    void testGetStudent() {
        final Student student = makeFakeStudent();
        student.setId(1);

        when(studentDao.readStudentById(1)).thenReturn(student);

        final Optional<Student> studentResult = studentManager.getStudent(1);

        assertTrue(studentResult.isPresent());
        assertEquals(student, studentResult.get());
        verify(studentDao).readStudentById(1);
    }

    @Test
    void testGetStudentByEmail() {
        final Student student = makeFakeStudent();

        when(studentDao.readStudentByEmail(student.getEmail())).thenReturn(student);

        final Optional<Student> studentResult = studentManager.getStudentByEmail(student.getEmail());

        assertTrue(studentResult.isPresent());
        assertEquals(student, studentResult.get());
        verify(studentDao).readStudentByEmail(student.getEmail());
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

    @Test
    void testUpdateStudent() {
        final Student persistedStudent = makeFakeStudent();
        persistedStudent.setId(1);
        when(studentDao.readStudentById(1)).thenReturn(persistedStudent);

        final Student updatedStudent = makeFakeStudent();
        updatedStudent.setId(1);
        updatedStudent.setEmail("UpdatedEmail");
        updatedStudent.setFirstname("UpdatedFirstname");
        updatedStudent.setLastname("UpdatedLastname");
        updatedStudent.setPassword("UpdatedPassword");

        studentManager.updateStudent(updatedStudent);
        verify(studentDao).update(persistedStudent);

        assertEquals("UpdatedEmail", persistedStudent.getEmail());
        assertEquals("UpdatedFirstname", persistedStudent.getFirstname());
        assertEquals("UpdatedLastname", persistedStudent.getLastname());
        assertEquals("UpdatedPassword", persistedStudent.getPassword());
    }

    @Test
    void testUpdateStudentDontUpdatePasswordIfNull() {
        final Student persistedStudent = makeFakeStudent();
        persistedStudent.setId(1);
        when(studentDao.readStudentById(1)).thenReturn(persistedStudent);

        final Student updatedStudent = makeFakeStudent();
        updatedStudent.setId(1);

        updatedStudent.setEmail("UpdatedEmail");
        updatedStudent.setFirstname("UpdatedFirstname");
        updatedStudent.setLastname("UpdatedLastname");
        studentManager.updateStudent(updatedStudent);
        verify(studentDao).update(persistedStudent);

        assertEquals("UpdatedEmail", persistedStudent.getEmail());
        assertEquals("UpdatedFirstname", persistedStudent.getFirstname());
        assertEquals("UpdatedLastname", persistedStudent.getLastname());
        assertEquals("password", persistedStudent.getPassword());
    }

    @Test
    void testUpdateSettings() {
        final Settings persistedSettings = new Settings();
        persistedSettings.setId(1);
        persistedSettings.setActiveDegree(5);
        persistedSettings.setGlobalCalendarId(6);
        when(settingsDao.read(1)).thenReturn(persistedSettings);

        final Settings updatedSettings = new Settings();
        updatedSettings.setId(1);
        updatedSettings.setActiveDegree(7);
        updatedSettings.setGlobalCalendarId(8);

        studentManager.updateSettings(updatedSettings);
        verify(settingsDao).update(persistedSettings);

        assertEquals(7, persistedSettings.getActiveDegree());
        assertEquals(6, persistedSettings.getGlobalCalendarId());
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
