package ch.zhaw.studyflow.services.persistance.memory;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.services.persistance.DegreeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryDegreeDao implements DegreeDao {
    private final HashMap<Long, Degree> degrees;
    private AtomicInteger idCounter;


    public MemoryDegreeDao() {
        this.degrees    = new HashMap<>();
        this.idCounter  = new AtomicInteger();
    }


    @Override
    public void create(Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() >= 0) {
            throw new IllegalArgumentException("Expected id not to be null");
        }

        degree.setId(idCounter.incrementAndGet());
        degrees.put(degree.getId(), degree);
    }

    @Override
    public Degree read(long degreeId) {
        return degrees.get(degreeId);
    }

    @Override
    public List<Degree> readAllByStudent(long degreeId) {
        return degrees.values()
                .stream().filter(d -> d.getOwnerId() == degreeId)
                .toList();
    }

    @Override
    public void update(Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() < 0) {
            throw new IllegalArgumentException("Expected id not to be null");
        }

        degrees.put(degree.getId(), degree);
    }

    @Override
    public void delete(long id) {
        degrees.remove(id);
    }
}
