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
public final class AppResources extends Resources {

    // STRING_RESOURCES_HASH_MAP
    private static final Map<String, AppResources> STRING_RESOURCES_HASH_MAP = new HashMap<String, AppResources>();
    // ADD_ASSET_PATH_METHOD
    private static Method ADD_ASSET_PATH_METHOD = null;

    /**
     * getResources
     *
     * @param appPath appPath
     * @return resources
     */
    public static Resources getResources(String appPath) {
        if (appPath == null || "".equals(appPath)) {
            return null;
        }
        String key = "resources_" + appPath;
        if (STRING_RESOURCES_HASH_MAP.containsKey(key)) {
            return STRING_RESOURCES_HASH_MAP.get(key);
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
                ADD_ASSET_PATH_METHOD.setAccessible(true);
            }
            ADD_ASSET_PATH_METHOD.invoke(assets, appPath);
        } catch (Exception e) {
        }
        AppResources apResources = new AppResources(assets, resources.getDisplayMetrics(), resources.getConfiguration());
        STRING_RESOURCES_HASH_MAP.put(key, apResources);
        return apResources;
    }

    /**
     * getAssets
     *
     * @param appPath appPath
     * @return assetManager
     */
    public static AssetManager getAssets(String appPath) {
        Resources resources = getResources(appPath);
        if (resources != null) {
            return getResources(appPath).getAssets();
        }
        return null;
    }

    /**
     * AppResources
     *
     * @param assets  assets
     * @param metrics metrics
     * @param config  config
     */
    private AppResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }

}
