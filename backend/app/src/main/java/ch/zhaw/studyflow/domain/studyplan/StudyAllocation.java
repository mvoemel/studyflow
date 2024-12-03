package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDateTime;

public interface StudyAllocation {

    public String getStudyAllocation();
    public Module getModule();
    public void setModule(Module module);
    public LocalDateTime getStartTime();
    public void setStartTime(LocalDateTime startTime);
    public LocalDateTime getEndTime();
    public void setEndTime(LocalDateTime endTime);
    public int getMinutes();
}
