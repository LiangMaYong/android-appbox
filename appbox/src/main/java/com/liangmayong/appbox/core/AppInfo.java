package com.liangmayong.appbox.core;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.manager.AppApplicationManager;
import com.liangmayong.appbox.core.parser.AppParser;
import com.liangmayong.appbox.core.utils.MD5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public final class AppInfo {

    // STRING_APP_INFO_MAP
    private static final Map<String, AppInfo> STRING_APP_INFO_MAP = new HashMap<String, AppInfo>();

    /**
     * get
     *
     * @param context context
     * @param appPath appPath
     * @return info
     */
    public static AppInfo get(Context context, String appPath) {
        if (appPath == null || "".equals(appPath)) {
            return null;
        }
        String key = MD5.encrypt("key_" + appPath);
        if (STRING_APP_INFO_MAP.containsKey(key)) {
            return STRING_APP_INFO_MAP.get(key);
        }
        AppInfo info = AppParser.parserApp(context, appPath);
        if (info != null) {
            STRING_APP_INFO_MAP.put(key, info);
        }
        return info;
    }


    private String lable = "";
    // packageInfo
    private PackageInfo packageInfo = null;
    // private
    private String signture = "";

    // icon
    private Drawable icon = null;
    // plugin path
    private String appPath = "";
    // infent filters
    private Map<String, IntentFilter> intentFilters;
    // configures
    private Map<String, String> configure = null;

    private AppInfo() {
    }

    /**
     * getAppPath
     *
     * @return appPath
     */
    public String getAppPath() {
        return appPath;
    }

    /**
     * getIcon
     *
     * @return icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * getSignture
     *
     * @return signture
     */
    public String getSignture() {
        return signture;
    }

    /**
     * getConfigure in appbox.xml
     *
     * @param key key
     * @return configure
     */
    public String getConfigure(String key) {
        String configureValue = "";
        if (configure != null) {
            if (configure.containsKey(key)) {
                configureValue = configure.get(key);
            }
        }
        return configureValue;
    }

    /**
     * getIntentFilters
     *
     * @return intentFilters
     */
    public Map<String, IntentFilter> getIntentFilters() {
        if (intentFilters == null) {
            return new HashMap<String, IntentFilter>();
        }
        return intentFilters;
    }

    /**
     * getPackageInfo
     *
     * @return packageInfo
     */
    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    /**
     * getLable
     *
     * @return lable
     */
    public String getLable() {
        return lable;
    }

    /**
     * getMainView
     *
     * @return view
     */
    public String getMainFragment() {
        String mainView = getConfigure("main-fragment");
        return replaceClassName(mainView);
    }

    /**
     * getMain
     *
     * @return main
     */
    public String getMain() {
        String main = getConfigure("main");
        if (main == null || "".equals(main)) {
            Map<String, IntentFilter> filters = getIntentFilters();
            for (Map.Entry<String, IntentFilter> entry : filters.entrySet()) {
                IntentFilter intentFilter = entry.getValue();
                if (intentFilter.countCategories() > 0) {
                    for (int i = 0; i < intentFilter.countCategories(); i++) {
                        String category = intentFilter.getCategory(i);
                        if (category.equals("android.intent.category.LAUNCHER")) {
                            return replaceClassName(entry.getKey());
                        }
                    }
                }
            }
            return "";
        }
        return replaceClassName(main);
    }

    /**
     * replaceClassName
     *
     * @param className className
     * @return className
     */
    private String replaceClassName(String className) {
        String newClassName = "";
        if (className.startsWith(".")) {
            newClassName = getPackageInfo().packageName + className;
        } else if (className.indexOf(".") == -1) {
            newClassName = getPackageInfo().packageName + "." + className;
        } else {
            newClassName = className;
        }
        return newClassName;
    }

    /**
     * getApplicationName
     *
     * @return application name
     */
    public String getApplicationName() {
        String appClassName = getPackageInfo().applicationInfo.className;
        if (appClassName == null || "".equals(appClassName) || "com.android.tools.fd.runtime.BootstrapApplication".equals(appClassName)) {
            appClassName = Application.class.getName();
        }
        return appClassName;
    }

    /**
     * getApplicationInfo
     *
     * @return applicationInfo
     */
    public ApplicationInfo getApplicationInfo() {
        if (getPackageInfo() == null)
            return null;
        return getPackageInfo().applicationInfo;
    }

    /**
     * getActivityInfo
     *
     * @param actName actName
     * @return activity info
     */
    public ActivityInfo getActivityInfo(String actName) {
        String activityName = replaceClassName(actName);
        if (getPackageInfo().activities != null) {
            for (ActivityInfo act : getPackageInfo().activities) {
                if (act.name.equals(activityName)) {
                    ApplicationInfo info = getApplicationInfo();
                    if (info != null) {
                        act.applicationInfo = info;
                    }
                    return act;
                }
            }
        }
        return null;
    }

    /**
     * getField
     *
     * @param fieldName fieldName
     * @return Object
     */
    public Object getField(String fieldName) {
        return AppReflect.getField(getClass(), this, fieldName);
    }

    /**
     * setField
     *
     * @param fieldName fieldName
     * @param value     value
     * @return boolean
     */
    public boolean setField(String fieldName, Object value) {
        return AppReflect.setField(getClass(), this, fieldName, value);
    }

    /**
     * getClassLoader
     *
     * @return classloader
     */
    public ClassLoader getClassLoader() {
        return AppClassLoader.getClassloader(getAppPath());
    }

    /**
     * getResources
     *
     * @return Resources
     */
    public Resources getResources(Context context) {
        return AppResources.getResources(context, getAppPath());
    }

    /**
     * getAssets
     *
     * @return AssetManager
     */
    public AssetManager getAssets(Context context) {
        return AppResources.getAssets(context, getAppPath());
    }

    /**
     * getApplication
     *
     * @return Application
     */
    public Application getApplication() {
        return AppApplicationManager.handleCreateApplication(getAppPath());
    }

    /**
     * getModel
     *
     * @param clazz     clazz
     * @param className className
     * @param <T>       type
     * @return model
     */
    public <T> T getModel(Class<T> clazz, String className) {
        if (!AppboxCore.getInstance().isInited()) {
            return null;
        }
        if (clazz != null) {
            AppModelBuilder<T> builder = new AppModelBuilder<T>(clazz);
            return builder.getModel(getAppPath(), className);
        }
        return null;
    }

    /**
     * getAppContext
     *
     * @return context
     */
    public Context getAppContext(Context base) {
        return AppContext.get(base, getAppPath());
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "lable='" + lable + '\'' +
                ", packageInfo=" + packageInfo +
                ", icon=" + icon +
                ", appPath='" + appPath + '\'' +
                ", intentFilters=" + intentFilters +
                ", configure=" + configure +
                '}';
    }
}
