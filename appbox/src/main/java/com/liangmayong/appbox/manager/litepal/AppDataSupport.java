package com.liangmayong.appbox.manager.litepal;

import android.content.Context;

import com.liangmayong.appbox.core.AppInfo;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by LiangMaYong on 2016/9/29.
 */
public class AppDataSupport extends DataSupport {

    /**
     * findByPackageName
     *
     * @param packageName packageName
     * @return data
     */
    public static AppDataSupport findByPackageName(Context context, String packageName) {
        List<AppDataSupport> apps = DataSupport.where("packageName like ?", packageName).find(AppDataSupport.class);
        if (apps != null && apps.size() > 0) {
            AppDataSupport dataSupport = apps.get(0);
            AppInfo info = AppInfo.get(context, dataSupport.getAppPath());
            if (info == null) {
                AppDataSupport.dalete(dataSupport.getAppPath());
                return null;
            }
            return apps.get(0);
        }
        return null;
    }

    /**
     * findByAppPath
     *
     * @param appPath appPath
     * @return data
     */
    public static AppDataSupport findByAppPath(Context context, String appPath) {
        List<AppDataSupport> apps = DataSupport.where("appPath = ?", appPath).find(AppDataSupport.class);
        if (apps != null && apps.size() > 0) {
            AppDataSupport dataSupport = apps.get(0);
            AppInfo info = AppInfo.get(context, dataSupport.getAppPath());
            if (info == null) {
                AppDataSupport.dalete(dataSupport.getAppPath());
                return null;
            }
            return apps.get(0);
        }
        return null;
    }

    public static void save(Context context, String appPath) {
        if (AppDataSupport.findByAppPath(context, appPath) == null) {
            AppInfo info = AppInfo.get(context, appPath);
            if (info != null) {
                AppDataSupport dataSupport = new AppDataSupport();
                dataSupport.setAppPath(appPath);
                dataSupport.setPackageName(info.getPackageInfo().packageName);
                dataSupport.save();
            }
        }
    }

    public static void dalete(String appPath) {
        DataSupport.deleteAll(AppDataSupport.class, "appPath = ?", appPath);
    }

    // id
    private int id;
    // appPath
    private String appPath;
    // packageName
    private String packageName;

    private AppDataSupport() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppDataSupport{" +
                "id=" + id +
                ", appPath='" + appPath + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
