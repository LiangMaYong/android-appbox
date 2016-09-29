package com.liangmayong.appbox.bundle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public abstract class BundleFragmentView {

    // mActivity
    private Activity mActivity;
    // mView
    private View mView;
    // mContext
    private Context mContext;
    // mExtras
    private Bundle mExtras;

    protected void onAttach(Activity activity, Context context, Bundle extras) {
        this.mActivity = activity;
        this.mContext = context;
        this.mExtras = extras;
    }

    /**
     * getHostActivity
     *
     * @return activity
     */
    protected Activity getHostActivity() {
        return mActivity;
    }

    /**
     * getBundleContext
     *
     * @return context
     */
    protected Context getBundleContext() {
        return mContext;
    }

    /**
     * getExtras
     *
     * @return extras
     */
    protected Bundle getExtras() {
        return mExtras;
    }

    /**
     * getView
     *
     * @return view
     */
    public View getView() {
        if (mView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getHostActivity()).cloneInContext(getBundleContext());
            mView = onCreateView(getBundleContext(), layoutInflater);
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
     * startActivity
     *
     * @param intent intent
     */
    public void startActivity(Intent intent) {
        getBundleContext().startActivity(intent);
    }

    /**
     * startActivity
     *
     * @param cls    cls
     * @param extras extras
     */
    public void startActivity(Class<? extends Activity> cls, Bundle extras) {
        Intent intent = new Intent(getBundleContext(), cls);
        if (extras != null) {
            intent.putExtras(extras);
        }
        getBundleContext().startActivity(intent);
    }

    /**
     * onDetach
     */
    public void onDetach() {
        mActivity = null;
        mContext = null;
        mExtras = null;
    }

    /**
     * onDestroyView
     */
    public void onDestroyView() {
        mView = null;
    }
}
