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

    /**
     * Saves a calendar for a specific user.
     *
     * @param userId the ID of the user
     * @param calendar the calendar to save
     * @return the saved calendar
     */
    public Calendar save(long userId, Calendar calendar) {
        return calendarDao.save(userId, calendar);
    }

    /**
     * Reads all calendars for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the specified user
     */
    public List<Calendar> readAll(long userId) {
        return calendarDao.readAll(userId);
    }

    /**
     * Reads a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return the calendar with the specified ID for the specified user, or null if not found
     */
    public Calendar read(long userId, long calendarId) {
        return calendarDao.read(userId, calendarId);
    }

    /**
     * Deletes a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar to delete
     */
    public void delete(long userId, long calendarId) {
        calendarDao.delete(userId, calendarId);
    }

    /**
     * Updates a calendar for a specific user.
     *
     * @param userId the ID of the user
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    public Calendar update(long userId, Calendar calendar) {
        return calendarDao.update(userId, calendar);
    }
}