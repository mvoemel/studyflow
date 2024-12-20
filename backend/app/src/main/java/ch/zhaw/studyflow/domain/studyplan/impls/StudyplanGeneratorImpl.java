package ch.zhaw.studyflow.domain.studyplan.impls;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.calendar.Calendar;
import ch.zhaw.studyflow.domain.calendar.CalendarManager;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.student.Settings;
import ch.zhaw.studyflow.domain.student.StudentManager;
import ch.zhaw.studyflow.domain.studyplan.ModuleAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyAllocation;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.StudyplanGenerator;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyplanAlgorithm;
import ch.zhaw.studyflow.services.ServiceCollection;

/**
 * Implementation of the StudyplanGenerator interface.
 * This class is responsible for generating study plans based on the provided parameters, services, and user ID.
 */
public class StudyplanGeneratorImpl implements StudyplanGenerator {
    private static final Logger LOGGER = Logger.getLogger(StudyplanGeneratorImpl.class.getName());

    private final StudyplanParameters parameters;
    private final ServiceCollection serviceCollection;
    private final long userId;

    /**
     * Constructs a StudyplanGeneratorImpl with the specified parameters, service collection, and user ID.
     *
     * @param parameters       the study plan parameters
     * @param serviceCollection the collection of services required for generating the study plan
     * @param userId           the ID of the user for whom the study plan is being generated
     */
    //threading: executor service for async tasks, limit nr of threads
    public StudyplanGeneratorImpl(StudyplanParameters parameters, ServiceCollection serviceCollection, long userId) {
        this.parameters = parameters;
        this.serviceCollection  = serviceCollection;
        this.userId = userId;
    }

    /**
     * Generates a study plan based on the provided parameters and user ID.
     *
     * @return the ID of the created study plan calendar, or null if an error occurs
     */
    //future possibility: add parameter "algorithm" to create different studyplans :)
    @Override
    public long generateStudyplan(){
        long globalCalendarId = getGlobalCalendarId(userId);
        List<Module> modules = getModules();
        List<Appointment> appointments = getAppointments(globalCalendarId);

        //call algorithm to create studyplan
        BasicStudyplanAlgorithm algorithm = new BasicStudyplanAlgorithm(parameters, globalCalendarId, appointments, modules);
        List<ModuleAllocation> moduleAllocations = algorithm.runAlgorithm();
        long studyplanCalendarId = createCalendar();
        createAppointments(studyplanCalendarId, moduleAllocations);

        //return calendar id of the created studyplan
        return studyplanCalendarId;
    }

    /**
     * Retrieves the global calendar ID for the specified user.
     *
     * @param userId the ID of the user
     * @return the global calendar ID
     */
    private long getGlobalCalendarId(long userId){
        Optional<StudentManager> studentManager = serviceCollection.getService(StudentManager.class);
        if(studentManager.isPresent()){
            Optional<Settings> settings = studentManager.get().getSettings(userId);
            if(settings.isPresent()){
                return settings.get().getGlobalCalendarId();
            } else {
                LOGGER.warning("Settings not available");
                return -1; 
            }
        } else {
            LOGGER.warning("StudentManager not available");
            return -1; 
        }
    }

    /**
     * Retrieves the modules for the specified user.
     *
     * @param userId the ID of the user
     * @return the list of modules
     */
    private List<Module> getModules(){
        Optional<ModuleManager> moduleManager = serviceCollection.getService(ModuleManager.class);
        if(moduleManager.isPresent()){
            return moduleManager.get().getModulesBySemester(parameters.getSemesterId());
        } else {
            LOGGER.warning("ModuleManager not available");
            return null; 
        }
    }

    /**
     * Retrieves the appointments for the specified global calendar.
     *
     * @param globalCalendarId the ID of the global calendar
     * @return the list of appointments
     */
    private List<Appointment> getAppointments(long globalCalendarId){
        Optional<AppointmentManager> appointmentManager = serviceCollection.getService(AppointmentManager.class);
        if(appointmentManager.isPresent()){
            return appointmentManager.get().readAllBy(globalCalendarId, parameters.getStartDate(), parameters.getEndDate());
        } else {
            LOGGER.warning("AppointmentManager not available");
            return null; 
        }
    }

    /**
     * Creates a calendar for the study plan.
     *
     * @param moduleAllocations the list of module allocations
     * @return the ID of the created calendar, or -1 if an error occurs
     */
    @Override
    public long createCalendar(){
        Optional<CalendarManager> calendarManager = serviceCollection.getService(CalendarManager.class);
        if(calendarManager.isPresent()){
            Calendar calendar = new Calendar();
            calendarManager.get().create(calendar);
            return calendar.getId();
        } else {
            LOGGER.warning("CalendarManager not available");
            return -1; 
        }
    }

    /**
     * Creates appointments for the study plan based on the module allocations.
     *
     * @param calendarId        the ID of the calendar
     * @param moduleAllocations the list of module allocations
     */
    @Override
    public void createAppointments(long calendarId, List<ModuleAllocation> moduleAllocations){
        Optional<AppointmentManager> appointmentManager = serviceCollection.getService(AppointmentManager.class);
        Optional<ModuleManager> moduleManager = serviceCollection.getService(ModuleManager.class);
        
        if(appointmentManager.isPresent() && moduleManager.isPresent()){
            for(ModuleAllocation moduleAllocation : moduleAllocations){
                Optional<Module> module = moduleManager.get().getModule(moduleAllocation.getModuleId());
                if(module.isPresent()){
                    for(StudyDay day : moduleAllocation.getStudyDays()){
                        for(StudyAllocation allocation : day.getStudyAllocations()){
                                Appointment appointment = new Appointment(allocation.getStartTime(), allocation.getEndTime(), calendarId, module.get().getName(), module.get().getDescription());
                                appointmentManager.get().create(appointment);
                        }
                    }
                } else {
                        LOGGER.warning("Module not available");
                }
            }     
        } else {
            LOGGER.warning("AppointmentManager or ModuleManager not available");
        }
    } 
    
}
