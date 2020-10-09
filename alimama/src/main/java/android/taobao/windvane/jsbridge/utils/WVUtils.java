package android.taobao.windvane.jsbridge.utils;

import android.content.Context;
import android.net.Uri;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;

import com.alimama.unionwl.utils.CommonUtils;

import java.io.File;

import anet.channel.util.HttpConstant;

public class WVUtils {
    public static final String LOCAL_CAPTURE_IMAGE = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=";
    public static final String URL_DATA_CHAR = "?";
    public static final String URL_SEPARATOR = "//";

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0065  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x006e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long saveBitmapToCache(android.graphics.Bitmap r7) throws java.io.IOException {
        /*
            long r0 = java.lang.System.currentTimeMillis()
            android.taobao.windvane.cache.WVFileInfo r2 = new android.taobao.windvane.cache.WVFileInfo
            r2.<init>()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t="
            r3.append(r4)
            r3.append(r0)
            java.lang.String r3 = r3.toString()
            java.lang.String r3 = android.taobao.windvane.util.DigestUtils.md5ToHex((java.lang.String) r3)
            r2.fileName = r3
            java.lang.String r3 = "image/jpeg"
            r2.mimeType = r3
            long r3 = java.lang.System.currentTimeMillis()
            r5 = 2592000000(0x9a7ec800, double:1.280618154E-314)
            long r3 = r3 + r5
            r2.expireTime = r3
            r3 = 1
            byte[] r4 = new byte[r3]
            r5 = 0
            r4[r5] = r5
            android.taobao.windvane.cache.WVCacheManager r5 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            r5.writeToFile(r2, r4)
            java.io.File r4 = new java.io.File
            android.taobao.windvane.cache.WVCacheManager r5 = android.taobao.windvane.cache.WVCacheManager.getInstance()
            java.lang.String r3 = r5.getCacheDir(r3)
            java.lang.String r2 = r2.fileName
            r4.<init>(r3, r2)
            r2 = 0
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0069, all -> 0x0062 }
            r3.<init>(r4)     // Catch:{ Exception -> 0x0069, all -> 0x0062 }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Exception -> 0x0060, all -> 0x005d }
            r4 = 100
            r7.compress(r2, r4, r3)     // Catch:{ Exception -> 0x0060, all -> 0x005d }
            r3.close()
            return r0
        L_0x005d:
            r7 = move-exception
            r2 = r3
            goto L_0x0063
        L_0x0060:
            r2 = r3
            goto L_0x006a
        L_0x0062:
            r7 = move-exception
        L_0x0063:
            if (r2 == 0) goto L_0x0068
            r2.close()
        L_0x0068:
            throw r7
        L_0x0069:
        L_0x006a:
            r0 = 0
            if (r2 == 0) goto L_0x0071
            r2.close()
        L_0x0071:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.WVUtils.saveBitmapToCache(android.graphics.Bitmap):long");
    }

    public static long saveBitmapToCache(File file) {
        long currentTimeMillis = System.currentTimeMillis();
        File file2 = new File(WVCacheManager.getInstance().getCacheDir(true), DigestUtils.md5ToHex("//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + currentTimeMillis));
        if (file != null && file.exists() && FileManager.copy(file, file2)) {
            return currentTimeMillis;
        }
        TaoLog.w("WVUtils", "fail to copy " + file.getAbsolutePath());
        return 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x002c A[SYNTHETIC, Splitter:B:15:0x002c] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0037 A[SYNTHETIC, Splitter:B:21:0x0037] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String bitmapToBase64(android.graphics.Bitmap r4) {
        /*
            r0 = 0
            if (r4 == 0) goto L_0x003e
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x0025, all -> 0x0022 }
            r1.<init>()     // Catch:{ IOException -> 0x0025, all -> 0x0022 }
            android.graphics.Bitmap$CompressFormat r2 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ IOException -> 0x0020 }
            r3 = 100
            r4.compress(r2, r3, r1)     // Catch:{ IOException -> 0x0020 }
            r1.flush()     // Catch:{ IOException -> 0x0020 }
            r1.close()     // Catch:{ IOException -> 0x0020 }
            byte[] r4 = r1.toByteArray()     // Catch:{ IOException -> 0x0020 }
            r2 = 0
            java.lang.String r4 = android.util.Base64.encodeToString(r4, r2)     // Catch:{ IOException -> 0x0020 }
            r0 = r1
            goto L_0x003f
        L_0x0020:
            r4 = move-exception
            goto L_0x0027
        L_0x0022:
            r4 = move-exception
            r1 = r0
            goto L_0x0035
        L_0x0025:
            r4 = move-exception
            r1 = r0
        L_0x0027:
            r4.printStackTrace()     // Catch:{ all -> 0x0034 }
            if (r1 == 0) goto L_0x0032
            r1.flush()     // Catch:{ Exception -> 0x0032 }
            r1.close()     // Catch:{ Exception -> 0x0032 }
        L_0x0032:
            r4 = r0
            goto L_0x0047
        L_0x0034:
            r4 = move-exception
        L_0x0035:
            if (r1 == 0) goto L_0x003d
            r1.flush()     // Catch:{ Exception -> 0x003d }
            r1.close()     // Catch:{ Exception -> 0x003d }
        L_0x003d:
            throw r4
        L_0x003e:
            r4 = r0
        L_0x003f:
            if (r0 == 0) goto L_0x0047
            r0.flush()     // Catch:{ Exception -> 0x0047 }
            r0.close()     // Catch:{ Exception -> 0x0047 }
        L_0x0047:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.jsbridge.utils.WVUtils.bitmapToBase64(android.graphics.Bitmap):java.lang.String");
    }

    public static String getVirtualPath(Long l) {
        return "//127.0.0.1/wvcache/photo.jpg?_wvcrc=1&t=" + l;
    }

    public static String getHost(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int indexOf = str.indexOf("?");
        if (indexOf != -1) {
            str = str.substring(0, indexOf);
        }
        if (str.startsWith("//")) {
            str = CommonUtils.HTTP_PRE + str;
        } else if (!str.contains(HttpConstant.SCHEME_SPLIT)) {
            str = "http://" + str;
        }
        Uri parse = Uri.parse(str);
        if (parse == null) {
            return null;
        }
        return parse.getHost();
    }

    public static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
