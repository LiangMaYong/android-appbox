package com.liangmayong.android_appbox;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class AppBean implements Parcelable{

    String name = "";

    public AppBean(String name) {
        this.name = name;
    }

    protected AppBean(Parcel in) {
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppBean> CREATOR = new Creator<AppBean>() {
        @Override
        public AppBean createFromParcel(Parcel in) {
            return new AppBean(in);
        }

        @Override
        public AppBean[] newArray(int size) {
            return new AppBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
