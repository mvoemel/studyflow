package ch.zhaw.studyflow.domain.student.impls;

import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.services.persistance.StudentDao;

import java.util.Optional;

public class StudentManagerImpl implements StudentManager {
    private final StudentDao studentDao;


    public StudentManagerImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }


    @Override
    public Optional<Student> login(String email, String password) {
        Optional<Student> student = studentDao.readStudentByEmail(email);
        if (student.isPresent()
        && student.get().checkPassword(password)) {
            return Optional.of(student.get());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> getStudent(long studentId) {
        return null;
    }

    @Override
    public Optional<Student> register(Student student) {
        return Optional.empty();
    }
}
