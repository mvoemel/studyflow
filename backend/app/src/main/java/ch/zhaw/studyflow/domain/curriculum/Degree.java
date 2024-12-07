package ch.zhaw.studyflow.domain.curriculum;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Degree {
    private long id;
    private long ownerId;
    private String name;
    private String description;
    private long activeSemesterId;

    public Degree() {
        this.id                 = -1;
        this.ownerId            = -1;
        this.activeSemesterId   = -1;
    }

    @JsonGetter("id")
    public long getId() {
        return id;
    }

    @JsonSetter("id")
    public void setId(long id) {
        if (this.id != -1) {
            throw new IllegalStateException("ID already set");
        }
        this.id = id;
    }

    @JsonGetter("owner")
    public long getOwnerId() {
        return ownerId;
    }

    @JsonSetter("owner")
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter("activeSemesterId")
    public long getActiveSemesterId() {
        return activeSemesterId;
    }

    @JsonSetter("activeSemesterId")
    public void setActiveSemesterId(long activeSemesterId) {
        this.activeSemesterId = activeSemesterId;
    }

    @JsonGetter("description")
    public String getDescription() {
        return description;
    }

    @JsonSetter("description")
    public void setDescription(String description) {
        this.description = description;
    }
}
