package com.liangmayong.appbox.core;

import android.os.Bundle;
import android.os.Parcel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppExtras {

    private AppExtras() {
    }

    //extras map
    private static final Map<String, LinkedList<Bundle>> STRING_LIST_MAP = new HashMap<String, LinkedList<Bundle>>();

    /**
     * saveExtras
     *
     * @param act    act
     * @param extras extras
     */
    public static void saveExtras(String appPath, String act, Bundle extras) {
        if (extras == null) {
            extras = new Bundle();
        }
        if (STRING_LIST_MAP.containsKey(appPath + act)) {
            STRING_LIST_MAP.get(appPath + act).add(extras);
        } else {
            LinkedList<Bundle> list = new LinkedList<Bundle>();
            list.add(extras);
            STRING_LIST_MAP.put(appPath + act, list);
        }
    }

    /**
     * getmExtras
     *
     * @param act act
     * @return extras
     */
    public synchronized static Bundle getExtras(String appPath, String act) {
        if (STRING_LIST_MAP.containsKey(appPath + act)) {
            LinkedList<Bundle> list = STRING_LIST_MAP.get(appPath + act);
            if (!list.isEmpty()) {
                Bundle extras = list.get(0);
                list.remove(extras);
                try {
                    Parcel parcel = Parcel.obtain();
                    parcel.writeBundle(extras);
                    parcel.setDataPosition(0);
                    extras = parcel.readBundle(AppClassLoader.getClassloader(appPath));
                } catch (Exception e) {
                }
                return extras;
            }
        }
        return null;
    }
}
