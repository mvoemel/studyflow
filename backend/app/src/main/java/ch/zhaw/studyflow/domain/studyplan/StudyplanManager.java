package ch.zhaw.studyflow.domain.studyplan;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;

public interface StudyplanManager {
    
    Long createStudyplan(StudyplanParameters parameters, long userId);
    
    
}
