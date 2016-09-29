package com.liangmayong.appbox;

/**
 * Created by LiangMaYong on 2016/9/29.
 */
public class AppboxManager {

    private static volatile AppboxManager INSTANCE = null;

    public static AppboxManager getInstance() {
        if (INSTANCE == null) {
            synchronized (AppboxManager.class) {
                INSTANCE = new AppboxManager();
            }
        }
        return INSTANCE;
    }

    private AppboxManager() {
    }
}
