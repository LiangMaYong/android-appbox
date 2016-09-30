package com.liangmayong.appbox.manager.install;

import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.manager.throwables.InstallError;

/**
 * Created by LiangMaYong on 2016/9/30.
 */
public interface OnInstallListener {

    void onInstalled(AppInfo info);

    void onFailed(InstallError error);

    void onStep(int step, String msg);

}
