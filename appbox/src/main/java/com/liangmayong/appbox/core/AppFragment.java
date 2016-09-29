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
    private final AppInfo info;
    // fragmentView
    private FragmentView fragmentView;

    public AppFragment(AppInfo info) {
        this.info = info;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (info != null) {
            if (AppboxCore.getInstance().isInited()) {
                try {
                    fragmentView = new FragmentView(getActivity(), info.getAppPath(), info.getMainView());
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (fragmentView != null) {
            fragmentView.onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fragmentView != null) {
            return fragmentView.getView();
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
            fragmentView.onStop();
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
        fragmentView = null;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        if (fragmentView != null) {
            fragmentView.onResume();
        }
        super.onResume();
    }

    @Override
    public void setArguments(Bundle args) {
        if (fragmentView != null) {
            fragmentView.setArguments(args);
        }
        super.setArguments(args);
    }

    /**
     * FragmentView
     */
    private static final class FragmentView {

        private Object mBase = null;

        public FragmentView(Activity activity, String appPath, String viewName) throws Exception {
            try {
                mBase = AppClassLoader.getClassloader(appPath).loadClass(viewName).newInstance();
                new AppMethod(mBase.getClass(), mBase, "attachBaseContext", Context.class).invoke(AppContext.get(activity, appPath));
                new AppMethod(mBase.getClass(), mBase, "onAttach", Activity.class).invoke(activity);
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


        /**
         * setExtras
         *
         * @param extras extras
         */
        public final void setArguments(Bundle extras) {
            try {
                new AppMethod(mBase.getClass(), mBase, "setArguments", Bundle.class).invoke(extras);
            } catch (Exception e) {
            }
        }

        public void onCreate(@Nullable Bundle savedInstanceState) {
            try {
                new AppMethod(mBase.getClass(), mBase, "onCreate", Bundle.class).invoke(savedInstanceState);
            } catch (Exception e) {
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
