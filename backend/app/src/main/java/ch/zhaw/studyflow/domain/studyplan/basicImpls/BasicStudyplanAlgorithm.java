package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.studyplan.ModuleAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.StudyplanAlgorithm;

/**
 * Basic implementation of the StudyplanAlgorithm interface.
 * This class represents the algorithm for creating and allocating study plans.
 */
public class BasicStudyplanAlgorithm implements StudyplanAlgorithm {
    private static final Logger LOGGER = Logger.getLogger(BasicStudyplanAlgorithm.class.getName());
    private long calendarId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<Appointment> appointments;
    private List<Module> modules;
    
    private List<ModuleAllocation> moduleAllocations;
    private List<StudyDay> studyDays;
    private int totalAvailableMinutes;

    /**
     * Constructs a BasicStudyplanAlgorithm with the specified parameters, calendar ID, appointments, and modules.
     *
     * @param parameters   the study plan parameters
     * @param calendarId   the ID of the calendar
     * @param appointments the list of appointments
     * @param modules      the list of modules
     */
    public BasicStudyplanAlgorithm(StudyplanParameters parameters, long calendarId, List<Appointment> appointments, List<Module> modules){ 
        this.calendarId = calendarId;
        this.startDate = parameters.getStartDate();
        this.endDate = parameters.getEndDate();
        this.daysOfWeek = parameters.getDaysOfWeek();
        this.startTime = parameters.getDayStartTime();
        this.endTime = parameters.getDayEndTime();

        this.appointments = appointments;
        this.modules = modules;

        this.moduleAllocations = new ArrayList<>();
        this.studyDays = new ArrayList<>();        
    }

    /**
     * Runs the study plan algorithm to create and allocate study plans.
     *
     * @return the list of module allocations
     */
    @Override
    public List<ModuleAllocation> runAlgorithm() {
        createStudyDays();
        allocateModules();
        return moduleAllocations;
    }

    /**
     * Returns the list of study days created by the algorithm.
     *
     * @return the list of study days
     */
    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }

    /**
     * Creates study days based on the specified parameters.
     */
    @Override
    public void createStudyDays(){
        LocalDate currentDate = startDate;
        
        while(currentDate.isBefore(endDate)){
            if(daysOfWeek.contains(currentDate.getDayOfWeek())){
                StudyDay studyDay = new BasicStudyDay(currentDate, startTime, endTime);
                studyDays.add(studyDay);

                //add appointments to studyDay
                for(Appointment appointment : appointments){
                    if(appointment.getStartTime().toLocalDate().equals(currentDate)){
                        studyDay.addAppointment(appointment);
                    }
                }
                studyDay.calculateStudyAllocations();
                studyDay.calculateMinutes();
            }
            currentDate = currentDate.plusDays(1);
        }
        calculateTotalAvailableMinutes();
    }

    /**
     * Allocates modules to the created study days.
     */
    @Override
    public void allocateModules(){
        calculateModulePercentages();

        //sort StudyDays by available minutes
        Collections.sort(studyDays);

        //allocate studyDays to modules 
        for(StudyDay studyDay : studyDays){
            //sort modules by remaining minutes
            Collections.sort(moduleAllocations);
            //allocate module to studyDay
            ModuleAllocation moduleAllocation = moduleAllocations.get(0);
            moduleAllocation.allocate(studyDay);
        }
    }

    /**
     * Calculates the total number of minutes available for study.
     *
     * @return the total number of minutes
     */
    private int calculateTotalAvailableMinutes(){
        int totalMinutes = 0;
        for(StudyDay studyDay : studyDays){
            totalMinutes += studyDay.getMinutes();
        }
        return totalMinutes;
    }

    /**
     * Calculates the percentages of study time allocated to each module.
     */
    private void calculateModulePercentages(){
        long moduleSum = 0;
        Map<Module, Long> moduleInitValues = new HashMap<>();
        //calculate percentage from complexity, understanding and time (and ECTS)
        for(Module module : modules){
            long initValue = (module.getComplexity() + module.getUnderstanding() + module.getTime())*module.getECTS();
            moduleInitValues.put(module, initValue);
            moduleSum += initValue;
        }

        //normalize percentages and calculate minutes
        for(Module module : modules){
            long percentage = moduleInitValues.get(module);
            moduleAllocations.add(new BasicModuleAllocation(module.getId(), percentage*100/moduleSum, percentage*totalAvailableMinutes/moduleSum));
        }
        
    }
    
}
