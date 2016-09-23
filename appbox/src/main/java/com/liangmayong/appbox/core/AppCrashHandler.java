package com.liangmayong.appbox.core;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    // INSTANCE
    private static volatile AppCrashHandler INSTANCE;
    // mDefaultHandler
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private AppCrashHandler() {
    }

    /**
     * getInstance
     *
     * @return handler
     */
    public static AppCrashHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (AppCrashHandler.class) {
                INSTANCE = new AppCrashHandler();
            }
        }
        return INSTANCE;
    }

    /**
     * init
     */
    public void init() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * uncaughtException
     *
     * @param thread thread
     * @param ex     ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }
    }

    /**
     * handleException
     *
     * @param ex ex
     * @return boolean
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return true;
        }
        AppLoger.getDefualt().error("Intercept to the exception", ex);
        return true;
    }

}
