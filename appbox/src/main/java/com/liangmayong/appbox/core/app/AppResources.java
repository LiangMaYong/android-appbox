package com.liangmayong.appbox.core.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppResources extends Resources {
    // STRING_APP_RESOURCES_MAP
    private static final Map<String, AppResources> STRING_APP_RESOURCES_MAP = new HashMap<String, AppResources>();
    // ADD_ASSET_PATH_METHOD
    private static Method ADD_ASSET_PATH_METHOD = null;

    /**
     * getResources
     *
     * @param appPath appPath
     * @return resources
     */
    public static Resources getResources(Context context, String appPath) {
        String key = "res_" + appPath;
        if (STRING_APP_RESOURCES_MAP.containsKey(key)) {
            return STRING_APP_RESOURCES_MAP.get(key);
        }
        AssetManager assets = null;
        Resources resources = null;
        if (appPath != null && !"".equals(appPath) && !(new File(appPath)).exists()) {
            try {
                resources = Resources.getSystem();
                assets = AssetManager.class.newInstance();
                if (ADD_ASSET_PATH_METHOD == null) {
                    ADD_ASSET_PATH_METHOD = AssetManager.class.getMethod("addAssetPath", String.class);
                }
                ADD_ASSET_PATH_METHOD.invoke(assets, appPath);
            } catch (Exception e) {
            }
        } else {
            resources = context.getResources();
            assets = resources.getAssets();
        }
        AppResources apResources = new AppResources(assets, resources.getDisplayMetrics(), resources.getConfiguration());
        STRING_APP_RESOURCES_MAP.put(key, apResources);
        return apResources;
    }

    /**
     * getAssets
     *
     * @param appPath appPath
     * @return assetManager
     */
    public static AssetManager getAssets(Context context, String appPath) {
        return getResources(context, appPath).getAssets();
    }

    private AppResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

}
