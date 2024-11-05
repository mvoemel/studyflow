package ch.zhaw.studyflow;

public class Calendar {
    private long id;
    private String name;

    // Constructors
    public Calendar() {}

    public Calendar(long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // toString method
    @Override
    public String toString() {
        return "Calendar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}