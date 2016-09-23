package com.liangmayong.android_appbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppNative;
import com.liangmayong.preferences.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;
    private AppInfo info = null;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            info = (AppInfo) msg.obj;
            Preferences.getDefaultPreferences().setString(appName, info.getAppPath());
            initView();
        }
    };

    private String appName = "app2.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        info = AppInfo.get(this, Preferences.getDefaultPreferences().getString(appName));
        initView();
        startActivity(new Intent(this, Main2Activity.class));
    }

    private void initView() {
        if (info != null) {
            if (new File(info.getAppPath()).exists()) {
                imageView.setImageDrawable(info.getIcon());
                textView.setText(info.getLable() + "\n" + info.getSignture());
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (info != null) {
                        AppboxCore.getInstance().startActivity(MainActivity.this, info.getAppPath(), info.getMain());
                    }
                }
            });
        } else {
            install();
        }
    }

    public void install() {
        textView.setText("正在安装插件");
        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream out = null;
                try {
                    InputStream inputStream = getAssets().open("plugins/" + appName);
                    File dexTemp = getDir("appbox", Context.MODE_PRIVATE);
                    dexTemp.mkdirs();
                    File pluginTemp = new File(dexTemp, appName);
                    out = new FileOutputStream(pluginTemp);
                    byte[] buffer = new byte[4096];
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, read);
                    }
                    inputStream.close();
                    inputStream = null;
                    out.flush();
                    out.close();
                    out = null;
                    AppInfo info = AppInfo.get(MainActivity.this, pluginTemp.getPath());
                    AppNative.copyNativeLibrary(info.getAppPath());
                    AppClassLoader.add(info.getAppPath());
                    Message message = new Message();
                    message.what = 1;
                    message.obj = info;
                    mHandler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
