package com.liangmayong.appbox.core.app;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppResources extends Resources {
    // STRING_APP_RESOURCES_MAP
    private static final Map<String, AppResources> STRING_APP_RESOURCES_MAP = new HashMap<String, AppResources>();
    // STRING_ASSET_MANAGER_MAP
    private static final Map<String, AssetManager> STRING_ASSET_MANAGER_MAP = new HashMap<String, AssetManager>();
    // ADD_ASSET_PATH_METHOD
    private static Method ADD_ASSET_PATH_METHOD = null;

    /**
     * getResources
     *
     * @param appPath appPath
     * @return resources
     */
    public static AppResources getResources(String appPath) {
        if (appPath == null || "".equals(appPath)) {
            return null;
        }
        if (STRING_APP_RESOURCES_MAP.containsKey(appPath)) {
            return STRING_APP_RESOURCES_MAP.get(appPath);
        }
        Resources resources = Resources.getSystem();
        AssetManager assets = null;
        try {
            assets = AssetManager.class.newInstance();
        } catch (Exception e) {
        }
        try {
            if (ADD_ASSET_PATH_METHOD == null) {
                ADD_ASSET_PATH_METHOD = AssetManager.class.getMethod("addAssetPath", String.class);
            }
            ADD_ASSET_PATH_METHOD.invoke(assets, appPath);
        } catch (Exception e) {
        }
        AppResources apResources = new AppResources(assets, resources.getDisplayMetrics(), resources.getConfiguration());
        STRING_APP_RESOURCES_MAP.put(appPath, apResources);
        STRING_ASSET_MANAGER_MAP.put(appPath, assets);
        return apResources;
    }

    /**
     * getAssets
     *
     * @param appPath appPath
     * @return assetManager
     */
    public static AssetManager getAssets(String appPath) {
        return getResources(appPath).getAssets();
    }

    private AppResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }
}
