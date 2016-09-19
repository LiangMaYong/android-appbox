package com.liangmayong.appbox.core.launchers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"LauncherActivity",Toast.LENGTH_SHORT).show();
    }
}
