package ch.zhaw.studyflow.services.persistence;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.domain.curriculum.Grade;

public interface GradeDao {
    void create(Grade grade);
    Grade read(long gradeId);
    void delete(long gradeId);
    void update(Degree degree);
}
