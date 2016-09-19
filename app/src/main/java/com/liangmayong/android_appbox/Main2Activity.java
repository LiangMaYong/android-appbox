package com.liangmayong.android_appbox;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Main2Activity" + getIntent().getExtras(), Toast.LENGTH_SHORT).show();
        //AppLifeCycle.exit();

        Intent intent = new Intent(this, Main4Activity.class);
        intent.putExtra("name", "appbox");
        AppBean bean = new AppBean("liangmayong");
        Log.d("TAG", bean + "");
        intent.putExtra("bean", bean);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, requestCode + " " + data.getStringExtra("name") + "", Toast.LENGTH_SHORT).show();
    }
}
