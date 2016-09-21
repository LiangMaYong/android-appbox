package com.liangmayong.appbox.core;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.liangmayong.appbox.core.hook.ActivityManagerHandler;
import com.liangmayong.appbox.core.hook.ActivityThreadHandlerCallback;
import com.liangmayong.appbox.core.hook.PackageManagerHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by LiangMaYong on 2016/9/21.
 */
public class AppHookHandler {

    private AppHookHandler() {
    }

    public static void hook(Application application) {
        hookPackageManager(application);
        hookActivityManagerNative(application);
        hookActivityThreadHandler(application);
    }

    private static void hookActivityThreadHandler(Application application) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            currentActivityThreadField.setAccessible(true);
            Object currentActivityThread = currentActivityThreadField.get(null);

            Field mHField = activityThreadClass.getDeclaredField("mH");
            mHField.setAccessible(true);
            Handler mH = (Handler) mHField.get(currentActivityThread);


            Field mCallBackField = Handler.class.getDeclaredField("mCallback");
            mCallBackField.setAccessible(true);

            mCallBackField.set(mH, new ActivityThreadHandlerCallback(application, mH));
        } catch (Exception e) {
        }
    }

    /**
     * hookActivityManagerNative
     *
     * @param application application
     */
    private static void hookActivityManagerNative(Application application) {
        try {
            Class<?> activityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");

            Field gDefaultField = activityManagerNativeClass.getDeclaredField("gDefault");
            gDefaultField.setAccessible(true);

            Object gDefault = gDefaultField.get(null);

            Class<?> singleton = Class.forName("android.util.Singleton");
            Field mInstanceField = singleton.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            Object rawIActivityManager = mInstanceField.get(gDefault);

            Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class<?>[]{iActivityManagerInterface}, new ActivityManagerHandler(application, rawIActivityManager));
            mInstanceField.set(gDefault, proxy);
        } catch (Exception e) {
        }
    }


    /**
     * hookPackageManager
     *
     * @param application application
     */
    private static void hookPackageManager(Application application) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);

            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader(),
                    new Class<?>[]{iPackageManagerInterface},
                    new PackageManagerHandler(application, sPackageManager));

            sPackageManagerField.set(currentActivityThread, proxy);

            PackageManager pm = application.getPackageManager();
            Field mPmField = pm.getClass().getDeclaredField("mPM");
            mPmField.setAccessible(true);
            mPmField.set(pm, proxy);
        } catch (Exception e) {
        }
    }
}
