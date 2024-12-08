package ch.zhaw.studyflow.domain.studyplan.basicImpls;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.studyflow.domain.studyplan.ModuleAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;

public class BasicModuleAllocation implements ModuleAllocation {
    private long moduleId;
    private long percentage;
    private long targetMinutes;
    private long allocatedMinutes;
    private List<StudyDay> studyDays;

    public BasicModuleAllocation(Long moduleId, Long percentage, long targetMinutes) {
        this.moduleId = moduleId;
        this.percentage = percentage;
        this.targetMinutes = targetMinutes;

        List<StudyDay> studyDays = new ArrayList<>();
    }

    @Override
    public long getModuleId() {
        return moduleId;
    }

    @Override
    public long getPercentage() {
        return percentage;
    }

    @Override
    public long getTargetMinutes() {
        return targetMinutes;
    }

    @Override
    public long getAllocatedMinutes() {
        return allocatedMinutes;
    }

    @Override
    public long getRemainingMinutes() {
        return targetMinutes - allocatedMinutes;
    }

    @Override
    public void allocate(StudyDay day) {
        studyDays.add(day);
        if (this.getRemainingMinutes() >= day.getMinutes()) {
            allocatedMinutes += day.getMinutes();
        } else {
            allocatedMinutes += this.getRemainingMinutes();
        }
    }

    @Override
    public int compareTo(ModuleAllocation other) {
        return Long.compare(this.getRemainingMinutes(), other.getRemainingMinutes());
    }
    
    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }
}
