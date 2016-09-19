package com.liangmayong.android_appbox;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liangmayong.appbox.core.AppboxCore;
import com.liangmayong.appbox.core.app.AppConstant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppboxCore.getInstance().startActivity(this, "", Main2Activity.class.getName());
        AppboxCore.getInstance().startService(this,"",MService.class.getName());
    }
}
