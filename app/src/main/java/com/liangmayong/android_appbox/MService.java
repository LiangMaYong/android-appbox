package com.liangmayong.android_appbox;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class MService extends Service {
    private static final String TAG = "BindService";

    public void MyMethod() {
        Log.e("TAG","MyMethod MService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return myBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class MyBinder extends Binder {

        public MService getService() {
            return MService.this;
        }
    }

    private MyBinder myBinder = new MyBinder();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand MService  " + getApplication().getClass().getName(), Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }
}
