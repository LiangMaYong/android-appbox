package com.liangmayong.android_appbox;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liangmayong.appbox.AppboxCore;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppNative;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;

    private String appName = "PLPlayer.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
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
        textView = (TextView) findViewById(R.id.textView);
        //startService(new Intent(this, MService.class));
        //AppboxCore.getInstance().startActivity(this, "", Main2Activity.class.getName());
        //AppboxCore.getInstance().startService(this, "", MService.class.getName());
        String path = "/data/user/0/com.liangmayong.android_appbox/app_appbox/" + appName;
        if (new File(path).exists()) {
            AppInfo info = AppInfo.get(MainActivity.this, path);
            if (info != null) {
                Log.e("TAG", info + "");
                //Toast.makeText(this, info + "", Toast.LENGTH_SHORT).show();
                AppboxCore.getInstance().startActivity(this, info.getAppPath(), info.getMain());
                imageView.setImageDrawable(info.getIcon());
                textView.setText(info.getLable());
            }else{
                install();
            }
        } else {
            install();
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AppNative.copyNativeLibrary("/data/user/0/com.liangmayong.android_appbox/app_appbox/1474354212104.apk");
//            }
//        }).start();
        // AppboxCore.getInstance().startActivity(this, path, "com.pili.pldroid.playerdemo.MainActivity");
        //AppboxCore.getInstance().bindService(this, "", MService.class.getName(), null, conn, Context.BIND_AUTO_CREATE);
        //install();
    }

    public void install() {
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
                    Log.e("TAG", info + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
