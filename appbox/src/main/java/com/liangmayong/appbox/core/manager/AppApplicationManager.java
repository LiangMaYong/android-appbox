package com.liangmayong.appbox.core.manager;

import android.app.Application;
import android.content.Context;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppLoger;
import com.liangmayong.appbox.core.AppMethod;
import com.liangmayong.appbox.core.utils.MD5;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public class AppApplicationManager {

    private static final Map<String, Application> STRING_APPLICATION_HASH_MAP = new HashMap<String, Application>();

    /**
     * remove
     *
     * @param appPath appPath
     */
    public static void remove(String appPath) {
        String key = MD5.encrypt("key_" + appPath);
        if (STRING_APPLICATION_HASH_MAP.containsKey(key)) {
            STRING_APPLICATION_HASH_MAP.remove(key);
        }
    }

    /**
     * handleCreateApplication
     *
     * @param appPath appPath
     * @return
     */
    public synchronized static Application handleCreateApplication(final String appPath) {
        Context context = getHostApplication();
        String key = MD5.encrypt("key_" + appPath);
        if (STRING_APPLICATION_HASH_MAP.containsKey(key)) {
            return STRING_APPLICATION_HASH_MAP.get(key);
        }
        Application application = null;
        AppInfo info = AppInfo.get(context, appPath);
        if (info != null) {
            Context ctx = AppContext.get(context, appPath);
            try {
                application = (Application) AppClassLoader.getClassloader(info.getAppPath()).loadClass(info.getApplicationName())
                        .newInstance();
                STRING_APPLICATION_HASH_MAP.put(key, application);
                AppMethod method = new AppMethod(Application.class, application, "attach", Context.class);
                method.invoke(ctx);
                AppLoger.getDefualt().error("create application : " + info.getApplicationName());
                AppReceiverManager.registerReceiver(appPath);
                try {
                    application.onCreate();
                } catch (Exception e) {
                    AppLoger.getDefualt().error("calling onCreate fail", e);
                }
            } catch (Exception e) {
                AppLoger.getDefualt().error("create application fail", e);
            }
        } else {
            application = getHostApplication();
        }
        if (application == null) {
            application = getHostApplication();
        }
        return application;
    }


    // application
    private static Application application = null;

    /**
     * getApplication
     *
     * @return application
     */
    public static Application getHostApplication() {
        if (application == null) {
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
                                    application = (Application) getApplication.invoke(object);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return application;
    }
}
