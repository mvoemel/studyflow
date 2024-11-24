package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Registration {
    private String firstname;
    private String lastname;
    private String email;
    private String password;


    @JsonGetter("username")
    public String getFirstname() {
        return firstname;
    }

    @JsonSetter("username")
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

    @JsonGetter("password")
    public String getPassword() {
        return password;
    }

    @JsonSetter("password")
    public void setPassword(String password) {
        this.password = password;
    }
}
