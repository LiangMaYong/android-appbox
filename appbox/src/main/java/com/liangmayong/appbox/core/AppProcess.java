package com.liangmayong.appbox.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.Iterator;

/**
 * AppProcess
 *
 * @author LiangMaYong
 * @version 1.0
 */
public class AppProcess {

    private AppProcess() {
    }

    /**
     * getCurrentProcessName
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        Iterator i$ = mActivityManager.getRunningAppProcesses().iterator();
        ActivityManager.RunningAppProcessInfo appProcess;
        do {
            if (!i$.hasNext()) {
                return null;
            }
            appProcess = (ActivityManager.RunningAppProcessInfo) i$.next();
        } while (appProcess.pid != pid);
        return appProcess.processName;
    }
}
