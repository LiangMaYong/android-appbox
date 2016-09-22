package com.liangmayong.appbox.core.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by LiangMaYong on 2016/9/22.
 */
public class FileUtil {

    /**
     * writeToFile
     *
     * @param dataIns dataIns
     * @param target  target
     * @throws IOException e
     */
    public static void writeToFile(InputStream dataIns, File target) throws IOException {
        final int BUFFER = 1024;
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(target));
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = dataIns.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        bos.close();
    }

    /**
     * copyFile
     *
     * @param source source
     * @param target target
     */
    public static void copyFile(File source, File target) {

        FileInputStream fi = null;
        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {
            fi = new FileInputStream(source);
            fo = new FileOutputStream(target);
            in = fi.getChannel();
            out = fo.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (Exception e) {
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
