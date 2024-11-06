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

    @Override
    public Calendar save(long userId, Calendar calendar) {
        userCalendars.computeIfAbsent(userId, k -> new HashMap<>()).put(calendar.getId(), calendar);
        return calendar;
    }

    @Override
    public List<Calendar> readAll(long userId) {
        return new ArrayList<>(userCalendars.getOrDefault(userId, new HashMap<>()).values());
    }

    @Override
    public Calendar read(long userId, long calendarId) {
        return userCalendars.getOrDefault(userId, new HashMap<>()).get(calendarId);
    }

    @Override
    public void delete(long userId, long calendarId) {
        Map<Long, Calendar> calendars = userCalendars.get(userId);
        if (calendars != null) {
            calendars.remove(calendarId);
        }
    }

    @Override
    public Calendar update(long userId, Calendar calendar) {
        Map<Long, Calendar> calendars = userCalendars.get(userId);
        if (calendars != null) {
            calendars.put(calendar.getId(), calendar);
        }
        return calendar;
    }
}