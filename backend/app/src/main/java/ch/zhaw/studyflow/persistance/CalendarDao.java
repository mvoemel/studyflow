package ch.zhaw.studyflow.persistance;

import ch.zhaw.studyflow.Calendar;
import java.util.List;

public interface CalendarDao {
    Calendar save(Calendar calendar);
    List<Calendar> readAll(long userId);
    Calendar read(long userId, long calendarId);
    void delete(long userId, long calendarId);
    Calendar update(Calendar calendar);
}