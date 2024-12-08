package ch.zhaw.studyflow.domain.studyplan;

import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicModuleAllocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BasicModuleAllocationTest {

    private BasicModuleAllocation moduleAllocation;
    private final long moduleId = 1L;
    private final long percentage = 50L;
    private final long targetMinutes = 120L;

    @BeforeEach
    public void setUp() {
        moduleAllocation = new BasicModuleAllocation(moduleId, percentage, targetMinutes);
    }

    @Test
    public void testGetModuleId() {
        assertEquals(moduleId, moduleAllocation.getModuleId());
    }

    @Test
    public void testGetPercentage() {
        assertEquals(percentage, moduleAllocation.getPercentage());
    }

    @Test
    public void testGetTargetMinutes() {
        assertEquals(targetMinutes, moduleAllocation.getTargetMinutes());
    }

    @Test
    public void testGetAllocatedMinutes() {
        assertEquals(0L, moduleAllocation.getAllocatedMinutes());
    }

    @Test
    public void testGetRemainingMinutes() {
        assertEquals(targetMinutes, moduleAllocation.getRemainingMinutes());
    }


    @Test
    public void testAllocate() throws Exception {
        BasicModuleAllocation moduleAllocation = spy(new BasicModuleAllocation(moduleId, percentage, targetMinutes));
        doReturn(new ArrayList<>()).when(moduleAllocation).getStudyDays();
        doAnswer(invocation -> {
            StudyDay day = invocation.getArgument(0);
            moduleAllocation.getStudyDays().add(day);
            Field allocatedMinutesField = BasicModuleAllocation.class.getDeclaredField("allocatedMinutes");
            allocatedMinutesField.setAccessible(true);
            long allocatedMinutes = allocatedMinutesField.getLong(moduleAllocation);
            if (moduleAllocation.getRemainingMinutes() >= day.getMinutes()) {
                allocatedMinutes += day.getMinutes();
            } else {
                allocatedMinutes += moduleAllocation.getRemainingMinutes();
            }
            allocatedMinutesField.setLong(moduleAllocation, allocatedMinutes);
            return null;
        }).when(moduleAllocation).allocate(any(StudyDay.class));

        StudyDay studyDay = mock(StudyDay.class);
        when(studyDay.getMinutes()).thenReturn(60L);

        moduleAllocation.allocate(studyDay);

        assertEquals(60L, moduleAllocation.getAllocatedMinutes());
        assertEquals(targetMinutes - 60L, moduleAllocation.getRemainingMinutes());
        List<StudyDay> studyDays = moduleAllocation.getStudyDays();
        assertEquals(1, studyDays.size());
        assertEquals(studyDay, studyDays.get(0));
    }

    @Test
    public void testCompareTo() {
        BasicModuleAllocation otherModuleAllocation = new BasicModuleAllocation(2L, 30L, 100L);
        assertTrue(moduleAllocation.compareTo(otherModuleAllocation) < 0);
    }
}