package ch.zhaw.studyflow.domain.curriculum;

public class Semester {
    private long id;
    private String name;
    private long degreeId;
    private long userId;
    private long calendarId;
    private String description;


    public Semester() {
        this.id = -1;
    }

    public Semester(long id, String name, String description, long degreeId, long userId, long calendarId) {
        this.id = id;
        this.name = name;
        this.degreeId = degreeId;
        this.userId = userId;
        this.description = description;
        this.calendarId = calendarId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        if (this.id != -1) {
            throw new IllegalStateException("The id can only be set once.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDegreeId() {
        return degreeId;
    }

    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
