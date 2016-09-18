package com.liangmayong.appbox.core;

import android.app.Application;
import android.app.Instrumentation;

import com.liangmayong.appbox.core.app.AppLifeCycle;
import com.liangmayong.appbox.core.app.AppInstrumentation;
import com.liangmayong.appbox.core.app.AppReflect;
import com.liangmayong.appbox.core.core.DefualtLifeCycle;

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
     * init
     *
     * @param application application
     * @return true or false
     */
    public boolean init(Application application) {
        if (isInit) {
            return true;
        }
        if (application == null) {
            return false;
        }
        AppLifeCycle.registerActivityLifeCycleListener(new DefualtLifeCycle());
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
