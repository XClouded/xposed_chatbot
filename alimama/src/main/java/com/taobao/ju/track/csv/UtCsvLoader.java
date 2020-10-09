package com.taobao.ju.track.csv;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class UtCsvLoader {
    public Map<String, Map<String, String>> load(Context context, String str) {
        return read(context, str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0049 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> read(android.content.Context r6, java.lang.String r7) {
        /*
            r5 = this;
            boolean r0 = com.taobao.ju.track.csv.CsvFileUtil.isCSV((java.lang.String) r7)
            r1 = 0
            if (r0 != 0) goto L_0x0008
            return r1
        L_0x0008:
            r0 = 0
            java.lang.String[] r0 = new java.lang.String[r0]
            if (r6 == 0) goto L_0x0029
            android.content.res.AssetManager r2 = r6.getAssets()     // Catch:{ Exception -> 0x0023 }
            if (r2 == 0) goto L_0x0029
            android.content.res.AssetManager r2 = r6.getAssets()     // Catch:{ Exception -> 0x0023 }
            java.io.InputStream r2 = r2.open(r7)     // Catch:{ Exception -> 0x0023 }
            java.lang.String[] r2 = com.taobao.ju.track.csv.CsvFileUtil.readHeaders((java.io.InputStream) r2)     // Catch:{ Exception -> 0x0023 }
            r0 = r2
            goto L_0x0029
        L_0x0021:
            r6 = move-exception
            goto L_0x0028
        L_0x0023:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x0021 }
            goto L_0x002c
        L_0x0028:
            throw r6
        L_0x0029:
            if (r0 != 0) goto L_0x002c
            return r1
        L_0x002c:
            if (r6 == 0) goto L_0x0046
            android.content.res.AssetManager r2 = r6.getAssets()     // Catch:{ Exception -> 0x0041 }
            if (r2 == 0) goto L_0x0046
            android.content.res.AssetManager r6 = r6.getAssets()     // Catch:{ Exception -> 0x0041 }
            java.io.InputStream r6 = r6.open(r7)     // Catch:{ Exception -> 0x0041 }
            java.util.List r6 = com.taobao.ju.track.csv.CsvFileUtil.read((java.io.InputStream) r6)     // Catch:{ Exception -> 0x0041 }
            goto L_0x0047
        L_0x0041:
            r6 = move-exception
            r6.printStackTrace()     // Catch:{ all -> 0x0045 }
        L_0x0045:
            return r1
        L_0x0046:
            r6 = r1
        L_0x0047:
            if (r6 != 0) goto L_0x004a
            return r1
        L_0x004a:
            java.util.HashMap r7 = new java.util.HashMap
            r7.<init>()
            java.util.Iterator r6 = r6.iterator()
        L_0x0053:
            boolean r2 = r6.hasNext()
            if (r2 == 0) goto L_0x006f
            java.lang.Object r2 = r6.next()
            java.lang.String[] r2 = (java.lang.String[]) r2
            int r3 = r0.length
            int r4 = r2.length
            if (r3 == r4) goto L_0x0064
            return r1
        L_0x0064:
            java.lang.String r3 = "_key"
            r5.readRow(r7, r0, r2, r3)
            java.lang.String r3 = "_AndroidKey"
            r5.readRow(r7, r0, r2, r3)
            goto L_0x0053
        L_0x006f:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.UtCsvLoader.read(android.content.Context, java.lang.String):java.util.Map");
    }

    private void readRow(Map<String, Map<String, String>> map, String[] strArr, String[] strArr2, String str) {
        HashMap hashMap = new HashMap();
        String str2 = null;
        for (int i = 0; i < strArr.length; i++) {
            if (!str.equals(strArr[i])) {
                hashMap.put(strArr[i], strArr2[i]);
            } else if (str2 == null) {
                str2 = strArr2[i];
            }
        }
        if (!TextUtils.isEmpty(str2)) {
            if (str2.startsWith(Operators.ARRAY_START_STR) && str2.endsWith(Operators.ARRAY_END_STR)) {
                str2 = str2.substring(1, str2.length() - 1);
            }
            map.put(str2, hashMap);
        }
    }
}
