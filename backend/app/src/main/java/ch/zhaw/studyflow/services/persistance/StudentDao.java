package ch.zhaw.studyflow.services.persistance;

import ch.zhaw.studyflow.domain.student.Student;

import java.util.Optional;


public interface StudentDao {
    void create(Student student);
    void update(Student student);
    void delete(long studentId);
    Optional<Student> readStudentById(long studentId);
    Optional<Student> readStudentByEmail(String email);
}
