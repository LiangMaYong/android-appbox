package com.liangmayong.appbox.core.parser;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;

import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppNative;
import com.liangmayong.appbox.core.AppResources;
import com.liangmayong.appbox.core.AppSignture;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
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

    // appinfo constructor
    private static Constructor<AppInfo> mConstructor = null;

    /**
     * parserApp
     *
     * @param context context
     * @param appPath appPath
     * @return appinfo
     */
    public static AppInfo parserApp(Context context, String appPath) {
        File file = new File(appPath);
        if (file.exists() && appPath.endsWith(".apk")) {
            try {
                PackageManager pm = context.getPackageManager();
                PackageInfo pkg = pm.getPackageArchiveInfo(appPath, PackageManager.GET_ACTIVITIES);
                if (pkg != null) {
                    if (mConstructor == null) {
                        mConstructor = AppInfo.class.getDeclaredConstructor();
                    }
                    mConstructor.setAccessible(true);
                    AppInfo appInfo = mConstructor.newInstance();
                    appInfo.setField("appPath", appPath);
                    appInfo.setField("signture", AppSignture.getSignture(context, appPath));
                    AssetManager assetManager = AppResources.getAssets(context, appPath);
                    try {
                        InputStream inputStream = assetManager.open("appbox.xml");
                        List<Map<String, String>> mapLists = AppXmlParser.readXml(inputStream, "appbox");
                        if (mapLists != null && !mapLists.isEmpty()) {
                            appInfo.setField("configure", mapLists.get(0));
                        }
                    } catch (Exception e) {
                        appInfo.setField("configure", new HashMap<String, String>());
                    }
                    ApplicationInfo info = pkg.applicationInfo;
                    if (Build.VERSION.SDK_INT >= 8) {
                        info.sourceDir = appPath;
                        info.publicSourceDir = appPath;
                    }
                    info.nativeLibraryDir = AppNative.getNativePath(appPath);
                    String applicationName = AppManifestParser.getApplicationName(appPath);
                    if (applicationName != null && !"".equals(applicationName)) {
                        if (applicationName.startsWith(".")) {
                            info.className = pkg.packageName + applicationName;
                        } else if (applicationName.indexOf(".") == -1) {
                            info.className = pkg.packageName + "." + applicationName;
                        } else {
                            info.className = applicationName;
                        }
                    }
                    appInfo.setField("packageInfo", pkg);
                    appInfo.setField("icon", info.loadIcon(pm));
                    appInfo.setField("intentFilters", AppManifestParser.getIntentFilter(appPath));
                    appInfo.setField("lable", pm.getApplicationLabel(info).toString());
                    return appInfo;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
