package com.uc.webview.export.internal.uc.wa;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Pair;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.Build;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.extension.SettingKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IGlobalSettings;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.internal.utility.d;
import com.uc.webview.export.internal.utility.i;
import com.uc.webview.export.internal.utility.k;
import com.xiaomi.mipush.sdk.Constants;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: U4Source */
public class a {
    public static boolean a = true;
    public static boolean b = false;
    public static int c = 0;
    public static int e = 20480;
    public static int f = 5242880;
    public static int g = (e + 1024);
    private static a j;
    public List<b> d;
    public SimpleDateFormat h = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Object i = new Object();
    /* access modifiers changed from: private */
    public Context k;
    private HandlerThread l = null;
    private Handler m = null;
    private Map<String, C0025a> n;
    private SimpleDateFormat o = new SimpleDateFormat("yyyyMMdd");

    /* renamed from: com.uc.webview.export.internal.uc.wa.a$a  reason: collision with other inner class name */
    /* compiled from: U4Source */
    class C0025a {
        Map<String, Integer> a;
        Map<String, String> b;

        private C0025a() {
            this.a = new HashMap();
            this.b = new HashMap();
        }

        /* synthetic */ C0025a(a aVar, byte b2) {
            this();
        }

        public final String toString() {
            return "Int Data: " + this.a.toString() + " String Data: " + this.b.toString();
        }
    }

    /* compiled from: U4Source */
    public class b {
        String a;
        Map<String, String> b;

        public b(String str, Map<String, String> map) {
            this.a = str;
            this.b = map;
        }

        public final String toString() {
            return "Key: " + this.a + " Data: " + this.b.toString();
        }
    }

    private a() {
    }

    public static a a() {
        if (j == null && SDKFactory.e != null) {
            a(SDKFactory.e);
        }
        return j;
    }

    public final Handler b() {
        if (this.m == null) {
            this.l = new HandlerThread("SDKWaStatThread", 0);
            this.l.start();
            this.m = new Handler(this.l.getLooper());
        }
        return this.m;
    }

    public static synchronized void a(Context context) {
        synchronized (a.class) {
            if (c()) {
                if (j == null) {
                    j = new a();
                }
                j.k = context.getApplicationContext();
            }
        }
    }

    public static boolean c() {
        return com.uc.webview.export.internal.cd.a.c(UCCore.EVENT_STAT).booleanValue();
    }

    public final void a(String str) {
        if (c()) {
            a(str, 0, 0, 1, (String) null);
        }
    }

    public static void a(Pair<String, HashMap<String, String>> pair) {
        UCLogger create = UCLogger.create("d", "SDKWaStat");
        if (create != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("ev_ac=");
            sb.append((String) pair.first);
            for (Map.Entry entry : ((HashMap) pair.second).entrySet()) {
                sb.append("`");
                sb.append((String) entry.getKey());
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append((String) entry.getValue());
            }
            create.print(sb.toString(), new Throwable[0]);
        }
    }

    public final void a(String str, int i2, int i3, int i4, String str2) {
        Date date = new Date(System.currentTimeMillis());
        int d2 = SDKFactory.b() ? SDKFactory.d() : 0;
        if (!(d2 == 2 || d2 == 0)) {
            d2 = (d2 * 10) + SDKFactory.h;
        }
        String str3 = this.o.format(date) + Constants.WAVE_SEPARATOR + d2;
        synchronized (this.i) {
            if (this.n == null) {
                this.n = new HashMap();
            }
            C0025a aVar = this.n.get(str3);
            if (aVar == null) {
                aVar = new C0025a(this, (byte) 0);
                a(aVar.b);
                this.n.put(str3, aVar);
            }
            aVar.b.put("tm", this.h.format(date));
            switch (i2) {
                case 0:
                    Integer num = aVar.a.get(str);
                    if (num != null) {
                        aVar.a.put(str, Integer.valueOf(i4 + num.intValue()));
                        break;
                    } else {
                        aVar.a.put(str, Integer.valueOf(i4));
                        break;
                    }
                case 1:
                    if (i3 != 1) {
                        String str4 = aVar.b.get(str);
                        if (!com.uc.webview.export.internal.utility.b.a(str4)) {
                            aVar.b.put(str, str4 + "|" + str2);
                            break;
                        } else {
                            aVar.b.put(str, str2);
                            break;
                        }
                    } else {
                        aVar.b.put(str, str2);
                        break;
                    }
                case 2:
                    Integer num2 = aVar.a.get(str);
                    if (num2 == null || Integer.MAX_VALUE - num2.intValue() >= i4) {
                        if (num2 != null) {
                            aVar.a.put(str, Integer.valueOf(i4 + num2.intValue()));
                            Map<String, Integer> map = aVar.a;
                            aVar.a.put(str + "_sc", Integer.valueOf(map.get(str + "_sc").intValue() + 1));
                            break;
                        } else {
                            aVar.a.put(str, Integer.valueOf(i4));
                            aVar.a.put(str + "_sc", 1);
                            break;
                        }
                    }
            }
        }
    }

