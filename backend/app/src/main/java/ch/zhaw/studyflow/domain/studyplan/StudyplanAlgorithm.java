package ch.zhaw.studyflow.domain.studyplan;

import java.util.List;
import java.util.Map;

import ch.zhaw.studyflow.domain.curriculum.Module;

public interface StudyplanAlgorithm {

public List<StudyDay> getStudyDays();

public Map<Module, List<StudyDay>> getModuleStudyDays();


}
