package ch.zhaw.studyflow.domain.calendar;

import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.services.persistence.CalendarDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarManagerTest {

    private CalendarManagerImpl calendarManager;
    private CalendarDao calendarDao;

    @BeforeEach
    void setUp() {
        calendarDao = mock(CalendarDao.class);
        calendarManager = new CalendarManagerImpl(calendarDao);
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

}