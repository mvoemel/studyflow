package ch.zhaw.studyflow.domain.student;

import ch.zhaw.studyflow.utils.Result;

import javax.swing.text.html.Option;
import java.util.Optional;

/**
 * The StudentManager is responsible for managing students. It provides methods to login, register, and get student
 */
public interface StudentManager {
    /**
     * Login a student with the given email and password
     * @param email The email of the student
     * @param password The password of the student
     * @return The student if the login was successful
     */
    Optional<Student> login(String email, String password);

    /**
     * Get the student with the given student id
     * @param studentId The id of the student
     * @return The student if it exists
     */
    Optional<Student> getStudent(long studentId);

    /**
     * Get the student with the given email
     * @param email The email of the student
     * @return The student if it exists
     */
    Optional<Student> getStudentByEmail(String email);

    /**
     * Get the settings of the student with the given student id
     * @param settingsId The id of the student
     * @return The settings if they exist
     */
    Optional<Settings> getSettings(long settingsId);

    /**
     * Update the settings of the student with the given student id
     * @param settings The new settings
     */
    void updateSettings(Settings settings);

    /**
     * Register a student with the given student
     * @param student The student to register
     * @return The registered student if the registration was successful
     */
    Optional<Student> register(Student student);

    /**
     * Update the student with the given student id
     * @param student The new student
     */
    void updateStudent(Student student);
}
