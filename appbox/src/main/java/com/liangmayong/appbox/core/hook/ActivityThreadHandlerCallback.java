package com.liangmayong.appbox.core.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.liangmayong.appbox.core.AppConstant;
import com.liangmayong.appbox.core.AppReflect;
import com.liangmayong.appbox.core.manager.AppApplicationManager;

import java.lang.reflect.Field;

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
//        try {
//            Object obj = msg.obj;
//            Field intent = obj.getClass().getDeclaredField("intent");
//            intent.setAccessible(true);
//            Intent raw = (Intent) intent.get(obj);
//            if (raw != null && raw.hasExtra(AppConstant.INTENT_APP_PATH)) {
//                String activityName = raw.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
//                raw.setComponent(new ComponentName(AppApplicationManager.handleCreateApplication(raw.getStringExtra(AppConstant.INTENT_APP_PATH)), activityName));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
