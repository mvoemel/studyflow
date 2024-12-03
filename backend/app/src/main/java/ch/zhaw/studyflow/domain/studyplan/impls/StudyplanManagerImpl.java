package ch.zhaw.studyflow.domain.studyplan.impls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.domain.studyplan.Studyplan;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyplan;


public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final CalendarManagerImpl calendarManager;
    private final ModuleManagerImpl moduleManager;
    private final AppointmentManagerImpl appointmentManager;

    private List<Studyplan> studyplans;

    public StudyplanManagerImpl(CalendarManagerImpl calendarManager, ModuleManagerImpl moduleManager, AppointmentManagerImpl appointmentManager) {
        this.calendarManager = calendarManager;
        this.moduleManager = moduleManager;
        this.appointmentManager = appointmentManager;
    }
    
    @Override
    public void createStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId){
        studyplans.add(new BasicStudyplan(startDate, endDate, daysOfWeek, startTime, endTime, calendarId));
        
    }

    @Override
    public void updateStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId){

    }

    @Override
    public void deleteStudyplan(long id){

    }
}
