package net.alenkin.alenkindrive.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public class HttpUtils {
    private HttpUtils() {
    }

    public static <T> ResponseEntity<T> buildResponse(Object methodParameter, T entity) {
        if (methodParameter == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return entity == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(entity, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> buildResponse(T entity, boolean condition) {
        return (entity!=null && condition)
                ? new ResponseEntity<>(entity, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
