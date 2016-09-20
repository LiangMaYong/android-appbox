package com.liangmayong.appbox.core.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
     * @param pluginPath pluginPath
     * @return library Path
     */
    public static String getNativePath(String pluginPath) {
        try {
            String libraryDir = new File(pluginPath).getParent() + "/libs/";
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
            unzipFile(appPath, targetDir, AppABI.getNativeLibraryABIs());
        }
    }

    /**
     * clearNativeLibrary
     *
     * @param appPath appPath
     */
    private static void clearNativeLibrary(String appPath) {
        String targetDir = getNativePath(appPath);
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
     * @param objDirs   objDirs
     */
    private final static void unzipFile(String zipFile, String targetDir, ArrayList<String> objDirs) {
        String strEntry = "";
        ZipInputStream zis = null;
        try {
            FileInputStream fis = new FileInputStream(zipFile);
            zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                strEntry = entry.getName();
                boolean find = false;
                for (String objDir : objDirs) {
                    if (isDirEquals(strEntry.toString(), objDir)) {
                        find = true;
                        break;
                    }
                }
                if (find) {
                    String targetEntry = strEntry.substring(strEntry.lastIndexOf("/") + 1);
                    String libraryEntry = targetDir + "/" + targetEntry;
                    File entryFile = new File(libraryEntry);
                    if (!entryFile.exists()) {
                        unzipFile(zis, entryFile);
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
