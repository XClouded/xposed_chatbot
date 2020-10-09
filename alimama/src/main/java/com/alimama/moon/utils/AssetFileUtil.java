package com.alimama.moon.utils;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class AssetFileUtil {
    public static JSONObject getJSONData(Context context, String str) {
        String str2 = new String(getAssertsFile(context, str));
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        return JSON.parseObject(str2);
    }

    public static String getAssertsFileData(Context context, String str) {
        return new String(getAssertsFile(context, str));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:13|(0)|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:20:0x0029 */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0026 A[SYNTHETIC, Splitter:B:18:0x0026] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x002d A[SYNTHETIC, Splitter:B:25:0x002d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static byte[] getAssertsFile(android.content.Context r1, java.lang.String r2) {
        /*
            android.content.res.AssetManager r1 = r1.getAssets()
            r0 = 0
            java.io.InputStream r1 = r1.open(r2)     // Catch:{ IOException -> 0x0031 }
            if (r1 != 0) goto L_0x000c
            return r0
        L_0x000c:
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x002a, all -> 0x0022 }
            r2.<init>(r1)     // Catch:{ IOException -> 0x002a, all -> 0x0022 }
            int r1 = r2.available()     // Catch:{ IOException -> 0x0020, all -> 0x001e }
            byte[] r1 = new byte[r1]     // Catch:{ IOException -> 0x0020, all -> 0x001e }
            r2.read(r1)     // Catch:{ IOException -> 0x0020, all -> 0x001e }
            r2.close()     // Catch:{ Exception -> 0x001d }
        L_0x001d:
            return r1
        L_0x001e:
            r1 = move-exception
            goto L_0x0024
        L_0x0020:
            goto L_0x002b
        L_0x0022:
            r1 = move-exception
            r2 = r0
        L_0x0024:
            if (r2 == 0) goto L_0x0029
            r2.close()     // Catch:{ Exception -> 0x0029 }
        L_0x0029:
            throw r1     // Catch:{ IOException -> 0x0031 }
        L_0x002a:
            r2 = r0
        L_0x002b:
            if (r2 == 0) goto L_0x0030
            r2.close()     // Catch:{ Exception -> 0x0030 }
        L_0x0030:
            return r0
        L_0x0031:
            r1 = move-exception
            r1.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.utils.AssetFileUtil.getAssertsFile(android.content.Context, java.lang.String):byte[]");
    }
}