    public final void a(boolean z) {
        if (c() && !com.uc.webview.export.internal.utility.b.a((String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION))) {
            try {
                b().post(new b(this));
                if (z) {
                    Thread.sleep(20);
                }
            } catch (Exception e2) {
                Log.e("SDKWaStat", "saveData", e2);
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void f() {
        if (c()) {
            try {
                if (b) {
                    Log.d("SDKWaStat", "saveData");
                }
                HashMap hashMap = new HashMap();
                ArrayList arrayList = new ArrayList();
                synchronized (this.i) {
                    if (SDKFactory.b() && !b((Map) this.n)) {
                        hashMap.putAll(this.n);
                        this.n.clear();
                    }
                    if (!a((List) this.d)) {
                        arrayList.addAll(this.d);
                        this.d.clear();
                    }
                }
                a((Map<String, C0025a>) hashMap, (List<b>) arrayList);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public String g() {
        String str = this.k.getApplicationContext().getApplicationInfo().dataDir + "/ucwa";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return str;
    }

    /* access modifiers changed from: private */
    public static String h() {
        String str = (String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION);
        if (com.uc.webview.export.internal.utility.b.a(str) || str.equals("0")) {
            return "wa_upload_new.wa";
        }
        return "wa_upload_new.wa_" + str;
    }

    /* access modifiers changed from: private */
    public static String i() {
        String str = (String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION);
        if (com.uc.webview.export.internal.utility.b.a(str) || str.equals("0")) {
            return "1";
        }
        return "1_" + str;
    }

    private static boolean a(List list) {
        return list == null || list.size() == 0;
    }

    private static boolean b(Map map) {
        return map == null || map.size() == 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v2, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v4, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: java.io.BufferedWriter} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v7, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v20, resolved type: java.io.FileOutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(java.util.Map<java.lang.String, com.uc.webview.export.internal.uc.wa.a.C0025a> r14, java.util.List<com.uc.webview.export.internal.uc.wa.a.b> r15) {
        /*
            r13 = this;
            boolean r0 = b((java.util.Map) r14)
            if (r0 == 0) goto L_0x000d
            boolean r0 = a((java.util.List) r15)
            if (r0 == 0) goto L_0x000d
            return
        L_0x000d:
            java.io.File r0 = new java.io.File
            java.lang.String r1 = r13.g()
            java.lang.String r2 = h()
            r0.<init>(r1, r2)
            boolean r1 = b
            if (r1 == 0) goto L_0x0031
            java.lang.String r1 = "SDKWaStat"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "savePVToFile:"
            r2.<init>(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r2)
        L_0x0031:
            r1 = 0
            boolean r2 = r0.exists()     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            r3 = 0
            if (r2 == 0) goto L_0x0051
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            r2.<init>(r0)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            int r4 = r2.available()     // Catch:{ Exception -> 0x004b, all -> 0x0046 }
            r2.close()     // Catch:{ Exception -> 0x004b, all -> 0x0046 }
            goto L_0x0052
        L_0x0046:
            r14 = move-exception
            r0 = r1
            r15 = r2
            goto L_0x01cb
        L_0x004b:
            r14 = move-exception
            r15 = r1
            r0 = r2
            r2 = r15
            goto L_0x01d1
        L_0x0051:
            r4 = 0
        L_0x0052:
            int r2 = e     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            if (r4 < r2) goto L_0x0084
            boolean r14 = b     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            if (r14 == 0) goto L_0x0077
            java.lang.String r14 = "SDKWaStat"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            java.lang.String r0 = "file size("
            r15.<init>(r0)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            r15.append(r4)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            java.lang.String r0 = ") more then "
            r15.append(r0)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            int r0 = e     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            r15.append(r0)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            java.lang.String r15 = r15.toString()     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            com.uc.webview.export.internal.utility.Log.d(r14, r15)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
        L_0x0077:
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            return
        L_0x0084:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            r5 = 1
            r2.<init>(r0, r5)     // Catch:{ Exception -> 0x01cd, all -> 0x01c8 }
            java.io.BufferedWriter r0 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x01c4, all -> 0x01bf }
            java.io.OutputStreamWriter r6 = new java.io.OutputStreamWriter     // Catch:{ Exception -> 0x01c4, all -> 0x01bf }
            r6.<init>(r2)     // Catch:{ Exception -> 0x01c4, all -> 0x01bf }
            r7 = 1024(0x400, float:1.435E-42)
            r0.<init>(r6, r7)     // Catch:{ Exception -> 0x01c4, all -> 0x01bf }
            java.util.Set r14 = r14.entrySet()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.util.Iterator r14 = r14.iterator()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r6 = 0
        L_0x009f:
            boolean r7 = r14.hasNext()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r7 == 0) goto L_0x0147
            java.lang.Object r7 = r14.next()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.util.Map$Entry r7 = (java.util.Map.Entry) r7     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r8 = r6 + r4
            int r9 = e     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r8 < r9) goto L_0x00d3
            boolean r14 = b     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r14 == 0) goto L_0x0147
            java.lang.String r14 = "SDKWaStat"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r9 = "write merge data, size("
            r7.<init>(r9)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r7.append(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r8 = ") more then "
            r7.append(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r8 = e     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r7.append(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            com.uc.webview.export.internal.utility.Log.d(r14, r7)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            goto L_0x0147
        L_0x00d3:
            java.lang.String r8 = "@0"
            r0.write(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 2
            java.lang.String r8 = "@k@"
            r0.write(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 3
            java.lang.Object r8 = r7.getKey()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r9 = "~"
            java.lang.String[] r9 = r8.split(r9)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r10 = r9[r5]     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r11 = "0"
            boolean r10 = r10.equals(r11)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r10 == 0) goto L_0x0119
            int r8 = com.uc.webview.export.internal.SDKFactory.d()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r10 = 2
            if (r8 == r10) goto L_0x0103
            int r8 = r8 * 10
            int r10 = com.uc.webview.export.internal.SDKFactory.h     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r8 = r8 + r10
        L_0x0103:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r10.<init>()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r9 = r9[r3]     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r10.append(r9)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r9 = "~"
            r10.append(r9)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r10.append(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r8 = r10.toString()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
        L_0x0119:
            r0.write(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r8 = r8.length()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + r8
            java.lang.String r8 = "@d@"
            r0.write(r8)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 3
            java.lang.Object r8 = r7.getValue()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            com.uc.webview.export.internal.uc.wa.a$a r8 = (com.uc.webview.export.internal.uc.wa.a.C0025a) r8     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.util.Map<java.lang.String, java.lang.Integer> r8 = r8.a     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r8 = a((java.io.BufferedWriter) r0, r8, (int) r3)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + r8
            java.lang.Object r7 = r7.getValue()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            com.uc.webview.export.internal.uc.wa.a$a r7 = (com.uc.webview.export.internal.uc.wa.a.C0025a) r7     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.util.Map<java.lang.String, java.lang.String> r7 = r7.b     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r7 = a((java.io.BufferedWriter) r0, r7, (int) r5)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + r7
            r0.newLine()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            goto L_0x009f
        L_0x0147:
            r14 = 10
            if (r3 >= r14) goto L_0x01a9
            int r14 = r15.size()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r3 >= r14) goto L_0x01a9
            int r14 = r6 + r4
            int r7 = e     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            if (r14 < r7) goto L_0x0175
            java.lang.String r15 = "SDKWaStat"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r4 = "write un merge data, size("
            r3.<init>(r4)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r3.append(r14)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r14 = ") more then "
            r3.append(r14)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r14 = e     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r3.append(r14)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r14 = r3.toString()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            com.uc.webview.export.internal.utility.Log.d(r15, r14)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            goto L_0x01a9
        L_0x0175:
            java.lang.Object r14 = r15.get(r3)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            com.uc.webview.export.internal.uc.wa.a$b r14 = (com.uc.webview.export.internal.uc.wa.a.b) r14     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r7 = "@1"
            r0.write(r7)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 2
            java.lang.String r7 = "@k@"
            r0.write(r7)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 3
            java.lang.String r7 = r14.a     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            r0.write(r7)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            java.lang.String r7 = r14.a     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r7 = r7.length()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + r7
            java.lang.String r7 = "@d@"
            r0.write(r7)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + 3
            java.util.Map<java.lang.String, java.lang.String> r14 = r14.b     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r14 = a((java.io.BufferedWriter) r0, r14, (int) r5)     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r6 = r6 + r14
            r0.newLine()     // Catch:{ Exception -> 0x01b9, all -> 0x01b6 }
            int r3 = r3 + 1
            goto L_0x0147
        L_0x01a9:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            return
        L_0x01b6:
            r14 = move-exception
            r15 = r1
            goto L_0x01c2
        L_0x01b9:
            r14 = move-exception
            r15 = r2
            r12 = r1
            r1 = r0
            r0 = r12
            goto L_0x01d1
        L_0x01bf:
            r14 = move-exception
            r15 = r1
            r0 = r15
        L_0x01c2:
            r1 = r2
            goto L_0x01e6
        L_0x01c4:
            r14 = move-exception
            r0 = r1
            r15 = r2
            goto L_0x01d1
        L_0x01c8:
            r14 = move-exception
            r15 = r1
            r0 = r15
        L_0x01cb:
            r2 = r0
            goto L_0x01e6
        L_0x01cd:
            r14 = move-exception
            r15 = r1
            r0 = r15
            r2 = r0
        L_0x01d1:
            r14.printStackTrace()     // Catch:{ all -> 0x01e1 }
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r15)
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            return
        L_0x01e1:
            r14 = move-exception
            r12 = r1
            r1 = r15
            r15 = r0
            r0 = r12
        L_0x01e6:
            com.uc.webview.export.cyclone.UCCyclone.close(r0)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r15)
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.uc.wa.a.a(java.util.Map, java.util.List):void");
    }

    private static <T> int a(BufferedWriter bufferedWriter, Map<String, T> map, int i2) throws Exception {
        int i3 = 0;
        if (!b((Map) map)) {
            for (Map.Entry next : map.entrySet()) {
                bufferedWriter.write((String) next.getKey());
                bufferedWriter.write(SymbolExpUtil.SYMBOL_EQUAL);
                bufferedWriter.write("#" + i2);
                bufferedWriter.write(next.getValue() + "`");
                StringBuilder sb = new StringBuilder("#");
                sb.append(i2);
                int length = ((String) next.getKey()).length() + 1 + sb.toString().length();
                i3 += length + (next.getValue() + "`").length();
            }
        }
        return i3;
    }

    private Object[] j() {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        FileInputStream fileInputStream2;
        BufferedReader bufferedReader2;
        FileInputStream fileInputStream3;
        File file = new File(g(), h());
        if (b) {
            Log.d("SDKWaStat", "getPVFromFile:" + file + " exists:" + file.exists());
        }
        if (!file.exists()) {
            return null;
        }
        file.length();
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        try {
            fileInputStream2 = new FileInputStream(file);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream2), 1024);
                int i2 = 0;
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        int i3 = 2;
                        char c2 = 1;
                        if (readLine == null) {
                            break;
                        }
                        if (b) {
                            Log.d("SDKWaStat", readLine);
                        }
                        if (!com.uc.webview.export.internal.utility.b.a(readLine)) {
                            if (readLine.length() + i2 <= e) {
                                i2 += readLine.length();
                                String trim = readLine.trim();
                                if (trim.startsWith("@0") || trim.startsWith("@1")) {
                                    int indexOf = trim.indexOf("@k@");
                                    int indexOf2 = trim.indexOf("@d@");
                                    if (indexOf >= 0 && indexOf2 >= 0) {
                                        String substring = trim.substring(indexOf + 3, indexOf2);
                                        String[] split = trim.substring(indexOf2 + 3).split("`");
                                        if (trim.startsWith("@0")) {
                                            String[] split2 = substring.split(Constants.WAVE_SEPARATOR);
                                            if (split2.length == 2 && split2[0].length() == 8 && split2[1].length() <= 2) {
                                                C0025a aVar = (C0025a) hashMap.get(substring);
                                                if (aVar == null) {
                                                    if (hashMap.size() == 8) {
                                                        if (arrayList.size() == 10) {
                                                            break;
                                                        }
                                                    } else {
                                                        aVar = new C0025a(this, (byte) 0);
                                                        hashMap.put(substring, aVar);
                                                    }
                                                }
                                                int length = split.length;
                                                int i4 = 0;
                                                while (i4 < length) {
                                                    String[] split3 = split[i4].split(SymbolExpUtil.SYMBOL_EQUAL);
                                                    if (split3.length == i3 && split3[c2].length() > i3) {
                                                        String substring2 = split3[c2].substring(i3);
                                                        if (split3[c2].startsWith("#0")) {
                                                            Integer num = aVar.a.get(split3[0]);
                                                            if (num == null) {
                                                                aVar.a.put(split3[0], Integer.valueOf(Integer.parseInt(substring2)));
                                                            } else {
                                                                aVar.a.put(split3[0], Integer.valueOf(Integer.parseInt(substring2) + num.intValue()));
                                                            }
                                                        } else if (split3[1].startsWith("#1")) {
                                                            aVar.b.put(split3[0], substring2);
                                                        }
                                                    }
                                                    i4++;
                                                    i3 = 2;
                                                    c2 = 1;
                                                }
                                                aVar.b.put("core_t", split2[1]);
                                            }
                                        } else if (arrayList.size() != 10) {
                                            HashMap hashMap2 = new HashMap(split.length);
                                            for (String split4 : split) {
                                                String[] split5 = split4.split(SymbolExpUtil.SYMBOL_EQUAL);
                                                if (split5.length == 2) {
                                                    hashMap2.put(split5[0], split5[1].substring(2));
                                                }
                                            }
                                            hashMap2.put("ev_ac", substring);
                                            arrayList.add(new b(substring, hashMap2));
                                        }
                                    }
                                }
                            } else if (b) {
                                Log.d("SDKWaStat", "upload data size(" + (i2 + readLine.length()) + ") more then " + e);
                            }
                        }
                    } catch (Exception e2) {
                        e = e2;
                        fileInputStream3 = fileInputStream2;
                        bufferedReader2 = bufferedReader;
                        try {
                            e.printStackTrace();
                            UCCyclone.close(bufferedReader2);
                            UCCyclone.close(fileInputStream2);
                            UCCyclone.close(fileInputStream3);
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            fileInputStream = fileInputStream3;
                            bufferedReader = bufferedReader2;
                            UCCyclone.close(bufferedReader);
                            UCCyclone.close(fileInputStream2);
                            UCCyclone.close(fileInputStream);
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream = fileInputStream2;
                        UCCyclone.close(bufferedReader);
                        UCCyclone.close(fileInputStream2);
                        UCCyclone.close(fileInputStream);
                        throw th;
                    }
                }
                if (hashMap.size() > 0 || arrayList.size() > 0) {
                    Object[] objArr = {hashMap, arrayList};
                    UCCyclone.close(bufferedReader);
                    UCCyclone.close(fileInputStream2);
                    UCCyclone.close(fileInputStream2);
                    return objArr;
                }
                UCCyclone.close(bufferedReader);
                UCCyclone.close(fileInputStream2);
                UCCyclone.close(fileInputStream2);
                return null;
            } catch (Exception e3) {
                e = e3;
                fileInputStream3 = fileInputStream2;
                bufferedReader2 = null;
                e.printStackTrace();
                UCCyclone.close(bufferedReader2);
                UCCyclone.close(fileInputStream2);
                UCCyclone.close(fileInputStream3);
                return null;
            } catch (Throwable th3) {
                th = th3;
                fileInputStream = fileInputStream2;
                bufferedReader = null;
                UCCyclone.close(bufferedReader);
                UCCyclone.close(fileInputStream2);
                UCCyclone.close(fileInputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            fileInputStream3 = null;
            bufferedReader2 = null;
            fileInputStream2 = null;
            e.printStackTrace();
            UCCyclone.close(bufferedReader2);
            UCCyclone.close(fileInputStream2);
            UCCyclone.close(fileInputStream3);
            return null;
        } catch (Throwable th4) {
            th = th4;
            fileInputStream2 = null;
            bufferedReader = null;
            fileInputStream = null;
            UCCyclone.close(bufferedReader);
            UCCyclone.close(fileInputStream2);
            UCCyclone.close(fileInputStream);
            throw th;
        }
    }

    public static void a(Map<String, String> map) {
        map.put("sdk_sn", Build.TIME);
        map.put("ver", Build.Version.NAME);
    }

    private List<String[]> a(List<PackageInfo> list, String str) {
        if (str == null) {
            return new ArrayList(0);
        }
        ArrayList arrayList = new ArrayList();
        String string = this.k.getSharedPreferences("UC_WA_STAT", 4).getString("4", (String) null);
        if (string == null || !string.equals(str)) {
            arrayList.add(new String[]{"sdk_3rdappf", c(list)});
        }
        return arrayList;
    }

    private static String b(Map<String, C0025a> map, List<b> list) {
        String[] strArr = {"01", "10", "20"};
        String str = null;
        for (int i2 = 0; i2 < 3; i2++) {
            String str2 = strArr[i2];
            for (Map.Entry<String, C0025a> value : map.entrySet()) {
                String c2 = c(((C0025a) value.getValue()).b);
                if (c2 != null && c2.endsWith(str2)) {
                    if (str == null || c2.compareTo(str) > 0) {
                        str = c2;
                    }
                }
            }
            for (b bVar : list) {
                String c3 = c(bVar.b);
                if (c3 != null && c3.endsWith(str2)) {
                    if (str == null || c3.compareTo(str) > 0) {
                        str = c3;
                    }
                }
            }
            if (str != null) {
                break;
            }
        }
        return str;
    }

    private static String c(Map<String, String> map) {
        String str = map.get("tm");
        if (str == null || str.length() <= 10) {
            return null;
        }
        return str.substring(0, 10);
    }

    private static void a(StringBuilder sb, String str, String str2) {
        sb.append(str);
        sb.append(SymbolExpUtil.SYMBOL_EQUAL);
        sb.append(str2);
        sb.append("`");
    }

    /* access modifiers changed from: private */
    public String a(String[] strArr) {
        Object[] j2 = j();
        if (j2 == null) {
            return null;
        }
        Map map = (Map) j2[0];
        List<b> list = (List) j2[1];
        try {
            JSONObject jSONObject = new JSONObject();
            List<PackageInfo> b2 = b(this.k);
            for (String[] next : b(b2)) {
                jSONObject.put(next[0], next[1]);
            }
            if (!k.f()) {
                strArr[0] = b((Map<String, C0025a>) map, list);
                for (String[] next2 : a(b2, strArr[0])) {
                    jSONObject.put(next2[0], next2[1]);
                }
            }
            for (Map.Entry next3 : SDKFactory.q.entrySet()) {
                jSONObject.put((String) next3.getKey(), ((Integer) next3.getValue()).intValue());
            }
            JSONArray jSONArray = new JSONArray();
            for (Map.Entry entry : map.entrySet()) {
                JSONObject jSONObject2 = new JSONObject();
                for (Map.Entry next4 : ((C0025a) entry.getValue()).a.entrySet()) {
                    jSONObject2.put((String) next4.getKey(), String.valueOf(next4.getValue()));
                }
                for (Map.Entry next5 : ((C0025a) entry.getValue()).b.entrySet()) {
                    jSONObject2.put((String) next5.getKey(), next5.getValue());
                }
                jSONArray.put(jSONObject2);
            }
            for (b bVar : list) {
                JSONObject jSONObject3 = new JSONObject();
                for (Map.Entry next6 : bVar.b.entrySet()) {
                    jSONObject3.put((String) next6.getKey(), next6.getValue());
                }
                jSONArray.put(jSONObject3);
            }
            jSONObject.put("items", jSONArray);
            jSONObject.put("stat_size", String.valueOf(jSONObject.toString().length()));
            return jSONObject.toString();
        } catch (Exception e2) {
            Log.e("SDKWaStat", "getJsonUploadData", e2);
            return null;
        }
    }

    /* access modifiers changed from: private */
    public String a(SharedPreferences sharedPreferences) {
        String string = sharedPreferences.getString("2", "");
        if (!com.uc.webview.export.internal.utility.b.a(string)) {
            return string;
        }
        String uuid = UUID.randomUUID().toString();
        SharedPreferences.Editor edit = this.k.getSharedPreferences("UC_WA_STAT", 4).edit();
        edit.putString("2", uuid);
        edit.commit();
        return uuid;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00fd A[Catch:{ all -> 0x010e }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean b(java.lang.String r8, byte[] r9) {
        /*
            boolean r0 = com.uc.webview.export.internal.SDKFactory.f     // Catch:{ Throwable -> 0x001d }
            if (r0 != 0) goto L_0x0021
            java.lang.String r0 = "traffic_stat"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ Throwable -> 0x001d }
            boolean r0 = java.lang.Boolean.parseBoolean(r0)     // Catch:{ Throwable -> 0x001d }
            if (r0 == 0) goto L_0x0021
            int r0 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x001d }
            r1 = 14
            if (r0 < r1) goto L_0x0021
            r0 = 40962(0xa002, float:5.74E-41)
            android.net.TrafficStats.setThreadStatsTag(r0)     // Catch:{ Throwable -> 0x001d }
            goto L_0x0021
        L_0x001d:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0021:
            r0 = 0
            r1 = 0
            java.net.URL r2 = new java.net.URL     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r2.<init>(r8)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.net.URLConnection r8 = r2.openConnection()     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            int r2 = com.uc.webview.export.internal.utility.k.c()     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r8.setConnectTimeout(r2)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            int r2 = com.uc.webview.export.internal.utility.k.d()     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r8.setReadTimeout(r2)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r2 = 1
            r8.setDoInput(r2)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r8.setDoOutput(r2)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.lang.String r3 = "POST"
            r8.setRequestMethod(r3)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r8.setUseCaches(r0)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.lang.String r3 = "Content-Type"
            java.lang.String r4 = "application/x-www-form-urlencoded"
            r8.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.lang.String r3 = "Content-Length"
            int r4 = r9.length     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.lang.String r4 = java.lang.String.valueOf(r4)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r8.setRequestProperty(r3, r4)     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            java.io.OutputStream r3 = r8.getOutputStream()     // Catch:{ Throwable -> 0x00f6, all -> 0x00f2 }
            r3.write(r9)     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            int r9 = r8.getResponseCode()     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            r4 = 200(0xc8, float:2.8E-43)
            if (r9 == r4) goto L_0x0085
            boolean r8 = b     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            if (r8 == 0) goto L_0x007b
            java.lang.String r8 = "SDKWaStat"
            java.lang.String r9 = "response == null"
            java.lang.Throwable r2 = new java.lang.Throwable     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            r2.<init>()     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            com.uc.webview.export.internal.utility.Log.e(r8, r9, r2)     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
        L_0x007b:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            return r0
        L_0x0085:
            java.io.InputStream r8 = r8.getInputStream()     // Catch:{ Throwable -> 0x00ef, all -> 0x00ec }
            r9 = 1024(0x400, float:1.435E-42)
            byte[] r9 = new byte[r9]     // Catch:{ Throwable -> 0x00e7, all -> 0x00e2 }
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x00e7, all -> 0x00e2 }
            int r5 = r8.available()     // Catch:{ Throwable -> 0x00e7, all -> 0x00e2 }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x00e7, all -> 0x00e2 }
        L_0x0096:
            int r1 = r8.read(r9)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            r5 = -1
            if (r1 == r5) goto L_0x00a1
            r4.write(r9, r0, r1)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            goto L_0x0096
        L_0x00a1:
            java.lang.String r9 = new java.lang.String     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            byte[] r1 = r4.toByteArray()     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            r9.<init>(r1)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            boolean r1 = b     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            if (r1 == 0) goto L_0x00c1
            java.lang.String r1 = "SDKWaStat"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            java.lang.String r6 = "response:"
            r5.<init>(r6)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            r5.append(r9)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            com.uc.webview.export.internal.utility.Log.i(r1, r5)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
        L_0x00c1:
            java.lang.String r1 = "retcode=0"
            boolean r9 = r9.contains(r1)     // Catch:{ Throwable -> 0x00df, all -> 0x00dd }
            if (r9 == 0) goto L_0x00d3
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
            return r2
        L_0x00d3:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r8)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
            goto L_0x010d
        L_0x00dd:
            r9 = move-exception
            goto L_0x00e4
        L_0x00df:
            r9 = move-exception
            r1 = r4
            goto L_0x00e8
        L_0x00e2:
            r9 = move-exception
            r4 = r1
        L_0x00e4:
            r1 = r8
            r8 = r9
            goto L_0x0111
        L_0x00e7:
            r9 = move-exception
        L_0x00e8:
            r7 = r9
            r9 = r8
            r8 = r7
            goto L_0x00f9
        L_0x00ec:
            r8 = move-exception
            r4 = r1
            goto L_0x0111
        L_0x00ef:
            r8 = move-exception
            r9 = r1
            goto L_0x00f9
        L_0x00f2:
            r8 = move-exception
            r3 = r1
            r4 = r3
            goto L_0x0111
        L_0x00f6:
            r8 = move-exception
            r9 = r1
            r3 = r9
        L_0x00f9:
            boolean r2 = b     // Catch:{ all -> 0x010e }
            if (r2 == 0) goto L_0x0104
            java.lang.String r2 = "SDKWaStat"
            java.lang.String r4 = ""
            com.uc.webview.export.internal.utility.Log.e(r2, r4, r8)     // Catch:{ all -> 0x010e }
        L_0x0104:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r9)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
        L_0x010d:
            return r0
        L_0x010e:
            r8 = move-exception
            r4 = r1
            r1 = r9
        L_0x0111:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r1)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.uc.wa.a.b(java.lang.String, byte[]):boolean");
    }

    private static List<PackageInfo> b(Context context) {
        return context.getPackageManager().getInstalledPackages(0);
    }

    private static String c(List<PackageInfo> list) {
        long currentTimeMillis = System.currentTimeMillis();
        HashSet hashSet = new HashSet(list.size());
        for (PackageInfo packageInfo : list) {
            hashSet.add(Integer.valueOf(packageInfo.packageName.hashCode()));
        }
        StringBuilder sb = new StringBuilder();
        int[] iArr = {744792033, -796004189, 1536737232, -1864872766, -245593387, 559984781, 1254578009, 460049591, -103524201, -191341086, 2075805265, -860300598, 195266379, 851655498, -172581751, -1692253156, -1709882794, 978047406, -1447376190, 1085732649, 400412247, 1007750384, 321803898, 1319538838, -1459422248, -173313837, 1488133239, 551552610, 1310504938, 633261597, -548160304, 1971200218, 757982267, 996952171, 1855462465, 2049668591, -189253699, -761937585, -1102972298, 195210534, -1433071276, -118960061, 810513273, 1659293491, 1552103645, 361910168, -973170826, -1805061386, -1635328017, -1131240456, 1429484426, -918490570, 1791072826, -894368837, -1394248969, -1476255283, 1994036591, 1219220171, 201325446, -1215205363, -257645900, 1197124177, 1765779203, 313184810, 308524937, -1652150487, 1174097286, -69877540, 2123438483, -1769871671};
        for (int i2 = 0; i2 < 70; i2++) {
            if (hashSet.contains(Integer.valueOf(iArr[i2]))) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        Log.i("SDKWaStat", "getOtherAppInstallFlag用时:" + (System.currentTimeMillis() - currentTimeMillis) + Operators.SPACE_STR + sb);
        return sb.toString();
    }

    private List<String[]> b(List<PackageInfo> list) {
        int i2;
        String str;
        String str2;
        Iterator<PackageInfo> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                i2 = 0;
                break;
            }
            PackageInfo next = it.next();
            if (!next.packageName.equals("com.UCMobile")) {
                if (next.packageName.equals("com.UCMobile.intl")) {
                    i2 = 2;
                    break;
                }
            } else {
                i2 = 1;
                break;
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new String[]{"lt", "ev"});
        arrayList.add(new String[]{"ct", "corepv"});
        arrayList.add(new String[]{"pkg", this.k.getPackageName()});
        String[] strArr = new String[2];
        strArr[0] = "sdk_pm";
        if (com.uc.webview.export.internal.utility.b.a(android.os.Build.MODEL)) {
            str = "unknown";
        } else {
            str = android.os.Build.MODEL.trim().replaceAll("[`|=]", "");
        }
        strArr[1] = str;
        arrayList.add(strArr);
        String[] strArr2 = new String[2];
        strArr2[0] = "sdk_f";
        StringBuilder sb = new StringBuilder();
        sb.append((SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED)).booleanValue() || SDKFactory.e(this.k) == null) ? "0" : "1");
        sb.append(SDKFactory.c((Long) 1L).booleanValue() ? "1" : "0");
        sb.append(d.a().b(UCCore.OPTION_MULTI_CORE_TYPE) ? "1" : "0");
        sb.append(SDKFactory.c((Long) 2L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 4L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 8L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 16L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 32L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 64L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 128L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 256L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 512L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 1024L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c((Long) 2048L).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_URI)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_URI)).booleanValue() ? "1" : "0");
        sb.append(SDKFactory.c(Long.valueOf(PlaybackStateCompat.ACTION_SET_REPEAT_MODE)).booleanValue() ? "1" : "0");
        strArr2[1] = sb.toString();
        arrayList.add(strArr2);
        arrayList.add(new String[]{"sdk_uf", String.valueOf(i2)});
        String[] strArr3 = new String[2];
        strArr3[0] = "sdk_bd";
        if (com.uc.webview.export.internal.utility.b.a(android.os.Build.BRAND)) {
            str2 = "unknown";
        } else {
            str2 = android.os.Build.BRAND.trim().replaceAll("[`|=]", "");
        }
        strArr3[1] = str2;
        arrayList.add(strArr3);
        arrayList.add(new String[]{"sdk_osv", Build.VERSION.RELEASE});
        arrayList.add(new String[]{"sdk_prd", com.uc.webview.export.Build.SDK_PRD});
        arrayList.add(new String[]{"sdk_pfid", com.uc.webview.export.Build.SDK_PROFILE_ID});
        arrayList.add(new String[]{"sdk_cos", k.e()});
        arrayList.add(new String[]{"pro_sf", (String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION)});
        arrayList.add(new String[]{"uuid", a(this.k.getSharedPreferences("UC_WA_STAT", 4))});
        String str3 = (String) UCCore.getGlobalOption(UCCore.ADAPTER_BUILD_TIMING);
        if (!com.uc.webview.export.internal.utility.b.a(str3)) {
            arrayList.add(new String[]{"ab_sn", str3});
        }
        String str4 = (String) UCCore.getGlobalOption(UCCore.ADAPTER_BUILD_VERSOPM);
        if (!com.uc.webview.export.internal.utility.b.a(str4)) {
            arrayList.add(new String[]{"ab_ve", str4});
        }
        if (!com.uc.webview.export.internal.utility.b.a(com.uc.webview.export.Build.CORE_VERSION)) {
            arrayList.add(new String[]{"sdk_sdk_cv", com.uc.webview.export.Build.CORE_VERSION.trim()});
        }
        if (!com.uc.webview.export.internal.utility.b.a(com.uc.webview.export.Build.UCM_VERSION)) {
            arrayList.add(new String[]{"sdk_ucm_cv", com.uc.webview.export.Build.UCM_VERSION.trim()});
        }
        if (i2 == 0) {
            String[] strArr4 = new String[2];
            strArr4[0] = "sdk_ucbackup";
            strArr4[1] = new File("/sdcard/Backucup/com.UCMobile").exists() ? "1" : "0";
            arrayList.add(strArr4);
        }
        Long l2 = (Long) UCCore.getGlobalOption(UCCore.STARTUP_ELAPSE_BEETWEEN_UC_INIT_AND_APP);
        if (l2 != null) {
            arrayList.add(new String[]{"st_el", Long.toString(l2.longValue())});
        }
        String[] strArr5 = new String[2];
        strArr5[0] = IWaStat.VIDEO_AC;
        strArr5[1] = SDKFactory.c((Long) 1048576L).booleanValue() ? "1" : "0";
        arrayList.add(strArr5);
        String a2 = k.b.a(this.k);
        if (!com.uc.webview.export.internal.utility.b.a(a2)) {
            arrayList.add(new String[]{IWaStat.UTDID_KEY, a2});
        } else {
            arrayList.add(new String[]{IWaStat.UTDID_KEY, BuildConfig.buildJavascriptFrameworkVersion});
        }
        arrayList.add(new String[]{"data_dir", this.k.getApplicationInfo().dataDir});
        File file = (File) ReflectionUtil.invokeNoThrow((Object) this.k, "getSharedPrefsFile", new Class[]{String.class}, new Object[]{"UC_WA_STAT"});
        if (file != null) {
            arrayList.add(new String[]{"sp_dir", file.getAbsolutePath()});
        }
        arrayList.add(new String[]{"thrct", Integer.toString(i.b())});
        Integer num = (Integer) UCCore.getGlobalOption(UCCore.OPTION_APP_STARTUP_OPPORTUNITY);
        if (num != null) {
            arrayList.add(new String[]{"ini_op", Integer.toString(num.intValue())});
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(k.g() ? "thick" : com.taobao.uc.BuildConfig.UC_CORE_TYPE);
        String str5 = (String) d.a().a(UCCore.OPTION_BUSINESS_INIT_TYPE);
        if (!k.a(str5)) {
            sb2.append('_');
            sb2.append(str5);
        }
        Boolean bool = (Boolean) d.a().a("gk_dec_exist");
        if (bool != null) {
            sb2.append('_');
            sb2.append(bool.booleanValue() ? "dec_exist" : "dec_not");
        }
        Boolean bool2 = (Boolean) d.a().a("gk_upd_exist");
        if (bool2 != null) {
            sb2.append('_');
            sb2.append(bool2.booleanValue() ? "upd_exist" : "upd_not");
        }
        Boolean bool3 = (Boolean) d.a().a("gk_quick_path");
        if (bool3 != null) {
            sb2.append('_');
            sb2.append(bool3.booleanValue() ? "qpath" : "qpath_not");
        }
        Boolean bool4 = (Boolean) d.a().a("gk_quick_init");
        if (bool4 != null) {
            sb2.append('_');
            sb2.append(bool4.booleanValue() ? "quick" : "quick_not");
        }
        if (b) {
            Log.i("SDKWaStat", "getSetupType:" + sb2.toString());
        }
        String sb3 = sb2.toString();
        if (!k.a(sb3)) {
            arrayList.add(new String[]{"setup_tp", sb3});
        }
        int i3 = d.a().b("gk_init_pre") ? 1 : 0;
        if (d.a().b("gk_preload_io")) {
            i3 += 2;
        }
        if (d.a().b("gk_preload_so")) {
            i3 += 4;
        }
        if (d.a().b("gk_preload_cl")) {
            i3 += 8;
        }
        if (b) {
            Log.i("SDKWaStat", "getInitPreprocesses:" + i3);
        }
        String num2 = Integer.toString(i3);
        if (!k.a(num2)) {
            arrayList.add(new String[]{"ini_pre", num2});
        }
        return arrayList;
    }

    static /* synthetic */ void b(String str) {
        IGlobalSettings e2 = SDKFactory.e();
        if (e2 != null) {
            e2.setStringValue(SettingKeys.SDKUUID, str);
        }
    }

    static /* synthetic */ void c(a aVar) {
        if (c()) {
            aVar.b().post(new e(aVar));
        }
    }

    static /* synthetic */ byte[] a(a aVar, String[] strArr) {
        Object[] j2 = aVar.j();
        if (j2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("lt=uc");
        Map map = (Map) j2[0];
        List list = (List) j2[1];
        List<PackageInfo> b2 = b(aVar.k);
        List<String[]> b3 = aVar.b(b2);
        strArr[0] = b((Map<String, C0025a>) map, (List<b>) list);
        Iterator it = map.entrySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Map.Entry entry = (Map.Entry) it.next();
            if (sb.length() < e) {
                sb.append("\n");
                for (String[] next : b3) {
                    a(sb, next[0], next[1]);
                }
                if (!k.f() && c(((C0025a) entry.getValue()).b).equals(strArr[0])) {
                    for (String[] next2 : aVar.a(b2, strArr[0])) {
                        a(sb, next2[0], next2[1]);
                    }
                }
                for (Map.Entry next3 : ((C0025a) entry.getValue()).a.entrySet()) {
                    a(sb, (String) next3.getKey(), String.valueOf(next3.getValue()));
                }
                for (Map.Entry next4 : ((C0025a) entry.getValue()).b.entrySet()) {
                    a(sb, (String) next4.getKey(), (String) next4.getValue());
                }
                for (Map.Entry next5 : SDKFactory.q.entrySet()) {
                    a(sb, (String) next5.getKey(), String.valueOf(((Integer) next5.getValue()).intValue()));
                }
            } else if (b) {
                Log.d("SDKWaStat", "getUploadData MergeData size(" + sb.length() + ") more then " + e);
            }
        }
        Iterator it2 = list.iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            b bVar = (b) it2.next();
            if (sb.length() < e) {
                sb.append("\n");
                for (String[] next6 : b3) {
                    a(sb, next6[0], next6[1]);
                }
                for (Map.Entry next7 : bVar.b.entrySet()) {
                    a(sb, (String) next7.getKey(), (String) next7.getValue());
                }
            } else if (b) {
                Log.d("SDKWaStat", "getUploadData UnMergeData size(" + sb.length() + ") more then " + e);
            }
        }
        if (b) {
            Log.i("SDKWaStat", "getUploadData:\n" + sb.toString());
        }
        sb.append("\n");
        a(sb, "stat_size", String.valueOf(sb.toString().getBytes().length));
        return sb.toString().getBytes();
    }

    static /* synthetic */ String a(String str, boolean z) {
        boolean b2 = d.a().b(UCCore.OPTION_SDK_INTERNATIONAL_ENV);
        String str2 = b2 ? "4ee01a39f0c1" : "27120f2b4115";
        String valueOf = String.valueOf(System.currentTimeMillis());
        String a2 = f.a(str2 + str + valueOf + "AppChk#2014");
        StringBuilder sb = new StringBuilder(b2 ? "https://gjapplog.ucweb.com/collect?uc_param_str=&chk=" : "https://applog.uc.cn/collect?uc_param_str=&chk=");
        sb.append(a2.substring(a2.length() - 8, a2.length()));
        sb.append("&vno=");
        sb.append(valueOf);
        sb.append("&uuid=");
        sb.append(str);
        sb.append("&app=");
        sb.append(str2);
        if (z) {
            sb.append("&enc=aes");
        }
        return sb.toString();
    }

    static /* synthetic */ void a(a aVar, long j2, String str) {
        SharedPreferences.Editor edit = aVar.k.getSharedPreferences("UC_WA_STAT", 4).edit();
        edit.putLong(i(), j2);
        if (str != null) {
            edit.putString("4", str);
        }
        edit.commit();
    }
}
