package com.liangmayong.appbox.core;

import android.os.Build;

/**
 * AppABI
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppABI {

    public static final int ARMv5 = 10;
    public static final int ARMv7 = 20;
    public static final int ARMv8 = 30;
    public static final int MIPS = 111;
    public static final int MIPS64 = 122;
    public static final int x86 = 211;
    public static final int x86_64 = 222;
    public static final int UNKOWN = -1000;

    public static final boolean withABI(String abi, boolean flag) {
        int d = parserABI(Build.CPU_ABI) - parserABI(abi);
        if ((d < 50 && d >= 0) || (flag && (d == x86 - ARMv7 || d == x86 - ARMv5 || d == x86_64 - ARMv5))) {
            return true;
        }
        return false;
    }

    public static final int parserABI(String abi) {
        if ("armeabi".equals(abi)) {
            return ARMv5;
        } else if ("armeabi-v7a".equals(abi)) {
            return ARMv7;
        } else if ("arm64-v8a".equals(abi)) {
            return ARMv8;
        } else if ("mips".equals(abi)) {
            return MIPS;
        } else if ("mips64".equals(abi)) {
            return MIPS64;
        } else if ("x86".equals(abi)) {
            return x86;
        } else if ("x86_64".equals(abi)) {
            return x86_64;
        } else {
            return UNKOWN;
        }
    }

}
