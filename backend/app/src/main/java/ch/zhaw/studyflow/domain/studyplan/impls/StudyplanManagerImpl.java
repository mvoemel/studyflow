package ch.zhaw.studyflow.domain.studyplan.impls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.logging.Logger;

import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.services.ServiceCollection;


public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final ServiceCollection serviceCollection;

    //threading: executor service for async tasks, limit nr of threads

    public StudyplanManagerImpl(ServiceCollection serviceCollection) {
        this.serviceCollection  = serviceCollection;
    }
    
    @Override
    public Long createStudyplan(LocalDate startDate, LocalDate endDate, List<DayOfWeek> daysOfWeek, LocalTime startTime, LocalTime endTime, long calendarId, long userId){

        return null;
        //return calendar id of the created studyplan
    }

}
