package com.liangmayong.appbox.core.app;

import android.app.Application;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import java.util.Map;

/**
 * Created by liangmayong on 2016/9/19.
 */
public final class AppInfo {

    private String lable = "";
    // packageInfo
    private PackageInfo packageInfo = null;
    // icon
    private Drawable icon = null;
    // plugin path
    private String appPath = "";
    // infent filters
    private Map<String, IntentFilter> intentFilters;
    // configures
    private Map<String, String> configure = null;
    // application
    private Application application = null;

    public AppInfo() {
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public PackageInfo getPackageInfo() {
        return packageInfo;
    }

    public void setPackageInfo(PackageInfo packageInfo) {
        this.packageInfo = packageInfo;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public Map<String, IntentFilter> getIntentFilters() {
        return intentFilters;
    }

    public void setIntentFilters(Map<String, IntentFilter> intentFilters) {
        this.intentFilters = intentFilters;
    }

    public Map<String, String> getConfigure() {
        return configure;
    }

    public void setConfigure(Map<String, String> configure) {
        this.configure = configure;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }


    /**
     * getActivityInfo
     *
     * @param actName actName
     * @return activity info
     */
    public ActivityInfo getActivityInfo(String actName) {
        if (getPackageInfo().activities == null) {
            return null;
        }
        for (ActivityInfo act : getPackageInfo().activities) {
            if (act.name.equals(replaceClassName(actName))) {
                //act.applicationInfo.i
                try {
                    ApplicationInfo info = getApplicationInfo();
                    if (info != null) {
                        act.applicationInfo = info;
                    }
                } catch (Exception e) {
                }
                return act;
            }
        }
        return null;
    }

    /**
     * getApplicationInfo
     *
     * @return info
     */
    public ApplicationInfo getApplicationInfo() {
        PackageInfo info = getPackageInfo();
        if (info == null)
            return null;
        return info.applicationInfo;
    }

    /**
     * replaceClassName
     *
     * @param className className
     * @return classname
     */
    private String replaceClassName(String className) {
        if (className == null || "".equals(className)) {
            return "";
        }
        String newClassName = "";
        if (className.startsWith(".")) {
            newClassName = getPackageInfo().packageName + className;
        }
        if (className.indexOf(".") == -1) {
            newClassName = getPackageInfo().packageName + "." + className;
        }
        return newClassName;
    }

    /**
     * getField
     *
     * @param fieldName fieldName
     * @return Object
     */
    public final Object getField(String fieldName) {
        return AppReflect.getField(getClass(), this, fieldName);
    }

    /**
     * setField
     *
     * @param fieldName fieldName
     * @param value     value
     * @return boolean
     */
    public final boolean setField(String fieldName, Object value) {
        return AppReflect.setField(getClass(), this, fieldName, value);
    }
}
