package com.liangmayong.android_appbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liangmayong.appbox.AppboxView;
import com.liangmayong.appbox.core.AppInfo;

/**
 * Created by LiangMaYong on 2016/9/23.
 */
public class AppFragment extends Fragment {

    AppInfo info = null;

    public void setInfo(AppInfo info) {
        this.info = info;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return AppboxView.getView(getActivity(), info.getAppPath(), info.getMainView(), null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
