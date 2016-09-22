package com.liangmayong.appbox.core.hook;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import com.liangmayong.appbox.core.box.BoxActivity;
import com.liangmayong.appbox.core.box.BoxService;
import com.liangmayong.appbox.core.modifiers.AppIntentModifier;

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
        if ("startActivity".equals(method.getName()) || "startActivityAndWait".equals(method.getName()) || "startActivityWithConfig".equals(method.getName()) || "startActivityIntentSender".equals(method.getName())) {
            Pair<Integer, Intent> integerIntentPair = foundFirstIntentOfArgs(args);
            Intent intent = integerIntentPair.second;
            Intent targetIntent = AppIntentModifier.modify(intent, null, new ComponentName(mContext.getPackageName(), BoxActivity.class.getName()), false);
            args[integerIntentPair.first] = targetIntent;
        } else if ("startService".equals(method.getName()) || "bindService".equals(method.getName())) {
            Pair<Integer, Intent> integerIntentPair = foundFirstIntentOfArgs(args);
            Intent intent = integerIntentPair.second;
            Intent targetIntent = AppIntentModifier.modify(intent, null, new ComponentName(mContext.getPackageName(), BoxService.class.getName()), false);
            args[integerIntentPair.first] = targetIntent;
        }
        return method.invoke(mBase, args);
    }

    /**
     * foundFirstStringOfArgs
     *
     * @param args args
     * @return pairs
     */
    private Pair<Integer, String> foundFirstStringOfArgs(Object... args) {
        int index = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof String) {
                index = i;
                break;
            }
        }
        return Pair.create(index, (String) args[index]);
    }

    /**
     * foundFirstIntentOfArgs
     *
     * @param args args
     * @return pairs
     */
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
