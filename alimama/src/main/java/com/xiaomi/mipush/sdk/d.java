package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.taobao.weex.common.WXConfig;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.g;
import com.xiaomi.push.i;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class d {
    private static volatile d a;

    /* renamed from: a  reason: collision with other field name */
    private Context f54a;

    /* renamed from: a  reason: collision with other field name */
    private a f55a;

    /* renamed from: a  reason: collision with other field name */
    String f56a;

    /* renamed from: a  reason: collision with other field name */
    private Map<String, a> f57a;

    public static class a {
        public int a = 1;

        /* renamed from: a  reason: collision with other field name */
        private Context f58a;

        /* renamed from: a  reason: collision with other field name */
        public String f59a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f60a = true;
        public String b;

        /* renamed from: b  reason: collision with other field name */
        public boolean f61b = false;
        public String c;
        public String d;
        public String e;
        public String f;
        public String g;
        public String h;

        public a(Context context) {
            this.f58a = context;
        }

        public static a a(Context context, String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                a aVar = new a(context);
                aVar.f59a = jSONObject.getString("appId");
                aVar.b = jSONObject.getString("appToken");
                aVar.c = jSONObject.getString("regId");
                aVar.d = jSONObject.getString("regSec");
                aVar.f = jSONObject.getString(WXConfig.devId);
                aVar.e = jSONObject.getString("vName");
                aVar.f60a = jSONObject.getBoolean("valid");
                aVar.f61b = jSONObject.getBoolean(IWXAudio.KEY_PAUSED);
                aVar.a = jSONObject.getInt("envType");
                aVar.g = jSONObject.getString("regResource");
                return aVar;
            } catch (Throwable th) {
                b.a(th);
                return null;
            }
        }

        private String a() {
            return g.a(this.f58a, this.f58a.getPackageName());
        }

        public static String a(a aVar) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("appId", aVar.f59a);
                jSONObject.put("appToken", aVar.b);
                jSONObject.put("regId", aVar.c);
                jSONObject.put("regSec", aVar.d);
                jSONObject.put(WXConfig.devId, aVar.f);
                jSONObject.put("vName", aVar.e);
                jSONObject.put("valid", aVar.f60a);
                jSONObject.put(IWXAudio.KEY_PAUSED, aVar.f61b);
                jSONObject.put("envType", aVar.a);
                jSONObject.put("regResource", aVar.g);
                return jSONObject.toString();
            } catch (Throwable th) {
                b.a(th);
                return null;
            }
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m62a() {
            d.a(this.f58a).edit().clear().commit();
            this.f59a = null;
            this.b = null;
            this.c = null;
            this.d = null;
            this.f = null;
            this.e = null;
            this.f60a = false;
            this.f61b = false;
            this.h = null;
            this.a = 1;
        }

        public void a(int i) {
            this.a = i;
        }

        public void a(String str, String str2) {
            this.c = str;
            this.d = str2;
            this.f = i.l(this.f58a);
            this.e = a();
            this.f60a = true;
        }

        public void a(String str, String str2, String str3) {
            this.f59a = str;
            this.b = str2;
            this.g = str3;
            SharedPreferences.Editor edit = d.a(this.f58a).edit();
            edit.putString("appId", this.f59a);
            edit.putString("appToken", str2);
            edit.putString("regResource", str3);
            edit.commit();
        }

        public void a(boolean z) {
            this.f61b = z;
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m63a() {
            return a(this.f59a, this.b);
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m64a(String str, String str2) {
            return TextUtils.equals(this.f59a, str) && TextUtils.equals(this.b, str2) && !TextUtils.isEmpty(this.c) && !TextUtils.isEmpty(this.d) && (TextUtils.equals(this.f, i.l(this.f58a)) || TextUtils.equals(this.f, i.k(this.f58a)));
        }

        public void b() {
            this.f60a = false;
            d.a(this.f58a).edit().putBoolean("valid", this.f60a).commit();
        }

        public void b(String str, String str2, String str3) {
            this.c = str;
            this.d = str2;
            this.f = i.l(this.f58a);
            this.e = a();
            this.f60a = true;
            this.h = str3;
            SharedPreferences.Editor edit = d.a(this.f58a).edit();
            edit.putString("regId", str);
            edit.putString("regSec", str2);
            edit.putString(WXConfig.devId, this.f);
            edit.putString("vName", a());
            edit.putBoolean("valid", true);
            edit.putString("appRegion", str3);
            edit.commit();
        }

        public void c(String str, String str2, String str3) {
            this.f59a = str;
            this.b = str2;
            this.g = str3;
        }
    }

    private d(Context context) {
        this.f54a = context;
        c();
    }

    public static SharedPreferences a(Context context) {
        return context.getSharedPreferences("mipush", 0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static d m50a(Context context) {
        if (a == null) {
            synchronized (d.class) {
                if (a == null) {
                    a = new d(context);
                }
            }
        }
        return a;
    }

    private void c() {
        this.f55a = new a(this.f54a);
        this.f57a = new HashMap();
        SharedPreferences a2 = a(this.f54a);
        this.f55a.f59a = a2.getString("appId", (String) null);
        this.f55a.b = a2.getString("appToken", (String) null);
        this.f55a.c = a2.getString("regId", (String) null);
        this.f55a.d = a2.getString("regSec", (String) null);
        this.f55a.f = a2.getString(WXConfig.devId, (String) null);
        if (!TextUtils.isEmpty(this.f55a.f) && this.f55a.f.startsWith("a-")) {
            this.f55a.f = i.l(this.f54a);
            a2.edit().putString(WXConfig.devId, this.f55a.f).commit();
        }
        this.f55a.e = a2.getString("vName", (String) null);
        this.f55a.f60a = a2.getBoolean("valid", true);
        this.f55a.f61b = a2.getBoolean(IWXAudio.KEY_PAUSED, false);
        this.f55a.a = a2.getInt("envType", 1);
        this.f55a.g = a2.getString("regResource", (String) null);
        this.f55a.h = a2.getString("appRegion", (String) null);
    }

    public int a() {
        return this.f55a.a;
    }

    public a a(String str) {
        if (this.f57a.containsKey(str)) {
            return this.f57a.get(str);
        }
        String str2 = "hybrid_app_info_" + str;
        SharedPreferences a2 = a(this.f54a);
        if (!a2.contains(str2)) {
            return null;
        }
        a a3 = a.a(this.f54a, a2.getString(str2, ""));
        this.f57a.put(str2, a3);
        return a3;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m51a() {
        return this.f55a.f59a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m52a() {
        this.f55a.a();
    }

    public void a(int i) {
        this.f55a.a(i);
        a(this.f54a).edit().putInt("envType", i).commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m53a(String str) {
        SharedPreferences.Editor edit = a(this.f54a).edit();
        edit.putString("vName", str);
        edit.commit();
        this.f55a.e = str;
    }

    public void a(String str, a aVar) {
        this.f57a.put(str, aVar);
        String a2 = a.a(aVar);
        a(this.f54a).edit().putString("hybrid_app_info_" + str, a2).commit();
    }

    public void a(String str, String str2, String str3) {
        this.f55a.a(str, str2, str3);
    }

    public void a(boolean z) {
        this.f55a.a(z);
        a(this.f54a).edit().putBoolean(IWXAudio.KEY_PAUSED, z).commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m54a() {
        return !TextUtils.equals(g.a(this.f54a, this.f54a.getPackageName()), this.f55a.e);
    }

    public boolean a(String str, String str2) {
        return this.f55a.a(str, str2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m55a(String str, String str2, String str3) {
        a a2 = a(str3);
        return a2 != null && TextUtils.equals(str, a2.f59a) && TextUtils.equals(str2, a2.b);
    }

    public String b() {
        return this.f55a.b;
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m56b() {
        this.f55a.b();
    }

    public void b(String str) {
        this.f57a.remove(str);
        a(this.f54a).edit().remove("hybrid_app_info_" + str).commit();
    }

    public void b(String str, String str2, String str3) {
        this.f55a.b(str, str2, str3);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m57b() {
        if (this.f55a.a()) {
            return true;
        }
        b.a("Don't send message before initialization succeeded!");
        return false;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m58c() {
        return this.f55a.c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m59c() {
        return this.f55a.a();
    }

    public String d() {
        return this.f55a.d;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m60d() {
        return this.f55a.f61b;
    }

    public String e() {
        return this.f55a.g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m61e() {
        return !this.f55a.f60a;
    }

    public String f() {
        return this.f55a.h;
    }
}
