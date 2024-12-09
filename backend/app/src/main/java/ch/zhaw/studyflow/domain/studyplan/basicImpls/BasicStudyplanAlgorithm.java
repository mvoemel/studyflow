package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
    private final long calendarId;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<DayOfWeek> daysOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    private final List<Appointment> appointments;
    private final List<Module> modules;
    
    private final List<ModuleAllocation> moduleAllocations;
    private final List<StudyDay> studyDays;
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
     * Returns the total number of available minutes for study.
     *
     * @return the total number of minutes
     */
    @Override
    public int getTotalAvailableMinutes() {
        return totalAvailableMinutes;
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
                    if(appointment.getStartTime().toLocalDate().equals(currentDate) || appointment.getEndTime().toLocalDate().equals(currentDate)){
                        studyDay.addAppointment(appointment);
                    } else if(appointment.getStartTime().toLocalDate().isBefore(currentDate) && appointment.getEndTime().toLocalDate().isAfter(currentDate)){
                        studyDay.addAppointment(appointment);
                    }
                }
                studyDay.calculateStudyAllocations();
                studyDay.calculateMinutes();
            }
            currentDate = currentDate.plusDays(1);
        }
        calculateTotalAvailableMinutes();
        LOGGER.log(Level.INFO, "Created {0} study days.", studyDays.size());
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
        LOGGER.log(Level.INFO, "Allocated modules to study days.");
    }

    /**
     * Calculates the total number of minutes available for study.
     *
     * @return the total number of minutes
     */
    private void calculateTotalAvailableMinutes(){
        int totalMinutes = 0;
        for(StudyDay studyDay : studyDays){
            totalMinutes += studyDay.getMinutes();
        }
        this.totalAvailableMinutes = totalMinutes;
        LOGGER.log(Level.INFO, "Total available minutes: {0}", totalMinutes);
    }

    /**
     * Calculates the percentages of study time allocated to each module.
     */
    private void calculateModulePercentages(){
        long moduleSum = 0;
        Map<Module, Long> moduleInitValues = new HashMap<>();
        //calculate percentage from complexity, understanding and time (and ECTS)
        for(Module module : modules){
            long initValue = (module.getComplexity() + (10 - module.getUnderstanding()) + module.getTime())*module.getECTS();
            moduleInitValues.put(module, initValue);
            moduleSum += initValue;
        }

        //normalize percentages and calculate minutes
        for(Module module : modules){
            long percentage = moduleInitValues.get(module);
            moduleAllocations.add(new BasicModuleAllocation(module.getId(), percentage*100/moduleSum, percentage*totalAvailableMinutes/moduleSum));
        }
        LOGGER.log(Level.INFO, "Calculated module percentages.");
    } 
}
