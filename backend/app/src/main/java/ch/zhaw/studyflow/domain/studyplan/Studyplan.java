package ch.zhaw.studyflow.domain.studyplan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface Studyplan {

public long getCalendarId();

public void setCalendarId(long calendarId);

public LocalDate getStartDate();

public void setStartDate(LocalDate startDate);

public LocalDate getEndDate();

public void setEndDate(LocalDate endDate);

public LocalTime getStartTime();

public void setStartTime(LocalTime startTime);

public LocalTime getEndTime();

public void setEndTime(LocalTime endTime);

public List<DayOfWeek> getDaysOfWeek();

public List<StudyDay> getStudyDays();

public Map<Module, List<StudyDay>> getModuleStudyDays();


}
