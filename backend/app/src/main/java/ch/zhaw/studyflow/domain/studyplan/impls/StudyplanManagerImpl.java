package ch.zhaw.studyflow.domain.studyplan.impls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.SemesterManagerImpl;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;


public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final SemesterManagerImpl semesterManager;
    private final ModuleManagerImpl moduleManager;
    private final CalendarManagerImpl calendarManager;
    private final AppointmentManagerImpl appointmentManager;

    

    public StudyplanManagerImpl(SemesterManagerImpl semesterManager, ModuleManagerImpl moduleManager, CalendarManagerImpl calendarManager, AppointmentManagerImpl appointmentManager) {
        this.semesterManager = semesterManager;
        this.moduleManager = moduleManager;
        this.calendarManager = calendarManager;
        this.appointmentManager = appointmentManager;
    }
    
    @Override
    public Long createStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId){

        return null;
        
    }



    //TODO: actually return a usable studyplan for frontend, aka create a new calendar?? or just return the appointments and modules?
    // I really need someone to explain to me how the frontend works with the backend, because I don't know how to return the data in a way that the frontend can use it
}
