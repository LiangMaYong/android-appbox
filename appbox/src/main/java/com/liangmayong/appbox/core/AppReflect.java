package com.liangmayong.appbox.core;

import java.lang.reflect.Field;

/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppReflect {

    private AppReflect() {
    }

    /**
     * isGeneric
     *
     * @param clazz clazz
     * @param name  name
     * @return true or false
     */
    public static boolean isGeneric(Class<?> clazz, String name) {
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            if (name.equals(clazz.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * setField
     *
     * @param clazz     clazz
     * @param object    object
     * @param fieldName fieldName
     * @param value     value
     * @return true or false
     */
    public static boolean setField(Class<?> clazz, Object object, String fieldName, Object value) {
        Field field = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (Exception e) {
            }
        }
        if (field != null) {
            field.setAccessible(true);
            try {
                field.set(object, value);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * setField
     *
     * @param clazz     clazz
     * @param object    object
     * @param fieldName fieldName
     * @return object
     */
    public static final Object getField(Class<?> clazz, Object object, String fieldName) {
        if (clazz == null) {
            return null;
        }
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (Exception e) {
            }
        }
        return null;
    }
}