package ch.zhaw.studyflow.domain.calendar;

import java.util.List;

/**
 * Interface for managing calendar data.
 */
public interface CalendarDao {

    /**
     * Creates a new calendar.
     *
     * @param calendar the calendar to create
     * @return the created calendar
     */
    Calendar create(Calendar calendar);

    /**
     * Reads a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param id the ID of the calendar
     * @return the calendar, or null if not found
     */
    Calendar read(long userId, long id);

    /**
     * Deletes a specific calendar for a user.
     *
     * @param userId the ID of the user
     * @param id the ID of the calendar
     */
    void delete(long userId, long id);

    /**
     * Updates a calendar.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    Calendar update(Calendar calendar);

    /**
     * Gets the calendar ID.
     *
     * @param calendar the calendar
     * @return the calendar ID
     */
    long getCalendarId(Calendar calendar);

    /**
     * Sets the calendar ID.
     *
     * @param calendar the calendar
     * @param calendarId the new calendar ID
     */
    void setCalendarId(Calendar calendar, long calendarId);
}