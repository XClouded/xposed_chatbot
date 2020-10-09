package com.xiaomi.push;

import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class gk {
    private static gk a;

    /* renamed from: a  reason: collision with other field name */
    private Map<String, Object> f414a = new ConcurrentHashMap();
    private Map<String, Object> b = new ConcurrentHashMap();

    private gk() {
        a();
    }

    public static synchronized gk a() {
        gk gkVar;
        synchronized (gk.class) {
            if (a == null) {
                a = new gk();
            }
            gkVar = a;
        }
        return gkVar;
    }

    private String a(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append(Operators.L);
        sb.append(str);
        sb.append("/>");
        if (str != null) {
            sb.append(Operators.L);
            sb.append(str2);
            sb.append("/>");
        }
        return sb.toString();
    }

    /* renamed from: a  reason: collision with other method in class */
    private ClassLoader[] m340a() {
        ClassLoader[] classLoaderArr = {gk.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        ArrayList arrayList = new ArrayList();
        for (ClassLoader classLoader : classLoaderArr) {
            if (classLoader != null) {
                arrayList.add(classLoader);
            }
        }
        return (ClassLoader[]) arrayList.toArray(new ClassLoader[arrayList.size()]);
    }

    /* renamed from: a  reason: collision with other method in class */
    public Object m341a(String str, String str2) {
        return this.f414a.get(a(str, str2));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:53:0x0102 */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00f6 A[SYNTHETIC, Splitter:B:46:0x00f6] */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void m342a() {
        /*
            r10 = this;
            java.lang.ClassLoader[] r0 = r10.a()     // Catch:{ Exception -> 0x0107 }
            int r1 = r0.length     // Catch:{ Exception -> 0x0107 }
            r2 = 0
        L_0x0006:
            if (r2 >= r1) goto L_0x010b
            r3 = r0[r2]     // Catch:{ Exception -> 0x0107 }
            java.lang.String r4 = "META-INF/smack.providers"
            java.util.Enumeration r3 = r3.getResources(r4)     // Catch:{ Exception -> 0x0107 }
        L_0x0010:
            boolean r4 = r3.hasMoreElements()     // Catch:{ Exception -> 0x0107 }
            if (r4 == 0) goto L_0x0103
            java.lang.Object r4 = r3.nextElement()     // Catch:{ Exception -> 0x0107 }
            java.net.URL r4 = (java.net.URL) r4     // Catch:{ Exception -> 0x0107 }
            r5 = 0
            java.io.InputStream r4 = r4.openStream()     // Catch:{ all -> 0x00fd }
            org.xmlpull.v1.XmlPullParserFactory r5 = org.xmlpull.v1.XmlPullParserFactory.newInstance()     // Catch:{ all -> 0x00fb }
            org.xmlpull.v1.XmlPullParser r5 = r5.newPullParser()     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = "http://xmlpull.org/v1/doc/features.html#process-namespaces"
            r7 = 1
            r5.setFeature(r6, r7)     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = "UTF-8"
            r5.setInput(r4, r6)     // Catch:{ all -> 0x00fb }
            int r6 = r5.getEventType()     // Catch:{ all -> 0x00fb }
        L_0x0038:
            r8 = 2
            if (r6 != r8) goto L_0x00f0
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00fb }
            java.lang.String r8 = "iqProvider"
            boolean r6 = r6.equals(r8)     // Catch:{ all -> 0x00fb }
            if (r6 == 0) goto L_0x0097
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = r5.nextText()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r8 = r5.nextText()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r9 = r5.nextText()     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = r10.a((java.lang.String) r6, (java.lang.String) r8)     // Catch:{ all -> 0x00fb }
            java.util.Map<java.lang.String, java.lang.Object> r8 = r10.b     // Catch:{ all -> 0x00fb }
            boolean r8 = r8.containsKey(r6)     // Catch:{ all -> 0x00fb }
            if (r8 != 0) goto L_0x00f0
            java.lang.Class r8 = java.lang.Class.forName(r9)     // Catch:{ ClassNotFoundException -> 0x0092 }
            java.lang.Class<com.xiaomi.push.gi> r9 = com.xiaomi.push.gi.class
            boolean r9 = r9.isAssignableFrom(r8)     // Catch:{ ClassNotFoundException -> 0x0092 }
            if (r9 == 0) goto L_0x0087
            java.util.Map<java.lang.String, java.lang.Object> r9 = r10.b     // Catch:{ ClassNotFoundException -> 0x0092 }
            java.lang.Object r8 = r8.newInstance()     // Catch:{ ClassNotFoundException -> 0x0092 }
        L_0x0083:
            r9.put(r6, r8)     // Catch:{ ClassNotFoundException -> 0x0092 }
            goto L_0x00f0
        L_0x0087:
            java.lang.Class<com.xiaomi.push.gb> r9 = com.xiaomi.push.gb.class
            boolean r9 = r9.isAssignableFrom(r8)     // Catch:{ ClassNotFoundException -> 0x0092 }
            if (r9 == 0) goto L_0x00f0
            java.util.Map<java.lang.String, java.lang.Object> r9 = r10.b     // Catch:{ ClassNotFoundException -> 0x0092 }
            goto L_0x0083
        L_0x0092:
            r6 = move-exception
        L_0x0093:
            r6.printStackTrace()     // Catch:{ all -> 0x00fb }
            goto L_0x00f0
        L_0x0097:
            java.lang.String r6 = r5.getName()     // Catch:{ all -> 0x00fb }
            java.lang.String r8 = "extensionProvider"
            boolean r6 = r6.equals(r8)     // Catch:{ all -> 0x00fb }
            if (r6 == 0) goto L_0x00f0
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = r5.nextText()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r8 = r5.nextText()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            r5.next()     // Catch:{ all -> 0x00fb }
            java.lang.String r9 = r5.nextText()     // Catch:{ all -> 0x00fb }
            java.lang.String r6 = r10.a((java.lang.String) r6, (java.lang.String) r8)     // Catch:{ all -> 0x00fb }
            java.util.Map<java.lang.String, java.lang.Object> r8 = r10.f414a     // Catch:{ all -> 0x00fb }
            boolean r8 = r8.containsKey(r6)     // Catch:{ all -> 0x00fb }
            if (r8 != 0) goto L_0x00f0
            java.lang.Class r8 = java.lang.Class.forName(r9)     // Catch:{ ClassNotFoundException -> 0x00ee }
            java.lang.Class<com.xiaomi.push.gj> r9 = com.xiaomi.push.gj.class
            boolean r9 = r9.isAssignableFrom(r8)     // Catch:{ ClassNotFoundException -> 0x00ee }
            if (r9 == 0) goto L_0x00e3
            java.util.Map<java.lang.String, java.lang.Object> r9 = r10.f414a     // Catch:{ ClassNotFoundException -> 0x00ee }
            java.lang.Object r8 = r8.newInstance()     // Catch:{ ClassNotFoundException -> 0x00ee }
        L_0x00df:
            r9.put(r6, r8)     // Catch:{ ClassNotFoundException -> 0x00ee }
            goto L_0x00f0
        L_0x00e3:
            java.lang.Class<com.xiaomi.push.ge> r9 = com.xiaomi.push.ge.class
            boolean r9 = r9.isAssignableFrom(r8)     // Catch:{ ClassNotFoundException -> 0x00ee }
            if (r9 == 0) goto L_0x00f0
            java.util.Map<java.lang.String, java.lang.Object> r9 = r10.f414a     // Catch:{ ClassNotFoundException -> 0x00ee }
            goto L_0x00df
        L_0x00ee:
            r6 = move-exception
            goto L_0x0093
        L_0x00f0:
            int r6 = r5.next()     // Catch:{ all -> 0x00fb }
            if (r6 != r7) goto L_0x0038
            r4.close()     // Catch:{ Exception -> 0x0010 }
            goto L_0x0010
        L_0x00fb:
            r0 = move-exception
            goto L_0x00ff
        L_0x00fd:
            r0 = move-exception
            r4 = r5
        L_0x00ff:
            r4.close()     // Catch:{ Exception -> 0x0102 }
        L_0x0102:
            throw r0     // Catch:{ Exception -> 0x0107 }
        L_0x0103:
            int r2 = r2 + 1
            goto L_0x0006
        L_0x0107:
            r0 = move-exception
            r0.printStackTrace()
        L_0x010b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gk.m342a():void");
    }

    public void a(String str, String str2, Object obj) {
        if ((obj instanceof gj) || (obj instanceof Class)) {
            this.f414a.put(a(str, str2), obj);
            return;
        }
        throw new IllegalArgumentException("Provider must be a PacketExtensionProvider or a Class instance.");
    }
}
