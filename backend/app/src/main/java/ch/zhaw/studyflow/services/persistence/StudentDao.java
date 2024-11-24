package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.student.Student;

/**
 * Data access object for reading, writing and updating students from and to a persistent storage.
 */
public interface StudentDao {
    /**
     * Creates a new student.
     * @param student the student to create
     */
    void create(Student student);

    /**
     * Updates a student.
     * @param student the student to update
     */
    void update(Student student);

    /**
     * Deletes a specific student.
     * @param studentId The id of the student
     */
    void delete(long studentId);

    /**
     * Reads a specific student.
     * @param studentId The id of the student
     * @return the student, or null if not found
     */
    Student readStudentById(long studentId);

    /**
     * Reads a specific student by email.
     * @param email The email of the student
     * @return the student, or null if not found
     */
    Student readStudentByEmail(String email);
}
