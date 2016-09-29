package com.liangmayong.appbox.core;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liangmayong.appbox.AppboxCore;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public class AppFragment extends Fragment {

    // info
    private AppInfo info = null;
    // fragmentView
    private AppFragmentView fragmentView;

    /**
     * setAppInfo
     *
     * @param info info
     */
    public void setAppInfo(AppInfo info) {
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (info != null) {
            if (AppboxCore.getInstance().isInited()) {
                try {
                    fragmentView = new AppFragmentView(getActivity(), info.getAppPath(), info.getMainView(), null);
                    return fragmentView.getView();
                } catch (Exception e) {
                }
            }
        }
        return null;
    }

    @Override
    public void onDestroyView() {
        if (fragmentView != null) {
            fragmentView.onDestroyView();
        }
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        if (fragmentView != null) {
            fragmentView.onStart();
        }
        super.onStart();
    }

    @Override
    public void onStop() {
        if (fragmentView != null) {
            fragmentView.onStart();
        }
        super.onStop();
    }


    @Override
    public void onPause() {
        if (fragmentView != null) {
            fragmentView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDetach() {
        if (fragmentView != null) {
            fragmentView.onDetach();
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        if (fragmentView != null) {
            fragmentView.onDestroy();
        }
        super.onDestroy();
    }

    /**
     * AppFragmentView
     */
    private static final class AppFragmentView {

        private Object mBase = null;

        public AppFragmentView(Activity activity, String appPath, String viewName, Bundle extras) throws Exception {
            try {
                mBase = AppClassLoader.getClassloader(appPath).loadClass(viewName).newInstance();
                AppContextThemeWrapper themeWrapper = new AppContextThemeWrapper();
                themeWrapper.attach(AppContext.get(activity, appPath));
                new AppMethod(mBase.getClass(), mBase, "onAttach", Activity.class, Context.class, Bundle.class).invoke(activity, themeWrapper, extras);
            } catch (Exception e) {
                throw e;
            }
        }

        public View getView() {
            try {
                View view = new AppMethod(mBase.getClass(), mBase, "getView").invoke();
                return view;
            } catch (Exception e) {
                return null;
            }
        }

        public void onStart() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onStart").invoke();
            } catch (Exception e) {
            }
        }

        public void onResume() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onResume").invoke();
            } catch (Exception e) {
            }
        }

        public void onPause() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onPause").invoke();
            } catch (Exception e) {
            }
        }

        public void onStop() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onStop").invoke();
            } catch (Exception e) {
            }
        }

        public void onDestroy() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onDestroy").invoke();
            } catch (Exception e) {
            }
        }

        public void onDetach() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onDetach").invoke();
            } catch (Exception e) {
            }
        }

        public void onDestroyView() {
            try {
                new AppMethod(mBase.getClass(), mBase, "onDestroyView").invoke();
            } catch (Exception e) {
            }
        }
    }
}
