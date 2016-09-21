package com.liangmayong.appbox;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.liangmayong.appbox.core.AppConstant;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppHookHandler;
import com.liangmayong.appbox.core.AppInstrumentation;
import com.liangmayong.appbox.core.AppReflect;
import com.liangmayong.appbox.core.launcher.AppboxActivity;
import com.liangmayong.appbox.core.launcher.AppboxService;

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
        AppHookHandler.hook(application);
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
        Intent intent = new Intent(context, AppboxActivity.class);
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
        Intent intent = new Intent();
        intent.setClassName(AppContext.get(activity, path), actName);
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
        Intent intent = new Intent(context, AppboxService.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, serName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        return context.startService(intent);
    }

    /**
     * bindService
     *
     * @param activity activity
     * @param path     path
     * @param serName  serName
     * @param conn     conn
     * @param flags    flags
     * @return false
     */
    public boolean bindService(Activity activity, String path, String serName, Bundle extars, ServiceConnection conn, int flags) {
        if (!isInit) {
            return false;
        }
        if (activity == null) {
            return false;
        }
        Intent intent = new Intent(activity, AppboxService.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, serName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        return activity.bindService(intent, conn, flags);
    }


    /**
     * unbindService
     *
     * @param activity activity
     * @param conn     conn
     */
    public void unbindService(Activity activity, ServiceConnection conn) {
        activity.unbindService(conn);
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
