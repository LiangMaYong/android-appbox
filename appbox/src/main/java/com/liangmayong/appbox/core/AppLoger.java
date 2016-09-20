package com.liangmayong.appbox.core;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

/**
 * AppLoger
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppLoger {

    // defualt
    private static volatile AppLoger defualt_l = null;

    /**
     * getDefualt
     *
     * @return defualt
     */
    public static AppLoger getDefualt() {
        if (defualt_l == null) {
            synchronized (AppLoger.class) {
                defualt_l = new AppLoger("APPBOX-DEBUG");
            }
        }
        return defualt_l;
    }

    /**
     * tag
     *
     * @param tag tag
     * @return loger
     */
    public static AppLoger tag(String tag) {
        return new AppLoger(tag);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // application
    private static WeakReference<Application> application = null;

    /**
     * getApplication
     *
     * @return application
     */
    private static Application getApplication() {
        if (application == null || application.get() == null) {
            synchronized (AppLoger.class) {
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

    /**
     * isDebugable
     *
     * @return true or false
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static boolean isDebugable() {
        try {
            ApplicationInfo info = getApplication().getApplicationInfo();
            boolean debugable = (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            return debugable;
        } catch (Exception e) {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private final String TAG;

    private AppLoger(String tag) {
        if (tag == null) {
            if (getApplication() != null) {
                tag = getApplication().getPackageName();
            } else {
                tag = "android-base-log";
            }
        }
        TAG = tag;
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public void debug(String msg) {
        if (isDebugable()) {
            Log.d(TAG, msg);
        }
    }

    /**
     * Send a DEBUG log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public void debug(String msg, Throwable tr) {
        if (isDebugable()) {
            Log.d(TAG, msg, tr);
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public void error(String msg) {
        if (isDebugable()) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Send a ERROR log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public void error(String msg, Throwable tr) {
        if (isDebugable()) {
            Log.e(TAG, msg, tr);
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public void info(String msg) {
        if (isDebugable()) {
            Log.i(TAG, msg);
        }
    }

    /**
     * Send a INFO log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public void info(String msg, Throwable tr) {
        if (isDebugable()) {
            Log.i(TAG, msg, tr);
        }
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public void verbose(String msg) {
        if (isDebugable()) {
            Log.v(TAG, msg);
        }
    }

    /**
     * Send a VERBOSE log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public void verbose(String msg, Throwable tr) {
        if (isDebugable()) {
            Log.v(TAG, msg, tr);
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     */
    public void warn(String msg) {
        if (isDebugable()) {
            Log.w(TAG, msg);
        }
    }

    /**
     * Send a WARN log message and log the exception.
     *
     * @param msg The message you would like logged.
     * @param tr  An exception to log
     */
    public void warn(String msg, Throwable tr) {
        if (isDebugable()) {
            Log.w(TAG, msg, tr);
        }
    }
}
