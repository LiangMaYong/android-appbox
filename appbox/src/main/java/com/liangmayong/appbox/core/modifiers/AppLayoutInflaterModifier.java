package com.liangmayong.appbox.core.modifiers;

import android.view.LayoutInflater;
import android.view.View;

import com.liangmayong.appbox.core.AppReflect;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by LiangMaYong on 2016/9/21.
 */
public class AppLayoutInflaterModifier {

    private AppLayoutInflaterModifier() {
    }

    static HashMap<String, Constructor<? extends View>> sConstructorMap = null;

    static {
        try {
            sConstructorMap = (HashMap<String, Constructor<? extends View>>) AppReflect.getField(LayoutInflater.class, null, "sConstructorMap");
        } catch (Throwable e) {
        }
    }

    public static void clearLayoutCache() {
        if (sConstructorMap != null) {
            sConstructorMap.clear();
        }
    }
}
