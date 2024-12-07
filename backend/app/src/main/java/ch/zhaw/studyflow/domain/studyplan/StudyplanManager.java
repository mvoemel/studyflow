package ch.zhaw.studyflow.domain.studyplan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface StudyplanManager {
    
    Long createStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId);

    
}
