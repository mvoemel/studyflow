package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LoginRequest {
    private String email;
    private String password;

    @JsonGetter("email")
    public String getEmail() {
        return email;
    }

    @JsonSetter("email")
    public void setEmail(String username) {
        this.email = username;
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
