package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.calendar.Calendar;

import java.util.List;

/**
 * Interface for managing calendar data.
 */
public interface CalendarDao {

    /**
     * Creates a new calendar.
     *
     * @param calendar the calendar to create
     */
    void create(Calendar calendar);

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
     * Gets all calendars for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the user
     */
    List<Calendar> readAllByUserId(long userId);
}