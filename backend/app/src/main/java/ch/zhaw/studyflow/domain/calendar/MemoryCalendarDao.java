package ch.zhaw.studyflow.domain.calendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the CalendarDao interface.
 * This class provides methods to perform CRUD operations on Calendar objects
 * stored in memory.
 */
public class MemoryCalendarDao implements CalendarDao {
    private final Map<Long, Map<Long, Calendar>> userCalendars = new HashMap<>();

    /**
     * Saves a calendar to the in-memory storage.
     *
     * @param calendar the calendar to save
     * @return the saved calendar
     */
    @Override
    public Calendar save(Calendar calendar) {
        userCalendars.computeIfAbsent(calendar.getId(), k -> new HashMap<>()).put(calendar.getId(), calendar);
        return calendar;
    }

    /**
     * Reads all calendars for a specific user from the in-memory storage.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the specified user
     */
    @Override
    public List<Calendar> readAll(long userId) {
        return new ArrayList<>(userCalendars.getOrDefault(userId, new HashMap<>()).values());
    }

    /**
     * Reads a specific calendar for a user from the in-memory storage.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return the calendar with the specified ID for the specified user, or null if not found
     */
    @Override
    public Calendar read(long userId, long calendarId) {
        return userCalendars.getOrDefault(userId, new HashMap<>()).get(calendarId);
    }

    /**
     * Deletes a specific calendar for a user from the in-memory storage.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar to delete
     */
    @Override
    public void delete(long userId, long calendarId) {
        Map<Long, Calendar> calendars = userCalendars.get(userId);
        if (calendars != null) {
            calendars.remove(calendarId);
        }
    }

    /**
     * Updates a calendar in the in-memory storage.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    @Override
    public Calendar update(Calendar calendar) {
        Map<Long, Calendar> calendars = userCalendars.get(calendar.getId());
        if (calendars != null) {
            calendars.put(calendar.getId(), calendar);
        }
        return calendar;
    }
}