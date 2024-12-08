package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;

public class BasicStudyAllocation implements StudyAllocation {
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public BasicStudyAllocation(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BasicStudyAllocation(LocalTime start, LocalTime end, LocalDate date) {
        this.startTime = LocalDateTime.of(date, start);
        this.endTime = LocalDateTime.of(date, end);
    }

    @Override
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int getMinutes() {
        return (int) (endTime.toLocalTime().toSecondOfDay() - startTime.toLocalTime().toSecondOfDay()) / 60;
    }
}
