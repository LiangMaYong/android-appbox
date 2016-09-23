package com.liangmayong.appbox;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppContextThemeWrapper;
import com.liangmayong.appbox.core.AppMethod;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public class AppboxView {

    /**
     * getView
     *
     * @param activity activity
     * @param appPath  appPath
     * @param viewName viewName
     * @param extras   extras
     * @return view
     */
    public static View getView(Activity activity, String appPath, String viewName, Bundle extras) {
        AppContextThemeWrapper themeWrapper = new AppContextThemeWrapper();
        themeWrapper.attach(AppContext.get(activity, appPath));
        LayoutInflater layoutInflater = LayoutInflater.from(activity).cloneInContext(themeWrapper);
        try {
            Object viewObj = AppClassLoader.getClassloader(appPath).loadClass(viewName).newInstance();
            return new AppMethod(viewObj.getClass(), viewObj, "getView", Activity.class, Context.class, LayoutInflater.class, Bundle.class).invoke(activity, themeWrapper, layoutInflater, extras);
        } catch (Exception e) {
        }
        return null;
    }

}
