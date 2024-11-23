package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

public interface CalendarManager {
    void create(Calendar calendar);

    Calendar read(long userId, long calendarId);

    void delete(long userId, long calendarId);

    Calendar update(Calendar calendar);

    List<Calendar> getCalendarsByUserId(long userId);
}
