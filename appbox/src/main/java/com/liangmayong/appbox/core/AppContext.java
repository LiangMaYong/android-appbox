package com.liangmayong.appbox.core;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.liangmayong.appbox.core.manager.AppApplicationManager;
import com.liangmayong.appbox.core.modifiers.AppContextModifier;


/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppContext extends Application {

    // plugin appPath
    private String appPath = "";
    // plugin classloader
    private ClassLoader classLoader = null;

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
            AppReflect.setField(base.getClass(), base, "mBasePackageName", AppApplicationManager.getHostApplication().getPackageName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                AppReflect.setField(base.getClass(), base, "mOpPackageName", AppApplicationManager.getHostApplication().getPackageName());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                AppReflect.setField(base.getClass(), base, "mPackageName", AppApplicationManager.getHostApplication().getPackageName());
            }
        }
        AppContext context = new AppContext(base);
        context.setAppPath(appPath);
        AppContextModifier.setOuterContext(base, context);
        return context;
    }

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
        AppContextModifier.setOuterContext(base, this);
    }

    @Override
    public String getPackageName() {
        if (appPath == null || "".equals(appPath)) {
            return super.getPackageName();
        }
        return AppProcess.getCurrentProcessName(this);
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (appPath == null || "".equals(appPath)) {
            return super.getApplicationInfo();
        }
        return AppInfo.get(this, appPath).getApplicationInfo();
    }

    @Override
    public Context getApplicationContext() {
        if (appPath == null || "".equals(appPath)) {
            return super.getApplicationContext();
        }
        //return this;
        return AppApplicationManager.handleCreateApplication(appPath);
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


    private Resources.Theme mTheme = null;

    /**
     * getTheme
     *
     * @return theme
     */
    @Override
    public Resources.Theme getTheme() {
        if (mTheme == null) {
            mTheme = getResources().newTheme();
            mTheme.setTo(super.getTheme());
        }
        return mTheme;
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

    @Override
    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }


    @Override
    public Object getSystemService(String name) {
        if (Context.LAYOUT_INFLATER_SERVICE.equals(name)) {
            return new AppLayoutInflater((LayoutInflater) super.getSystemService(name));
        }
        return super.getSystemService(name);
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
    public void startActivity(Intent intent, Bundle options) {
        if (!intent.hasExtra(AppConstant.INTENT_APP_PATH)) {
            intent.putExtra(AppConstant.INTENT_APP_PATH, appPath);
            if (intent.hasExtra(AppConstant.INTENT_APP_LAUNCH) && intent.getComponent() != null) {
                intent.putExtra(AppConstant.INTENT_APP_LAUNCH, intent.getComponent().getClassName());
            }
        }
        super.startActivity(intent, options);
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
