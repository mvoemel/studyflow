package ch.zhaw.studyflow.services.persistance;

import ch.zhaw.studyflow.domain.student.Student;

import java.util.Optional;


public interface StudentDao {
    void create(Student student);
    Optional<Student> readStudentById(long studentId);
    Optional<Student> readStudentByEmail(String email);
}
