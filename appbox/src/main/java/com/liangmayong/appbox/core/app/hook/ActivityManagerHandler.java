package com.liangmayong.appbox.core.app.hook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;

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
        Log.e(TAG, method.getName());
        if ("startService".equals(method.getName())) {

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
