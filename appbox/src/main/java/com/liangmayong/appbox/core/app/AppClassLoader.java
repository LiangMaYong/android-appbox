package com.liangmayong.appbox.core.app;

import com.liangmayong.appbox.core.AppboxCore;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppClassLoader {

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
     * getClassloader
     *
     * @param appPath appPath
     * @return classloader
     */
    public static ClassLoader getClassloader(String appPath) {
        if (appPath == null || "".equals(appPath)) {
            return null;
        }
        ClassLoader classLoader = null;
        if (CLASS_LOADER_MAP.containsKey(appPath)) {
            classLoader = CLASS_LOADER_MAP.get(appPath);
        } else {
            classLoader = newPluginDexLoader(appPath);
            if (classLoader != null) {
                CLASS_LOADER_MAP.put(appPath, classLoader);
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
            try {
                clazz = mClassLoader.loadClass(className);
                if (clazz != null) {
                    return clazz;
                }
            } catch (Exception e) {
            }
            return super.loadClass(className);
        }
    }

}
