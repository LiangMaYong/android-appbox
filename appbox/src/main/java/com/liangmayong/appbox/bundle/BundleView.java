package com.liangmayong.appbox.bundle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public interface BundleView {

    /**
     * activity
     *
     * @param activity activity
     * @param context  context
     * @param inflater inflater
     * @param extras   extras
     * @return view
     */
    View getView(Activity activity, Context context, LayoutInflater inflater, Bundle extras);

}
