package com.taobao.android.dinamic.parser;

import android.util.Log;
import java.lang.reflect.Constructor;

public class AssetParser extends AbstractParser {
    private static final String TAG = "Home.AssetParser";
    private Constructor<?> xmlBlockConstructor;

    public AssetParser() {
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

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xmlpull.v1.XmlPullParser openXmlResourceParser(java.lang.String r5, com.taobao.android.dinamic.tempate.DinamicTemplate r6, com.taobao.android.dinamic.view.ViewResult r7) {
        /*
            r4 = this;
            java.lang.reflect.Constructor<?> r0 = r4.xmlBlockConstructor
            r1 = 0
            if (r0 == 0) goto L_0x0068
            if (r6 != 0) goto L_0x0008
            goto L_0x0068
        L_0x0008:
            com.taobao.android.dinamic.tempate.DTemplateManager r5 = com.taobao.android.dinamic.tempate.DTemplateManager.templateManagerWithModule(r5)
            byte[] r5 = r5.readAssert(r6)
            byte[] r5 = r4.beforeProcess(r5, r7)
            if (r5 == 0) goto L_0x005c
            int r0 = r5.length
            if (r0 != 0) goto L_0x001a
            goto L_0x005c
        L_0x001a:
            java.lang.String r0 = "Home.AssetParser"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "File parser is applied: "
            r2.append(r3)
            java.lang.String r6 = r6.name
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            android.util.Log.d(r0, r6)
            java.lang.reflect.Constructor<?> r6 = r4.xmlBlockConstructor     // Catch:{ Exception -> 0x004d }
            r0 = 1
            java.lang.Object[] r0 = new java.lang.Object[r0]     // Catch:{ Exception -> 0x004d }
            r2 = 0
            r0[r2] = r5     // Catch:{ Exception -> 0x004d }
            java.lang.Object r5 = r6.newInstance(r0)     // Catch:{ Exception -> 0x004d }
            java.lang.String r6 = "newParser"
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x004d }
            java.lang.Object r5 = com.taobao.android.dinamic.parser.ReflectUtils.invoke(r5, r6, r0)     // Catch:{ Exception -> 0x004d }
            boolean r6 = r5 instanceof android.content.res.XmlResourceParser     // Catch:{ Exception -> 0x004d }
            if (r6 == 0) goto L_0x005b
            android.content.res.XmlResourceParser r5 = (android.content.res.XmlResourceParser) r5     // Catch:{ Exception -> 0x004d }
            return r5
        L_0x004d:
            r5 = move-exception
            com.taobao.android.dinamic.view.DinamicError r6 = r7.getDinamicError()
            java.lang.String r7 = "byteToParserError"
            java.lang.String r5 = r5.getMessage()
            r6.addErrorCodeWithInfo(r7, r5)
        L_0x005b:
            return r1
        L_0x005c:
            com.taobao.android.dinamic.view.DinamicError r5 = r7.getDinamicError()
            java.lang.String r6 = "templateFileEmpty"
            java.lang.String r7 = "assert error"
            r5.addErrorCodeWithInfo(r6, r7)
            return r1
        L_0x0068:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamic.parser.AssetParser.openXmlResourceParser(java.lang.String, com.taobao.android.dinamic.tempate.DinamicTemplate, com.taobao.android.dinamic.view.ViewResult):org.xmlpull.v1.XmlPullParser");
    }
}
