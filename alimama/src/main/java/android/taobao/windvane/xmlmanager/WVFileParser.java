package android.taobao.windvane.xmlmanager;

import java.lang.reflect.Constructor;

class WVFileParser {
    private Constructor<?> xmlBlockConstructor;

    public WVFileParser() {
        init();
    }

    private void init() {
        try {
            this.xmlBlockConstructor = Class.forName("android.content.res.XmlBlock").getConstructor(new Class[]{byte[].class});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xmlpull.v1.XmlPullParser openXmlResourceParser(java.lang.String r5) {
        /*
            r4 = this;
            java.lang.reflect.Constructor<?> r0 = r4.xmlBlockConstructor
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            java.lang.Class<android.taobao.windvane.xmlmanager.WVXmlResUtils> r0 = android.taobao.windvane.xmlmanager.WVXmlResUtils.class
            java.io.InputStream r5 = r0.getResourceAsStream(r5)     // Catch:{ Exception -> 0x0011 }
            byte[] r5 = android.taobao.windvane.xmlmanager.WVInputStreamUtils.InputStreamTOByte(r5)     // Catch:{ Exception -> 0x0011 }
            goto L_0x001c
        L_0x0011:
            r5 = move-exception
            java.lang.String r0 = "Read Error"
            java.lang.String r5 = r5.toString()
            android.taobao.windvane.util.TaoLog.e(r0, r5)
            r5 = r1
        L_0x001c:
            if (r5 == 0) goto L_0x0048
            int r0 = r5.length
            if (r0 != 0) goto L_0x0022
            goto L_0x0048
        L_0x0022:
            java.lang.reflect.Constructor<?> r0 = r4.xmlBlockConstructor     // Catch:{ Exception -> 0x003d }
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch:{ Exception -> 0x003d }
            r3 = 0
            r2[r3] = r5     // Catch:{ Exception -> 0x003d }
            java.lang.Object r5 = r0.newInstance(r2)     // Catch:{ Exception -> 0x003d }
            java.lang.String r0 = "newParser"
            java.lang.Object[] r2 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x003d }
            java.lang.Object r5 = android.taobao.windvane.xmlmanager.WVReflectUtils.invoke(r5, r0, r2)     // Catch:{ Exception -> 0x003d }
            boolean r0 = r5 instanceof android.content.res.XmlResourceParser     // Catch:{ Exception -> 0x003d }
            if (r0 == 0) goto L_0x0047
            android.content.res.XmlResourceParser r5 = (android.content.res.XmlResourceParser) r5     // Catch:{ Exception -> 0x003d }
            return r5
        L_0x003d:
            r5 = move-exception
            java.lang.String r0 = "Read Error"
            java.lang.String r5 = r5.toString()
            android.taobao.windvane.util.TaoLog.e(r0, r5)
        L_0x0047:
            return r1
        L_0x0048:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.xmlmanager.WVFileParser.openXmlResourceParser(java.lang.String):org.xmlpull.v1.XmlPullParser");
    }
}
