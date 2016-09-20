package com.liangmayong.appbox.core.app.parser;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;

import com.liangmayong.appbox.core.app.AppInfo;
import com.liangmayong.appbox.core.app.AppResources;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * AppParser
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppParser {

    private AppParser() {
    }

    public static AppInfo parserApp(Context context, String appPath) {
        File file = new File(appPath);
        if (file.exists() && appPath.endsWith(".apk")) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pkg = pm.getPackageArchiveInfo(appPath, PackageManager.GET_ACTIVITIES);
                if (pkg != null) {
                    AppInfo appInfo = AppInfo.c;
                    appInfo.setField("appPath", appPath);
                    AssetManager assetManager = AppResources.getAssets(context, appPath);
                    InputStream inputStream = assetManager.open("appbox.xml");
                    List<Map<String, String>> mapLists = XmlParser.readXml(inputStream, "appbox");
                    if (mapLists != null && !mapLists.isEmpty()) {
                        appInfo.setField("configure", mapLists.get(0));
                    }
                    appInfo.setField("packageInfo", pkg);
                    ApplicationInfo info = pkg.applicationInfo;
                    if (Build.VERSION.SDK_INT >= 8) {
                        info.sourceDir = appPath;
                        info.publicSourceDir = appPath;
                    }
                    String applicationName = ManifestParser.getApplicationName(appPath);
                    if (applicationName != null && !"".equals(applicationName)) {
                        if (applicationName.startsWith(".")) {
                            info.className = pkg.packageName + applicationName;
                        } else {
                            info.className = applicationName;
                        }
                    }
                    appInfo.setField("icon", info.loadIcon(pm));
                    appInfo.setField("intentFilters", ManifestParser.getIntentFilter(appPath));
                    appInfo.setField("lable", pm.getApplicationLabel(info).toString());
                    return appInfo;
                }
            } catch (Exception e) {
            }
        }
    }

}
