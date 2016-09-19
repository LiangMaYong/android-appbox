package com.liangmayong.android_appbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "MAIN4", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, getIntent().getStringExtra("name") + "", Toast.LENGTH_SHORT).show();
        AppBean bean = getIntent().getParcelableExtra("bean");
        Toast.makeText(this, bean.getName() + "", Toast.LENGTH_SHORT).show();
        Log.d("TAG", bean + "");

        Intent data = new Intent();
        data.putExtra("name", "MAIN4");
        setResult(RESULT_OK, data);

    }
}
