package ch.zhaw.studyflow.services.persistance.memory;

import ch.zhaw.studyflow.domain.student.Student;
import ch.zhaw.studyflow.services.persistance.StudentDao;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryStudentDao implements StudentDao {
    private AtomicLong idCounter = new AtomicLong(0);
    private HashMap<Long, Student> studentsById;


    public InMemoryStudentDao() {
        this.studentsById = new HashMap<>();
    }


    @Override
    public void create(Student student) {
        Objects.requireNonNull(student);
        student.setId(idCounter.getAndIncrement());
        studentsById.put(student.getId(), student);
    }

    @Override
    public void update(Student student) {
        Objects.requireNonNull(student);
        studentsById.put(student.getId(), student);
    }

    @Override
    public void delete(long studentId) {
        studentsById.remove(studentId);
    }

    @Override
    public Optional<Student> readStudentById(long studentId) {
        return Optional.ofNullable(studentsById.get(studentId));
    }

    @Override
    public Optional<Student> readStudentByEmail(String email) {
        return studentsById.values().stream()
                .filter(student -> student.getEmail().equals(email))
                .findFirst();
    }
}
