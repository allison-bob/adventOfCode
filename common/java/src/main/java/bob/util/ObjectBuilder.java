package bob.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class with methods for building other objects.
 */
public class ObjectBuilder {

    private ObjectBuilder() {
    }

    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw Assert.failed(ex, "Unable to locate constructor");
        }
    }
    
    public static <T> T getInstance(Constructor<T> constr, Object ... initargs) {
        try {
            return constr.newInstance(initargs);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException ex) {
            throw Assert.failed(ex, "Unable to create instance");
        }
    }
}
