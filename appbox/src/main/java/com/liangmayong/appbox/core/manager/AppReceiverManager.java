package com.liangmayong.appbox.core.manager;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppReflect;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by LiangMaYong on 2016/9/20.
 */
public final class AppReceiverManager {

    // plugin STRING_LINKED_LIST_HASH_MAP
    private static final Map<String, LinkedList<BroadcastReceiver>> STRING_LINKED_LIST_HASH_MAP = new HashMap<String, LinkedList<BroadcastReceiver>>();


    /**
     * unregisterReceiver
     *
     * @param appPath appPath
     */
    public static void unregisterReceiver(String appPath) {
        String key = "receiver_" + appPath;
        if (STRING_LINKED_LIST_HASH_MAP.containsKey(key)) {
            LinkedList<BroadcastReceiver> receivers = STRING_LINKED_LIST_HASH_MAP.get(key);
            for (int i = 0; i < receivers.size(); i++) {
                AppApplicationManager.handleCreateApplication(appPath).unregisterReceiver(receivers.get(i));
            }
            STRING_LINKED_LIST_HASH_MAP.remove(key);
        }
    }

    /**
     * registerReceiver
     *
     * @param appPath appPath
     */
    public static void registerReceiver(String appPath) {
        String key = "receiver_" + appPath;
        if (STRING_LINKED_LIST_HASH_MAP.containsKey(key)) {
            return;
        }
        AppInfo info = AppInfo.get(AppApplicationManager.getHostApplication(), appPath);
        if (info != null) {
            Map<String, IntentFilter> filters = info.getIntentFilters();
            LinkedList<BroadcastReceiver> receivers = new LinkedList<BroadcastReceiver>();
            for (Map.Entry<String, IntentFilter> entry : filters.entrySet()) {
                String clazzName = replaceClassName(info.getPackageInfo().packageName, entry.getKey());
                Class<?> clazz = AppClassLoader.loadClass(appPath, clazzName);
                if (clazz != null && AppReflect.isGeneric(clazz, BroadcastReceiver.class.getName())) {
                    try {
                        BroadcastReceiver broadcastReceiver = (BroadcastReceiver) clazz.newInstance();
                        receivers.add(broadcastReceiver);
                        AppApplicationManager.getHostApplication().registerReceiver(broadcastReceiver, entry.getValue());
                    } catch (Exception e) {
                    }
                }
            }
            STRING_LINKED_LIST_HASH_MAP.put(key, receivers);
        }
    }

    /**
     * replaceClassName
     *
     * @param className className
     * @return className
     */
    private static String replaceClassName(String packageName, String className) {
        String newClassName = "";
        if (className.startsWith(".")) {
            newClassName = packageName + className;
        } else if (className.indexOf(".") == -1) {
            newClassName = packageName + "." + className;
        } else {
            newClassName = className;
        }
        return newClassName;
    }
}
