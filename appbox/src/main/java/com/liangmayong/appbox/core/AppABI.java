package com.liangmayong.appbox.core;

import android.os.Build;
import android.util.Log;

/**
 * AppABI
 *
 * @author LiangMaYong
 * @version 1.0
 */
public final class AppABI {

    public static final int ARMv5 = 1;
    public static final int ARMv7 = 2;
    public static final int ARMv8 = 3;
    public static final int MIPS = 21;
    public static final int MIPS64 = 22;
    public static final int x86 = 51;
    public static final int x86_64 = 52;

    public static final int getABI1() {
        return parserABI(Build.CPU_ABI);
    }

    public static final int getABI2() {
        return parserABI(Build.CPU_ABI2);
    }

    public static final boolean withABI(String abi) {
        int d = getABI1() - parserABI(abi);
        if (d < 10 && d >= 0) {
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
            return 100;
        }
    }

}
