package ch.zhaw.studyflow.domain.studyplan;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.services.ServiceCollection;

public interface StudyplanManager {
    
    Long createStudyplan(StudyplanParameters parameters, ServiceCollection serviceCollection, long userId);
    
    
}
