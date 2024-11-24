package ch.zhaw.studyflow.controllers;

import ch.zhaw.studyflow.webserver.http.contents.JsonContent;

import java.lang.reflect.Field;

/**
 * This entire class is a workaround for the lack of a public getter for the content field in JsonContent.
 * This is not a good practice and should be avoided. Thus this class should be removed once the content field is made public.
 */
public class JsonContentHelpers {

    private JsonContentHelpers() {
        // This class should not be instantiated.
    }

    public static <T> T getContent(JsonContent content, Class<T> valueType) throws IllegalAccessException, NoSuchFieldException {
        Field contentField =  content.getClass().getDeclaredField("content");
        contentField.setAccessible(true);
        Object value = contentField.get(content);
        if (!valueType.isInstance(value)) {
            throw new IllegalArgumentException("Cannot read content as " + valueType.getSimpleName());
        }
        //noinspection unchecked
        return (T) value;
    }
}
