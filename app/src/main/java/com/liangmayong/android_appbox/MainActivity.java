package com.liangmayong.android_appbox;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppNative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initView();
        }
    };

    private String appName = "PLPlayer.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = "/data/user/0/com.liangmayong.android_appbox/app_appbox/" + appName;
                if (new File(path).exists()) {
                    AppInfo info = AppInfo.get(MainActivity.this, path);
                    Log.e("TAG", info.getMain());
                    if (info != null) {
                        AppboxCore.getInstance().startActivity(MainActivity.this, info.getAppPath(), info.getMain());
                    }
                }
            }
        });
        initView();
//        install();

//        startService(new Intent(this, MService.class));
//        startActivity(new Intent(this, Main2Activity.class));
    }

    private void initView() {
        String path = "/data/user/0/com.liangmayong.android_appbox/app_appbox/" + appName;
        if (new File(path).exists()) {
            AppInfo info = AppInfo.get(MainActivity.this, path);
            if (info != null) {
                imageView.setImageDrawable(info.getIcon());
                textView.setText(info.getLable());
            } else {
                install();
            }
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
                    long savefile_time = System.currentTimeMillis();
                    if (!pluginTemp.exists()) {
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
                    }
                    AppInfo info = AppInfo.get(MainActivity.this, pluginTemp.getPath());
                    AppNative.copyNativeLibrary(info.getAppPath());
                    AppClassLoader.add(info.getAppPath());
                    mHandler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
