package com.liangmayong.appbox.core.app;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppMethod {

    // STRING_METHOD_MAP
    private static final Map<String, Method> STRING_METHOD_MAP = new HashMap<String, Method>();

    private static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        String key = clazz.getName() + "@" + name;
        if (STRING_METHOD_MAP.containsKey(key)) {
            return STRING_METHOD_MAP.get(key);
        }
        Method method = null;
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(name, parameterTypes);
                STRING_METHOD_MAP.put(key, method);
                return method;
            } catch (Exception e) {
            }
        }
        return null;
    }

    private Method method = null;
    private Object object = null;

    public AppMethod(Class<?> cls, Object object, String method, Class<?>... parameterTypes) {
        try {
            this.object = object;
            this.method = getMethod(cls, method, parameterTypes);
        } catch (Exception e) {
        }
    }

    public <T> T invoke(Object... args) throws Exception {
        if (method != null) {
            method.setAccessible(true);
            Object object = method.invoke(this.object, args);
            method = null;
            this.object = null;
            return (T) object;
        }
        return null;
    }
}
