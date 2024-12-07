package ch.zhaw.studyflow.domain.studyplan.impls;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.studyplan.StudyplanGenerator;
import ch.zhaw.studyflow.domain.studyplan.StudyplanManager;
import ch.zhaw.studyflow.services.ServiceCollection;


public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final ServiceCollection serviceCollection;
    private final ThreadPoolExecutor executor;

    //threading: executor service for async tasks, limit nr of threads

    public StudyplanManagerImpl(ServiceCollection serviceCollection) {
        this.serviceCollection  = serviceCollection;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        this.executor = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, workQueue);
    }
    
    //future possibility: add parameter "algorithm" to create different studyplans :)
    @Override
    public Long createStudyplan(StudyplanParameters parameters, ServiceCollection serviceCollection, long userId){  
        //create studyplanGenerator which handles the creation of the studyplan
        Callable<Long> task  = () -> {
            StudyplanGenerator studyplanGenerator = new StudyplanGeneratorImpl(parameters, serviceCollection, userId);
            return studyplanGenerator.generateStudyplan();
        };

        Future<Long> studyCalendarId = executor.submit(task);
        
        try {
            return studyCalendarId.get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.warning("Studyplan creation failed: " + e.getMessage());
            return null;
        }
        //return calendar id of the created studyplan
    }



}
