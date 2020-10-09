package com.huawei.hianalytics.f.b;

import android.text.TextUtils;
import com.huawei.hianalytics.a.c;
import com.huawei.hianalytics.g.b;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class h {
    private String a;
    private i b;
    private j c;
    private b d;
    private a[] e;
    private String f;
    private String g;

    public h(String str, String str2, String str3) {
        this.a = str;
        this.f = str2;
        this.g = str3;
    }

    public void a(b bVar) {
        this.d = bVar;
    }

    public void a(i iVar) {
        this.b = iVar;
    }

    public void a(j jVar) {
        this.c = jVar;
    }

    public void a(List<a> list) {
        this.e = list == null ? null : (a[]) list.toArray(new a[list.size()]);
    }

    public a[] a() {
        a[] aVarArr = this.e;
        if (aVarArr == null) {
            return new a[0];
        }
        a[] aVarArr2 = new a[aVarArr.length];
        System.arraycopy(aVarArr, 0, aVarArr2, 0, aVarArr.length);
        return aVarArr2;
    }

    public JSONObject b() {
        String str;
        String str2;
        String str3;
        Object obj;
        if (this.e == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            JSONObject jSONObject2 = new JSONObject();
            if (this.b != null) {
                jSONObject.put("header", this.b.b());
            }
            if (!(this.c == null || this.d == null)) {
                JSONObject a2 = this.d.a();
                a2.put("properties", this.c.a());
                String f2 = c.f(this.f, this.g);
                if (!TextUtils.isEmpty(f2)) {
                    obj = new JSONObject(f2);
                    str3 = "events_global_properties";
                } else {
                    str3 = "events_global_properties";
                    obj = "";
                }
                a2.put(str3, obj);
                jSONObject2.put("events_common", a2);
            }
            JSONArray jSONArray = new JSONArray();
            for (a b2 : this.e) {
                jSONArray.put(b2.b());
            }
            jSONObject2.put(ProtocolConst.KEY_EVENTS, jSONArray);
            byte[] a3 = com.huawei.hianalytics.f.g.h.a(jSONObject2.toString().getBytes("UTF-8"));
            byte[] a4 = com.huawei.hianalytics.f.g.c.a();
            jSONObject.put("event", com.huawei.hianalytics.f.g.c.a(a4, com.huawei.hianalytics.f.g.c.a(this.a, a4, a3)));
        } catch (JSONException unused) {
            str2 = "UploadData";
            str = "event upload data - toJsonObj(): JSON Exception has happen";
            b.c(str2, str);
            return jSONObject;
        } catch (UnsupportedEncodingException unused2) {
            str2 = "UploadData";
            str = "getBitZip(): Unsupported coding : utf-8";
            b.c(str2, str);
            return jSONObject;
        }
        return jSONObject;
    }
}
