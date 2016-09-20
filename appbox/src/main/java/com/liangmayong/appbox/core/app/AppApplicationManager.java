package com.liangmayong.appbox.core.app;

import android.app.Application;
import android.app.Service;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public class AppApplicationManager {

    private static final Map<String, Service> STRING_SERVICE_MAP = new HashMap<String, Service>();

    /**
     * handleCreateApplication
     *
     * @param context context
     * @param appPath appPath
     * @return
     */
    public static Application handleCreateApplication(Context context, String appPath) {
        AppInfo info = AppInfo.get(context, appPath);
        if (info == null) {
            return getHostApplication();
        }
        Context ctx = AppContext.get(context, appPath);
        try {
            Application application = (Application) AppClassLoader.getClassloader(info.getAppPath()).loadClass(info.getApplicationName())
                    .newInstance();
            AppMethod method = new AppMethod(Application.class, application, "attach", Context.class);
            method.invoke(ctx);
            application.onCreate();
            AppLoger.getDefualt().error("create application:" + info.getApplicationName());
            return application;
        } catch (Exception e) {
        }
        return (Application) ctx;
    }


    // application
    private static WeakReference<Application> application = null;

    /**
     * getApplication
     *
     * @return application
     */
    public static Application getHostApplication() {
        if (application == null || application.get() == null) {
            synchronized (AppApplicationManager.class) {
                if (application == null) {
                    try {
                        Class<?> clazz = Class.forName("android.app.ActivityThread");
                        Method currentActivityThread = clazz.getDeclaredMethod("currentActivityThread");
                        if (currentActivityThread != null) {
                            Object object = currentActivityThread.invoke(null);
                            if (object != null) {
                                Method getApplication = object.getClass().getDeclaredMethod("getApplication");
                                if (getApplication != null) {
                                    application = new WeakReference<Application>((Application) getApplication.invoke(object));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return application.get();
    }
}
