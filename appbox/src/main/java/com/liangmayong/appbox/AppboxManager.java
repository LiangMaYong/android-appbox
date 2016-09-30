package com.liangmayong.appbox;

import android.content.Context;

import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.manager.install.OnInstallListener;
import com.liangmayong.appbox.manager.litepal.AppDataSupport;
import com.liangmayong.appbox.manager.verifier.OnVerifierListener;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LiangMaYong on 2016/9/29.
 */
public class AppboxManager {
    private static volatile AppboxManager INSTANCE = null;

    public static AppboxManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AppboxManager.class) {
                INSTANCE = new AppboxManager();
            }
        }
        return INSTANCE;
    }

    private AppboxManager() {
    }

    private int corePoolSize = 3;
    private int maximumPoolSize = 5;
    private int keepAliveTime = 3;
    private ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(
            corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(3),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    private ThreadPoolExecutor getPoolExecutor() {
        return poolExecutor;
    }

    /**
     * isInstall
     *
     * @param context     context
     * @param packageName packageName
     * @return install flag
     */
    public boolean isInstalled(Context context, String packageName) {
        AppDataSupport dataSupport = AppDataSupport.findByPackageName(context, packageName);
        if (dataSupport != null) {
            return true;
        }
        return false;
    }

    /**
     * getAppInfoByPackageName
     *
     * @param context     context
     * @param packageName packageName
     * @return info
     */
    public AppInfo findApp(Context context, String packageName) {
        AppDataSupport dataSupport = AppDataSupport.findByPackageName(context, packageName);
        if (dataSupport != null) {
            return AppInfo.get(context, dataSupport.getAppPath());
        }
        return null;
    }

    /**
     * uninstall
     *
     * @param context     context
     * @param packageName packageName
     */
    public void uninstall(Context context, String packageName) {
        if (!isInstalled(context, packageName)) {
            return;
        }
        getPoolExecutor().execute(new UnInstallRun(context, findApp(context, packageName)));
    }

    /**
     * install
     *
     * @param context          context
     * @param stream           stream
     * @param installListener  installListener
     * @param verifierListener verifierListener
     */
    public void install(Context context, InputStream stream, OnInstallListener installListener, OnVerifierListener verifierListener) {
        getPoolExecutor().execute(new InstallRun(context, stream, installListener, verifierListener));
    }

    /**
     * InstallRun
     */
    private class InstallRun implements Runnable {

        public InstallRun(Context context, InputStream stream, OnInstallListener installListener, OnVerifierListener verifierListener) {
        }

        @Override
        public void run() {

        }
    }

    /**
     * UnInstallRun
     */
    private class UnInstallRun implements Runnable {

        public UnInstallRun(Context context, AppInfo info) {
        }

        @Override
        public void run() {
        }
    }
}
