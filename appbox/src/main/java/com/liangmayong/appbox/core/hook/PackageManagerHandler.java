package com.liangmayong.appbox.core.hook;

import android.content.Context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by LiangMaYong on 2016/9/19.
 */
public class PackageManagerHandler implements InvocationHandler {

    private Object mBase;
    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public PackageManagerHandler(Context context, Object base) {
        this.mBase = base;
        this.mContext = context;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(mBase, args);
    }
}
