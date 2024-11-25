package ch.zhaw.studyflow.domain.student;

import java.util.Objects;

public class Student {
    private long id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private long settingsId;


    public Student() {
        this.id = -1;
    }

    public Student(long id, String name) {
        Objects.requireNonNull(name);

        this.id = id;
        this.firstname = name;
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

    public String getLastname() {
        return firstname;
    }

    public void setLastname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return lastname;
    }

    public void setFirstname(String lastname) {
        this.lastname = lastname;
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

    public long getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(long settingsId) {
        this.settingsId = settingsId;
    }
}
