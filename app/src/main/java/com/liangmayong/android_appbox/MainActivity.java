package com.liangmayong.android_appbox;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.liangmayong.appbox.core.AppClassLoader;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppNative;
import com.liangmayong.preferences.Preferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private AppInfo info = null;
    private String appName = "universal.apk";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            info = (AppInfo) msg.obj;
            Preferences.getDefaultPreferences().setString(appName, info.getAppPath());
            initView();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        info = AppInfo.get(this, Preferences.getDefaultPreferences().getString(appName));
        initView();


    }

    private void initView() {
        if (info != null) {
            if (new File(info.getAppPath()).exists()) {
                textView.setText(info.getLable() + "\n" + info.getSignture());
            }
            AppFragment frag = new AppFragment();
            frag.setInfo(info);
            commitFragment(frag, "AppboxFragment");
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


    private Fragment mCurrentFragment;

    private void commitFragment(Fragment fragment, String tag) {
        if (mCurrentFragment != null
                && mCurrentFragment.getClass() != null
                && mCurrentFragment.getClass().getName() != null
                && fragment.getClass() != null
                && mCurrentFragment.getClass().getName().equals(fragment.getClass().getName())) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (mCurrentFragment != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        if (fragmentManager.findFragmentByTag(tag) == null) {
            fragmentTransaction.add(R.id.main_fragment, fragment, tag);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
        mCurrentFragment = fragment;
    }

}
