package com.liangmayong.appbox.core.box;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.liangmayong.appbox.core.AppConstant;
import com.liangmayong.appbox.core.manager.AppServiceManager;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class BoxService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null && intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
            String serviceName = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null) {
                path = "";
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
            Service service = AppServiceManager.handleCreateService(this, path, serviceName);
            service.onStart(intent, startId);
            service.onStartCommand(intent, flags, startId);
        }
        return START_REDELIVER_INTENT;
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
