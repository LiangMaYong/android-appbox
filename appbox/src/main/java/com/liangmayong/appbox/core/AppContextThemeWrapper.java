package com.liangmayong.appbox.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;

import com.liangmayong.appbox.core.manager.AppApplicationManager;
import com.liangmayong.appbox.core.modifiers.AppContextModifier;


/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppContextThemeWrapper extends ContextThemeWrapper {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    public void attach(Context newBase) {
        attachBaseContext(newBase);
    }
}
