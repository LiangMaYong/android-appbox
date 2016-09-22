package com.liangmayong.appbox.core;

import android.content.Context;
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

    // STRING_ASSET_MANAGER_HASH_MAP
    private static final Map<String, AssetManager> STRING_ASSET_MANAGER_HASH_MAP = new HashMap<String, AssetManager>();
    // ADD_ASSET_PATH_METHOD
    private static Method ADD_ASSET_PATH_METHOD = null;

    public static AssetManager getAssets(Context context, String appPath) {
        String key = "asset_" + appPath;
        if (STRING_ASSET_MANAGER_HASH_MAP.containsKey(key)) {
            return STRING_ASSET_MANAGER_HASH_MAP.get(key);
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            if (ADD_ASSET_PATH_METHOD == null) {
                ADD_ASSET_PATH_METHOD = AssetManager.class.getMethod("addAssetPath", String.class);
                ADD_ASSET_PATH_METHOD.setAccessible(true);
            }
            ADD_ASSET_PATH_METHOD.invoke(assetManager, appPath);
            STRING_ASSET_MANAGER_HASH_MAP.put(key, assetManager);
            return assetManager;
        } catch (Throwable th) {
        }
        return context.getAssets();
    }

    // STRING_RESOURCES_HASH_MAP
    private static final Map<String, AppResources> STRING_RESOURCES_HASH_MAP = new HashMap<String, AppResources>();

    /**
     * remove
     *
     * @param appPath appPath
     */
    public static void remove(String appPath) {
        String reskey = "resources_" + appPath;
        if (STRING_RESOURCES_HASH_MAP.containsKey(reskey)) {
            STRING_RESOURCES_HASH_MAP.remove(reskey);
        }
        String assetkey = "asset_" + appPath;
        if (STRING_ASSET_MANAGER_HASH_MAP.containsKey(assetkey)) {
            STRING_ASSET_MANAGER_HASH_MAP.remove(assetkey);
        }
    }

    /**
     * getResources
     *
     * @param appPath appPath
     * @return resources
     */
    public static Resources getResources(Context context, String appPath) {
        if (appPath == null || "".equals(appPath)) {
            return context.getResources();
        }
        String key = "resources_" + appPath;
        if (STRING_RESOURCES_HASH_MAP.containsKey(key)) {
            return STRING_RESOURCES_HASH_MAP.get(key);
        }
        AssetManager assetManager = getAssets(context, appPath);
        AppResources apResources = new AppResources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        STRING_RESOURCES_HASH_MAP.put(key, apResources);
        return apResources;
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

//    public static void parserXML(XmlResourceParser xmlResourceParser) throws Exception {
//
//        int event = xmlResourceParser.getEventType();//
//        while (event != XmlResourceParser.END_DOCUMENT) {
//            switch (event) {
//                case XmlResourceParser.START_DOCUMENT://start
//                    break;
//                case XmlResourceParser.START_TAG:
//                    Log.e("TAG", "-----------------------------------------------------------------");
//                    Log.e("TAG", "start name:" + xmlResourceParser.getName());
//                    Log.e("TAG", "-----------------------------------------------------------------");
//                    for (int i = 0; i < xmlResourceParser.getAttributeCount(); i++) {
//                        Log.e("TAG", xmlResourceParser.getAttributeName(i) + "=" + xmlResourceParser.getAttributeValue(i));
//                    }
//                    break;
//                case XmlResourceParser.END_TAG://end
//                    break;
//            }
//            event = xmlResourceParser.next();//next
//        }
//    }
}
