package ch.zhaw.studyflow.domain.curriculum;

import java.util.List;

public interface DegreeManager {
    void createDegree(Degree degree);
    List<Degree> getDegreesForStudent(long userId);
    Degree getDegree(long degreeId);
    void updateDegree(Degree degree);
    void deleteDegree(long degreeId);
}
