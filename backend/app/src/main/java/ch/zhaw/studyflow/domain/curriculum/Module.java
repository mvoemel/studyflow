package ch.zhaw.studyflow.domain.curriculum;

/**
 * Represents a module in the curriculum.
 */
public class Module {

    private long id;
    private String name;
    private String description;
    private long ECTS;
    private long understanding;
    private long time;
    private long complexity;
    private long semesterId;
    private long degreeId;

    /**
     * Default constructor.
     */
    public Module() {
        this.id = -1;
    }

    /**
     * Constructs a module with the specified details.
     *
     * @param id the ID of the module
     * @param name the name of the module
     */
    public Module(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Constructs a module with the specified details.
     *
     * @param id the ID of the module
     * @param name the name of the module
     * @param semesterId the semester ID of the module
     * @param degreeId the degree ID of the module
     */
    public Module(long id, String name, long semesterId) {
        this.id = id;
        this.name = name;
        this.semesterId = semesterId;
        this.degreeId = degreeId;
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
        if(this.id >= 0) {
            throw new IllegalStateException("ID is already set");
        }
        if(id < 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
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
    public long getUnderstanding() {
        return understanding;
    }

    /**
     * Sets the understanding value of the module.
     *
     * @param understanding the new understanding value of the module
     */
    public void setUnderstanding(long understanding) {
        this.understanding = understanding;
    }

    /**
     * Gets the time value of the module.
     *
     * @return the time value of the module
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time value of the module.
     *
     * @param time the new time value of the module
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Gets the importance value of the module.
     *
     * @return the importance value of the module
     */
    public long getComplexity() {
        return complexity;
    }

    /**
     * Sets the importance value of the module.
     *
     * @param complexity the new importance value of the module
     */
    public void setComplexity(long complexity) {
        this.complexity = complexity;
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
     * Gets the degree ID of the module.
     *
     * @return the degree ID of the module
     */
    public long getDegreeId() {
        return degreeId;
    }

    /**
     * Sets the degree ID of the module.
     *
     * @param degreeId the new degree ID of the module
     */
    public void setDegreeId(long degreeId) {
        this.degreeId = degreeId;
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
