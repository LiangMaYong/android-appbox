package com.liangmayong.appbox.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.AppConstant;
import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppModelBuilder;
import com.liangmayong.appbox.core.box.BoxActivity;
import com.liangmayong.appbox.core.box.BoxService;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public final class AppLauncher {

    private AppLauncher() {
    }


    /**
     * startActivity
     *
     * @param context activity
     * @param path    path
     * @param actName actName
     */
    public static void startActivity(Context context, String path, String actName) {
        startActivity(context, path, actName, null);
    }

    /**
     * startActivity
     *
     * @param context activity
     * @param path    path
     * @param actName actName
     * @param extars  extars
     */
    public static void startActivity(Context context, String path, String actName, Bundle extars) {
        if (!AppboxCore.getInstance().isInited()) {
            return;
        }
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, BoxActivity.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, actName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        context.startActivity(intent);
    }

    /**
     * startActivityForResult
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param path        path
     * @param actName     actName
     */
    public static void startActivityForResult(Activity activity, int requestCode, String path, String actName) {
        startActivityForResult(activity, requestCode, path, actName, null);
    }

    /**
     * startActivityForResult
     *
     * @param activity    activity
     * @param requestCode requestCode
     * @param path        path
     * @param actName     actName
     * @param extars      extars
     */
    public static void startActivityForResult(Activity activity, int requestCode, String path, String actName, Bundle extars) {
        if (!AppboxCore.getInstance().isInited()) {
            return;
        }
        if (activity == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(AppContext.get(activity, path), actName);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, actName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * startService
     *
     * @param context context
     * @param path    path
     * @param serName serName
     */
    public static ComponentName startService(Context context, String path, String serName) {
        return startService(context, path, serName, null);
    }

    /**
     * startService
     *
     * @param context context
     * @param path    path
     * @param serName serName
     * @param extars  extars
     */
    public static ComponentName startService(Context context, String path, String serName, Bundle extars) {
        if (!AppboxCore.getInstance().isInited()) {
            return null;
        }
        if (context == null) {
            return null;
        }
        Intent intent = new Intent(context, BoxService.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, serName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        return context.startService(intent);
    }

    /**
     * bindService
     *
     * @param activity activity
     * @param path     path
     * @param serName  serName
     * @param conn     conn
     * @param flags    flags
     * @return false
     */
    public static boolean bindService(Activity activity, String path, String serName, Bundle extars, ServiceConnection conn, int flags) {
        if (!AppboxCore.getInstance().isInited()) {
            return false;
        }
        if (activity == null) {
            return false;
        }
        Intent intent = new Intent(activity, BoxService.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, path);
        intent.putExtra(AppConstant.INTENT_APP_LAUNCH, serName);
        if (extars != null) {
            intent.putExtras(extars);
        }
        return activity.bindService(intent, conn, flags);
    }
}
