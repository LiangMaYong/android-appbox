package com.liangmayong.appbox.core;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.utils.MD5;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppClassLoader {

    /**
     * CLASS_LOADER_MAP
     */
    private static final Map<String, ClassLoader> CLASS_LOADER_MAP = new HashMap<String, ClassLoader>();

    private AppClassLoader() {
    }

    /**
     * loadClass
     *
     * @param appPath   appPath
     * @param className className
     * @return clazz
     */
    public static Class<?> loadClass(String appPath, String className) {
        try {
            return getClassloader(appPath).loadClass(className);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * remove
     *
     * @param appPath appPath
     */
    public static void remove(String appPath) {
        String key = MD5.encrypt("key_" + appPath);
        if (CLASS_LOADER_MAP.containsKey(key)) {
            CLASS_LOADER_MAP.remove(key);
        }
    }

    /**
     * add
     *
     * @param appPath appPath
     */
    public static void add(String appPath) {
        getClassloader(appPath);
    }

    /**
     * getClassloader
     *
     * @param appPath appPath
     * @return classloader
     */
    public static ClassLoader getClassloader(String appPath) {
        if (appPath == null || "".equals(appPath) || !(new File(appPath).exists())) {
            return AppClassLoader.class.getClassLoader();
        }
        String key = MD5.encrypt("key_" + appPath);
        ClassLoader classLoader = null;
        if (CLASS_LOADER_MAP.containsKey(key)) {
            classLoader = CLASS_LOADER_MAP.get(key);
        } else {
            classLoader = newPluginDexLoader(appPath);
            if (classLoader != null) {
                CLASS_LOADER_MAP.put(key, classLoader);
            }
        }
        return classLoader;
    }

    /**
     * newPluginDexLoader
     *
     * @param appPath appPath
     * @return ClassLoader
     */
    private static ClassLoader newPluginDexLoader(String appPath) {
        try {
            ClassLoader dexClassLoader = new DexClassLoader(appPath, new File(appPath).getParent(),
                    AppNative.getNativePath(appPath), ClassLoader.getSystemClassLoader());
            return new AppboxClassLoader(dexClassLoader);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * plugin classloader
     *
     * @author LiangMaYong
     * @version 1.0
     */
    private static class AppboxClassLoader extends ClassLoader {

        private ClassLoader mClassLoader;

        public AppboxClassLoader(ClassLoader classLoader) {
            this.mClassLoader = classLoader;
        }

        @Override
        public Class<?> loadClass(String className) throws ClassNotFoundException {
            Class<?> clazz = findLoadedClass(className);
            if (clazz != null) {
                return clazz;
            }
            if (AppboxCore.getInstance().getSupportClassloader() != null) {
                try {
                    clazz = AppboxCore.getInstance().getSupportClassloader().loadClass(className);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (Exception e) {
                }
            }
            Exception exception = null;
            try {
                clazz = mClassLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {
                exception = e;
            }
            try {
                return super.loadClass(className);
            } catch (Exception e) {
            }
            throw new ClassNotFoundException("not found class " + className, exception);
        }
    }

}
