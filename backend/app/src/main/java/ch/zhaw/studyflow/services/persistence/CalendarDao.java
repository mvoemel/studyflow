package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.calendar.Calendar;

import java.util.List;

/**
 * Data access object for reading, writing and updating calendar from and to a persistent storage.
 */
public interface CalendarDao {

    /**
     * Writes a new calendar to the persistent storage and assigns an ID to it.
     *
     * @param calendar the calendar to create
     */
    void create(Calendar calendar);

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

    /**
     * Gets all calendars for a specific user.
     *
     * @param studentId the ID of the user
     * @return a list of calendars for the user
     */
    List<Calendar> readAllByStudent(long studentId);
}