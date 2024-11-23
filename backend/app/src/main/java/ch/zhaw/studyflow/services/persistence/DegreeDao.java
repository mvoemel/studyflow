package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

public interface DegreeDao {
    void create(Degree degree);
    Degree read(long degreeId);
    List<Degree> readAllBy(long userId);
    void delete(long id);
    void update(Degree degree);

}
