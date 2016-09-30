package com.liangmayong.appbox.manager.verifier;

import com.liangmayong.appbox.core.AppInfo;

import java.io.File;

/**
 * OnVerifierListener
 *
 * @author LiangMaYong
 * @version 1.0
 */
public interface OnVerifierListener {

    /**
     * updateVerifier
     *
     * @param origin origin
     * @param target target
     * @return true or false
     */
    boolean updateVerifier(AppInfo origin, AppInfo target);

    /**
     * installVerifier
     *
     * @param targetFile targetFile
     * @return true or false
     */
    boolean installVerifier(File targetFile);
}
