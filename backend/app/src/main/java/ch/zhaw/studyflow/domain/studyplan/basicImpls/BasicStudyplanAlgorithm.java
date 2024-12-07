package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.StudyplanAlgorithm;

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
    
    private Map<Module, Long> modulePercentages;
    private List<StudyDay> studyDays;
    private Map<Module, List<StudyDay>> moduleStudyDays;

    public BasicStudyplanAlgorithm(StudyplanParameters parameters, long calendarId, List<Appointment> appointments, List<Module> modules){ 
        this.calendarId = calendarId;
        this.startDate = parameters.getStartDate();
        this.endDate = parameters.getEndDate();
        this.daysOfWeek = parameters.getDaysOfWeek();
        this.startTime = parameters.getDayStartTime();
        this.endTime = parameters.getDayEndTime();

        this.appointments = appointments;
        this.modules = modules;

        this.modulePercentages = new HashMap<>();
        this.studyDays = new ArrayList<>();
        this.moduleStudyDays = new HashMap<>();
        
    }

    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }

    @Override
    public Map<Module, List<StudyDay>> getModuleStudyDays() {
        return moduleStudyDays;
    }

    //TODO: part of interface?
    private void createStudyDays(){
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
            }
            currentDate = currentDate.plusDays(1);
        }
    }


    private int calculateTotalMinutes(){
        int totalMinutes = 0;
        for(StudyDay studyDay : studyDays){
            totalMinutes += studyDay.getMinutes();
        }
        return totalMinutes;
    }

    private void calculateModulePercentages(){
        long moduleSum = 0;
        Map<Module, Long> moduleInitValues = new HashMap<>();
        //calculate percentage from complexity, understanding and time (and ECTS)
        //TODO: how to calculate percentage? multiply or add complexity value?
        for(Module module : modules){
            long initValue = (module.getComplexity() + module.getUnderstanding() + module.getTime())*module.getECTS();
            moduleInitValues.put(module, initValue);
            moduleSum += initValue;
        }

        //normalize percentages
        for(Module module : modules){
            long percentage = moduleInitValues.get(module);
            modulePercentages.put(module, percentage/moduleSum);
        }
        
    }

    private void allocateModules(){
        //TODO: implement
        //allocate modules to studyDays
        //use michaels concept:

        /*
         public static Map<String, List<String>> createStudyPlan(List<Day> days, List<Module> modules) {
        // Step 1: Calculate total hours available
        int totalHours = days.stream().mapToInt(day -> day.hours).sum();

        // Step 2: Calculate target hours for each module
        List<ModuleAllocation> moduleAllocations = new ArrayList<>();
        for (Module module : modules) {
            double targetHours = (module.percentage / 100) * totalHours;
            moduleAllocations.add(new ModuleAllocation(module.module, targetHours));
        }

        // Step 3: Sort days by hours in descending order
        days.sort((a, b) -> Integer.compare(b.hours, a.hours));

        // Step 4: Allocate days to modules
        for (Day day : days) {
            // Sort modules by unmet need (target - allocated)
            moduleAllocations.sort((a, b) -> Double.compare(
                (b.targetHours - b.allocatedHours), 
                (a.targetHours - a.allocatedHours)
            ));

            // Assign the day to the module with the highest unmet need
            ModuleAllocation selectedModule = moduleAllocations.get(0);
            selectedModule.allocatedHours += day.hours;
            selectedModule.days.add(day.date);
        }

        // Step 5: Build the final study plan
        Map<String, List<String>> studyPlan = new HashMap<>();
        for (ModuleAllocation allocation : moduleAllocations) {
            studyPlan.put(allocation.module, allocation.days);
        }

        return studyPlan;
    }
         */
    }


    
}
