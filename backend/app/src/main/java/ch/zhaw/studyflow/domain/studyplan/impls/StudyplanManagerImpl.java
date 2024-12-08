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

/**
 * Implementation of the StudyplanManager interface.
 * This class is responsible for managing the creation of study plans using a thread pool executor.
 */
public class StudyplanManagerImpl implements StudyplanManager {
    private static final Logger LOGGER = Logger.getLogger(StudyplanManagerImpl.class.getName());

    private final ServiceCollection serviceCollection;
    private final ThreadPoolExecutor executor;

    /**
     * Constructs a StudyplanManagerImpl with the specified service collection.
     * Initializes a thread pool executor for handling asynchronous tasks.
     *
     * @param serviceCollection the collection of services required for managing study plans
     */
    //threading: executor service for async tasks, limit nr of threads
    public StudyplanManagerImpl(ServiceCollection serviceCollection) {
        this.serviceCollection  = serviceCollection;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        this.executor = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS, workQueue);
    }

    /**
     * Creates a study plan based on the provided parameters and user ID.
     * The study plan creation is handled asynchronously using a thread pool executor.
     *
     * @param parameters the study plan parameters
     * @param userId     the ID of the user for whom the study plan is being created
     * @return the ID of the created study plan calendar, or null if an error occurs
     */
    //future possibility: add parameter "algorithm" to create different studyplans :)
    @Override
    public Long createStudyplan(StudyplanParameters parameters, long userId){
        //TODO: Removed Future Threading - Shpetim
        try {
            StudyplanGenerator studyplanGenerator = new StudyplanGeneratorImpl(parameters, serviceCollection, userId);
            return studyplanGenerator.generateStudyplan();
        } catch (Exception e) {
            LOGGER.warning("Studyplan creation failed: " + e.getMessage());
            return null;
        }
    }



}
