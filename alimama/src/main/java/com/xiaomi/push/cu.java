package com.xiaomi.push;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Process;
import android.text.TextUtils;
import com.alibaba.aliweex.adapter.module.calendar.DateUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.ui.component.WXBasicComponentType;
import com.taobao.weex.ui.module.WXModalUIModule;
import com.xiaomi.push.service.module.PushChannelRegion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cu {
    protected static Context a;

    /* renamed from: a  reason: collision with other field name */
    private static a f197a;

    /* renamed from: a  reason: collision with other field name */
    private static cu f198a;

    /* renamed from: a  reason: collision with other field name */
    protected static boolean f199a = false;
    protected static Map<String, cq> b = new HashMap();
    private static String c;
    private static String d;

    /* renamed from: a  reason: collision with other field name */
    private long f200a;

    /* renamed from: a  reason: collision with other field name */
    private ct f201a;

    /* renamed from: a  reason: collision with other field name */
    protected b f202a;

    /* renamed from: a  reason: collision with other field name */
    private String f203a;

    /* renamed from: a  reason: collision with other field name */
    protected Map<String, cr> f204a;

    /* renamed from: b  reason: collision with other field name */
    private final long f205b;

    /* renamed from: b  reason: collision with other field name */
    private String f206b;

    /* renamed from: c  reason: collision with other field name */
    private long f207c;

    public interface a {
        cu a(Context context, ct ctVar, b bVar, String str);
    }

    public interface b {
        String a(String str);
    }

    protected cu(Context context, ct ctVar, b bVar, String str) {
        this(context, ctVar, bVar, str, (String) null, (String) null);
    }

    protected cu(Context context, ct ctVar, b bVar, String str, String str2, String str3) {
        this.f204a = new HashMap();
        this.f203a = "0";
        this.f200a = 0;
        this.f205b = 15;
        this.f207c = 0;
        this.f206b = "isp_prov_city_country_ip";
        this.f202a = bVar;
        this.f201a = ctVar == null ? new cv(this) : ctVar;
        this.f203a = str;
        c = str2 == null ? context.getPackageName() : str2;
        d = str3 == null ? f() : str3;
    }

    public static synchronized cu a() {
        cu cuVar;
        synchronized (cu.class) {
            if (f198a != null) {
                cuVar = f198a;
            } else {
                throw new IllegalStateException("the host manager is not initialized yet.");
            }
        }
        return cuVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    static String m162a() {
        NetworkInfo activeNetworkInfo;
        if (a == null) {
            return "unknown";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) a.getSystemService("connectivity");
            if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
                return "unknown";
            }
            if (activeNetworkInfo.getType() == 1) {
                WifiManager wifiManager = (WifiManager) a.getSystemService("wifi");
                if (wifiManager == null || wifiManager.getConnectionInfo() == null) {
                    return "unknown";
                }
                return "WIFI-" + wifiManager.getConnectionInfo().getSSID();
            }
            return activeNetworkInfo.getTypeName() + "-" + activeNetworkInfo.getSubtypeName();
        } catch (Throwable unused) {
            return "unknown";
        }
    }

    static String a(String str) {
        try {
            int length = str.length();
            byte[] bytes = str.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                byte b2 = bytes[i];
                byte b3 = b2 & 240;
                if (b3 != 240) {
                    bytes[i] = (byte) (((b2 & 15) ^ ((byte) (((b2 >> 4) + length) & 15))) | b3);
                }
            }
            return new String(bytes);
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    private ArrayList<cq> a(ArrayList<String> arrayList) {
        boolean z;
        JSONObject jSONObject;
        JSONObject jSONObject2;
        ArrayList<String> arrayList2 = arrayList;
        d();
        synchronized (this.f204a) {
            a();
            for (String next : this.f204a.keySet()) {
                if (!arrayList2.contains(next)) {
                    arrayList2.add(next);
                }
            }
        }
        boolean isEmpty = b.isEmpty();
        synchronized (b) {
            z = isEmpty;
            for (Object obj : b.values().toArray()) {
                cq cqVar = (cq) obj;
                if (!cqVar.b()) {
                    b.remove(cqVar.f194b);
                    z = true;
                }
            }
        }
        if (!arrayList2.contains(b())) {
            arrayList2.add(b());
        }
        ArrayList<cq> arrayList3 = new ArrayList<>(arrayList.size());
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList3.add((Object) null);
        }
        try {
            String str = as.d(a) ? "wifi" : "wap";
            String a2 = a(arrayList2, str, this.f203a, z);
            if (!TextUtils.isEmpty(a2)) {
                JSONObject jSONObject3 = new JSONObject(a2);
                com.xiaomi.channel.commonutils.logger.b.b(a2);
                if (WXModalUIModule.OK.equalsIgnoreCase(jSONObject3.getString("S"))) {
                    JSONObject jSONObject4 = jSONObject3.getJSONObject("R");
                    String string = jSONObject4.getString("province");
                    String string2 = jSONObject4.getString("city");
                    String string3 = jSONObject4.getString("isp");
                    String string4 = jSONObject4.getString("ip");
                    String string5 = jSONObject4.getString("country");
                    JSONObject jSONObject5 = jSONObject4.getJSONObject(str);
                    com.xiaomi.channel.commonutils.logger.b.c("get bucket: ip = " + string4 + " net = " + string3 + " hosts = " + jSONObject5.toString());
                    int i2 = 0;
                    while (i2 < arrayList.size()) {
                        String str2 = arrayList2.get(i2);
                        JSONArray optJSONArray = jSONObject5.optJSONArray(str2);
                        if (optJSONArray == null) {
                            com.xiaomi.channel.commonutils.logger.b.a("no bucket found for " + str2);
                            jSONObject = jSONObject5;
                        } else {
                            cq cqVar2 = new cq(str2);
                            int i3 = 0;
                            while (i3 < optJSONArray.length()) {
                                String string6 = optJSONArray.getString(i3);
                                if (!TextUtils.isEmpty(string6)) {
                                    jSONObject2 = jSONObject5;
                                    cqVar2.a(new cz(string6, optJSONArray.length() - i3));
                                } else {
                                    jSONObject2 = jSONObject5;
                                }
                                i3++;
                                jSONObject5 = jSONObject2;
                            }
                            jSONObject = jSONObject5;
                            arrayList3.set(i2, cqVar2);
                            cqVar2.g = string5;
                            cqVar2.c = string;
                            cqVar2.e = string3;
                            cqVar2.f = string4;
                            cqVar2.d = string2;
                            if (jSONObject4.has("stat-percent")) {
                                cqVar2.a(jSONObject4.getDouble("stat-percent"));
                            }
                            if (jSONObject4.has("stat-domain")) {
                                cqVar2.b(jSONObject4.getString("stat-domain"));
                            }
                            if (jSONObject4.has("ttl")) {
                                cqVar2.a(((long) jSONObject4.getInt("ttl")) * 1000);
                            }
                            a(cqVar2.a());
                        }
                        i2++;
                        jSONObject5 = jSONObject;
                    }
                    JSONObject optJSONObject = jSONObject4.optJSONObject("reserved");
                    if (optJSONObject != null) {
                        long j = DateUtils.WEEK;
                        if (jSONObject4.has("reserved-ttl")) {
                            j = ((long) jSONObject4.getInt("reserved-ttl")) * 1000;
                        }
                        Iterator<String> keys = optJSONObject.keys();
                        while (keys.hasNext()) {
                            String next2 = keys.next();
                            JSONArray optJSONArray2 = optJSONObject.optJSONArray(next2);
                            if (optJSONArray2 == null) {
                                com.xiaomi.channel.commonutils.logger.b.a("no bucket found for " + next2);
                            } else {
                                cq cqVar3 = new cq(next2);
                                cqVar3.a(j);
                                for (int i4 = 0; i4 < optJSONArray2.length(); i4++) {
                                    String string7 = optJSONArray2.getString(i4);
                                    if (!TextUtils.isEmpty(string7)) {
                                        cqVar3.a(new cz(string7, optJSONArray2.length() - i4));
                                    }
                                }
                                synchronized (b) {
                                    if (this.f201a.a(next2)) {
                                        b.put(next2, cqVar3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a("failed to get bucket " + e.getMessage());
        }
        for (int i5 = 0; i5 < arrayList.size(); i5++) {
            cq cqVar4 = arrayList3.get(i5);
            if (cqVar4 != null) {
                a(arrayList2.get(i5), cqVar4);
            }
        }
        c();
        return arrayList3;
    }

    public static synchronized void a(Context context, ct ctVar, b bVar, String str, String str2, String str3) {
        synchronized (cu.class) {
            a = context.getApplicationContext();
            if (a == null) {
                a = context;
            }
            if (f198a == null) {
                if (f197a == null) {
                    f198a = new cu(context, ctVar, bVar, str, str2, str3);
                } else {
                    f198a = f197a.a(context, ctVar, bVar, str);
                }
            }
        }
    }

    public static synchronized void a(a aVar) {
        synchronized (cu.class) {
            f197a = aVar;
            f198a = null;
        }
    }

    public static void a(String str, String str2) {
        cq cqVar = b.get(str);
        synchronized (b) {
            if (cqVar == null) {
                try {
                    cq cqVar2 = new cq(str);
                    cqVar2.a((long) DateUtils.WEEK);
                    cqVar2.a(str2);
                    b.put(str, cqVar2);
                } catch (Throwable th) {
                    throw th;
                }
            } else {
                cqVar.a(str2);
            }
        }
    }

    private String f() {
        try {
            PackageInfo packageInfo = a.getPackageManager().getPackageInfo(a.getPackageName(), 16384);
            return packageInfo != null ? packageInfo.versionName : "0";
        } catch (Exception unused) {
            return "0";
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public cq m163a(String str) {
        if (!TextUtils.isEmpty(str)) {
            return a(new URL(str).getHost(), true);
        }
        throw new IllegalArgumentException("the url is empty");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
        r4 = d(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.xiaomi.push.cq a(java.lang.String r3, boolean r4) {
        /*
            r2 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 != 0) goto L_0x0034
            com.xiaomi.push.ct r0 = r2.f201a
            boolean r0 = r0.a(r3)
            if (r0 != 0) goto L_0x0010
            r3 = 0
            return r3
        L_0x0010:
            com.xiaomi.push.cq r0 = r2.c(r3)
            if (r0 == 0) goto L_0x001d
            boolean r1 = r0.b()
            if (r1 == 0) goto L_0x001d
            return r0
        L_0x001d:
            if (r4 == 0) goto L_0x002e
            android.content.Context r4 = a
            boolean r4 = com.xiaomi.push.as.b(r4)
            if (r4 == 0) goto L_0x002e
            com.xiaomi.push.cq r4 = r2.d(r3)
            if (r4 == 0) goto L_0x002e
            return r4
        L_0x002e:
            com.xiaomi.push.cw r4 = new com.xiaomi.push.cw
            r4.<init>(r2, r3, r0)
            return r4
        L_0x0034:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "the host is empty"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.cu.a(java.lang.String, boolean):com.xiaomi.push.cq");
    }

    /* access modifiers changed from: protected */
    public String a(ArrayList<String> arrayList, String str, String str2, boolean z) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        ArrayList<ar> arrayList3 = new ArrayList<>();
        arrayList3.add(new ap("type", str));
        if (str.equals("wap")) {
            arrayList3.add(new ap("conpt", a(as.a(a))));
        }
        if (z) {
            arrayList3.add(new ap("reserved", "1"));
        }
        arrayList3.add(new ap("uuid", str2));
        arrayList3.add(new ap(WXBasicComponentType.LIST, ay.a((Collection<?>) arrayList, ",")));
        arrayList3.add(new ap("countrycode", com.xiaomi.push.service.a.a(a).b()));
        cq c2 = c(b());
        String format = String.format(Locale.US, "http://%1$s/gslb/?ver=4.0", new Object[]{b()});
        if (c2 == null) {
            arrayList2.add(format);
            synchronized (b) {
                cq cqVar = b.get("resolver.msg.xiaomi.net");
                if (cqVar != null) {
                    Iterator<String> it = cqVar.a(true).iterator();
                    while (it.hasNext()) {
                        arrayList2.add(String.format(Locale.US, "http://%1$s/gslb/?ver=4.0", new Object[]{it.next()}));
                    }
                }
            }
        } else {
            arrayList2 = c2.a(format);
        }
        Iterator<String> it2 = arrayList2.iterator();
        IOException e = null;
        while (it2.hasNext()) {
            Uri.Builder buildUpon = Uri.parse(it2.next()).buildUpon();
            for (ar arVar : arrayList3) {
                buildUpon.appendQueryParameter(arVar.a(), arVar.b());
            }
            try {
                return this.f202a == null ? as.a(a, new URL(buildUpon.toString())) : this.f202a.a(buildUpon.toString());
            } catch (IOException e2) {
                e = e2;
            }
        }
        if (e == null) {
            return null;
        }
        com.xiaomi.channel.commonutils.logger.b.a("network exception: " + e.getMessage());
        throw e;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a  reason: collision with other method in class */
    public JSONObject m164a() {
        JSONObject jSONObject;
        synchronized (this.f204a) {
            jSONObject = new JSONObject();
            jSONObject.put("ver", 2);
            JSONArray jSONArray = new JSONArray();
            for (cr a2 : this.f204a.values()) {
                jSONArray.put(a2.a());
            }
            jSONObject.put("data", jSONArray);
            JSONArray jSONArray2 = new JSONArray();
            for (cq a3 : b.values()) {
                jSONArray2.put(a3.a());
            }
            jSONObject.put("reserved", jSONArray2);
        }
        return jSONObject;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m165a() {
        synchronized (this.f204a) {
            this.f204a.clear();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m166a(String str) {
        this.f206b = str;
    }

    public void a(String str, cq cqVar) {
        if (TextUtils.isEmpty(str) || cqVar == null) {
            throw new IllegalArgumentException("the argument is invalid " + str + AVFSCacheConstants.COMMA_SEP + cqVar);
        } else if (this.f201a.a(str)) {
            synchronized (this.f204a) {
                a();
                if (this.f204a.containsKey(str)) {
                    this.f204a.get(str).a(cqVar);
                } else {
                    cr crVar = new cr(str);
                    crVar.a(cqVar);
                    this.f204a.put(str, crVar);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: a  reason: collision with other method in class */
    public boolean m167a() {
        synchronized (this.f204a) {
            if (f199a) {
                return true;
            }
            f199a = true;
            this.f204a.clear();
            try {
                String d2 = d();
                if (!TextUtils.isEmpty(d2)) {
                    b(d2);
                    com.xiaomi.channel.commonutils.logger.b.b("loading the new hosts succeed");
                    return true;
                }
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.a("load bucket failure: " + th.getMessage());
            }
        }
        return false;
    }

    public cq b(String str) {
        return a(str, true);
    }

    /* access modifiers changed from: protected */
    public String b() {
        String a2 = com.xiaomi.push.service.a.a(a).a();
        return (TextUtils.isEmpty(a2) || PushChannelRegion.China.name().equals(a2)) ? "resolver.msg.xiaomi.net" : "resolver.msg.global.xiaomi.net";
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m168b() {
        ArrayList arrayList;
        synchronized (this.f204a) {
            a();
            arrayList = new ArrayList(this.f204a.keySet());
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                cr crVar = this.f204a.get(arrayList.get(size));
                if (!(crVar == null || crVar.a() == null)) {
                    arrayList.remove(size);
                }
            }
        }
        ArrayList<cq> a2 = a((ArrayList<String>) arrayList);
        for (int i = 0; i < arrayList.size(); i++) {
            if (a2.get(i) != null) {
                a((String) arrayList.get(i), a2.get(i));
            }
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: b  reason: collision with other method in class */
    public void m169b(String str) {
        synchronized (this.f204a) {
            this.f204a.clear();
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("ver") == 2) {
                JSONArray optJSONArray = jSONObject.optJSONArray("data");
                for (int i = 0; i < optJSONArray.length(); i++) {
                    cr a2 = new cr().a(optJSONArray.getJSONObject(i));
                    this.f204a.put(a2.a(), a2);
                }
                JSONArray optJSONArray2 = jSONObject.optJSONArray("reserved");
                for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                    cq a3 = new cq("").a(optJSONArray2.getJSONObject(i2));
                    b.put(a3.f194b, a3);
                }
            } else {
                throw new JSONException("Bad version");
            }
        }
    }

    /* access modifiers changed from: protected */
    public cq c(String str) {
        cr crVar;
        cq a2;
        synchronized (this.f204a) {
            a();
            crVar = this.f204a.get(str);
        }
        if (crVar == null || (a2 = crVar.a()) == null) {
            return null;
        }
        return a2;
    }

    public String c() {
        StringBuilder sb = new StringBuilder();
        synchronized (this.f204a) {
            for (Map.Entry next : this.f204a.entrySet()) {
                sb.append((String) next.getKey());
                sb.append(":\n");
                sb.append(((cr) next.getValue()).toString());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m170c() {
        synchronized (this.f204a) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(a.openFileOutput(e(), 0)));
                String jSONObject = a().toString();
                if (!TextUtils.isEmpty(jSONObject)) {
                    bufferedWriter.write(jSONObject);
                }
                bufferedWriter.close();
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a("persist bucket failure: " + e.getMessage());
            }
        }
    }

    /* access modifiers changed from: protected */
    public cq d(String str) {
        if (System.currentTimeMillis() - this.f207c <= this.f200a * 60 * 1000) {
            return null;
        }
        this.f207c = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        cq cqVar = a((ArrayList<String>) arrayList).get(0);
        if (cqVar != null) {
            this.f200a = 0;
            return cqVar;
        } else if (this.f200a >= 15) {
            return null;
        } else {
            this.f200a++;
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String d() {
        BufferedReader bufferedReader;
        try {
            File file = new File(a.getFilesDir(), e());
            if (file.isFile()) {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                try {
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        String readLine = bufferedReader.readLine();
                        if (readLine != null) {
                            sb.append(readLine);
                        } else {
                            String sb2 = sb.toString();
                            y.a((Closeable) bufferedReader);
                            return sb2;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    try {
                        com.xiaomi.channel.commonutils.logger.b.a("load host exception " + th.getMessage());
                        y.a((Closeable) bufferedReader);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        y.a((Closeable) bufferedReader);
                        throw th;
                    }
                }
            } else {
                y.a((Closeable) null);
                return null;
            }
        } catch (Throwable th3) {
            bufferedReader = null;
            th = th3;
            y.a((Closeable) bufferedReader);
            throw th;
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m171d() {
        synchronized (this.f204a) {
            for (cr a2 : this.f204a.values()) {
                a2.a(true);
            }
            while (true) {
                for (boolean z = false; !z; z = true) {
                    for (String next : this.f204a.keySet()) {
                        if (this.f204a.get(next).a().isEmpty()) {
                            this.f204a.remove(next);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public String e() {
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) a.getSystemService("activity")).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return "com.xiaomi";
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == Process.myPid()) {
                return next.processName;
            }
        }
        return "com.xiaomi";
    }
}
