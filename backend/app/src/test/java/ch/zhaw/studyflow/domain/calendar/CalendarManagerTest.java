package ch.zhaw.studyflow.domain.calendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarManagerTest {

    private CalendarManager calendarManager;
    private CalendarDao calendarDao;

    @BeforeEach
    void setUp() {
        calendarDao = mock(CalendarDao.class);
        calendarManager = new CalendarManager(calendarDao);
    }

    @Test
    void testSave() {
        Calendar calendar = new Calendar(1, "Test Calendar");
        when(calendarDao.save(calendar)).thenReturn(calendar);

        Calendar savedCalendar = calendarManager.save(calendar);
        assertEquals(calendar, savedCalendar);
        verify(calendarDao).save(calendar);
    }

    @Test
    void testReadAll() {
        Calendar calendar1 = new Calendar(1, "Test Calendar 1");
        Calendar calendar2 = new Calendar(2, "Test Calendar 2");
        when(calendarDao.readAll(1L)).thenReturn(List.of(calendar1, calendar2));

        List<Calendar> calendars = calendarManager.readAll(1L);
        assertEquals(2, calendars.size());
        assertTrue(calendars.contains(calendar1));
        assertTrue(calendars.contains(calendar2));
        verify(calendarDao).readAll(1L);
    }

    @Test
    void testRead() {
        Calendar calendar = new Calendar(1, "Test Calendar");
        when(calendarDao.read(1L, 1L)).thenReturn(calendar);

        Calendar readCalendar = calendarManager.read(1L, 1L);
        assertEquals(calendar, readCalendar);
        verify(calendarDao).read(1L, 1L);
    }

    @Test
    void testDelete() {
        doNothing().when(calendarDao).delete(1L, 1L);

        calendarManager.delete(1L, 1L);
        verify(calendarDao).delete(1L, 1L);
    }

    @Test
    void testUpdate() {
        Calendar calendar = new Calendar(1, "Test Calendar");
        when(calendarDao.update(calendar)).thenReturn(calendar);

        Calendar updatedCalendar = calendarManager.update(calendar);
        assertEquals(calendar, updatedCalendar);
        verify(calendarDao).update(calendar);
    }
}