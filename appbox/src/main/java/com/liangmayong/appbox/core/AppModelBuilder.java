package com.liangmayong.appbox.core;

/**
 * AppModelBuilder
 *
 * @param <T> type
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppModelBuilder<T> {

    private Class<T> clazz;

    public AppModelBuilder(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * getModel
     *
     * @param appPath   appPath
     * @param className className
     * @return t
     */
    public T getModel(String appPath, String className) {
        try {
            Class<?> c = null;
            c = AppClassLoader.loadClass(appPath, className);
            Object model = c.newInstance();
            T newModel = clazz.newInstance();
            AppReflect.cloneModel(model, newModel);
            return newModel;
        } catch (Exception e) {
        }
        return null;
    }

}
