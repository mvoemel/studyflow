package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

/**
 * Interface for managing calendar data.
 */
public interface CalendarDao {

    /**
     * Saves a calendar.
     *
     * @param calendar the calendar to save
     * @return the saved calendar
     */
    Calendar save(Calendar calendar);

    /**
     * Reads all calendars for a user.
     *
     * @param userId the ID of the user
     * @return a list of calendars
     */
    List<Calendar> readAll(long userId);

    /**
     * Reads a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return the calendar, or null if not found
     */
    Calendar read(long userId, long calendarId);

    /**
     * Deletes a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     */
    void delete(long userId, long calendarId);

    /**
     * Updates a calendar.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    Calendar update(Calendar calendar);
}