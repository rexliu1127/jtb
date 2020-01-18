package io.grx.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectUtil {
    protected static final Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

    private ReflectUtil() {
    }

    private static Object operate(final Object obj, final String fieldName,
                                  final Object fieldVal, final String type) {
        Object ret = null;
        try {
            final Class<? extends Object> classType = obj.getClass();
            final Field[] fields = classType.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                final Field field = fields[i];
                if (field.getName().equals(fieldName)) {

                    final String firstLetter = fieldName.substring(0, 1)
                            .toUpperCase();
                    if ("set".equals(type)) {
                        final String setMethodName = "set" + firstLetter
                                + fieldName.substring(1);
                        final Method setMethod = classType.getMethod(setMethodName,
                                new Class[] {field.getType() });
                        ret = setMethod.invoke(obj, new Object[] {fieldVal });
                    }
                    if ("get".equals(type)) {
                        final String getMethodName = "get" + firstLetter
                                + fieldName.substring(1);
                        final Method getMethod = classType.getMethod(getMethodName,
                                new Class[] {});
                        ret = getMethod.invoke(obj, new Object[] {});
                    }
                    return ret;
                }
            }
        } catch (final Exception e) {
            logger.warn("reflect error:" + fieldName, e);
        }
        return ret;
    }

    /**
     * Get variable from object
     * @param obj the object
     * @param fieldName the field name to get value
     * @return the variable value
     */
    public static Object getVal(final Object obj, final String fieldName) {
        return operate(obj, fieldName, null, "get");
    }

    /**
     * set variable value for object
     * @param obj the object
     * @param fieldName the field name
     * @param fieldVal the field value
     */
    public static void setVal(final Object obj, final String fieldName, final Object fieldVal) {
        operate(obj, fieldName, fieldVal, "set");
    }

    private static Method getDeclaredMethod(final Object object, final String methodName,
                                            final Class<?>[] parameterTypes) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                // superClass.getMethod(methodName, parameterTypes);
                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (final NoSuchMethodException e) {
                // Method is not here
            }
        }

        return null;
    }

    private static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers())) {
            field.setAccessible(true);
        }
    }

    private static Field getDeclaredField(final Object object, final String filedName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
                .getSuperclass()) {
            try {
                return superClass.getDeclaredField(filedName);
            } catch (final NoSuchFieldException e) {
                // Field definition is not here
            }
        }
        return null;
    }

    public static Object invokeMethod(final Object object, final String methodName,
                                      final Class<?>[] parameterTypes, final Object[] parameters)
            throws InvocationTargetException {
        final Method method = getDeclaredMethod(object, methodName, parameterTypes);

        if (method == null) {
            throw new IllegalArgumentException("Could not find method ["
                    + methodName + "] on target [" + object + "]");
        }

        method.setAccessible(true);

        try {
            return method.invoke(object, parameters);
        } catch (final IllegalAccessException e) {
            //parameter is not here
        }

        return null;
    }

    public static void setFieldValue(final Object object, final String fieldName,
                                     final Object value) {
        final Field field = getDeclaredField(object, fieldName);

        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        try {
            field.set(object, value);
        } catch (final IllegalAccessException e) {
            logger.error("Fail to set the field value. fieldName=" + fieldName);
        }
    }

    public static Object getFieldValue(final Object object, final String fieldName) {
        final Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field ["
                    + fieldName + "] on target [" + object + "]");
        }

        makeAccessible(field);

        Object result = null;
        try {
            result = field.get(object);
        } catch (final IllegalAccessException e) {
            logger.error("Fail to get the field value. fieldName=" + fieldName);
        }

        return result;
    }
}
