package ch.zhaw.studyflow.services.persistence.memory;

import ch.zhaw.studyflow.domain.curriculum.Degree;
import ch.zhaw.studyflow.services.persistence.DegreeDao;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDegreeDao implements DegreeDao {
    private final HashMap<Long, Degree> degrees;
    private final AtomicInteger idCounter;


    public InMemoryDegreeDao() {
        this.degrees    = new HashMap<>();
        this.idCounter  = new AtomicInteger();
    }


    @Override
    public void create(Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() > 0) {
            throw new IllegalArgumentException("Expected id to be negative (unset)");
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
        if (degreeId < 0)
            return List.of();
        return degrees.values()
                .stream().filter(d -> d.getOwnerId() == degreeId)
                .toList();
    }

    @Override
    public void update(Degree degree) {
        Objects.requireNonNull(degree);

        if (degree.getId() < 0) {
            throw new IllegalArgumentException("Expected id to be positive");
        }

        degrees.put(degree.getId(), degree);
    }

    @Override
    public void delete(long id) {
        degrees.remove(id);
    }
}
