package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

/**
 * Manages calendar operations such as creating, reading, updating, and deleting calendars.
 * This class acts as a service layer between the controller and the data access object (DAO).
 */
public class CalendarManager {
    private final CalendarDao calendarDao;

    /**
     * Constructs a CalendarManager with the specified CalendarDao.
     *
     * @param calendarDao the data access object for calendars
     */
    public CalendarManager(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }

    /**
     * Creates a new calendar.
     *
     * @param calendar the calendar to create
     */
    public void create(Calendar calendar) {
        calendarDao.create(calendar);
    }

    /**
     * Reads a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return the calendar, or null if not found
     */
    public Calendar read(long userId, long calendarId) {
        return calendarDao.read(userId, calendarId);
    }

    /**
     * Deletes a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     */
    public void delete(long userId, long calendarId) {
        calendarDao.delete(userId, calendarId);
    }

    /**
     * Updates a calendar.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    public Calendar update(Calendar calendar) {
        return calendarDao.update(calendar);
    }

    /**
     * Gets all calendars for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the user
     */
    public List<Calendar> getCalendarsByUserId(long userId) {
        return calendarDao.getAllByUserId(userId);
    }
}