package ch.zhaw.studyflow.controllers.deo;

import ch.zhaw.studyflow.domain.student.Student;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class StudentDeo {
    private long id;
    private String firstname;
    private String lastname;
    private String email;


    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonGetter("firstname")
    public String getFirstname() {
        return this.firstname;
    }

    @JsonSetter("firstname")
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @JsonGetter("lastname")
    public String getLastname() {
        return lastname;
    }

    @JsonSetter("lastname")
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @JsonGetter("email")
    public String getEmail() {
        return email;
    }

    @JsonSetter("email")
    public void setEmail(String email) {
        this.email = email;
    }

    public static StudentDeo of(Student student) {
        StudentDeo studentDeo = new StudentDeo();
        studentDeo.setFirstname(student.getFirstname());
        studentDeo.setLastname(student.getLastname());
        studentDeo.setEmail(student.getEmail());
        return studentDeo;
    }
}
