package ch.zhaw.studyflow.domain.student.impls;

import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.services.persistance.SettingsDao;
import ch.zhaw.studyflow.services.persistance.StudentDao;

import java.util.Optional;
import java.util.logging.Logger;

public class StudentManagerImpl implements StudentManager {
    private static final Logger LOGGER = Logger.getLogger(StudentManagerImpl.class.getName());

    private final StudentDao studentDao;
    private final SettingsDao settingsDao;


    public StudentManagerImpl(StudentDao studentDao, SettingsDao settingsDao) {
        this.studentDao     = studentDao;
        this.settingsDao    = settingsDao;
    }


    @Override
    public Optional<Student> login(String email, String password) {
        Optional<Student> student = studentDao.readStudentByEmail(email);
        if (student.isPresent()
        && student.get().checkPassword(password)) {
            return student;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> getStudent(long studentId) {
        return studentDao.readStudentById(studentId);
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
        if (studentDao.readStudentByEmail(student.getEmail()).isPresent()) {
            result = Optional.empty();
        } else {
            studentDao.create(student);
            Settings settings = new Settings();
            settings.setGlobalCalendarId(-1); // TODO: use a real calendar id
            settingsDao.create(student.getId(), settings);
            result = Optional.of(student);
        }
        return result;
    }
}
