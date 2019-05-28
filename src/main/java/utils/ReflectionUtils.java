package utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class ReflectionUtils {

    public static Object createInstance(Class<?> clazz, Object... args) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return getMatchingAccessibleConstructor(clazz, convertToTypes(args)).newInstance(args);
    }

    public static Object invokeMethod(Object self, String method, Object... args) throws
            NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return self.getClass().getMethod(method, convertToTypes(args)).invoke(self, args);
    }

    private static Constructor getMatchingAccessibleConstructor(Class<?> clazz, Class<?>... args) throws
            NoSuchMethodException {
        loop:
        for (var constructor : clazz.getConstructors()) {
            var types = constructor.getParameterTypes();
            if (types.length == args.length) {
                for (var i = 0; i < args.length; i++) {
                    if (!types[i].isAssignableFrom(args[i])) {
                        continue loop;
                    }
                }
            }
            return constructor;
        }
        return clazz.getConstructor(args);
    }

    private static Class<?>[] convertToTypes(Object[] objects) {
        return Arrays.stream(objects).map(Object::getClass).toArray(Class[]::new);
    }
}
