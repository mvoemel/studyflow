package ch.zhaw.studyflow.domain.studyplan.impls;

import ch.zhaw.studyflow.domain.studyplan.Studyplan;

public class BasicStudyplanImpl implements Studyplan {
    private String name;
    private String description;
    private String studyplanId;

    public BasicStudyplanImpl(String name, String description, String studyplanId) {
        this.name = name;
        this.description = description;
        this.studyplanId = studyplanId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStudyplanId() {
        return studyplanId;
    }
    
}
