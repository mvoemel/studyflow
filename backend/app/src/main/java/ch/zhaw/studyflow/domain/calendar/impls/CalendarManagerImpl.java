package ch.zhaw.studyflow.domain.calendar.impls;

import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.services.persistence.CalendarDao;

import java.util.List;

/**
 * Manages calendar operations such as creating, reading, updating, and deleting calendars.
 * This class acts as a service layer between the controller and the data access object (DAO).
 */
public class CalendarManagerImpl implements CalendarManager {
    private final CalendarDao calendarDao;

    /**
     * Constructs a CalendarManager with the specified CalendarDao.
     *
     * @param calendarDao the data access object for calendars
     */
    public CalendarManagerImpl(CalendarDao calendarDao) {
        this.calendarDao = calendarDao;
    }

    /**
     * Creates a new calendar.
     *
     * @param calendar the calendar to create
     */
    @Override
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
    @Override
    public Calendar read(long userId, long calendarId) {
        return calendarDao.read(userId, calendarId);
    }

    /**
     * Deletes a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     */
    @Override
    public void delete(long userId, long calendarId) {
        calendarDao.delete(userId, calendarId);
    }

    /**
     * Updates a calendar.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    @Override
    public Calendar update(Calendar calendar) {
        return calendarDao.update(calendar);
    }

    /**
     * Gets all calendars for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the user
     */
    @Override
    public List<Calendar> getCalendarsByUserId(long userId) {
        return calendarDao.readAllByUserId(userId);
    }
}