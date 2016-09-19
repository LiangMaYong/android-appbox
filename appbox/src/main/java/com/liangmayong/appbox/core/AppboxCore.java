package com.liangmayong.appbox.core;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.liangmayong.appbox.core.app.AppConstant;
import com.liangmayong.appbox.core.app.AppInstrumentation;
import com.liangmayong.appbox.core.app.AppReflect;
import com.liangmayong.appbox.core.app.hook.ActivityManagerHandler;
import com.liangmayong.appbox.core.app.hook.PackageManagerHandler;
import com.liangmayong.appbox.core.launchers.LauncherActivity;
import com.liangmayong.appbox.core.launchers.LauncherService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by LiangMaYong on 2016/9/18.
 */
public class AppboxCore {

    private AppboxCore() {
    }

    private static volatile AppboxCore APPBOX_CORE = null;

    private boolean isInit = false;

    /**
     * getInstance
     *
     * @return appboxCore
     */
    public static AppboxCore getInstance() {
        if (APPBOX_CORE == null) {
            synchronized (AppboxCore.class) {
                APPBOX_CORE = new AppboxCore();
            }
        }
        return APPBOX_CORE;
    }


    /**
     * initialize
     *
     * @param application application
     * @return true or false
     */
    public boolean initialize(Application application) {
        if (isInit) {
            return true;
        }
        if (application == null) {
            return false;
        }
        hookActivityManagerNative(application);
        hookPackageManager(application);
        try {
            Object loadedApk = AppReflect.getField(Application.class, application, "mLoadedApk");
            if (loadedApk != null) {
                Object activityThread = AppReflect.getField(loadedApk.getClass(), loadedApk, "mActivityThread");
                if (activityThread != null) {
                    AppReflect.setField(activityThread.getClass(), activityThread, "mInstrumentation",
                            new AppInstrumentation((Instrumentation) AppReflect.getField(activityThread.getClass(),
                                    activityThread, "mInstrumentation")));
                }
            }
            isInit = true;
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * startActivity
     *
     * @param context activity
     * @param path    path
     * @param actName actName
     */
    public void startActivity(Context context, String path, String actName) {
        startActivity(context, path, actName, null);
    }

    /**
     * startActivity
     *
     * @param context activity
     * @param path    path
     * @param actName actName
     * @param extars  extars
     */
    public void startActivity(Context context, String path, String actName, Bundle extars) {
        if (!isInit) {
            return;
        }
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, LauncherActivity.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, actName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        context.startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param path        path
     * @param actName     actName
     */
    public void startActivityForResult(Activity activity, int requestCode, String path, String actName) {
        startActivityForResult(activity, requestCode, path, actName, null);
    }

    /**
     * startActivityForResult
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param path        path
     * @param actName     actName
     * @param extars      extars
     */
    public void startActivityForResult(Activity activity, int requestCode, String path, String actName, Bundle extars) {
        if (!isInit) {
            return;
        }
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, LauncherActivity.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, actName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * startService
     *
     * @param context context
     * @param path    path
     * @param serName serName
     */
    public ComponentName startService(Context context, String path, String serName) {
        return startService(context, path, serName, null);
    }

    /**
     * startService
     *
     * @param context context
     * @param path    path
     * @param serName serName
     * @param extars  extars
     */
    public ComponentName startService(Context context, String path, String serName, Bundle extars) {
        if (!isInit) {
            return null;
        }
        if (context == null) {
            return null;
        }
        Intent intent = new Intent(context, LauncherService.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, serName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        return context.startService(intent);
    }

    /**
     * hookActivityManagerNative
     *
     * @param application application
     */
    private void hookActivityManagerNative(Application application) {
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
    private void hookPackageManager(Application application) {
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

    /**
     * parent classloader
     */
    private ClassLoader supportClassLoader = null;

    /**
     * setSupportClassLoader
     *
     * @param supportClassLoader supportClassLoader
     */
    public void setSupportClassLoader(ClassLoader supportClassLoader) {
        this.supportClassLoader = supportClassLoader;
    }

    /**
     * getSupportClassloader
     *
     * @return supportClassLoader
     */
    public ClassLoader getSupportClassloader() {
        return this.supportClassLoader;
    }
}
