package ch.zhaw.studyflow.domain.studyplan;

import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

import ch.zhaw.studyflow.controllers.deo.StudyplanParameters;
import ch.zhaw.studyflow.domain.studyplan.impls.StudyplanManagerImpl;
import ch.zhaw.studyflow.services.ServiceCollection;



public class StudyplanManagerImplTest {

    private StudyplanManagerImpl studyPlanManager;
    private ServiceCollection serviceCollection;
    private StudyplanParameters parameters;
    private final long userId = 1L;

    @BeforeEach
    void beforeEach() {
        this.serviceCollection = mock(ServiceCollection.class);
        this.studyPlanManager = new StudyplanManagerImpl(serviceCollection);
        this.parameters = mock(StudyplanParameters.class);
    }

    @Test
    void testFailToCreateStudyplan() {
        Long result = studyPlanManager.createStudyplan(parameters, userId);
        assertNull(result);
    }
  
}
