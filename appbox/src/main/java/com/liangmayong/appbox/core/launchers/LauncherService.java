package com.liangmayong.appbox.core.launchers;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.liangmayong.appbox.core.app.AppConstant;
import com.liangmayong.appbox.core.app.AppExtras;
import com.liangmayong.appbox.core.app.AppServiceManager;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class LauncherService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
            String serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null) {
                path = "";
            }
            Bundle bundle = AppExtras.getExtras(path, serviceName);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            Service service = AppServiceManager.handleCreateService(this, path, serviceName);
            return service.onBind(intent);
        }
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
            String serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null) {
                path = "";
            }
            Bundle bundle = AppExtras.getExtras(path, serviceName);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            Service service = AppServiceManager.handleCreateService(this, path, serviceName);
            service.onRebind(intent);
        }
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
            String serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null) {
                path = "";
            }
            Service service = AppServiceManager.handleCreateService(this, path, serviceName);
            return service.onUnbind(intent);
        }
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
            String serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null) {
                path = "";
            }
            Bundle bundle = AppExtras.getExtras(path, serviceName);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            Service service = AppServiceManager.handleCreateService(this, path, serviceName);
            service.onStart(intent, startId);
            return service.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        AppServiceManager.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppServiceManager.onDestroy();
    }
}
