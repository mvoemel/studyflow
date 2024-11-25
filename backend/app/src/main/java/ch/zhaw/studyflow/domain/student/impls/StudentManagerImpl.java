package ch.zhaw.studyflow.domain.student.impls;

import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.services.persistence.SettingsDao;
import ch.zhaw.studyflow.services.persistence.StudentDao;

import java.util.Optional;
import java.util.logging.Logger;

public class StudentManagerImpl implements StudentManager {
    private static final Logger LOGGER = Logger.getLogger(StudentManagerImpl.class.getName());

    private final CalendarManager calendarManager;
    private final StudentDao studentDao;
    private final SettingsDao settingsDao;


    public StudentManagerImpl(CalendarManager calendarManager, StudentDao studentDao, SettingsDao settingsDao) {
        this.calendarManager    = calendarManager;
        this.studentDao         = studentDao;
        this.settingsDao        = settingsDao;
    }


    @Override
    public Optional<Student> login(String email, String password) {
        Student student = studentDao.readStudentByEmail(email);

        Optional<Student> result = Optional.empty();
        if (student != null
                && student.checkPassword(password)) {
            result = Optional.of(student);
        }
        return result;
    }

    @Override
    public Optional<Student> getStudent(long studentId) {
        return Optional.ofNullable(studentDao.readStudentById(studentId));
    }

    @Override
    public Optional<Student> getStudentByEmail(String email) {
        return Optional.ofNullable(studentDao.readStudentByEmail(email));
    }

    @Override
    public Optional<Settings> getSettings(long studentId) {
        Optional<Settings> result;
        try {
            result = Optional.of(settingsDao.readByUserId(studentId));
        } catch (Exception e) {
            LOGGER.warning("Could not load settings for student with id " + studentId);
            result = Optional.empty();
        }
        return result;
    }

    @Override
    public Optional<Student> register(Student student) {
        Optional<Student> result;
        if (studentDao.readStudentByEmail(student.getEmail()) != null) {
            result = Optional.empty();
        } else {
            studentDao.create(student);

            Calendar calendar = new Calendar();
            calendar.setName("Global Calendar");
            calendar.setOwnerId(student.getId());
            calendarManager.create(calendar);

            Settings settings = new Settings();
            settings.setGlobalCalendarId(calendar.getCalendarId());
            settingsDao.create(student.getId(), settings);
            result = Optional.of(student);
        }
        return result;
    }
}
