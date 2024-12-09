package ch.zhaw.studyflow.controllers.deo;

import ch.zhaw.studyflow.domain.student.Settings;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * The MeResponse contains all the information about the student and its settings.
 * It's an ease of life class to return the student and its settings in one response.
 */
public class MeResponse {
    private StudentDeo student;
    private Settings settings;

    public MeResponse() {
    }

    public MeResponse(StudentDeo studentDeo, Settings settings) {
        this.student    = studentDeo;
        this.settings   = settings;
    }

    @JsonGetter("student")
    public StudentDeo getStudent() {
        return student;
    }

    @JsonSetter("student")
    public void setStudent(StudentDeo student) {
        this.student = student;
    }

    @JsonSetter("settings")
    public Settings getSettings() {
        return settings;
    }

    @JsonGetter("settings")
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
