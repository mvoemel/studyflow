package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;

public interface StudyplanAlgorithm {   
    List<ModuleAllocation> runAlgorithm();
    List<StudyDay> getStudyDays();
    void createStudyDays();
    void allocateModules();


}
