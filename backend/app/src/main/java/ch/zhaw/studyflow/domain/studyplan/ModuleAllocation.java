package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;

public interface ModuleAllocation extends Comparable<ModuleAllocation> {
    long getModuleId();
    long getPercentage();
    long getTargetMinutes();
    long getAllocatedMinutes();
    long getRemainingMinutes();
    void allocate(StudyDay day);
    List<StudyDay> getStudyDays();

    @Override
    int compareTo(ModuleAllocation other);
}
