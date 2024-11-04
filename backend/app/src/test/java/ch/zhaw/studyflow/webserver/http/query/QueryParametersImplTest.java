package ch.zhaw.studyflow.webserver.http.query;

import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParametersImplTest {

    @Test
    public void testParseQueryParameters() {
        String query = "key1=value1&key2=value2&key2=value3";
        QueryParametersImpl queryParams = new QueryParametersImpl(query);

        assertEquals(2, queryParams.keys().size());
        assertTrue(queryParams.keys().contains("key1"));
        assertTrue(queryParams.keys().contains("key2"));

        assertEquals(Optional.of("value1"), queryParams.getSingleValue("key1"));
        assertThrows(IllegalArgumentException.class, () -> queryParams.getSingleValue("key2"));

        assertEquals(Optional.of(Arrays.asList("value2", "value3")), queryParams.getValues("key2"));
    }

    @Test
    public void testGetSingleValueWithNullKey() {
        QueryParametersImpl queryParams = new QueryParametersImpl("key1=value1");
        assertThrows(IllegalArgumentException.class, () -> queryParams.getSingleValue(null));
    }

    @Test
    public void testGetValuesWithNullKey() {
        QueryParametersImpl queryParams = new QueryParametersImpl("key1=value1");
        assertThrows(IllegalArgumentException.class, () -> queryParams.getValues(null));
    }

    @Test
    public void testEmptyQuery() {
        QueryParametersImpl queryParams = new QueryParametersImpl("");
        assertTrue(queryParams.keys().isEmpty());
    }

    @Test
    public void testNullQuery() {
        QueryParametersImpl queryParams = new QueryParametersImpl(null);
        assertTrue(queryParams.keys().isEmpty());
    }
}