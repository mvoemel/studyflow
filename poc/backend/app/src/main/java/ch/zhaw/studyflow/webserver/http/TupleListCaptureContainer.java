package ch.zhaw.studyflow.webserver.http;

import ch.zhaw.studyflow.webserver.Tuple;

import java.util.*;

public class TupleListCaptureContainer implements CaptureContainer {
    private final List<Tuple<String , String >> captureTuples;


    public  TupleListCaptureContainer(List<Tuple<String, String>> captures) {
        this.captureTuples  = Collections.unmodifiableList(captures);
    }

    @Override
    public int size() {
        return captureTuples.size();
    }

    @Override
    public Collection<String> keys() {
        final ArrayList<String> result = new ArrayList<>(size());
        captureTuples.stream()
                .map(Tuple::value1)
                .forEach(result::add);
        return Collections.unmodifiableList(result);
    }

    @Override
    public Collection<String> values() {
        final ArrayList<String> result = new ArrayList<>(size());
        captureTuples.stream()
                .map(Tuple::value2)
                .forEach(result::add);
        return Collections.unmodifiableList(result);
    }

    @Override
    public Optional<String> get(final String key) {
        return captureTuples.stream()
                .filter(e -> e.value1().equalsIgnoreCase(key))
                .map(Tuple::value2)
                .findFirst();
    }
}
