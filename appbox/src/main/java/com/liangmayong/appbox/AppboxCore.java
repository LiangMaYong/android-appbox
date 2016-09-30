package com.liangmayong.appbox;

import android.app.Application;
import android.app.Instrumentation;

import com.liangmayong.appbox.core.AppHookHandler;
import com.liangmayong.appbox.core.AppInstrumentation;
import com.liangmayong.appbox.core.AppReflect;

import org.litepal.LitePalApplication;

/**
 * Created by LiangMaYong on 2016/9/18.
 */
public class AppboxCore {

    private AppboxCore() {
    }

    private static volatile AppboxCore APPBOX_CORE = null;

    private boolean isInited = false;

    /**
     * isInited
     *
     * @return isInited
     */
    public boolean isInited() {
        return isInited;
    }

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
        if (isInited) {
            return true;
        }
        if (application == null) {
            return false;
        }
        AppHookHandler.hook(application);
        LitePalApplication.initialize(application);
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
            isInited = true;
            return true;
        } catch (Exception e) {
        }
        return false;
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
