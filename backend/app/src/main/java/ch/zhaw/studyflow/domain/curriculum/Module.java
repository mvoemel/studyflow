package ch.zhaw.studyflow.domain.curriculum;

/**
 * Represents a module in the curriculum.
 */
public class Module {

    private long id;
    private String name;
    private String description;
    private long ECTS;
    private long understandingValue;
    private long timeValue;
    private long importanceValue;
    private long semesterId;

    /**
     * Default constructor.
     */
    public Module() {}

    /**
     * Constructs a module with the specified details.
     *
     * @param id the ID of the module
     * @param name the name of the module
     * @param description the description of the module
     * @param ECTS the European Credit Transfer System value of the module
     * @param understandingValue the understanding value of the module, ranging from 1 to 10
     * @param timeValue the time value of the module, ranging from 1 to 10
     * @param importanceValue the importance value of the module, ranging from 1 to 10
     */
    public Module(long id, String name, String description, long ECTS, long understandingValue, long timeValue, long importanceValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ECTS = ECTS;
        this.understandingValue = understandingValue;
        this.timeValue = timeValue;
        this.importanceValue = importanceValue;
    }

    /**
     * Gets the ID of the module.
     *
     * @return the ID of the module
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the module.
     *
     * @param id the new ID of the module
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the name of the module.
     *
     * @return the name of the module
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the module.
     *
     * @param name the new name of the module
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the module.
     *
     * @return the description of the module
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the module.
     *
     * @param description the new description of the module
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the ECTS value of the module.
     *
     * @return the ECTS value of the module
     */
    public long getECTS() {
        return ECTS;
    }

    /**
     * Sets the ECTS value of the module.
     *
     * @param ECTS the new ECTS value of the module
     */
    public void setECTS(long ECTS) {
        this.ECTS = ECTS;
    }

    /**
     * Gets the understanding value of the module.
     *
     * @return the understanding value of the module
     */
    public long getUnderstandingValue() {
        return understandingValue;
    }

    /**
     * Sets the understanding value of the module.
     *
     * @param understandingValue the new understanding value of the module
     */
    public void setUnderstandingValue(long understandingValue) {
        this.understandingValue = understandingValue;
    }

    /**
     * Gets the time value of the module.
     *
     * @return the time value of the module
     */
    public long getTimeValue() {
        return timeValue;
    }

    /**
     * Sets the time value of the module.
     *
     * @param timeValue the new time value of the module
     */
    public void setTimeValue(long timeValue) {
        this.timeValue = timeValue;
    }

    /**
     * Gets the importance value of the module.
     *
     * @return the importance value of the module
     */
    public long getImportanceValue() {
        return importanceValue;
    }

    /**
     * Sets the importance value of the module.
     *
     * @param importanceValue the new importance value of the module
     */
    public void setImportanceValue(long importanceValue) {
        this.importanceValue = importanceValue;
    }

    /**
     * Gets the semester ID of the module.
     *
     * @return the semester ID of the module
     */
    public long getSemesterId() {
        return semesterId;
    }

    /**
     * Sets the semester ID of the module.
     *
     * @param semesterId the new semester ID of the module
     */
    public void setSemesterId(long semesterId) {
        this.semesterId = semesterId;
    }

}