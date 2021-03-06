package com.liangmayong.appbox.core.manager;

import android.app.Service;
import android.content.Context;
import android.os.Binder;
import android.os.Build;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppMethod;
import com.liangmayong.appbox.core.AppReflect;
import com.liangmayong.appbox.core.utils.MD5;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public class AppServiceManager {

    private static final Map<String, Service> STRING_SERVICE_MAP = new HashMap<String, Service>();

    /**
     * remove
     *
     * @param appPath appPath
     */
    public static void remove(String appPath) {
        String key = MD5.encrypt("key_" + appPath);
        for (Map.Entry<String, Service> entry : STRING_SERVICE_MAP.entrySet()) {
            if (entry.getKey().startsWith(key)) {
                STRING_SERVICE_MAP.remove(entry.getKey());
                entry.getValue().onDestroy();
            }
        }
    }

    public static void onDestroy() {
        for (Map.Entry<String, Service> entry : STRING_SERVICE_MAP.entrySet()) {
            entry.getValue().onDestroy();
        }
        STRING_SERVICE_MAP.clear();
    }

    public static void onLowMemory() {
        for (Map.Entry<String, Service> entry : STRING_SERVICE_MAP.entrySet()) {
            entry.getValue().onLowMemory();
        }
    }

    public static Service handleCreateService(Context context, String appPath, String serviceName) {
        String key = MD5.encrypt("key_" + appPath) + serviceName;

        if (STRING_SERVICE_MAP.containsKey(key)) {
            return STRING_SERVICE_MAP.get(key);
        }
        Service service = null;
        try {
            service = (Service) AppClassLoader.getClassloader(appPath).loadClass(serviceName).newInstance();
        } catch (Exception e) {
        }
        if (service != null) {
            try {
                Context ctx = AppContext.get(context, appPath);
                AppMethod attachBaseContextMethod = new AppMethod(Service.class, service, "attachBaseContext", Context.class);
                attachBaseContextMethod.invoke(ctx);

                AppReflect.setField(Service.class, service, "mClassName", serviceName);
                AppReflect.setField(Service.class, service, "mToken", new Binder());
                AppReflect.setField(Service.class, service, "mApplication", AppApplicationManager.handleCreateApplication(appPath));
                AppReflect.setField(Service.class, service, "mStartCompatibility", ctx.getApplicationInfo().targetSdkVersion < Build.VERSION_CODES.ECLAIR);

                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
                Object currentActivityThread = currentActivityThreadMethod.invoke(null);
                AppReflect.setField(Service.class, service, "mThread", currentActivityThread);

                Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
                Method getDefaultMethod = activityManagerNativeClass.getDeclaredMethod("getDefault");
                Object activityManager = getDefaultMethod.invoke(null);
                AppReflect.setField(Service.class, service, "mActivityManager", activityManager);
            } catch (Exception e) {
            }
            service.onCreate();
            STRING_SERVICE_MAP.put(key, service);
        }
        return service;
    }
}
