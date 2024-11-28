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
        this.id = -1;
    }

    @JsonGetter
    public long getId() {
        return id;
    }

    @JsonSetter
    public void setId(long id) {
        if (this.id != -1) {
            throw new IllegalStateException("ID already set");
        }
        this.id = id;
    }

    @JsonGetter
    public long getOwnerId() {
        return ownerId;
    }

    @JsonSetter
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    @JsonGetter
    public String getName() {
        return name;
    }

    @JsonSetter
    public void setName(String name) {
        this.name = name;
    }

    @JsonSetter
    public long getActiveSemesterId() {
        return activeSemesterId;
    }

    @JsonSetter
    public void setActiveSemesterId(long activeSemesterId) {
        this.activeSemesterId = activeSemesterId;
    }

    @JsonGetter
    public String getDescription() {
        return description;
    }

    @JsonSetter
    public void setDescription(String description) {
        this.description = description;
    }
}
