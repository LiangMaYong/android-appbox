package com.liangmayong.appbox.core.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import com.liangmayong.appbox.core.launchers.LauncherActivity;
import com.liangmayong.appbox.core.launchers.LauncherService;


/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppContext extends Application {

    // plugin appPath
    private String appPath = "";
    // plugin classloader
    private ClassLoader classLoader = null;

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
     * get plugin context
     *
     * @param base    base
     * @param appPath appPath
     * @return context
     */
    public static Context get(Context base, String appPath) {
        if (base instanceof ContextWrapper) {
            base = ((ContextWrapper) base).getBaseContext();
        }
        if (appPath == null || "".equals(appPath)) {
            return base;
        }
        if (base == null) {
            return base;
        }
        if (AppContext.class.getName().equals(base.getClass().getName())) {
            AppContext context = (AppContext) base;
            context.setAppPath(appPath);
            return context;
        }
        try {
            AppContext context = new AppContext(base);
            context.setAppPath(appPath);
            return context;
        } catch (Exception e) {
            return base;
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

    /**
     * startService
     *
     * @param service service
     * @return componentName
     */
    @Override
    public ComponentName startService(Intent service) {
        if (!LauncherActivity.class.getName().equals(service.getComponent().getClassName())) {
            Intent proxyIntent = new Intent(this, LauncherService.class);
            proxyIntent.putExtras(service);
            String path = service.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null || "".equals(path)) {
                path = appPath;
            }
            proxyIntent.putExtra(AppConstant.INTENT_APP_PATH, path);
            String serviceName = service.getStringExtra(AppConstant.INTENT_APP_ACTIVITY);
            if ((serviceName == null || "".equals(serviceName))) {
                try {
                    serviceName = service.getComponent().getClassName();
                } catch (Exception e) {
                }
            }
            proxyIntent.putExtra(AppConstant.INTENT_APP_ACTIVITY, serviceName);
            return super.startService(proxyIntent);
        }
        return super.startService(service);
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
            if (path == null || "".equals(path)) {
                path = appPath;
            }
            proxyIntent.putExtra(AppConstant.INTENT_APP_PATH, path);
            String activityName = intent.getStringExtra(AppConstant.INTENT_APP_ACTIVITY);
            if ((activityName == null || "".equals(activityName))) {
                try {
                    activityName = intent.getComponent().getClassName();
                } catch (Exception e) {
                }
            }
            proxyIntent.putExtra(AppConstant.INTENT_APP_ACTIVITY, activityName);
            super.startActivity(proxyIntent, options);
        } else {
            super.startActivity(intent, options);
        }
    }

    /**
     * getSystemService
     *
     * @param name name
     * @return object
     */
    @Override
    public Object getSystemService(String name) {
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

}
