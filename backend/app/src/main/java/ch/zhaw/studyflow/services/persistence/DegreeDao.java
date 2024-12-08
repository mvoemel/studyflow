package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;

import java.util.List;

public interface DegreeDao {
    void create(Degree degree);
    Degree read(long degreeId);
    List<Degree> readAllByStudent(long studentId);
    void update(Degree degree);
    void delete(long degreeId);
}
