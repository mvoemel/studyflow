package ch.zhaw.studyflow.webserver.http.query;

import java.util.*;

public class QueryParametersImpl implements QueryParameters {
    private final Map<String, List<String>> parameters;

    public QueryParametersImpl(String query) {
        this.parameters = parseQueryParameters(query);
    }

    @Override
    public Set<String> keys() {
        return parameters.keySet();
    }

    @Override
    public Optional<String> getSingleValue(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        List<String> values = parameters.get(key);
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }
        if (values.size() > 1) {
            throw new IllegalArgumentException("Multiple values found for key: " + key);
        }
        return Optional.of(values.get(0));
    }

    @Override
    public Optional<List<String>> getValues(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        List<String> values = parameters.get(key);
        return values == null ? Optional.empty() : Optional.of(values);
    }

    /**
     * Parses the query parameters from a query string.
     *
     * @param query the query string to parse, may be null
     * @return a map containing the query parameters, where the key is the parameter name
     *         and the value is a list of parameter values
     */
    private Map<String, List<String>> parseQueryParameters(String query) {
        Map<String, List<String>> parameters = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String key = pair[0];
                String value = pair.length > 1 ? pair[1] : "";
                parameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
            }
        }
        return parameters;
    }
}