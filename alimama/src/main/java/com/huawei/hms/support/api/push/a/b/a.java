package com.huawei.hms.support.api.push.a.b;

import alimama.com.unwetaologger.base.LogContent;
import android.text.TextUtils;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.agoo.control.data.BaseDO;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.weex.ui.component.richtext.node.RichTextNode;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PushSelfShowMessage */
public class a implements Serializable {
    private String A;
    private String B = "";
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private String H = "";
    private int I = 1;
    private int J = 0;
    private String K;
    private String L;
    private String M;
    private int N = com.huawei.hms.support.api.push.a.c.a.STYLE_1.ordinal();
    private int O = 0;
    private String[] P = null;
    private String[] Q = null;
    private String[] R = null;
    private int S = 0;
    private String[] T = null;
    private String U = "";
    private String V = "";
    public int a = 1;
    public String b = "";
    private String c = "";
    private String d;
    private String e;
    private String f;
    private String g;
    private int h;
    private String i;
    private int j;
    private String k;
    private int l;
    private int m;
    private String n;
    private String o = "";
    private String p = "";
    private String q;
    private String r = "";
    private String s = "";
    private String t = "";
    private String u = "";
    private String v;
    private String w;
    private String x;
    private String y;
    private String z;

    public a() {
    }

    public a(byte[] bArr, byte[] bArr2) {
        try {
            this.L = new String(bArr, "UTF-8");
            this.M = new String(bArr2, "UTF-8");
        } catch (Exception unused) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "get msg byte arr error");
        }
    }

    public String a() {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "msgId =" + this.o);
        return this.o;
    }

    public boolean b() {
        try {
            if (TextUtils.isEmpty(this.M)) {
                com.huawei.hms.support.log.a.a("PushSelfShowLog", "token is null");
                return false;
            }
            this.k = this.M;
            if (TextUtils.isEmpty(this.L)) {
                com.huawei.hms.support.log.a.a("PushSelfShowLog", "msg is null");
                return false;
            }
            JSONObject jSONObject = new JSONObject(this.L);
            this.j = jSONObject.getInt(com.alipay.sdk.authjs.a.g);
            int i2 = 1;
            if (this.j != 1) {
                com.huawei.hms.support.log.a.a("PushSelfShowLog", "not a selefShowMsg");
                return false;
            }
            a(jSONObject);
            JSONObject jSONObject2 = jSONObject.getJSONObject("msgContent");
            if (jSONObject2 == null) {
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "msgObj == null");
                return false;
            } else if (!b(jSONObject2)) {
                return false;
            } else {
                if (jSONObject2.has("dispPkgName")) {
                    this.p = jSONObject2.getString("dispPkgName");
                }
                this.m = jSONObject2.has("rtn") ? jSONObject2.getInt("rtn") : 1;
                if (jSONObject2.has("fm")) {
                    i2 = jSONObject2.getInt("fm");
                }
                this.l = i2;
                c(jSONObject2);
                if (jSONObject2.has("extras")) {
                    this.q = jSONObject2.getJSONArray("extras").toString();
                }
                return d(jSONObject2);
            }
        } catch (JSONException unused) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "parse message exception.");
            return false;
        } catch (Exception e2) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", e2.toString());
            return false;
        }
    }

    private void a(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("group")) {
            this.c = jSONObject.getString("group");
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "NOTIFY_GROUP:" + this.c);
        }
        if (jSONObject.has("badgeClass")) {
            this.b = jSONObject.getString("badgeClass");
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "BADGE_CLASS:" + this.b);
        }
        if (jSONObject.has("badgeAddNum")) {
            this.a = jSONObject.getInt("badgeAddNum");
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "BADGE_ADD_NUM:" + this.a);
        }
    }

    private boolean b(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("msgId")) {
            Object obj = jSONObject.get("msgId");
            if (obj instanceof String) {
                this.o = (String) obj;
                return true;
            } else if (!(obj instanceof Integer)) {
                return true;
            } else {
                this.o = String.valueOf(((Integer) obj).intValue());
                return true;
            }
        } else {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "msgId == null");
            return false;
        }
    }

    private void c(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has(DXBindingXConstant.AP)) {
            String string = jSONObject.getString(DXBindingXConstant.AP);
            StringBuilder sb = new StringBuilder();
            if (TextUtils.isEmpty(string) || string.length() >= 48) {
                this.n = string.substring(0, 48);
                return;
            }
            int length = 48 - string.length();
            for (int i2 = 0; i2 < length; i2++) {
                sb.append("0");
            }
            sb.append(string);
            this.n = sb.toString();
        }
    }

    private boolean d(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2;
        if (!jSONObject.has("psContent") || (jSONObject2 = jSONObject.getJSONObject("psContent")) == null) {
            return false;
        }
        this.r = jSONObject2.getString(BaseDO.JSON_CMD);
        if (jSONObject2.has("content")) {
            this.s = jSONObject2.getString("content");
        } else {
            this.s = "";
        }
        if (jSONObject2.has("notifyIcon")) {
            this.t = jSONObject2.getString("notifyIcon");
        } else {
            this.t = "" + this.o;
        }
        if (jSONObject2.has("statusIcon")) {
            this.v = jSONObject2.getString("statusIcon");
        }
        if (jSONObject2.has("notifyTitle")) {
            this.u = jSONObject2.getString("notifyTitle");
        }
        if (jSONObject2.has("notifyParam")) {
            j(jSONObject2);
        }
        if (jSONObject2.has("param")) {
            return e(jSONObject2);
        }
        return false;
    }

    private boolean e(JSONObject jSONObject) {
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("param");
            if (jSONObject2.has("autoClear")) {
                this.h = jSONObject2.getInt("autoClear");
            } else {
                this.h = 0;
            }
            if (!LogContent.LOG_VALUE_SOURCE_DEFAULT.equals(this.r)) {
                if (!"cosa".equals(this.r)) {
                    if ("email".equals(this.r)) {
                        g(jSONObject2);
                        return true;
                    } else if ("phone".equals(this.r)) {
                        if (jSONObject2.has("phoneNum")) {
                            this.y = jSONObject2.getString("phoneNum");
                            return true;
                        }
                        com.huawei.hms.support.log.a.a("PushSelfShowLog", "phoneNum is null");
                        return false;
                    } else if ("url".equals(this.r)) {
                        h(jSONObject2);
                        return true;
                    } else if (!"rp".equals(this.r)) {
                        return true;
                    } else {
                        i(jSONObject2);
                        return true;
                    }
                }
            }
            f(jSONObject2);
            return true;
        } catch (Exception e2) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "ParseParam error ", (Throwable) e2);
            return false;
        }
    }

    private boolean f(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (jSONObject.has("acn")) {
            this.D = jSONObject.getString("acn");
            this.i = this.D;
        }
        if (jSONObject.has("intentUri")) {
            this.i = jSONObject.getString("intentUri");
        }
        if (jSONObject.has("appPackageName")) {
            this.C = jSONObject.getString("appPackageName");
            return true;
        }
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "appPackageName is null");
        return false;
    }

    private boolean g(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (!jSONObject.has("emailAddr") || !jSONObject.has("emailSubject")) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "emailAddr or emailSubject is null");
            return false;
        }
        this.z = jSONObject.getString("emailAddr");
        this.A = jSONObject.getString("emailSubject");
        if (!jSONObject.has("emailContent")) {
            return true;
        }
        this.B = jSONObject.getString("emailContent");
        return true;
    }

    private boolean h(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (jSONObject.has("url")) {
            this.E = jSONObject.getString("url");
            if (jSONObject.has("inBrowser")) {
                this.I = jSONObject.getInt("inBrowser");
            }
            if (jSONObject.has("needUserId")) {
                this.J = jSONObject.getInt("needUserId");
            }
            if (jSONObject.has("sign")) {
                this.K = jSONObject.getString("sign");
            }
            if (!jSONObject.has("rpt") || !jSONObject.has("rpl")) {
                return true;
            }
            this.F = jSONObject.getString("rpl");
            this.G = jSONObject.getString("rpt");
            if (!jSONObject.has("rpct")) {
                return true;
            }
            this.H = jSONObject.getString("rpct");
            return true;
        }
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "url is null");
        return false;
    }

    private boolean i(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        if (!jSONObject.has("rpt") || !jSONObject.has("rpl")) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "rpl or rpt is null");
            return false;
        }
        this.F = jSONObject.getString("rpl");
        this.G = jSONObject.getString("rpt");
        if (jSONObject.has("rpct")) {
            this.H = jSONObject.getString("rpct");
        }
        if (!jSONObject.has("needUserId")) {
            return true;
        }
        this.J = jSONObject.getInt("needUserId");
        return true;
    }

    public byte[] c() {
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = new JSONObject();
            JSONObject jSONObject4 = new JSONObject();
            jSONObject4.put("autoClear", this.h);
            jSONObject4.put("s", this.d);
            jSONObject4.put(UploadQueueMgr.MSGTYPE_REALTIME, this.e);
            jSONObject4.put("smsC", this.f);
            jSONObject4.put("mmsUrl", this.g);
            jSONObject4.put("url", this.E);
            jSONObject4.put("inBrowser", this.I);
            jSONObject4.put("needUserId", this.J);
            jSONObject4.put("sign", this.K);
            jSONObject4.put("rpl", this.F);
            jSONObject4.put("rpt", this.G);
            jSONObject4.put("rpct", this.H);
            jSONObject4.put("appPackageName", this.C);
            jSONObject4.put("acn", this.D);
            jSONObject4.put("intentUri", this.i);
            jSONObject4.put("emailAddr", this.z);
            jSONObject4.put("emailSubject", this.A);
            jSONObject4.put("emailContent", this.B);
            jSONObject4.put("phoneNum", this.y);
            jSONObject4.put("replyToSms", this.x);
            jSONObject4.put("smsNum", this.w);
            jSONObject3.put(BaseDO.JSON_CMD, this.r);
            jSONObject3.put("content", this.s);
            jSONObject3.put("notifyIcon", this.t);
            jSONObject3.put("notifyTitle", this.u);
            jSONObject3.put("statusIcon", this.v);
            jSONObject3.put("param", jSONObject4);
            jSONObject2.put("dispPkgName", this.p);
            jSONObject2.put("msgId", this.o);
            jSONObject2.put("fm", this.l);
            jSONObject2.put(DXBindingXConstant.AP, this.n);
            jSONObject2.put("rtn", this.m);
            jSONObject2.put("psContent", jSONObject3);
            if (this.q != null && this.q.length() > 0) {
                jSONObject2.put("extras", new JSONArray(this.q));
            }
            jSONObject.put(com.alipay.sdk.authjs.a.g, this.j);
            jSONObject.put("msgContent", jSONObject2);
            jSONObject.put("group", this.c);
            jSONObject.put("badgeClass", this.b);
            jSONObject.put("badgeAddNum", this.a);
            return jSONObject.toString().getBytes("UTF-8");
        } catch (JSONException unused) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "getMsgData failed JSONException");
            return new byte[0];
        } catch (UnsupportedEncodingException unused2) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "getMsgData failed UnsupportedEncodingException");
            return new byte[0];
        }
    }

    public byte[] d() {
        try {
            if (this.k != null && this.k.length() > 0) {
                return this.k.getBytes("UTF-8");
            }
        } catch (Exception unused) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", "getToken getByte failed ");
        }
        return new byte[0];
    }

    private boolean j(JSONObject jSONObject) {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter parseNotifyParam");
        try {
            JSONObject jSONObject2 = jSONObject.getJSONObject("notifyParam");
            if (!jSONObject2.has(RichTextNode.STYLE)) {
                return false;
            }
            l(jSONObject2);
            k(jSONObject2);
            return true;
        } catch (JSONException e2) {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", e2.toString());
            return false;
        }
    }

    private void k(JSONObject jSONObject) throws JSONException {
        this.N = jSONObject.getInt(RichTextNode.STYLE);
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "style:" + this.N);
        com.huawei.hms.support.api.push.a.c.a aVar = com.huawei.hms.support.api.push.a.c.a.STYLE_1;
        if (this.N >= 0 && this.N < com.huawei.hms.support.api.push.a.c.a.values().length) {
            aVar = com.huawei.hms.support.api.push.a.c.a.values()[this.N];
        }
        switch (b.a[aVar.ordinal()]) {
            case 1:
                if (jSONObject.has("iconCount")) {
                    this.S = jSONObject.getInt("iconCount");
                }
                if (this.S > 0) {
                    if (this.S > 6) {
                        this.S = 6;
                    }
                    com.huawei.hms.support.log.a.a("PushSelfShowLog", "iconCount:" + this.S);
                    this.T = new String[this.S];
                    int i2 = 0;
                    while (i2 < this.S) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("icon");
                        int i3 = i2 + 1;
                        sb.append(i3);
                        String sb2 = sb.toString();
                        if (jSONObject.has(sb2)) {
                            this.T[i2] = jSONObject.getString(sb2);
                        }
                        i2 = i3;
                    }
                    return;
                }
                return;
            case 2:
                if (jSONObject.has("subTitle")) {
                    this.U = jSONObject.getString("subTitle");
                    com.huawei.hms.support.log.a.a("PushSelfShowLog", "subTitle:" + this.U);
                    return;
                }
                return;
            case 3:
            case 4:
                if (jSONObject.has("bigPic")) {
                    this.V = jSONObject.getString("bigPic");
                    com.huawei.hms.support.log.a.a("PushSelfShowLog", "bigPicUrl:" + this.V);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void l(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("btnCount")) {
            this.O = jSONObject.getInt("btnCount");
        }
        if (this.O > 0) {
            if (this.O > 3) {
                this.O = 3;
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "btnCount:" + this.O);
            this.P = new String[this.O];
            this.Q = new String[this.O];
            this.R = new String[this.O];
            int i2 = 0;
            while (i2 < this.O) {
                StringBuilder sb = new StringBuilder();
                sb.append("btn");
                int i3 = i2 + 1;
                sb.append(i3);
                sb.append("Text");
                String sb2 = sb.toString();
                String str = "btn" + i3 + "Image";
                String str2 = "btn" + i3 + DXMonitorConstant.DX_MONITOR_EVENT;
                if (jSONObject.has(sb2)) {
                    this.P[i2] = jSONObject.getString(sb2);
                }
                if (jSONObject.has(str)) {
                    this.Q[i2] = jSONObject.getString(str);
                }
                if (jSONObject.has(str2)) {
                    this.R[i2] = jSONObject.getString(str2);
                }
                i2 = i3;
            }
        }
    }

    public String e() {
        return this.c;
    }

    public int f() {
        return this.h;
    }

    public String g() {
        return this.i;
    }

    public String h() {
        return this.n;
    }

    public String i() {
        return this.p;
    }

    public String j() {
        return this.q;
    }

    public String k() {
        return this.r;
    }

    public String l() {
        return this.s;
    }

    public String m() {
        return this.t;
    }

    public String n() {
        return this.u;
    }

    public String o() {
        return this.y;
    }

    public String p() {
        return this.z;
    }

    public String q() {
        return this.A;
    }

    public String r() {
        return this.B;
    }

    public String s() {
        return this.C;
    }

    public String t() {
        return this.D;
    }

    public int u() {
        return this.N;
    }

    public String[] v() {
        return this.P;
    }

    public String[] w() {
        return this.R;
    }

    public String x() {
        return this.U;
    }
}
