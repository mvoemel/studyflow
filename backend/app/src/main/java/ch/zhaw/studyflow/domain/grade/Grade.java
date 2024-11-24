package ch.zhaw.studyflow.domain.grade;

/**
 * Represents a grade in the system.
 */
public class Grade {
    private long id;
    private long belongsTo;
    private long mark;

    /**
     * Gets the ID of the grade.
     *
     * @return the ID of the grade.
     */
    public long getId() {
        return id;
    }


    /**
     * Sets the ID of the grade.
     *
     * @param id the new ID of the grade.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the module ID to which this grade belongs.
     *
     * @return the module ID.
     */
    public long getBelongsTo() {
        return belongsTo;
    }

    /**
     * Sets the module ID to which this grade belongs.
     *
     * @param moduleId the new module ID.
     */
    public void setBelongsTo(long moduleId) {
        this.belongsTo = moduleId;
    }

    /**
     * Gets the mark of the grade.
     *
     * @return the mark.
     */
    public long getMark() {
        return mark;
    }

    /**
     * Sets the mark of the grade.
     *
     * @param mark the new mark.
     */
    public void setMark(long mark) {
        this.mark = mark;
    }
}
