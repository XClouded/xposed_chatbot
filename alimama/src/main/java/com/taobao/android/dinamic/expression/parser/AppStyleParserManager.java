package com.taobao.android.dinamic.expression.parser;

import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamic.Dinamic;

public class AppStyleParserManager {
    private static final String FILE_PATh = "dinamic/dx_appstyle.json";
    private static volatile AppStyleParserManager instance;
    private JSONObject mConfigJson;
    private boolean mIsInit = false;

    private AppStyleParserManager() {
    }

    public static AppStyleParserManager getInstance() {
        if (instance == null) {
            synchronized (AppStyleParserManager.class) {
                if (instance == null) {
                    instance = new AppStyleParserManager();
                }
            }
        }
        return instance;
    }

    public JSONObject loadJsonObj() {
        if (this.mConfigJson != null && this.mIsInit) {
            return this.mConfigJson;
        }
        this.mConfigJson = JSONObject.parseObject(loadJson(Dinamic.getContext()));
        this.mIsInit = true;
        return this.mConfigJson;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x002a, code lost:
        if (r5 != null) goto L_0x002c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x004a, code lost:
        if (r5 != null) goto L_0x002c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x003b A[SYNTHETIC, Splitter:B:23:0x003b] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0040 A[Catch:{ IOException -> 0x0043 }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0047 A[SYNTHETIC, Splitter:B:33:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String loadJson(android.content.Context r5) {
        /*
            r4 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r1 = 0
            if (r5 != 0) goto L_0x0009
            return r1
        L_0x0009:
            android.content.res.AssetManager r5 = r5.getAssets()     // Catch:{ IOException -> 0x0044, all -> 0x0037 }
            java.lang.String r2 = "dinamic/dx_appstyle.json"
            java.io.InputStream r5 = r5.open(r2)     // Catch:{ IOException -> 0x0044, all -> 0x0037 }
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x0045, all -> 0x0035 }
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0045, all -> 0x0035 }
            r3.<init>(r5)     // Catch:{ IOException -> 0x0045, all -> 0x0035 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0045, all -> 0x0035 }
        L_0x001d:
            java.lang.String r1 = r2.readLine()     // Catch:{ IOException -> 0x0033, all -> 0x0030 }
            if (r1 == 0) goto L_0x0027
            r0.append(r1)     // Catch:{ IOException -> 0x0033, all -> 0x0030 }
            goto L_0x001d
        L_0x0027:
            r2.close()     // Catch:{ IOException -> 0x004d }
            if (r5 == 0) goto L_0x004d
        L_0x002c:
            r5.close()     // Catch:{ IOException -> 0x004d }
            goto L_0x004d
        L_0x0030:
            r0 = move-exception
            r1 = r2
            goto L_0x0039
        L_0x0033:
            r1 = r2
            goto L_0x0045
        L_0x0035:
            r0 = move-exception
            goto L_0x0039
        L_0x0037:
            r0 = move-exception
            r5 = r1
        L_0x0039:
            if (r1 == 0) goto L_0x003e
            r1.close()     // Catch:{ IOException -> 0x0043 }
        L_0x003e:
            if (r5 == 0) goto L_0x0043
            r5.close()     // Catch:{ IOException -> 0x0043 }
        L_0x0043:
            throw r0
        L_0x0044:
            r5 = r1
        L_0x0045:
            if (r1 == 0) goto L_0x004a
            r1.close()     // Catch:{ IOException -> 0x004d }
        L_0x004a:
            if (r5 == 0) goto L_0x004d
            goto L_0x002c
        L_0x004d:
            java.lang.String r5 = r0.toString()
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.expression.parser.AppStyleParserManager.loadJson(android.content.Context):java.lang.String");
    }
}
