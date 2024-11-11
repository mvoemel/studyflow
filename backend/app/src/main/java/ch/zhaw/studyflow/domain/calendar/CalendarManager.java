package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

public class CalendarManager {
    private final CalendarDao calendarDao;

    public CalendarManager(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }

    public void create(Calendar calendar) {
        calendarDao.create(calendar);
    }

    public Calendar read(long userId, long calendarId) {
        return calendarDao.read(userId, calendarId);
    }

    public void delete(long userId, long calendarId) {
        calendarDao.delete(userId, calendarId);
    }

    public Calendar update(Calendar calendar) {
        return calendarDao.update(calendar);
    }

    public long getCalendarId(Calendar calendar) {
        return calendarDao.getCalendarId(calendar);
    }

    public void setCalendarId(Calendar calendar, long newId) {
        calendar.setId(newId);
        calendarDao.setCalendarId(calendar, newId);
    }

    public List<Calendar> getCalendars() {
        return calendarDao.getAll();
    }
}