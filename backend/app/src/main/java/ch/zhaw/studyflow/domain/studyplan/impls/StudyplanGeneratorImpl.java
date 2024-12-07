package ch.zhaw.studyflow.domain.studyplan.impls;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.domain.calendar.AppointmentManager;
import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.domain.curriculum.ModuleManager;
import ch.zhaw.studyflow.domain.studyplan.StudyDay;
import ch.zhaw.studyflow.domain.studyplan.StudyplanGenerator;
import ch.zhaw.studyflow.domain.studyplan.basicImpls.BasicStudyplanAlgorithm;
import ch.zhaw.studyflow.services.ServiceCollection;

public class StudyplanGeneratorImpl implements StudyplanGenerator {
    private static final Logger LOGGER = Logger.getLogger(StudyplanGeneratorImpl.class.getName());

    private final StudyplanParameters parameters;
    private final ServiceCollection serviceCollection;
    private final long userId;

    //threading: executor service for async tasks, limit nr of threads

    public StudyplanGeneratorImpl(StudyplanParameters parameters, ServiceCollection serviceCollection, long userId) {
        this.parameters = parameters;
        this.serviceCollection  = serviceCollection;
        this.userId = userId;
    }
    
    //future possibility: add parameter "algorithm" to create different studyplans :)
    @Override
    public Long generateStudyplan(){
                //fetch information needed to create studyplan
        List<Module> modules;
        List<Appointment> appointments;
        long globalCalendarId = 0; //TODO: get global calendar id from settings

        Optional<ModuleManager> moduleManager = serviceCollection.getService(ModuleManager.class);
        if(moduleManager.isPresent()){
            modules = moduleManager.get().getModules(userId);
        } else {
            LOGGER.warning("ModuleManager not available");
            return null; //TODO: return error code? or what do we do?
        }

        Optional<AppointmentManager> appointmentManager = serviceCollection.getService(AppointmentManager.class);
        if(appointmentManager.isPresent()){
            appointments = appointmentManager.get().readAllBy(globalCalendarId, parameters.getStartDate(), parameters.getEndDate());
        } else {
            LOGGER.warning("AppointmentManager not available");
            return null; //TODO: return error code? or what do we do?
        }

        //call algorithm to create studyplan
        BasicStudyplanAlgorithm algorithm = new BasicStudyplanAlgorithm(parameters, globalCalendarId, appointments, modules);

        //return calendar id of the created studyplan
        return createCalendar(algorithm.getModuleStudyDays());
    }

    public Long createCalendar(Map<Module, List<StudyDay>> moduleStudyDays){
        //TODO: implement
        return null;
    }
    
}
