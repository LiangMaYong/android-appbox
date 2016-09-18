package com.liangmayong.appbox.core.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.liangmayong.appbox.core.listeners.OnActivityLifeCycleListener;

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

    /**
     * activitys
     */
    private static final List<Activity> ACTIVITIES = new ArrayList<Activity>();
    /**
     * pluginPath -> List<Activity>
     */
    private static final Map<String, List<Activity>> STRING_ACTIVITY_LIST_MAP = new HashMap<String, List<Activity>>();

    /**
     * life cycle listeners
     */
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
        Bundle bundle = AppExtras.getExtras(target.getClass().getName());
        if (bundle != null) {
            Intent intent = new Intent();
            intent.putExtras(bundle);
            target.setIntent(intent);
        }
        if (!ACTIVITIES.contains(target)) {
            ACTIVITIES.add(target);
        }
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

}
