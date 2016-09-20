package com.liangmayong.appbox.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;

import com.liangmayong.appbox.core.listener.OnActivityLifeCycleListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppLifeCycle
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppLifeCycle {

    //currentActivity
    private static Activity currentActivity = null;
    // currentPath
    private static String currentPath = "";

    /**
     * getCurrentActivity
     *
     * @return currentActivity
     */
    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * exit
     */
    public static void exit() {
        if (!getActivities().isEmpty()) {
            for (int i = 0; i < getActivities().size(); i++) {
                try {
                    getActivities().get(i).finish();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * exitApp
     *
     * @param appPath appPath
     */
    public static void exitApp(String appPath) {
        if (STRING_ACTIVITY_LIST_MAP.containsKey(appPath)) {
            List<Activity> list = STRING_ACTIVITY_LIST_MAP.get(appPath);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    try {
                        list.get(i).finish();
                        list.remove(i);
                    } catch (Exception e) {
                    }
                }
            }
            STRING_ACTIVITY_LIST_MAP.remove(appPath);
        }
    }

    /**
     * getActivities
     *
     * @return ACTIVITIES
     */
    public static List<Activity> getActivities() {
        return ACTIVITIES;
    }

    // ACTIVITIES
    private static final List<Activity> ACTIVITIES = new ArrayList<Activity>();
    // STRING_ACTIVITY_LIST_MAP
    private static final Map<String, List<Activity>> STRING_ACTIVITY_LIST_MAP = new HashMap<String, List<Activity>>();
    // LIFE_CYCLE_LISTENERS
    private static final List<OnActivityLifeCycleListener> LIFE_CYCLE_LISTENERS = new ArrayList<OnActivityLifeCycleListener>();

    /**
     * registerActivityLifeCycleListener
     *
     * @param lifeCycleListener lifeCycleListener
     */
    public static void registerActivityLifeCycleListener(OnActivityLifeCycleListener lifeCycleListener) {
        if (!LIFE_CYCLE_LISTENERS.contains(lifeCycleListener)) {
            LIFE_CYCLE_LISTENERS.add(lifeCycleListener);
        }
    }

    /**
     * unregisterActivityLifeCycleListener
     *
     * @param lifeCycleListener lifeCycleListener
     */
    public static void unregisterActivityLifeCycleListener(OnActivityLifeCycleListener lifeCycleListener) {
        if (LIFE_CYCLE_LISTENERS.contains(lifeCycleListener)) {
            LIFE_CYCLE_LISTENERS.remove(lifeCycleListener);
        }
    }

    private AppLifeCycle() {
    }

    /**
     * onCreate
     *
     * @param target             target
     * @param savedInstanceState savedInstanceState
     */
    protected static void onCreate(Activity target, Bundle savedInstanceState) {
        if (!ACTIVITIES.contains(target)) {
            ACTIVITIES.add(target);
        }
        replaceActivity(target);
        String appPath = getAppPathByActivity(target);
        if (appPath != null && !"".equals(appPath)) {
            if (STRING_ACTIVITY_LIST_MAP.containsKey(appPath)) {
                STRING_ACTIVITY_LIST_MAP.get(appPath).add(target);
            } else {
                List<Activity> activities = new ArrayList<Activity>();
                activities.add(target);
                STRING_ACTIVITY_LIST_MAP.put(appPath, activities);
            }
        }
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onCreate(target, savedInstanceState);
            }
        }
    }

    /**
     * onStart
     *
     * @param target target
     */
    protected static void onStart(Activity target) {
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onStart(target);
            }
        }
    }

    /**
     * onRestart
     *
     * @param target target
     */
    protected static void onRestart(Activity target) {
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onRestart(target);
            }
        }
    }

    /**
     * onDestroy
     *
     * @param target target
     */
    protected static void onDestroy(Activity target) {
        if (ACTIVITIES.contains(target)) {
            ACTIVITIES.remove(target);
        }
        String appPath = getAppPathByActivity(target);
        if (appPath != null && !"".equals(appPath)) {
            if (STRING_ACTIVITY_LIST_MAP.containsKey(appPath)) {
                STRING_ACTIVITY_LIST_MAP.get(appPath).remove(target);
            }
        }
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onDestroy(target);
            }
        }
    }

    /**
     * getAppInfoByActivity
     *
     * @param target target
     * @return appinfo
     */
    private static String getAppPathByActivity(Activity target) {
        try {
            String appPath = target.getIntent().getStringExtra(AppConstant.INTENT_APP_PATH);
            return appPath;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * onPause
     *
     * @param target target
     */
    protected static void onPause(Activity target) {
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onPause(target);
            }
        }
    }

    /**
     * onStop
     *
     * @param target target
     */
    protected static void onStop(Activity target) {
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onStop(target);
            }
        }
    }

    /**
     * onResume
     *
     * @param target target
     */
    protected static void onResume(Activity target) {
        if (!LIFE_CYCLE_LISTENERS.isEmpty()) {
            for (int i = 0; i < LIFE_CYCLE_LISTENERS.size(); i++) {
                LIFE_CYCLE_LISTENERS.get(i).onResume(target);
            }
        }
        currentActivity = target;
    }

    private static void replaceActivity(Activity target) {
        String path = getAppPathByActivity(target);
        Bundle bundle = AppExtras.getExtras(path, target.getClass().getName());
        if (bundle != null) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            target.setIntent(intent);
        }
        if (!currentPath.equals(path)) {
            currentPath = path == null ? "" : path;
            //clear LayoutInflater cache
            try {
                Object sConstructorMap = AppReflect.getField(LayoutInflater.class, null, "sConstructorMap");
                AppMethod method = new AppMethod(sConstructorMap.getClass(), sConstructorMap, "clear");
                method.invoke();
            } catch (Exception e) {
            }
        }
        // resources
        Resources resources = AppResources.getResources(path);
        if (resources != null) {
            boolean flag = AppReflect.setField(target.getClass(), target, "mResources", resources);
            if (!flag) {
                AppLoger.getDefualt().error("hook resources fail");
            }
        }
        // context
        Context context = AppContext.get(target.getBaseContext(), path);
        if (context != null) {
            boolean flag = AppReflect.setField(target.getClass(), target, "mBase", context);
            if (!flag) {
                AppLoger.getDefualt().error("hook context fail");
            }
        }

        if (path != null && !"".equals(path)) {
            //TODO:replace application
        }
        AppInfo info = AppInfo.get(target, path);
        if (info != null) {
            target.setTitle(info.getLable());
            boolean flag = AppReflect.setField(target.getClass(), target, "mApplication", AppApplicationManager.handleCreateApplication(target, info.getAppPath()));
            if (!flag) {
                AppLoger.getDefualt().error("hook application fail");
            }
            ActivityInfo activityInfo = info.getActivityInfo(target.getClass().getName());
            if (activityInfo != null) {
                replaceActivityInfo(activityInfo, target);
                replaceTheme(activityInfo, resources, target);
            }
        }

        // with samsung
        if (android.os.Build.MODEL.startsWith("GT")) {
            Window window = target.getWindow();
            try {
                LayoutInflater originInflater = window.getLayoutInflater();
                if (!(originInflater instanceof AppLayoutInflater)) {
                    AppReflect.setField(window.getClass(), window, "mLayoutInflater",
                            new AppLayoutInflater(originInflater));
                }
            } catch (Throwable e) {
            }
        }
    }

    /**
     * replaceActivityInfo
     *
     * @param activityInfo activityInfo
     * @param activity     activity
     */
    private static void replaceActivityInfo(ActivityInfo activityInfo, Activity activity) {
        Field field_mActivityInfo;
        try {
            field_mActivityInfo = Activity.class.getDeclaredField("mActivityInfo");
            field_mActivityInfo.setAccessible(true);
        } catch (Exception e) {
            return;
        }
        try {
            field_mActivityInfo.set(activity, activityInfo);
        } catch (Exception e) {
        }
    }


    /**
     * replaceTheme
     *
     * @param activityInfo activityInfo
     * @param resources    resources
     * @param target       target
     */
    private static void replaceTheme(ActivityInfo activityInfo, Resources resources, Activity target) {
        boolean flag = false;
        if (activityInfo != null) {
            int resTheme = activityInfo.getThemeResource();
            if (resTheme != 0) {
                flag = true;
                boolean hasNotSetTheme = true;
                try {
                    Object theme = AppReflect.getField(ContextThemeWrapper.class, target, "mTheme");
                    hasNotSetTheme = theme == null ? true : false;
                } catch (Exception e) {
                }
                if (hasNotSetTheme) {
                    target.setTheme(resTheme);
                }
            }
        }
        if (!flag) {
            Resources.Theme mTheme = resources.newTheme();
            mTheme.setTo(target.getBaseContext().getTheme());
            AppReflect.setField(target.getClass(), target, "mTheme", mTheme);
        }
    }
}
