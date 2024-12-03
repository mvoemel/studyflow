package ch.zhaw.studyflow.domain.studyplan.impls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.calendar.impls.AppointmentManagerImpl;
import ch.zhaw.studyflow.domain.calendar.impls.CalendarManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.impls.ModuleManagerImpl;
import ch.zhaw.studyflow.domain.curriculum.imps.SemesterManagerImpl;
import ch.zhaw.studyflow.domain.studyplan.Studyplan;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyplan;


public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final SemesterManagerImpl semesterManager;
    private final ModuleManagerImpl moduleManager;
    private final CalendarManagerImpl calendarManager;
    private final AppointmentManagerImpl appointmentManager;

    private List<Studyplan> studyplans;

    public StudyplanManagerImpl(SemesterManagerImpl semesterManager, ModuleManagerImpl moduleManager, CalendarManagerImpl calendarManager, AppointmentManagerImpl appointmentManager) {
        this.semesterManager = semesterManager;
        this.moduleManager = moduleManager;
        this.calendarManager = calendarManager;
        this.appointmentManager = appointmentManager;
    }
    
    @Override
    public void createStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId){
        studyplans.add(new BasicStudyplan(startDate, endDate, daysOfWeek, startTime, endTime, calendarId));
        studyplans.getLast().setAppointments(appointmentManager.readAllBy(calendarId, startDate, endDate));
        studyplans.getLast().setModules(moduleManager.getModules(userId));

        
        
    }

    @Override
    public void updateStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId){

    }

    @Override
    public void deleteStudyplan(long id){

    }
}
