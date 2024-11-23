package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Semester;

public interface SemesterDao {
    void create(Semester semester);
    Semester read(long id);
    void delete(long id);
    void update(Semester semester);
}
