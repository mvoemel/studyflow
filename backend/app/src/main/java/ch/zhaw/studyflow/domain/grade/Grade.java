package ch.zhaw.studyflow.domain.grade;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * Represents a grade in the system.
 */
public class Grade {
    private long id;
    private long belongsTo;
    private String name;
    private double percentage;
    private double value;

    /**
     * Default constructor.
     */
    public Grade() {}

    /**
     * Constructs a grade with the specified ID, module ID, and mark.
     *
     * @param id the ID of the grade
     * @param belongsTo the module ID to which this grade belongs
     * @param mark the mark of the grade
     */
    public Grade(long id, long belongsTo, long mark) {
        this.id = id;
        this.belongsTo = belongsTo;
        this.mark = mark;
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
     * Gets the module ID to which this grade belongs.
     *
     * @return the module ID.
     */
    @JsonGetter("belongsTo")
    public long getBelongsTo() {
        return belongsTo;
    }

    /**
     * Sets the module ID to which this grade belongs.
     *
     * @param belongsTo the new module ID.
     */
    @JsonSetter("belongsTo")
    public void setBelongsTo(long belongsTo) {
        this.belongsTo = belongsTo;
    }

    /**
     * Gets the mark of the grade.
     *
     * @return the mark.
     */
    @JsonGetter("mark")
    public long getMark() {
        return mark;
    }

    /**
     * Sets the mark of the grade.
     *
     * @param mark the new mark.
     */
    @JsonSetter("mark")
    public void setMark(long mark) {
        this.mark = mark;
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