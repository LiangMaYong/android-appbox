package com.liangmayong.appbox.core.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by liangmayong on 2016/9/19.
 */
public class AppApplicationManager {

    public static Application handleCreateApplication(Context context, String appPath, String serviceName) {
        return (Application) AppContext.get(context, appPath);
    }

}
