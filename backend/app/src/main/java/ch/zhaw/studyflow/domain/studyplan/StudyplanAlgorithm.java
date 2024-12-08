package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;

public interface StudyplanAlgorithm {

    List<StudyDay> getStudyDays();
    List<ModuleAllocation> getModuleAllocations();
    void createStudyDays();
    void allocateModules();


}
