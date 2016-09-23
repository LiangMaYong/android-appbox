package com.liangmayong.appbox.core.manager;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppLoger;
import com.liangmayong.appbox.core.AppMethod;
import com.liangmayong.appbox.core.AppProcess;
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
    public static Application handleCreateApplication(final String appPath) {
        Context context = getHostApplication();
        String key = MD5.encrypt("key_" + appPath);
        Log.e("TAG", AppProcess.getCurrentProcessName(context));
        Log.e("TAG", key + " ---------------------------------------0");
        Log.e("TAG", STRING_APPLICATION_HASH_MAP + "");
        if (STRING_APPLICATION_HASH_MAP.containsKey(key)) {
            return STRING_APPLICATION_HASH_MAP.get(key);
        }
        Log.e("TAG", key + " ---------------------------------------1");
        Application application = null;
        AppInfo info = AppInfo.get(context, appPath);
        Log.e("TAG", key + " ---------------------------------------2");
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        AppReceiverManager.registerReceiver(appPath);
                    }
                }).start();
                application.onCreate();
                AppLoger.getDefualt().error("create application:" + info.getApplicationName());
            } catch (Exception e) {
                AppLoger.getDefualt().error("create application fail:" + info.getApplicationName(), e);
                application = (Application) ctx;
            }
        }
        Log.e("TAG", key + " ---------------------------------------3");
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
