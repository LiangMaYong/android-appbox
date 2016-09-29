package com.liangmayong.appbox.bundle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public abstract class BundleFragment extends ContextThemeWrapper {

    // mActivity
    private Activity mActivity;
    // mView
    private View mView;
    // mExtras
    private Bundle mExtras;

    @Override
    protected final void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    /**
     * attachActivity
     *
     * @param activity activity
     */
    protected void onAttach(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * setArguments
     *
     * @param extras extras
     */
    public final void setArguments(Bundle extras) {
        this.mExtras = extras;
    }

    /**
     * getArguments
     *
     * @return extras
     */
    protected Bundle getArguments() {
        if (mExtras == null) {
            return new Bundle();
        }
        return new Bundle(mExtras);
    }

    /**
     * getHostActivity
     *
     * @return activity
     */
    protected final Activity getHostActivity() {
        return mActivity;
    }

    /**
     * getContext
     *
     * @return context
     */
    public Context getContext() {
        return this;
    }

    /**
     * getView
     *
     * @return view
     */
    public final View getView() {
        if (mView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getHostActivity()).cloneInContext(this);
            mView = onCreateView(this, layoutInflater);
        }
        return mView;
    }

    /**
     * onCreateView
     *
     * @param context  context
     * @param inflater inflater
     * @return view
     */
    protected abstract View onCreateView(Context context, LayoutInflater inflater);

    /**
     * onCreate
     *
     * @param savedInstanceState savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState) {
    }

    /**
     * onStart
     */
    public void onStart() {
    }

    /**
     * onStart
     */
    public void onResume() {
    }

    /**
     * onStart
     */
    public void onPause() {
    }

    /**
     * onStop
     */
    public void onStop() {
    }

    /**
     * onDestroy
     */
    public void onDestroy() {
    }

    /**
     * onDetach
     */
    public void onDetach() {
        mActivity = null;
        mExtras = null;
    }

    /**
     * onDestroyView
     */
    public void onDestroyView() {
        mView = null;
    }
}
