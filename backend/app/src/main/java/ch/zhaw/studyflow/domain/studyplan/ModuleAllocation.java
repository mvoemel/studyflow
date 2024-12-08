package ch.zhaw.studyflow.domain.studyplan;

public interface ModuleAllocation extends Comparable<ModuleAllocation> {
    long getModuleId();
    long getPercentage();
    long getTargetMinutes();
    long getAllocatedMinutes();
    long getRemainingMinutes();
    void allocate(StudyDay day);

    @Override
    int compareTo(ModuleAllocation other);
}
