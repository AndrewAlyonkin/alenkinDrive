package net.alenkin.alenkindrive.util;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
public class ValidationUtils {
    private ValidationUtils() {
    }

    public static <T> T checkNotFoundWithId(T object, long id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, long id) {
        checkNotFound(found, "id=" + id);
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new IllegalArgumentException("Not found entity with " + msg);
        }
    }
}
