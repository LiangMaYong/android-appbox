package com.liangmayong.appbox.core.app;

import java.io.File;

/**
 * Created by liangmayong on 2016/9/18.
 */
public class AppNative {

    private AppNative() {
    }

    /**
     * getNativePath
     *
     * @param pluginPath pluginPath
     * @return library Path
     */
    public static String getNativePath(String pluginPath) {
        try {
            String libraryDir = new File(pluginPath).getParent() + "/libs/";
            File file = new File(libraryDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getPath();
        } catch (Exception e) {
        }
        return "";
    }
}
