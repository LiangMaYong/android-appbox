package com.liangmayong.android_appbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.liangmayong.appbox.core.app.AppConstant;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Main2Activity.class);
        intent.putExtra(AppConstant.INTENT_APP_PATH, "");
        intent.putExtra(AppConstant.INTENT_APP_ACTIVITY, Main2Activity.class.getName());
        startActivity(intent);

    }
}
