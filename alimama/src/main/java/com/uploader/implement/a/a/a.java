package com.uploader.implement.a.a;

import android.text.TextUtils;
import android.util.Pair;
import com.uploader.implement.a.c.b;
import com.uploader.implement.a.e;
import com.uploader.implement.a.f;
import com.uploader.implement.a.h;
import com.uploader.implement.b.a.g;
import com.uploader.implement.c;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: DeclareUploadActionRequest */
public class a implements e {
    g a;
    final String b;
    final long c;
    final Pair<String, Integer> d;
    final c e;
    final int f = hashCode();
    final h g;

    public a(c cVar) throws Exception {
        this.e = cVar;
        this.d = cVar.a.b();
        this.b = cVar.a.g();
        this.c = cVar.a.f();
        this.g = new h((File) null, 0, 0, 0, d(), (byte[]) null, (byte[]) null, (byte[]) null);
    }

    /* renamed from: c */
    public g a() {
        if (this.a != null) {
            return this.a;
        }
        StringBuilder sb = new StringBuilder(32);
        if (((Integer) this.d.second).intValue() == 443) {
            sb.append("https://");
            sb.append((String) this.d.first);
            sb.append(":");
            sb.append(this.d.second);
        } else {
            sb.append("http://");
            sb.append((String) this.d.first);
        }
        sb.append("/dispatchUpload.api");
        g gVar = new g((String) this.d.first, ((Integer) this.d.second).intValue(), sb.toString(), this.b);
        this.a = gVar;
        return gVar;
    }

    public h b() {
        return this.g;
    }

    public Pair<f, Integer> a(Map<String, String> map, byte[] bArr, int i, int i2) {
        if (map == null || bArr == null) {
            return new Pair<>((Object) null, 0);
        }
        try {
            return new Pair<>(b(map, bArr, i, i2), Integer.valueOf(i2));
        } catch (Exception e2) {
            if (com.uploader.implement.a.a(2)) {
                com.uploader.implement.a.a(2, "DeclareUploadActionRequest", e2.toString());
            }
            return new Pair<>((Object) null, -1);
        }
    }

    private f b(Map<String, String> map, byte[] bArr, int i, int i2) throws Exception {
        if (map.get("x-arup-error-code") != null) {
            return new com.uploader.implement.a.b.a(5, map);
        }
        String str = null;
        if (bArr != null) {
            str = new String(bArr, i, i2);
        }
        map.put("divided_length", Integer.toString(i2));
        return new com.uploader.implement.a.b.a(1, map, a(str));
    }

    private final Map<String, String> d() throws Exception {
        String str = this.e.b.getCurrentElement().appKey;
        String utdid = this.e.b.getUtdid();
        String userId = this.e.b.getUserId();
        String appVersion = this.e.b.getAppVersion();
        String valueOf = String.valueOf(this.c + (System.currentTimeMillis() / 1000));
        HashMap hashMap = new HashMap();
        hashMap.put("content-type", "application/json;charset=utf-8");
        hashMap.put("x-arup-version", "2.1");
        hashMap.put("host", b.b(this.b));
        hashMap.put("x-arup-appkey", b.b(str));
        hashMap.put("x-arup-appversion", b.b(appVersion));
        hashMap.put("x-arup-device-id", b.b(utdid));
        if (!TextUtils.isEmpty(userId)) {
            hashMap.put("x-arup-userinfo", b.b(userId));
        }
        hashMap.put("x-arup-timestamp", b.b(valueOf));
        StringBuilder sb = new StringBuilder(128);
        sb.append("/dispatchUpload.api");
        sb.append("&");
        sb.append(str);
        sb.append("&");
        sb.append(appVersion);
        sb.append("&");
        sb.append(utdid);
        sb.append("&");
        sb.append(valueOf);
        String signature = this.e.b.signature(sb.toString());
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "DeclareUploadActionRequest", "compute api sign:" + signature + ", input=" + sb);
        }
        if (TextUtils.isEmpty(signature)) {
            if (com.uploader.implement.a.a(16)) {
                com.uploader.implement.a.a(16, "DeclareUploadActionRequest", "compute api sign failed.");
            }
            throw new Exception("compute api sign failed.");
        }
        hashMap.put("x-arup-sign", b.b(signature));
        if (com.uploader.implement.a.a(2)) {
            com.uploader.implement.a.a(2, "DeclareUploadActionRequest", " create declare header:" + hashMap.toString());
        }
        return hashMap;
    }

    private Object[] a(String str) throws Exception {
        JSONObject jSONObject = new JSONObject(str);
        JSONArray jSONArray = jSONObject.getJSONArray("apiServerList");
        ArrayList arrayList = new ArrayList(jSONArray.length());
        for (int length = jSONArray.length() - 1; length >= 0; length--) {
            JSONObject jSONObject2 = jSONArray.getJSONObject(length);
            arrayList.add(0, new Pair(jSONObject2.getString("ip"), Integer.valueOf(jSONObject2.getInt("port"))));
        }
        JSONArray jSONArray2 = jSONObject.getJSONArray("uploadServerList");
        ArrayList arrayList2 = new ArrayList();
        for (int length2 = jSONArray2.length() - 1; length2 >= 0; length2--) {
            JSONObject jSONObject3 = jSONArray2.getJSONObject(length2);
            arrayList2.add(0, new Pair(Boolean.valueOf(jSONObject3.getBoolean("encrypt")), new Pair(jSONObject3.getString("ip"), Integer.valueOf(jSONObject3.getInt("port")))));
        }
        return new Object[]{jSONObject.getString("token"), Long.valueOf(jSONObject.getLong("expires")), arrayList, arrayList2};
    }
}
