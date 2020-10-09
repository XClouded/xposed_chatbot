package com.xiaomi.push;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;

public final class fs {
    private static int a = 5000;

    /* renamed from: a  reason: collision with other field name */
    private static Vector<String> f386a = new Vector<>();
    private static int b = 330000;
    private static int c = 600000;
    private static int d = 330000;

    /* JADX WARNING: Can't wrap try/catch for region: R(6:34|35|40|41|42|43) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:42:0x00ae */
    static {
        /*
            java.util.Vector r0 = new java.util.Vector
            r0.<init>()
            f386a = r0
            java.lang.ClassLoader[] r0 = a()     // Catch:{ Exception -> 0x00b3 }
            int r1 = r0.length     // Catch:{ Exception -> 0x00b3 }
            r2 = 0
        L_0x000d:
            if (r2 >= r1) goto L_0x00b7
            r3 = r0[r2]     // Catch:{ Exception -> 0x00b3 }
            java.lang.String r4 = "META-INF/smack-config.xml"
            java.util.Enumeration r3 = r3.getResources(r4)     // Catch:{ Exception -> 0x00b3 }
        L_0x0017:
            boolean r4 = r3.hasMoreElements()     // Catch:{ Exception -> 0x00b3 }
            if (r4 == 0) goto L_0x00af
            java.lang.Object r4 = r3.nextElement()     // Catch:{ Exception -> 0x00b3 }
            java.net.URL r4 = (java.net.URL) r4     // Catch:{ Exception -> 0x00b3 }
            r5 = 0
            java.io.InputStream r4 = r4.openStream()     // Catch:{ Exception -> 0x00a3, all -> 0x00a0 }
            org.xmlpull.v1.XmlPullParserFactory r5 = org.xmlpull.v1.XmlPullParserFactory.newInstance()     // Catch:{ Exception -> 0x009e }
            org.xmlpull.v1.XmlPullParser r5 = r5.newPullParser()     // Catch:{ Exception -> 0x009e }
            java.lang.String r6 = "http://xmlpull.org/v1/doc/features.html#process-namespaces"
            r7 = 1
            r5.setFeature(r6, r7)     // Catch:{ Exception -> 0x009e }
            java.lang.String r6 = "UTF-8"
            r5.setInput(r4, r6)     // Catch:{ Exception -> 0x009e }
            int r6 = r5.getEventType()     // Catch:{ Exception -> 0x009e }
        L_0x003f:
            r8 = 2
            if (r6 != r8) goto L_0x0091
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x009e }
            java.lang.String r8 = "className"
            boolean r6 = r6.equals(r8)     // Catch:{ Exception -> 0x009e }
            if (r6 == 0) goto L_0x0052
            a(r5)     // Catch:{ Exception -> 0x009e }
            goto L_0x0091
        L_0x0052:
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x009e }
            java.lang.String r8 = "packetReplyTimeout"
            boolean r6 = r6.equals(r8)     // Catch:{ Exception -> 0x009e }
            if (r6 == 0) goto L_0x0067
            int r6 = a     // Catch:{ Exception -> 0x009e }
            int r6 = a(r5, r6)     // Catch:{ Exception -> 0x009e }
            a = r6     // Catch:{ Exception -> 0x009e }
            goto L_0x0091
        L_0x0067:
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x009e }
            java.lang.String r8 = "keepAliveInterval"
            boolean r6 = r6.equals(r8)     // Catch:{ Exception -> 0x009e }
            if (r6 == 0) goto L_0x007c
            int r6 = b     // Catch:{ Exception -> 0x009e }
            int r6 = a(r5, r6)     // Catch:{ Exception -> 0x009e }
            b = r6     // Catch:{ Exception -> 0x009e }
            goto L_0x0091
        L_0x007c:
            java.lang.String r6 = r5.getName()     // Catch:{ Exception -> 0x009e }
            java.lang.String r8 = "mechName"
            boolean r6 = r6.equals(r8)     // Catch:{ Exception -> 0x009e }
            if (r6 == 0) goto L_0x0091
            java.util.Vector<java.lang.String> r6 = f386a     // Catch:{ Exception -> 0x009e }
            java.lang.String r8 = r5.nextText()     // Catch:{ Exception -> 0x009e }
            r6.add(r8)     // Catch:{ Exception -> 0x009e }
        L_0x0091:
            int r6 = r5.next()     // Catch:{ Exception -> 0x009e }
            if (r6 != r7) goto L_0x003f
        L_0x0097:
            r4.close()     // Catch:{ Exception -> 0x0017 }
            goto L_0x0017
        L_0x009c:
            r0 = move-exception
            goto L_0x00ab
        L_0x009e:
            r5 = move-exception
            goto L_0x00a7
        L_0x00a0:
            r0 = move-exception
            r4 = r5
            goto L_0x00ab
        L_0x00a3:
            r4 = move-exception
            r9 = r5
            r5 = r4
            r4 = r9
        L_0x00a7:
            r5.printStackTrace()     // Catch:{ all -> 0x009c }
            goto L_0x0097
        L_0x00ab:
            r4.close()     // Catch:{ Exception -> 0x00ae }
        L_0x00ae:
            throw r0     // Catch:{ Exception -> 0x00b3 }
        L_0x00af:
            int r2 = r2 + 1
            goto L_0x000d
        L_0x00b3:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00b7:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fs.<clinit>():void");
    }

    private fs() {
    }

    public static int a() {
        return b;
    }

    private static int a(XmlPullParser xmlPullParser, int i) {
        try {
            return Integer.parseInt(xmlPullParser.nextText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m316a() {
        return "3.1.0";
    }

    private static void a(XmlPullParser xmlPullParser) {
        String nextText = xmlPullParser.nextText();
        try {
            Class.forName(nextText);
        } catch (ClassNotFoundException unused) {
            PrintStream printStream = System.err;
            printStream.println("Error! A startup class specified in smack-config.xml could not be loaded: " + nextText);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private static ClassLoader[] m317a() {
        ClassLoader[] classLoaderArr = {fs.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        ArrayList arrayList = new ArrayList();
        for (ClassLoader classLoader : classLoaderArr) {
            if (classLoader != null) {
                arrayList.add(classLoader);
            }
        }
        return (ClassLoader[]) arrayList.toArray(new ClassLoader[arrayList.size()]);
    }

    public static int b() {
        return c;
    }
}
