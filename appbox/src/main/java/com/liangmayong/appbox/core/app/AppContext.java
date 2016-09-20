package com.liangmayong.appbox.core.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;


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
        if (appPath == null || "".equals(appPath)) {
            return super.getApplicationContext();
        }
        return AppApplicationManager.handleCreateApplication(this, appPath);
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
        return AppResources.getAssets(appPath);
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
        return AppResources.getResources(appPath);
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
        if (base instanceof AppContext) {
            base = ((AppContext) base).getBaseContext();
        }
        AppContext context = new AppContext(base);
        context.setAppPath(appPath);
        return context;
    }

    @Override
    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        if (!intent.hasExtra(AppConstant.INTENT_APP_PATH)) {
            intent.putExtra(AppConstant.INTENT_APP_PATH, appPath);
            if (intent.hasExtra(AppConstant.INTENT_APP_LAUNCH) && intent.getComponent() != null) {
                intent.putExtra(AppConstant.INTENT_APP_LAUNCH, intent.getComponent().getClassName());
            }
        }
        super.startActivity(intent, options);
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


    @Override
    public ComponentName startService(Intent service) {
        if (!service.hasExtra(AppConstant.INTENT_APP_PATH)) {
            service.putExtra(AppConstant.INTENT_APP_PATH, appPath);
            if (service.hasExtra(AppConstant.INTENT_APP_LAUNCH) && service.getComponent() != null) {
                service.putExtra(AppConstant.INTENT_APP_LAUNCH, service.getComponent().getClassName());
            }
        }
        return super.startService(service);
    }
}
