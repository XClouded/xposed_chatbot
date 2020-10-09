package com.taobao.android.dinamic.parser;

import android.util.Log;
import com.taobao.android.dinamic.tempate.DinamicTemplate;
import java.io.File;
import java.lang.reflect.Constructor;

public class SDCardFileParser extends AbstractParser {
    private static final String ROOT_DIR = "/sdcard/com.taobao.taobao/home";
    private static final String TAG = "Home.FileParser";
    private Constructor<?> xmlBlockConstructor;

    public SDCardFileParser() {
        init();
    }

    private void init() {
        try {
            this.xmlBlockConstructor = Class.forName("android.content.res.XmlBlock").getConstructor(new Class[]{byte[].class});
            this.xmlBlockConstructor.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "Fail to get XmlBlock", e);
        }
    }

    public boolean isFileExist(DinamicTemplate dinamicTemplate) {
        return new File(ROOT_DIR, dinamicTemplate.name).exists();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0099 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xmlpull.v1.XmlPullParser openXmlResourceParser(java.lang.String r5, com.taobao.android.dinamic.tempate.DinamicTemplate r6, com.taobao.android.dinamic.view.ViewResult r7) {
        /*
            r4 = this;
            java.lang.reflect.Constructor<?> r5 = r4.xmlBlockConstructor
            r0 = 0
            if (r5 != 0) goto L_0x0006
            return r0
        L_0x0006:
            java.lang.String r5 = r6.name
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 == 0) goto L_0x000f
            return r0
        L_0x000f:
            java.io.File r5 = new java.io.File     // Catch:{ Exception -> 0x0028 }
            java.lang.String r1 = "/sdcard/com.taobao.taobao/home"
            java.lang.String r2 = r6.name     // Catch:{ Exception -> 0x0028 }
            r5.<init>(r1, r2)     // Catch:{ Exception -> 0x0028 }
            boolean r1 = r5.exists()     // Catch:{ Exception -> 0x0028 }
            if (r1 == 0) goto L_0x0041
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ Exception -> 0x0028 }
            r1.<init>(r5)     // Catch:{ Exception -> 0x0028 }
            byte[] r5 = com.taobao.android.dinamic.parser.IOUtils.read(r1)     // Catch:{ Exception -> 0x0028 }
            goto L_0x0042
        L_0x0028:
            r5 = move-exception
            java.lang.String r1 = "Home.FileParser"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Fail to read sdcard layout: "
            r2.append(r3)
            java.lang.String r3 = r6.name
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            android.util.Log.e(r1, r2, r5)
        L_0x0041:
            r5 = r0
        L_0x0042:
            byte[] r5 = r4.beforeProcess(r5, r7)
            if (r5 == 0) goto L_0x0099
            int r7 = r5.length
            if (r7 != 0) goto L_0x004c
            goto L_0x0099
        L_0x004c:
            java.lang.String r7 = "Home.FileParser"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "File parser is applied: "
            r1.append(r2)
            java.lang.String r2 = r6.name
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r7, r1)
            java.lang.reflect.Constructor<?> r7 = r4.xmlBlockConstructor     // Catch:{ Exception -> 0x007f }
            r1 = 1
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Exception -> 0x007f }
            r2 = 0
            r1[r2] = r5     // Catch:{ Exception -> 0x007f }
            java.lang.Object r5 = r7.newInstance(r1)     // Catch:{ Exception -> 0x007f }
            java.lang.String r7 = "newParser"
            java.lang.Object[] r1 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x007f }
            java.lang.Object r5 = com.taobao.android.dinamic.parser.ReflectUtils.invoke(r5, r7, r1)     // Catch:{ Exception -> 0x007f }
            boolean r7 = r5 instanceof android.content.res.XmlResourceParser     // Catch:{ Exception -> 0x007f }
            if (r7 == 0) goto L_0x0098
            android.content.res.XmlResourceParser r5 = (android.content.res.XmlResourceParser) r5     // Catch:{ Exception -> 0x007f }
            return r5
        L_0x007f:
            r5 = move-exception
            java.lang.String r7 = "Home.FileParser"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "New sdcard parser exception: "
            r1.append(r2)
            java.lang.String r6 = r6.name
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            android.util.Log.e(r7, r6, r5)
        L_0x0098:
            return r0
        L_0x0099:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.parser.SDCardFileParser.openXmlResourceParser(java.lang.String, com.taobao.android.dinamic.tempate.DinamicTemplate, com.taobao.android.dinamic.view.ViewResult):org.xmlpull.v1.XmlPullParser");
    }
}
