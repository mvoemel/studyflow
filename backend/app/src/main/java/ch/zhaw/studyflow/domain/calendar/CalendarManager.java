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


    public List<Calendar> getCalendars() {
        return calendarDao.getAll();
    }
}