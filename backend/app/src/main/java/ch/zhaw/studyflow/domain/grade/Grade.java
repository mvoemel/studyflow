package ch.zhaw.studyflow.domain.grade;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a grade in the system.
 */
public class Grade {
    private long id;
    private String name;
    private double percentage;
    private double value;

    /**
     * Default constructor.
     */
    public Grade() {}

    /**
     * Constructs a grade with the specified ID, name, percentage, and value.
     *
     * @param id the ID of the grade
     * @param name the name of the grade
     * @param percentage the percentage of the grade
     * @param value the value of the grade
     */
    public Grade(long id, String name, double percentage, double value) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.value = value;
    }

    /**
     * Gets the ID of the grade.
     *
     * @return the ID of the grade.
     */
    @JsonGetter("id")
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the grade.
     *
     * @param id the new ID of the grade.
     */
    @JsonSetter("id")
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name of the grade.
     *
     * @return the name.
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the grade.
     *
     * @param name the new name.
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the percentage of the grade.
     *
     * @return the percentage.
     */
    @JsonGetter("percentage")
    public double getPercentage() {
        return percentage;
    }

    /**
     * Sets the percentage of the grade.
     *
     * @param percentage the new percentage.
     */
    @JsonSetter("percentage")
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    /**
     * Gets the value of the grade.
     *
     * @return the value.
     */
    @JsonGetter("value")
    public double getValue() {
        return value;
    }

    /**
     * Sets the value of the grade.
     *
     * @param value the new value.
     */
    @JsonSetter("value")
    public void setValue(double value) {
        this.value = value;
    }
}