package com.liangmayong.appbox.manager.install;

import com.liangmayong.appbox.core.AppInfo;

import java.io.File;

/**
 * Created by LiangMaYong on 2016/9/29.
 */
public interface AppInstall {
    /**
     * updateVerifier
     *
     * @param appInfo       appInfo
     * @param targetAppInfo targetAppInfo
     * @return true or false
     */
    boolean updateVerifier(AppInfo appInfo, AppInfo targetAppInfo);

    /**
     * installVerifier
     *
     * @param targetFile targetFile
     * @return true or false
     */
    boolean installVerifier(File targetFile);

    /**
     * decompressionVerifier
     *
     * @param targetFile targetFile
     * @return unzipFile
     */
    File handlerFile(File targetFile);
}
