package com.liangmayong.appbox.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by liangmayong on 2016/9/18.
 */
public final class AppNative {

    private AppNative() {
    }

    /**
     * getNativePath
     *
     * @param appPath appPath
     * @return library Path
     */
    public static String getNativePath(String appPath) {
        File appFile = new File(appPath);
        if (!appFile.exists()) {
            return "";
        }
        try {
            String libraryDir = appFile.getParent() + "/libs-" + encrypt(appPath) + "/";
            File file = new File(libraryDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getPath();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * MD5 encrypt
     *
     * @param str string
     * @return encrypt string
     */
    private final static String encrypt(String str) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte tmp[] = mdTemp.digest();
            char strs[] = new char[16 * 2];
            int k = 0;
            for (int i = 0; i < 16; i++) {
                byte byte0 = tmp[i];
                strs[k++] = hexDigits[byte0 >>> 4 & 0xf];
                strs[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(strs).toUpperCase();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * copyPluginSO
     *
     * @param appPath appPath
     */
    public static void copyNativeLibrary(String appPath) {
        clearNativeLibrary(appPath);
        if (appPath != null && !"".equals(appPath) && (new File(appPath)).exists()) {
            String targetDir = getNativePath(appPath);
            File file = new File(targetDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            unzipFile(appPath, targetDir);
        }
    }

    /**
     * clearNativeLibrary
     *
     * @param appPath appPath
     */
    private static void clearNativeLibrary(String appPath) {
        String targetDir = getNativePath(appPath);
        if (targetDir == null || "".equals(targetDir)) {
            return;
        }
        File file = new File(targetDir);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * isDirEquals
     *
     * @param srcfile srcfile
     * @param objDir  objDir
     * @return boolean
     */
    private final static boolean isDirEquals(String srcfile, String objDir) {
        try {
            int index = srcfile.lastIndexOf("/");
            String dir = null;
            String firstDirName = null;
            if (index != -1)
                dir = srcfile.substring(0, index);
            index = srcfile.indexOf("/");
            if (index != -1)
                firstDirName = srcfile.substring(0, index);
            if (null != dir && dir.equalsIgnoreCase(objDir)) {
                return true;
            } else if (null != firstDirName && firstDirName.equalsIgnoreCase(objDir)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * unzipFile
     *
     * @param zipFile   zipFile
     * @param targetDir targetDir
     */
    private final static void unzipFile(String zipFile, String targetDir) {
        String strEntry = "";
        ZipInputStream zis = null;
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                strEntry = entry.getName();
                if (entry.getName().startsWith("lib/")) {
                    String abi = entry.getName().substring("lib/".length(), entry.getName().lastIndexOf("/"));
                    String targetEntry = strEntry.substring(strEntry.lastIndexOf("/") + 1);
                    String libraryEntry = targetDir + "/" + targetEntry;
                    File entryFile = new File(libraryEntry);
                    if (entryFile.exists()) {
                        if (AppABI.withABI(abi, false)) {
                            unzipFile(zis, entryFile);
                        }
                    } else {
                        if (AppABI.withABI(abi, true)) {
                            unzipFile(zis, entryFile);
                        }
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                if (null != zis) {
                    zis.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * saveZipFile
     *
     * @param zis       zis
     * @param entryFile entryFile
     * @throws Exception e
     */
    private final static void unzipFile(ZipInputStream zis, File entryFile) throws Exception {
        BufferedOutputStream dest = null;
        try {
            int buffer = 4096;
            byte data[] = new byte[buffer];
            int count;
            if (entryFile.exists()) {
                entryFile.delete();
            }
            File entryDir = new File(entryFile.getParent());
            FileOutputStream fos = new FileOutputStream(entryFile);
            dest = new BufferedOutputStream(fos, buffer);
            while ((count = zis.read(data, 0, buffer)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
        } catch (Exception e) {
        } finally {
            if (null != dest) {
                dest.close();
                dest = null;
            }
        }
    }


}
