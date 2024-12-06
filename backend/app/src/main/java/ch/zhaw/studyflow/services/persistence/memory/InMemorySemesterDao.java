package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Semester;
import ch.zhaw.studyflow.services.persistence.SemesterDao;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySemesterDao implements SemesterDao {
    private final HashMap<Long, Semester> semesters;
    private final HashMap<Long, Long> semestersToDegree;
    private final HashMap<Long, Long> degreeToUser;
    private final HashMap<Long, Long> calendarToSemester;
    private AtomicInteger idCounter;


    public InMemorySemesterDao() {
        this.semesters = new HashMap<>();
        this.semestersToDegree = new HashMap<>();
        this.degreeToUser = new HashMap<>();
        this.calendarToSemester = new HashMap<>();
        this.idCounter = new AtomicInteger();
    }

    @Override
    public void create(Semester semester, long degreeId, long userId) {
        Objects.requireNonNull(semester);
        if(semester.getId() >= 0) {
            throw new IllegalStateException("The id can only be set once.");
        }
        semester.setId(idCounter.getAndIncrement());
        semesters.put(semester.getId(), semester);
        semestersToDegree.put(semester.getId(), degreeId);
        degreeToUser.put(degreeId, userId);
        calendarToSemester.put(semester.getCalendarId(), semester.getId());
    }

    @Override
    public List<Semester> readAllByStudent(long studentId) {
        List<Semester> semestersForStudent = new ArrayList<>();
        degreeToUser.forEach((degreeId, sId) -> {
            if(sId == studentId) {
                semestersToDegree.forEach((semesterId, dId) -> {
                    if(Objects.equals(degreeId, dId)) {
                        semestersForStudent.add(semesters.get(semesterId));
                    }
                });
            }
        });
        return semestersForStudent;
    }

    @Override
    public List<Semester> readAllByDegree(long degreeId) {
        return semestersToDegree.entrySet().stream()
                .filter(entry -> Objects.equals(entry.getValue(), degreeId))
                .map(entry -> semesters.get(entry.getKey()))
                .toList();
    }

    @Override
    public Optional<Semester> readSemesterById(long semesterId) {
        return Optional.ofNullable(semesters.get(semesterId));
    }

    @Override
    public void update(Semester semester) {
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
