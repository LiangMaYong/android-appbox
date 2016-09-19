package com.liangmayong.android_appbox;

import android.app.Application;

import com.liangmayong.appbox.core.AppboxCore;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppboxCore.getInstance().initialize(this);
    }
}
