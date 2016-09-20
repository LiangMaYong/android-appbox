package com.liangmayong.appbox.core;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.ArrayList;

/**
 * AppABI
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppABI {

    //cpuabi1
    private static String cpuabi1 = "";
    //cpuabi2
    private static String cpuabi2 = "";
    //libraryDirs
    private static ArrayList<String> libraryDirs = null;

    /**
     * setCpuABI1
     *
     * @param cpuabi cpuabi
     */
    public static void setABI1(String cpuabi) {
        AppABI.cpuabi1 = cpuabi;
    }

    /**
     * setCpuABI2
     *
     * @param cpuabi cpuabi
     */
    public static void setABI2(String cpuabi) {
        AppABI.cpuabi2 = cpuabi;
    }

    /**
     * getCpuABI
     *
     * @return cpuapi1
     */
    @TargetApi(Build.VERSION_CODES.DONUT)
    public static final String getABI() {
        return cpuabi1 == null || "".equals(cpuabi1) ? Build.CPU_ABI : cpuabi1;
    }

    /**
     * getCpuABI2
     *
     * @return cpuapi2
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static final String getABI2() {
        return cpuabi2 == null || "".equals(cpuabi2) ? Build.CPU_ABI2 : cpuabi2;
    }

    /**
     * getNativelibraryDirs
     *
     * @return native library dirs
     */
    public static ArrayList<String> getNativeLibraryABIs() {
        if (libraryDirs == null) {
            libraryDirs = new ArrayList<String>();
            libraryDirs.add("lib/" + AppABI.getABI());
            libraryDirs.add("lib/" + AppABI.getABI2());
        }
        return libraryDirs;
    }
}
