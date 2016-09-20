package com.liangmayong.appbox.core.app.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import com.liangmayong.appbox.core.app.AppConstant;
import com.liangmayong.appbox.core.app.AppExtras;
import com.liangmayong.appbox.core.launchers.LauncherService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class ActivityManagerHandler implements InvocationHandler {

    private static final String TAG = "ActivityManagerHandler";

    private Object mBase;
    private Context mContext;

    public ActivityManagerHandler(Context context, Object base) {
        this.mBase = base;
        this.mContext = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startService".equals(method.getName()) || "bindService".equals(method.getName())) {
            Pair<Integer, Intent> integerIntentPair = foundFirstIntentOfArgs(args);
            Intent intent = integerIntentPair.second;
            Intent targetIntent = null;
            if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_PATH)) {
                String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
                if (path == null) {
                    path = "";
                }
                String serviceName = "";
                if (intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
                    serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
                } else {
                    serviceName = intent.getComponent().getClassName();
                }
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    AppExtras.saveExtras(path, serviceName, extras);
                }
                Intent newIntent = new Intent();
                ComponentName componentName = new ComponentName(mContext.getPackageName(), LauncherService.class.getName());
                newIntent.setComponent(componentName);
                newIntent.putExtra(AppConstant.INTENT_APP_LAUNCH, serviceName);
                newIntent.putExtra(AppConstant.INTENT_APP_PATH, path);
                targetIntent = newIntent;
            } else {
                targetIntent = intent;
            }
            args[integerIntentPair.first] = targetIntent;
            return method.invoke(mBase, args);
        } else if ("stopService".equals(method.getName())) {
        }
        return method.invoke(mBase, args);
    }

    private Pair<Integer, Intent> foundFirstIntentOfArgs(Object... args) {
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                index = i;
                break;
            }
        }
        return Pair.create(index, (Intent) args[index]);
    }
}
