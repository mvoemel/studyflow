package ch.zhaw.studyflow.domain.studyplan;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyAllocation;


public class BasicStudyAllocationTest {
    private BasicStudyAllocation allocation;

    @BeforeEach
    public void setUp() {
        allocation = new BasicStudyAllocation(
                LocalDateTime.of(2023, 10, 1, 9, 0),
                LocalDateTime.of(2023, 10, 1, 11, 0)
        );
    }

    @Test
    public void testGetStartTime() {
        assertEquals(LocalDateTime.of(2023, 10, 1, 9, 0), allocation.getStartTime());
    }

    @Test
    public void testSetStartTime() {
        LocalDateTime newStartTime = LocalDateTime.of(2023, 10, 1, 8, 0);
        allocation.setStartTime(newStartTime);
        assertEquals(newStartTime, allocation.getStartTime());
    }

    @Test
    public void testGetEndTime() {
        assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0), allocation.getEndTime());
    }

    @Test
    public void testSetEndTime() {
        LocalDateTime newEndTime = LocalDateTime.of(2023, 10, 1, 12, 0);
        allocation.setEndTime(newEndTime);
        assertEquals(newEndTime, allocation.getEndTime());
    }

    @Test
    public void testGetMinutes() {
        assertEquals(120, allocation.getMinutes());
    }

    @Test
    public void testConstructorWithDate() {
        BasicStudyAllocation allocationWithDate = new BasicStudyAllocation(
                LocalTime.of(9, 0),
                LocalTime.of(11, 0),
                LocalDate.of(2023, 10, 1)
        );
        assertEquals(LocalDateTime.of(2023, 10, 1, 9, 0), allocationWithDate.getStartTime());
        assertEquals(LocalDateTime.of(2023, 10, 1, 11, 0), allocationWithDate.getEndTime());
    }
}

