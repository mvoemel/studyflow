package ch.zhaw.studyflow.domain.student;

import ch.zhaw.studyflow.utils.Result;

import java.util.Optional;

public interface StudentManager {
    Optional<Student> login(String email, String password);
    Optional<Student> getStudent(long studentId);

    Optional<Student> register(Student student);
}
