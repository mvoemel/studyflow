package ch.zhaw.studyflow.services.persistance.memory;

import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.services.persistance.CalendarDao;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the CalendarDao interface.
 * This class provides methods to perform CRUD operations on Calendar objects
 * stored in memory.
 */
public class MemoryCalendarDao implements CalendarDao {
    private final Map<Long, Map<Long, Calendar>> userCalendars = new HashMap<>();

    @Override
    public void create(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar cannot be null");
        }
        long newId = userCalendars.values().stream()
                .flatMap(map -> map.keySet().stream())
                .max(Long::compare)
                .orElse(0L) + 1;
        calendar.setId(newId);
        userCalendars.computeIfAbsent(calendar.getOwnerId(), k -> new HashMap<>()).put(calendar.getId(), calendar);
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
    public Calendar update(Calendar calendar) {
        Map<Long, Calendar> calendars = userCalendars.get(calendar.getOwnerId());
        if (calendars != null) {
            calendars.put(calendar.getId(), calendar);
        }
        return calendar;
    }

    @Override
    public long getCalendarId(Calendar calendar) {
        return calendar.getId();
    }

    @Override
    public void setCalendarId(Calendar calendar, long calendarId) {
        calendar.setId(calendarId);
    }
}