package com.liangmayong.appbox.core;

import com.liangmayong.appbox.core.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
            String libraryDir = appFile.getParent() + "/libs/" + encrypt(appPath);
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
            try {
                ZipFile zipFile = new ZipFile(new File(appPath), ZipFile.OPEN_READ);
                extractLibFile(zipFile, file);
            } catch (Exception e) {
            }
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


    private static boolean extractLibFile(ZipFile zip, File tardir)
            throws IOException {

        if (!tardir.exists()) {
            tardir.mkdirs();
        }

        String defaultArch = "armeabi";
        Map<String, List<ZipEntry>> archLibEntries = new HashMap<String, List<ZipEntry>>();
        for (Enumeration<? extends ZipEntry> e = zip.entries(); e
                .hasMoreElements(); ) {
            ZipEntry entry = e.nextElement();
            String name = entry.getName();
            if (name.startsWith("/")) {
                name = name.substring(1);
            }
            if (name.startsWith("lib/")) {
                if (entry.isDirectory()) {
                    continue;
                }
                int sp = name.indexOf('/', 4);
                String en2add;
                if (sp > 0) {
                    String osArch = name.substring(4, sp);
                    en2add = osArch.toLowerCase();
                } else {
                    en2add = defaultArch;
                }
                List<ZipEntry> ents = archLibEntries.get(en2add);
                if (ents == null) {
                    ents = new LinkedList<ZipEntry>();
                    archLibEntries.put(en2add, ents);
                }
                ents.add(entry);
            }
        }
        String arch = System.getProperty("os.arch");
        List<ZipEntry> libEntries = archLibEntries.get(arch.toLowerCase());
        if (libEntries == null) {
            libEntries = archLibEntries.get(defaultArch);
        }
        boolean hasLib = false;
        if (libEntries != null) {
            hasLib = true;
            if (!tardir.exists()) {
                tardir.mkdirs();
            }
            for (ZipEntry libEntry : libEntries) {
                String ename = libEntry.getName();
                String pureName = ename.substring(ename.lastIndexOf('/') + 1);
                File target = new File(tardir, pureName);
                FileUtil.writeToFile(zip.getInputStream(libEntry), target);
            }
        }
        return hasLib;
    }
}
