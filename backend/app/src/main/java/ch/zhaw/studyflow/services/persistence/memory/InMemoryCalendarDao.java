package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.services.persistence.CalendarDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * In-memory implementation of the CalendarDao interface.
 * This class provides methods to perform CRUD operations on Calendar objects
 * stored in memory.
 */
public class InMemoryCalendarDao implements CalendarDao {
    private final AtomicInteger idCounter = new AtomicInteger(0);
    private final Map<Long, Map<Long, Calendar>> userCalendars = new HashMap<>();

    @Override
    public void create(Calendar calendar) {

        if (calendar == null) {
            throw new IllegalArgumentException("Calendar cannot be null");
        }
        calendar.setId(idCounter.getAndIncrement());
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
    public List<Calendar> readAllByStudent(long studentId) {
        return new ArrayList<>(userCalendars.getOrDefault(studentId, new HashMap<>()).values());
    }
}