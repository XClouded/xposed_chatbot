package com.xiaomi.push;

import android.text.TextUtils;
import com.alipay.sdk.app.statistic.c;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

public class cq {
    private double a = 0.1d;

    /* renamed from: a  reason: collision with other field name */
    private long f191a;

    /* renamed from: a  reason: collision with other field name */
    public String f192a = "";

    /* renamed from: a  reason: collision with other field name */
    private ArrayList<cz> f193a = new ArrayList<>();
    private long b = 86400000;

    /* renamed from: b  reason: collision with other field name */
    public String f194b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    protected String h;
    private String i;
    private String j = "s.mi1.cc";

    public cq(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.f191a = System.currentTimeMillis();
            this.f193a.add(new cz(str, -1));
            this.f192a = cu.a();
            this.f194b = str;
            return;
        }
        throw new IllegalArgumentException("the host is empty");
    }

    private synchronized void c(String str) {
        Iterator<cz> it = this.f193a.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(it.next().f210a, str)) {
                it.remove();
            }
        }
    }

    public synchronized cq a(JSONObject jSONObject) {
        this.f192a = jSONObject.optString(c.a);
        this.b = jSONObject.getLong("ttl");
        this.a = jSONObject.getDouble("pct");
        this.f191a = jSONObject.getLong("ts");
        this.d = jSONObject.optString("city");
        this.c = jSONObject.optString("prv");
        this.g = jSONObject.optString("cty");
        this.e = jSONObject.optString("isp");
        this.f = jSONObject.optString("ip");
        this.f194b = jSONObject.optString("host");
        this.h = jSONObject.optString("xf");
        JSONArray jSONArray = jSONObject.getJSONArray("fbs");
        for (int i2 = 0; i2 < jSONArray.length(); i2++) {
            a(new cz().a(jSONArray.getJSONObject(i2)));
        }
        return this;
    }

    public synchronized String a() {
        if (!TextUtils.isEmpty(this.i)) {
            return this.i;
        } else if (TextUtils.isEmpty(this.e)) {
            return "hardcode_isp";
        } else {
            this.i = ay.a((Object[]) new String[]{this.e, this.c, this.d, this.g, this.f}, "_");
            return this.i;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized ArrayList<String> m153a() {
        return a(false);
    }

    public ArrayList<String> a(String str) {
        if (!TextUtils.isEmpty(str)) {
            URL url = new URL(str);
            if (TextUtils.equals(url.getHost(), this.f194b)) {
                ArrayList<String> arrayList = new ArrayList<>();
                Iterator<String> it = a(true).iterator();
                while (it.hasNext()) {
                    cs a2 = cs.a(it.next(), url.getPort());
                    arrayList.add(new URL(url.getProtocol(), a2.a(), a2.a(), url.getFile()).toString());
                }
                return arrayList;
            }
            throw new IllegalArgumentException("the url is not supported by the fallback");
        }
        throw new IllegalArgumentException("the url is empty.");
    }

    public synchronized ArrayList<String> a(boolean z) {
        ArrayList<String> arrayList;
        String substring;
        cz[] czVarArr = new cz[this.f193a.size()];
        this.f193a.toArray(czVarArr);
        Arrays.sort(czVarArr);
        arrayList = new ArrayList<>();
        for (cz czVar : czVarArr) {
            if (z) {
                substring = czVar.f210a;
            } else {
                int indexOf = czVar.f210a.indexOf(":");
                substring = indexOf != -1 ? czVar.f210a.substring(0, indexOf) : czVar.f210a;
            }
            arrayList.add(substring);
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized JSONObject m154a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put(c.a, this.f192a);
        jSONObject.put("ttl", this.b);
        jSONObject.put("pct", this.a);
        jSONObject.put("ts", this.f191a);
        jSONObject.put("city", this.d);
        jSONObject.put("prv", this.c);
        jSONObject.put("cty", this.g);
        jSONObject.put("isp", this.e);
        jSONObject.put("ip", this.f);
        jSONObject.put("host", this.f194b);
        jSONObject.put("xf", this.h);
        JSONArray jSONArray = new JSONArray();
        Iterator<cz> it = this.f193a.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().a());
        }
        jSONObject.put("fbs", jSONArray);
        return jSONObject;
    }

    public void a(double d2) {
        this.a = d2;
    }

    public void a(long j2) {
        if (j2 > 0) {
            this.b = j2;
            return;
        }
        throw new IllegalArgumentException("the duration is invalid " + j2);
    }

    /* access modifiers changed from: package-private */
    public synchronized void a(cz czVar) {
        c(czVar.f210a);
        this.f193a.add(czVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m155a(String str) {
        a(new cz(str));
    }

    public void a(String str, int i2, long j2, long j3, Exception exc) {
        a(str, new cp(i2, j2, j3, exc));
    }

    public void a(String str, long j2, long j3) {
        try {
            b(new URL(str).getHost(), j2, j3);
        } catch (MalformedURLException unused) {
        }
    }

    public void a(String str, long j2, long j3, Exception exc) {
        try {
            b(new URL(str).getHost(), j2, j3, exc);
        } catch (MalformedURLException unused) {
        }
    }

    public synchronized void a(String str, cp cpVar) {
        Iterator<cz> it = this.f193a.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            cz next = it.next();
            if (TextUtils.equals(str, next.f210a)) {
                next.a(cpVar);
                break;
            }
        }
    }

    public synchronized void a(String[] strArr) {
        int i2;
        int size = this.f193a.size() - 1;
        while (true) {
            i2 = 0;
            if (size < 0) {
                break;
            }
            int length = strArr.length;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (TextUtils.equals(this.f193a.get(size).f210a, strArr[i2])) {
                    this.f193a.remove(size);
                    break;
                }
                i2++;
            }
            size--;
        }
        Iterator<cz> it = this.f193a.iterator();
        int i3 = 0;
        while (it.hasNext()) {
            cz next = it.next();
            if (next.a > i3) {
                i3 = next.a;
            }
        }
        while (i2 < strArr.length) {
            a(new cz(strArr[i2], (strArr.length + i3) - i2));
            i2++;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m156a() {
        return TextUtils.equals(this.f192a, cu.a());
    }

    public boolean a(cq cqVar) {
        return TextUtils.equals(this.f192a, cqVar.f192a);
    }

    public void b(String str) {
        this.j = str;
    }

    public void b(String str, long j2, long j3) {
        a(str, 0, j2, j3, (Exception) null);
    }

    public void b(String str, long j2, long j3, Exception exc) {
        a(str, -1, j2, j3, exc);
    }

    public boolean b() {
        return System.currentTimeMillis() - this.f191a < this.b;
    }

    /* access modifiers changed from: package-private */
    public boolean c() {
        long j2 = 864000000;
        if (864000000 < this.b) {
            j2 = this.b;
        }
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis - this.f191a > j2 || (currentTimeMillis - this.f191a > this.b && this.f192a.startsWith("WIFI-"));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.f192a);
        sb.append("\n");
        sb.append(a());
        Iterator<cz> it = this.f193a.iterator();
        while (it.hasNext()) {
            sb.append("\n");
            sb.append(it.next().toString());
        }
        sb.append("\n");
        return sb.toString();
    }
}
