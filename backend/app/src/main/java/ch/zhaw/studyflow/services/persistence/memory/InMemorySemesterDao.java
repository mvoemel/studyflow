package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySemesterDao implements SemesterDao {
    private final HashMap<Long, Semester> semesters;
    private final HashMap<Long, Long> semestersToDegree;
    private final HashMap<Long, Long> degreeToUser;
    private AtomicInteger idCounter;


    public InMemorySemesterDao() {
        this.semesters = new HashMap<>();
        this.semestersToDegree = new HashMap<>();
        this.degreeToUser = new HashMap<>();
        this.idCounter = new AtomicInteger();
    }

    @Override
    public void createSemester(Semester semester, long degreeId, long userId) {
        Objects.requireNonNull(semester);
        if(semester.getId() >= 0) {
            throw new IllegalStateException("The id can only be set once.");
        }
        semester.setId(idCounter.incrementAndGet());
        semesters.put(semester.getId(), semester);
        semestersToDegree.put(semester.getId(), degreeId);
        degreeToUser.put(degreeId, userId);
    }

    @Override
    public List<Semester> getSemestersForStudent(long userId) {
        List<Semester> semestersForStudent = new ArrayList<>();
        degreeToUser.forEach((degreeId, sId) -> {
            if(sId == userId) {
                semestersToDegree.forEach((semesterId, dId) -> {
                    if(degreeId == dId) {
                        semestersForStudent.add(semesters.get(semesterId));
                    }
                });
            }
        });
        return semestersForStudent;
    }

    @Override
    public Optional<Semester> getSemesterById(long semesterId) {
        return Optional.ofNullable(semesters.get(semesterId));
    }

    @Override
    public void updateSemester(Semester semester) {
        Objects.requireNonNull(semester);
        if(semester.getId() < 0) {
            throw new IllegalStateException("The id must be set.");
        }
        semesters.put(semester.getId(), semester);
    }

    @Override
    public void deleteSemester(long semesterId) {
        semesters.remove(semesterId);
        semestersToDegree.remove(semesterId);
    }
}