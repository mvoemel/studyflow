package ch.zhaw.studyflow.domain.student;

import java.util.Objects;

public class Student {
    private long id;
    private String name;
    private String email;
    private String password;


    public Student() {
        this.id = -1;
    }

    public Student(long id, String name) {
        Objects.requireNonNull(name);

        this.id = id;
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (this.id >= 0) {
            throw new IllegalStateException("The id can only be set once.");
        }
        if (id < 0) {
            throw new IllegalArgumentException("The id must be greater than or equal to 0.");
        }

        this.id = id;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
