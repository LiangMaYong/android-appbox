package com.liangmayong.android_appbox;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.liangmayong.appbox.core.AppboxCore;
import com.liangmayong.appbox.core.app.AppConstant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppboxCore.getInstance().startActivity(this, "", Main2Activity.class.getName());
        AppboxCore.getInstance().startService(this, "", MService.class.getName());
        AppboxCore.getInstance().bindService(this, "", MService.class.getName(), null, conn, Context.BIND_AUTO_CREATE);
    }

    boolean flag = false;

    private void unBind() {
        if (flag == true) {
            unbindService(conn);
            flag = false;
        }
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // TODO Auto-generated method stub
            //
            MService.MyBinder binder = (MService.MyBinder) service;
            MService bindService = binder.getService();
            bindService.MyMethod();
            flag = true;

            unBind();
        }
    };

}
