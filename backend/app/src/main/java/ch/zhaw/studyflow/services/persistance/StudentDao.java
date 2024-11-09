package ch.zhaw.studyflow.services.persistance;

import ch.zhaw.studyflow.domain.student.Student;

import java.util.Optional;


public interface StudentDao {
    Optional<Student> readStudentByEmail(String email);
}
