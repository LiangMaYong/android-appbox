package com.liangmayong.appbox.core.manager;

import android.app.Application;
import android.content.Context;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppLoger;
import com.liangmayong.appbox.core.AppMethod;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public class AppApplicationManager {

    private static final Map<String, Application> STRING_SERVICE_MAP = new HashMap<String, Application>();

    /**
     * handleCreateApplication
     *
     * @param context context
     * @param appPath appPath
     * @return
     */
    public static Application handleCreateApplication(Context context, String appPath) {
        String key = "application_" + appPath;
        if (STRING_SERVICE_MAP.containsKey(key)) {
            return STRING_SERVICE_MAP.get(key);
        }
        Application application = null;
        AppInfo info = AppInfo.get(context, appPath);
        if (info == null) {
            application = getHostApplication();
        } else {
            Context ctx = AppContext.get(context, appPath);
            try {
                application = (Application) AppClassLoader.getClassloader(info.getAppPath()).loadClass(info.getApplicationName())
                        .newInstance();
                AppMethod method = new AppMethod(Application.class, application, "attach", Context.class);
                method.invoke(ctx);
                // register receiver
                AppReceiverManager.registerReceiver(context, appPath);
                application.onCreate();
                AppLoger.getDefualt().error("create application:" + info.getApplicationName());
            } catch (Exception e) {
                AppLoger.getDefualt().error("create application fail:" + info.getApplicationName(), e);
                application = (Application) ctx;
            }
        }
        STRING_SERVICE_MAP.put(key, application);
        return application;
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
