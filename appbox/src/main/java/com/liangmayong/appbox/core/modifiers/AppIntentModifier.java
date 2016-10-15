package com.liangmayong.appbox.core.modifiers;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import com.liangmayong.appbox.core.AppConstant;

/**
 * Created by LiangMaYong on 2016/9/22.
 */
public class AppIntentModifier {

    private AppIntentModifier() {
    }


    public static Intent modify(Intent intent, Intent extrasIntent, ComponentName componentName, boolean replaceFlag) {
        Intent targetIntent = null;
        if (replaceFlag || (intent != null && !intent.hasExtra(AppConstant.INTENT_APP_MODIFIERED) && intent.hasExtra(AppConstant.INTENT_APP_PATH)) || (extrasIntent != null && extrasIntent.hasExtra(AppConstant.INTENT_APP_PATH))) {
            String path = intent.getStringExtra(AppConstant.INTENT_APP_PATH);
            if (path == null || "".equals(path)) {
                if (extrasIntent != null && extrasIntent.hasExtra(AppConstant.INTENT_APP_PATH)) {
                    path = extrasIntent.getStringExtra(AppConstant.INTENT_APP_PATH);
                }
                if (path == null) {
                    path = "";
                }
            }
            String launch = "";
            if (intent.hasExtra(AppConstant.INTENT_APP_LAUNCH)) {
                launch = intent.getStringExtra(AppConstant.INTENT_APP_LAUNCH);
            } else {
                launch = intent.getComponent().getClassName();
            }
            Bundle extras = intent.getExtras();
            Intent newIntent = new Intent();
            if (replaceFlag) {
                newIntent.setComponent(componentName);
            } else {
                newIntent.setComponent(intent.getComponent());
            }
            newIntent.putExtras(extras);
            newIntent.putExtra(AppConstant.INTENT_APP_LAUNCH, launch);
            newIntent.putExtra(AppConstant.INTENT_APP_PATH, path);
            newIntent.putExtra(AppConstant.INTENT_APP_MODIFIERED, true);
            targetIntent = newIntent;
        } else {
            targetIntent = intent;
        }
        return targetIntent;
    }
}
