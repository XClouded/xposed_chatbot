package com.huawei.updatesdk.sdk.service.download;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.SparseIntArray;
import com.alipay.literpc.android.phone.mrpc.core.RpcException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class a {
    private static a g = new a();
    private static SparseIntArray h = new SparseIntArray();
    public List<String> a = new ArrayList();
    public Map<String, SparseIntArray> b = new HashMap();
    private String c = null;
    private String d = null;
    private String e = null;
    private String f = null;

    static {
        h.put(1, 3000);
        h.put(2, RpcException.ErrorCode.SERVER_SERVICENOTFOUND);
    }

    public static a a() {
        return g;
    }

    public static String a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            URL url = new URL(str);
            return str2 + url.getFile();
        } catch (MalformedURLException e2) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("ConnectionParam", "updataIP exception:" + e2.getMessage());
            return str;
        }
    }

    private void a(String str) {
        this.a.add(str);
        this.b.put(str, h);
    }

    public void a(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ConnectionParam", 0);
        String string = sharedPreferences.getString("appstore.client.connectionparam.ip", (String) null);
        if (string != null) {
            String[] split = TextUtils.split(string, ",");
            if (split.length > 0) {
                b();
                for (String str : split) {
                    if (str.endsWith("\\") || str.endsWith("/")) {
                        str = str.substring(0, str.length() - 1);
                    }
                    a(str);
                }
            }
        }
        this.c = sharedPreferences.getString("appstore.client.connectionparam.domainname_http", (String) null);
        this.d = sharedPreferences.getString("appstore.client.connectionparam.domainname_https", (String) null);
    }

    public synchronized void b() {
        this.a.clear();
        this.b.clear();
    }

    public synchronized String c() {
        for (String next : this.a) {
            if (next.startsWith("https")) {
                return next;
            }
        }
        return null;
    }

    public synchronized List<String> d() {
        if (this.a.size() <= 0) {
            a(com.huawei.updatesdk.sdk.service.a.a.a().b());
        }
        return this.a;
    }
}
