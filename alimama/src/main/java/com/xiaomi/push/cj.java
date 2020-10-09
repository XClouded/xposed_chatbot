package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class cj implements co {
    private static cj a;

    /* renamed from: a  reason: collision with other field name */
    private int f180a;

    /* renamed from: a  reason: collision with other field name */
    private Context f181a;

    /* renamed from: a  reason: collision with other field name */
    private ci f182a;

    /* renamed from: a  reason: collision with other field name */
    private String f183a;

    /* renamed from: a  reason: collision with other field name */
    private HashMap<String, ch> f184a;
    private int b;

    /* renamed from: b  reason: collision with other field name */
    private String f185b;
    private int c;

    /* renamed from: c  reason: collision with other field name */
    private String f186c;
    private int d;

    public static synchronized cj a() {
        cj cjVar;
        synchronized (cj.class) {
            cjVar = a;
        }
        return cjVar;
    }

    private String a(ArrayList<cg> arrayList, String str) {
        JSONObject jSONObject = new JSONObject();
        if (!TextUtils.isEmpty(this.f183a)) {
            jSONObject.put("imei", cm.a(this.f183a));
        }
        jSONObject.put("actionType", str);
        jSONObject.put("actionTime", System.currentTimeMillis());
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            JSONObject jSONObject2 = null;
            if (TextUtils.isEmpty(arrayList.get(i).f177a)) {
                jSONObject2 = new JSONObject();
            } else {
                try {
                    jSONObject2 = new JSONObject(arrayList.get(i).f177a);
                } catch (Exception unused) {
                    Log.e("com.xiaomi.miui.ads.pushsdk", "content 不是json串");
                }
            }
            if (jSONObject2 == null) {
                jSONObject2 = new JSONObject();
            }
            jSONObject2.put("adId", arrayList.get(i).f176a);
            arrayList2.add(jSONObject2);
        }
        jSONObject.put("adList", new JSONArray(arrayList2));
        return Base64.encodeToString(jSONObject.toString().getBytes(), 2);
    }

    private void a(ch chVar) {
        if (!this.f184a.containsKey(chVar.b)) {
            this.b++;
            cm.a("send: " + this.b);
            ck ckVar = new ck(this, this.f185b, this.f186c, chVar);
            this.f184a.put(chVar.b, chVar);
            ckVar.execute(new String[0]);
        }
    }

    private void a(ArrayList<cg> arrayList, String str, int i) {
        try {
            String a2 = a(arrayList, str);
            String a3 = cm.a(a2);
            if (a(new ch(i, a2, a3))) {
                a(new ch(i, a2, a3));
            }
        } catch (JSONException unused) {
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m149a(ch chVar) {
        if (cl.a(this.f181a)) {
            return true;
        }
        b(chVar);
        return false;
    }

    private void b(ch chVar) {
        this.d++;
        cm.a("cacheCount: " + this.d);
        this.f182a.a(chVar);
        this.f182a.a();
    }

    public void a(cg cgVar) {
        if (cgVar.f176a > 0) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(cgVar);
            a(arrayList, "click", cgVar.a);
        }
    }

    public void a(Integer num, ch chVar) {
        if (this.f184a.containsKey(chVar.b)) {
            if (num.intValue() != 0) {
                this.c++;
                cm.a("faild: " + this.c + Operators.SPACE_STR + chVar.b + "  " + this.f184a.size());
                b(chVar);
            } else {
                this.f180a++;
                cm.a("success: " + this.f180a);
            }
            this.f184a.remove(chVar.b);
        }
    }

    public void b(cg cgVar) {
        if (cgVar.f176a > 0) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(cgVar);
            a(arrayList, "remove", cgVar.a);
        }
    }

    public void c(cg cgVar) {
        if (cgVar.f176a > 0) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(cgVar);
            a(arrayList, "received", cgVar.a);
        }
    }
}
