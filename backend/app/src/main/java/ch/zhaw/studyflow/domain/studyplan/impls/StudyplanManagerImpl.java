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
        //hier frage wie wir am besten die Daten von Modules und Appointments weitergeben, so ist es irgendwie etwas unschön
        //plus check ii ned ganz wie da mitm controller funktioniert det wirds ja anders instanziiert und joa?
        
        
    }

    @Override
    public void updateStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId){
        //Info: wollte das hier eigentlich beim Interface so drin haben damit wir eben im Bericht bei den Results argumentieren können, dass wir diese Funktionalität noch einbauen wollen
        //Wir sagen nämlich überall dass die Lernpläne dynamisch sind und wir sie anpassen können, aber das ist noch nicht implementiert
        //daher auch die List<Studyplan> studyplans; oben, die wir noch nicht verwenden, soo könnte der User auch auf vorherige Lernpläne wieder zugreifen oder verschiedene Arten von Lernplänen generieren
    }

    @Override
    public void deleteStudyplan(long id){
        
    }

    //TODO: actually return a usable studyplan for frontend, aka create a new calendar?? or just return the appointments and modules?
    // I really need someone to explain to me how the frontend works with the backend, because I don't know how to return the data in a way that the frontend can use it
}
