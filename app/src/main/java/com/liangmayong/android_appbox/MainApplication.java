package com.liangmayong.android_appbox;

import android.app.Application;

import com.liangmayong.appbox.AppboxCore;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppboxCore.getInstance().initialize(this);
    }
}
