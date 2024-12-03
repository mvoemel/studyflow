package ch.zhaw.studyflow.domain.studyplan.basicImpls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.Studyplan;

public class BasicStudyplan implements Studyplan {
    private static final Logger LOGGER = Logger.getLogger(BasicStudyplan.class.getName());
    private CalendarManagerImpl calendarManager;
    private ModuleManagerImpl moduleManager;
    private AppointmentManagerImpl appointmentManager;

    private long calendarId;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<DayOfWeek> daysOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    private List<StudyDay> studyDays;
    private Map<Module, List<StudyDay>> moduleStudyDays;

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
    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    @Override
    public List<StudyDay> getStudyDays() {
        return studyDays;
    }

    @Override
    public Map<Module, List<StudyDay>> getModuleStudyDays() {
        return moduleStudyDays;
    }

    public void createStudyDays(){
        LocalDate currentDate = startDate;
        
        while(currentDate.isBefore(endDate)){
            if(daysOfWeek.contains(currentDate.getDayOfWeek())){
                StudyDay studyDay = new BasicStudyDay(currentDate, startTime, endTime);
                studyDays.add(studyDay);

                //TODO: add appointments to studyDay
            }
            currentDate = currentDate.plusDays(1);
        }
    }

    public int calculateTotalMinutes(){
        int totalMinutes = 0;
        for(StudyDay studyDay : studyDays){
            studyDay.calculateMinutes();
            totalMinutes += studyDay.getMinutes();
        }
        return totalMinutes;
    }
    
}
