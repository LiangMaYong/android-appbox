package com.liangmayong.appbox.core.modifiers;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Window;

import com.liangmayong.appbox.core.AppContext;
import com.liangmayong.appbox.core.AppInfo;
import com.liangmayong.appbox.core.AppLayoutInflater;
import com.liangmayong.appbox.core.AppLoger;
import com.liangmayong.appbox.core.AppReflect;
import com.liangmayong.appbox.core.AppResources;
import com.liangmayong.appbox.core.manager.AppApplicationManager;

import java.lang.reflect.Field;

/**
 * Created by LiangMaYong on 2016/9/21.
 */
public class AppActivityModifier {
    private AppActivityModifier() {
    }

    // currentPath
    private static String currentPath = "";

    public static void modify(Activity target, String path) {
        if (!currentPath.equals(path)) {
            currentPath = path == null ? "" : path;
            //clear LayoutInflater cache
            AppLayoutInflaterModifier.clearLayoutCache();
        }
        // resources
        Resources resources = AppResources.getResources(target, path);
        if (resources != null) {
            boolean flag = AppReflect.setField(target.getClass(), target, "mResources", resources);
            if (!flag) {
                AppLoger.getDefualt().error("hook Resources fail");
            }
        }
        // context
        Context context = AppContext.get(target.getBaseContext(), path);
        if (context != null) {
            boolean flag = AppReflect.setField(target.getClass(), target, "mBase", context);
            if (!flag) {
                AppLoger.getDefualt().error("hook Context fail");
            }
        }

        if (path != null && !"".equals(path)) {
            AppInfo info = AppInfo.get(target, path);
            if (info != null) {
                try {
                    target.setTitle(info.getLable());
                } catch (Exception e) {
                }
                boolean flag = AppReflect.setField(target.getClass(), target, "mApplication", AppApplicationManager.handleCreateApplication(info.getAppPath()));
                if (!flag) {
                    AppLoger.getDefualt().error("hook application fail");
                }
                ActivityInfo activityInfo = info.getActivityInfo(target.getClass().getName());
                if (activityInfo != null) {
                    replaceActivityInfo(activityInfo, target);
                    replaceTheme(activityInfo, context, target);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = target.getIntent();
                        if (intent != null && target.isTaskRoot()) {
                            String label = "" + info.getLable();
                            Bitmap icon = null;
                            Drawable drawable = info.getIcon();
                            if (drawable instanceof BitmapDrawable) {
                                icon = ((BitmapDrawable) drawable).getBitmap();
                            }
                            target.setTaskDescription(new ActivityManager.TaskDescription(label, icon));
                        }
                    }
                    if (target.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                            && activityInfo.screenOrientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                        target.setRequestedOrientation(activityInfo.screenOrientation);
                    }
                }
            }
        }

        Window window = target.getWindow();
        LayoutInflater originInflater = target.getLayoutInflater();
        if (!(originInflater instanceof AppLayoutInflater)) {
            boolean flag = AppReflect.setField(window.getClass(), window, "mLayoutInflater",
                    new AppLayoutInflater(originInflater));
            if (!flag) {
                AppLoger.getDefualt().error("hook LayoutInflater fail");
            }
        }

        try {
            Class<?> clazz = Class.forName("com.android.internal.R.styleable");
            TypedArray typedArray = target.obtainStyledAttributes((int[]) AppReflect.getField(clazz, null, "Window"));
            if (typedArray != null) {
                boolean showWallpaper = typedArray.getBoolean((Integer) AppReflect.getField(clazz, null, "Window_windowShowWallpaper"),
                        false);
                if (showWallpaper) {
                    target.getWindow().setBackgroundDrawable(WallpaperManager.getInstance(target).getDrawable());
                }
                typedArray.recycle();
            }
        } catch (Throwable e) {
        }
    }

    /**
     * replaceActivityInfo
     *
     * @param activityInfo activityInfo
     * @param activity     activity
     */
    private static void replaceActivityInfo(ActivityInfo activityInfo, Activity activity) {
        Field field_mActivityInfo;
        try {
            field_mActivityInfo = Activity.class.getDeclaredField("mActivityInfo");
            field_mActivityInfo.setAccessible(true);
        } catch (Exception e) {
            return;
        }
        try {
            field_mActivityInfo.set(activity, activityInfo);
        } catch (Exception e) {
        }
    }


    /**
     * replaceTheme
     *
     * @param activityInfo activityInfo
     * @param target       target
     */
    private static void replaceTheme(ActivityInfo activityInfo, Context context, Activity target) {
        if (activityInfo != null) {
            int resTheme = activityInfo.getThemeResource();
            AppReflect.setField(target.getClass(), target, "mTheme", context.getTheme());
            if (resTheme != 0) {
                context.getTheme().applyStyle(resTheme, true);
            }
        }
    }

    /**
     * isSetTheme
     *
     * @param target target
     * @return boolean
     */
    private static boolean isSetTheme(Activity target) {
        boolean isSet = false;
        try {
            Object theme = AppReflect.getField(ContextThemeWrapper.class, target, "mTheme");
            isSet = theme != null;
        } catch (Exception e) {
        }
        return isSet;
    }
}
