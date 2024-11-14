package ch.zhaw.studyflow.services.persistance;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

public interface DegreeDao {
    void create(Degree degree);
    Degree read(long degreeId);
    List<Degree> readAllByStudent(long degreeId);
    void update(Degree degree);
    void delete(long id);
}
