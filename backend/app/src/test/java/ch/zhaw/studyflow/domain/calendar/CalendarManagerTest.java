package ch.zhaw.studyflow.domain.calendar;

import ch.zhaw.studyflow.services.persistance.CalendarDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testCreate() {
        Calendar calendar = new Calendar(1, "Test Calendar", 1L);
        doNothing().when(calendarDao).create(calendar);

        calendarManager.create(calendar);
        verify(calendarDao).create(calendar);
    }

    @Test
    void testRead() {
        Calendar calendar = new Calendar(1, "Test Calendar", 1L);
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
        Calendar calendar = new Calendar(1, "Test Calendar", 1L);
        when(calendarDao.update(calendar)).thenReturn(calendar);

        Calendar updatedCalendar = calendarManager.update(calendar);
        assertEquals(calendar, updatedCalendar);
        verify(calendarDao).update(calendar);
    }

    @Test
    void testGetCalendarId() {
        Calendar calendar = new Calendar(1, "Test Calendar", 1L);
        when(calendarDao.getCalendarId(calendar)).thenReturn(1L);

        long calendarId = calendarManager.getCalendarId(calendar);
        assertEquals(1L, calendarId);
        verify(calendarDao).getCalendarId(calendar);
    }

    @Test
    void testSetCalendarId() {
        Calendar calendar = new Calendar(1, "Test Calendar", 1L);
        doNothing().when(calendarDao).setCalendarId(calendar, 2L);

        calendarManager.setCalendarId(calendar, 2L);
        assertEquals(2L, calendar.getId());
        verify(calendarDao).setCalendarId(calendar, 2L);
    }
}