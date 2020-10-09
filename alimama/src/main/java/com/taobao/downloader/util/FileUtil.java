package com.taobao.downloader.util;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.downloader.request.Item;
import java.io.File;
import java.net.URL;

public class FileUtil {
    private static final String TEMP_SUFFIX = ".download";

    public static String getStorePath(Context context, String str) {
        return getStorePath(context, str, true);
    }

    public static String getStorePath(Context context, String str, boolean z) {
        File externalCacheDir = z ? context.getExternalCacheDir() : null;
        if (externalCacheDir == null) {
            externalCacheDir = context.getCacheDir();
        }
        File file = new File(externalCacheDir, File.separator + "downloadsdk" + File.separator + str);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getAbsolutePath();
    }

    public static String getFileName(Item item) {
        if (!TextUtils.isEmpty(item.name)) {
            return item.name;
        }
        try {
            return new File(new URL(item.url).getFile()).getName();
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String getLocalFile(String str, Item item) {
        String fileName = getFileName(item);
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        File file = new File(str, fileName);
        File file2 = new File(str, fileName + TEMP_SUFFIX);
        String absolutePath = file.getAbsolutePath();
        if (file.exists() && ((0 == item.size || item.size == file.length()) && Md5Util.isMd5Same(item.md5, absolutePath))) {
            return file.getAbsolutePath();
        }
        if (!file2.exists()) {
            return "";
        }
        if ((0 != item.size && item.size != file2.length()) || !Md5Util.isMd5Same(item.md5, file2.getAbsolutePath())) {
            return "";
        }
        file2.renameTo(file);
        return file.getAbsolutePath();
    }

    public static boolean mvFile(File file, File file2) {
        if (!file.exists()) {
            return false;
        }
        if (file2.exists() && !file2.delete()) {
            return false;
        }
        if (!file2.getParentFile().exists() && !file2.getParentFile().mkdirs()) {
            return false;
        }
        if (!file2.getParentFile().canWrite()) {
            file2.getParentFile().setWritable(true);
        }
        if (file.renameTo(file2)) {
            return true;
        }
        LogUtil.error("rename", "remane fail at index ", new Object[0]);
        return false;
    }
}
