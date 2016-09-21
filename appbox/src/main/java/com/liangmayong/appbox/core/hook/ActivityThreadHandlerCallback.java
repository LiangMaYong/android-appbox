package com.liangmayong.appbox.core.hook;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.liangmayong.appbox.core.AppReflect;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public final class ActivityThreadHandlerCallback implements Handler.Callback {

    private final Handler mBase;
    private int LAUNCH_ACTIVITY = 100;
    private final Context mContext;

    public ActivityThreadHandlerCallback(Context context, Handler base) {
        mBase = base;
        mContext = context;
        try {
            LAUNCH_ACTIVITY = (int) AppReflect.getField(Class.forName("android.app.ActivityThread$H"), null, "LAUNCH_ACTIVITY");
        } catch (Exception e) {
            LAUNCH_ACTIVITY = 100;
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == LAUNCH_ACTIVITY) {
            handleLaunchActivity(msg);
        }
        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
    }
}
