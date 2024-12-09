package ch.zhaw.studyflow.domain.studyplan;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyplanAlgorithm;



public class BasicStudyplanAlgorithmTest {

    private BasicStudyplanAlgorithm algorithm;
    private StudyplanParameters parameters;
    private List<Appointment> appointments;
    private List<Module> modules;
    private List<ModuleAllocation> moduleAllocations;

    @BeforeEach
    public void setUp() {
        parameters = mock(StudyplanParameters.class);
        when(parameters.getStartDate()).thenReturn(LocalDate.of(2023, 1, 1)); // January 1, 2023 is a Sunday
        when(parameters.getEndDate()).thenReturn(LocalDate.of(2023, 1, 31));
        when(parameters.getDaysOfWeek()).thenReturn(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        when(parameters.getDayStartTime()).thenReturn(LocalTime.of(9, 0));
        when(parameters.getDayEndTime()).thenReturn(LocalTime.of(17, 0));

        appointments = new ArrayList<>();
        modules = new ArrayList<>();
        moduleAllocations = new ArrayList<>();
        algorithm = new BasicStudyplanAlgorithm(parameters, 1L, appointments, modules);
    }

    @AfterEach
    public void tearDown() {
        appointments.clear();
        modules.clear();
        moduleAllocations.clear();
    }

    public void mockAppointment(LocalDateTime startTime, LocalDateTime endTime) {
        Appointment appointment = mock(Appointment.class);
        when(appointment.getStartTime()).thenReturn(startTime);
        when(appointment.getEndTime()).thenReturn(endTime);
        appointments.add(appointment);
    }

    public void mockModule(long id, String name, long ects, long complexity, long time, long understanding) {
        Module module = mock(Module.class);
        when(module.getId()).thenReturn(id);
        when(module.getName()).thenReturn(name);
        when(module.getECTS()).thenReturn(ects);
        when(module.getComplexity()).thenReturn(complexity);
        when(module.getTime()).thenReturn(time);
        when(module.getUnderstanding()).thenReturn(understanding);
        modules.add(module);
    }

    public void mockModuleAllocation(Module module, long percentage, long targetMinutes) {
        ModuleAllocation moduleAllocation = mock(ModuleAllocation.class);
        when(moduleAllocation.getModuleId()).thenReturn(module.getId());
        when(moduleAllocation.getPercentage()).thenReturn(percentage);
        when(moduleAllocation.getTargetMinutes()).thenReturn(targetMinutes);
        moduleAllocations.add(moduleAllocation);
    }

    @Test
    public void testGetStudyDays() {
        mockModule(1L, "Module 1", 5L, 3L, 120L, 4L);
        algorithm.runAlgorithm();
        List<StudyDay> studyDays = algorithm.getStudyDays();
        assertEquals(13, studyDays.size()); // 13 days in January 2023 that are Monday, Wednesday, or Friday
    }

    @Test
    public void getTotalAvailableMinutes() {
        mockModule(1L, "Module", 4L, 3L, 120L, 4L);

        algorithm.runAlgorithm();
        assertEquals(5200, algorithm.getTotalAvailableMinutes());
    }

    @Test
    public void testCreateStudyDays() {

        mockAppointment(LocalDateTime.of(2023, 1, 4, 9, 0), LocalDateTime.of(2023, 1, 4, 10, 0));
        mockAppointment(LocalDateTime.of(2023, 1, 4, 14, 0), LocalDateTime.of(2023, 1, 4, 15, 0));
        mockAppointment(LocalDateTime.of(2023, 1, 6, 9, 0), LocalDateTime.of(2023, 1, 10, 10, 0));

        algorithm.createStudyDays();

        List<StudyDay> studyDays = algorithm.getStudyDays();
        assertEquals(13, studyDays.size());
        assertEquals(0, studyDays.get(0).getAppointments().size());
        assertEquals(2, studyDays.get(1).getAppointments().size());
        assertEquals(1, studyDays.get(2).getAppointments().size());
        assertEquals(1, studyDays.get(3).getAppointments().size());
        assertEquals(0, studyDays.get(4).getAppointments().size());

        //check if correct total minutes are calculated
        assertEquals(4930, algorithm.getTotalAvailableMinutes());
    }

    @Test
    public void testAllocateModules() {
        mockModule(1L, "Module 1", 4L, 3L, 2L, 4L);
        mockModule(2L, "Module 2", 2L, 3L, 2L, 4L);
 
        moduleAllocations = algorithm.runAlgorithm();
        assertEquals(2, moduleAllocations.size());
        assertEquals(3466, moduleAllocations.get(0).getAllocatedMinutes());
        assertEquals(1600, moduleAllocations.get(1).getAllocatedMinutes());
        
    }
}

