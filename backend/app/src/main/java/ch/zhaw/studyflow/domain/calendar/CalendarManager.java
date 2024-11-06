package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

/**
 * Manages calendar data for users.
 */
public class CalendarManager {
    private final CalendarDao calendarDao;

    /**
     * Constructs a CalendarManager with the specified CalendarDao.
     *
     * @param calendarDao the CalendarDao to use
     */
    public CalendarManager(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }

    public Calendar save(Calendar calendar) {
        return calendarDao.save(calendar);
    }

    public List<Calendar> readAll(long userId) {
        return calendarDao.readAll(userId);
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
}