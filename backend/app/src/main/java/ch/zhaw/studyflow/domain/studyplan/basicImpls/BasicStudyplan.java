package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.Studyplan;

public class BasicStudyplan implements Studyplan {
    private static final Logger LOGGER = Logger.getLogger(BasicStudyplan.class.getName());
    private long calendarId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<Appointment> appointments;
    private List<ch.zhaw.studyflow.domain.curriculum.Module> modules;

    private List<StudyDay> studyDays;
    private Map<ch.zhaw.studyflow.domain.curriculum.Module, List<StudyDay>> moduleStudyDays;

    public BasicStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId) {
        this.calendarId = calendarId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.daysOfWeek = daysOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;

        this.studyDays = new ArrayList<>();
        this.moduleStudyDays = new HashMap<>();
        
    }

    @Override
    public long getCalendarId() {
        return calendarId;
    }

    @Override
    public void setCalendarId(long calendarId) {
        this.calendarId = calendarId;
    }

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public LocalTime getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalTime getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public List<Appointment> getAppointments() {
        return appointments;
    }

    @Override
    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public List<ch.zhaw.studyflow.domain.curriculum.Module> getModules() {
        return modules;
    }

    @Override
    public void setModules(List<ch.zhaw.studyflow.domain.curriculum.Module> modules) {
        this.modules = modules;
    }

    @Override
    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }

    @Override
    public Map<ch.zhaw.studyflow.domain.curriculum.Module, List<StudyDay>> getModuleStudyDays() {
        return moduleStudyDays;
    }

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
            studyDay.calculateMinutes();
            totalMinutes += studyDay.getMinutes();
        }
        return totalMinutes;
    }

    private void calculateModulePercentages(){
        //calculate percentage from importance, understanding and time value
        //TODO: how do we store that? just as a map/list in the studyplan, do we make a new class, do we add that to the module class?
        //personally I would store it in the studyplan but idk looking at michaels code he has a class for that
    }

    private void allocateModules(){
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
