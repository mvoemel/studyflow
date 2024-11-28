package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySemesterDao implements SemesterDao {
    private final HashMap<Long,HashMap<Long,Semester>> semester;
    private AtomicInteger idCounter;


    public InMemorySemesterDao() {
        this.semester = new HashMap<>();
        this.idCounter = new AtomicInteger();
    }

    @Override
    public void createSemester(Semester semester) {
        Objects.requireNonNull(semester);


    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        return List.of();
    }

    @Override
    public Semester getSemesterById(long semesterId) {
        return null;
    }

    @Override
    public void updateSemester(Semester semester) {

    }

    @Override
    public void deleteSemester(long semesterId) {

    }
}
