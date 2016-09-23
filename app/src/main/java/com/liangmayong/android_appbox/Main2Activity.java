package com.liangmayong.android_appbox;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        String s = null;
//        Toast.makeText(this,s.toString(),Toast.LENGTH_SHORT).show();

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        Toast.makeText(this, processAppName + "", Toast.LENGTH_SHORT).show();
    }


    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     *
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    Log.d("Process", "Id: " + info.pid + " ProcessName: " +
                            info.processName + "  Label: " + c.toString());
                    processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e("Process", "Error>> :" + e.toString(), e);
            }
        }
        return processName;
    }

    private ApplicationInfo getApplicationInfo(String processName, int getMetaData) {
        return getBaseContext().getApplicationInfo();
    }
}
