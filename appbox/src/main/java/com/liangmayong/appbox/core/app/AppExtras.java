package com.liangmayong.appbox.core.app;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppExtras {

    private AppExtras() {
    }

    //extras map
    private static final Map<String, List<Bundle>> STRING_LIST_MAP = new HashMap<String, List<Bundle>>();

    /**
     * saveExtras
     *
     * @param act    act
     * @param extras extras
     */
    public static void saveExtras(String act, Bundle extras) {
        if (extras == null) {
            extras = new Bundle();
        }
        if (STRING_LIST_MAP.containsKey(act)) {
            STRING_LIST_MAP.get(act).add(extras);
        } else {
            List<Bundle> list = new ArrayList<>();
            list.add(extras);
            STRING_LIST_MAP.put(act, list);
        }
    }

    /**
     * getExtras
     *
     * @param act act
     * @return extras
     */
    public synchronized static Bundle getExtras(String act) {
        if (STRING_LIST_MAP.containsKey(act)) {
            List<Bundle> list = STRING_LIST_MAP.get(act);
            if (!list.isEmpty()) {
                Bundle bundle = list.get(0);
                list.remove(0);
                return bundle;
            }
        }
        return null;
    }
}
