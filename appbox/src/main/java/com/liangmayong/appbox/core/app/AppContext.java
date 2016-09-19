package com.liangmayong.appbox.core.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.liangmayong.appbox.core.launchers.LauncherActivity;


/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppContext extends Application {

    // plugin appPath
    private String appPath = "";
    // plugin classloader
    private ClassLoader classLoader = null;

    /**
     * AppContext
     *
     * @param base
     */
    private AppContext(Context base) {
        try {
            AppMethod method = new AppMethod(getClass(), this, "attach", Context.class);
            method.invoke(base);
        } catch (Exception e) {
        }
    }

    @Override
    public Context getApplicationContext() {
        return this;
    }

    /**
     * getClassLoader
     *
     * @return classLoader
     */
    @Override
    public ClassLoader getClassLoader() {
        if (appPath == null || "".equals(appPath)) {
            return super.getClassLoader();
        }
        if (classLoader == null) {
            classLoader = AppClassLoader.getClassloader(appPath);
        }
        return classLoader;
    }


    /**
     * getAssets
     *
     * @return assets
     */
    @Override
    public AssetManager getAssets() {
        if (appPath == null || "".equals(appPath)) {
            return super.getAssets();
        }
        return AppResources.getAssets(getBaseContext(), appPath);
    }

    /**
     * getResources
     *
     * @return resources
     */
    @Override
    public Resources getResources() {
        if (appPath == null || "".equals(appPath)) {
            return super.getResources();
        }
        return AppResources.getResources(getBaseContext(), appPath);
    }


    /**
     * setAppPath
     *
     * @param appPath appPath
     */
    private void setAppPath(String appPath) {
        if (this.appPath == null) {
            this.appPath = appPath;
            this.classLoader = null;
        } else if (!this.appPath.equals(appPath)) {
            this.appPath = appPath;
            this.classLoader = null;
        }
    }

    /**
     * get
     *
     * @param base    base
     * @param appPath appPath
     * @return context
     */
    public static Context get(Context base, String appPath) {
        AppContext context = new AppContext(base);
        context.setAppPath(appPath);
        return context;
    }


    /**
     * startActivity
     *
     * @param intent intent
     */
    @Override
    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    /**
     * startActivity
     *
     * @param intent  intent
     * @param options options
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void startActivity(Intent intent, Bundle options) {
        if (!LauncherActivity.class.getName().equals(intent.getComponent().getClassName())) {
            Intent proxyIntent = new Intent(this, LauncherActivity.class);
            proxyIntent.putExtras(intent);
            proxyIntent.setFlags(intent.getFlags());
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path != null && !"".equals(path)) {
                proxyIntent.putExtra(AppConstant.INTENT_APP_PATH, path);
            } else {
                proxyIntent.putExtra(AppConstant.INTENT_APP_PATH, appPath);
            }
            String launch = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            if ((launch == null || "".equals(launch))) {
                try {
                    launch = intent.getComponent().getClassName();
                } catch (Exception e) {
                }
            }
            proxyIntent.putExtra(AppConstant.INTENT_APP_LAUNCH, launch);
            super.startActivity(proxyIntent, options);
        } else {
            super.startActivity(intent, options);
        }
    }

    /**
     * startActivities
     *
     * @param intents intents
     */
    @Override
    public void startActivities(Intent[] intents) {
        for (int i = 0; i < intents.length; i++) {
            startActivity(intents[0]);
        }
    }

    /**
     * startActivities
     *
     * @param intents intents
     * @param options options
     */
    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        for (int i = 0; i < intents.length; i++) {
            startActivity(intents[0], options);
        }
    }
}
