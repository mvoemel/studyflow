package ch.zhaw.studyflow.controllers.deo;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class LoginRequest {
    private String username;
    private String password;

    @JsonGetter("email")
    public String getEmail() {
        return username;
    }

    @JsonSetter("email")
    public void setEmail(String username) {
        this.username = username;
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
