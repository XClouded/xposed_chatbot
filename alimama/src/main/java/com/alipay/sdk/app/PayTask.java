package com.alipay.sdk.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import com.alipay.sdk.app.PayResultActivity;
import com.alipay.sdk.data.a;
import com.alipay.sdk.data.c;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.H5PayResultModel;
import com.alipay.sdk.util.e;
import com.alipay.sdk.util.i;
import com.alipay.sdk.util.l;
import com.alipay.sdk.util.n;
import com.huawei.hms.support.api.entity.core.JosStatusCodes;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONObject;

public class PayTask {
    static final Object a = e.class;
    private static long h = 0;
    private static final long i = 3000;
    private static long j = -1;
    private Activity b;
    private com.alipay.sdk.widget.a c;
    private String d = "wappaygw.alipay.com/service/rest.htm";
    private String e = "mclient.alipay.com/service/rest.htm";
    private String f = "mclient.alipay.com/home/exterfaceAssign.htm";
    private Map<String, a> g = new HashMap();

    public String getVersion() {
        return "15.6.2";
    }

    public PayTask(Activity activity) {
        this.b = activity;
        b.a().a(this.b, c.b());
        com.alipay.sdk.app.statistic.a.a(activity);
        this.c = new com.alipay.sdk.widget.a(activity, com.alipay.sdk.widget.a.b);
    }

    public synchronized String pay(String str, boolean z) {
        String str2;
        if (b()) {
            return j.d();
        }
        if (z) {
            showLoading();
        }
        if (str.contains("payment_inst=")) {
            String substring = str.substring(str.indexOf("payment_inst=") + 13);
            int indexOf = substring.indexOf(38);
            if (indexOf > 0) {
                substring = substring.substring(0, indexOf);
            }
            i.a(substring.replaceAll("\"", "").toLowerCase(Locale.getDefault()).replaceAll("alipay", ""));
        } else {
            i.a("");
        }
        if (str.contains(com.alipay.sdk.cons.a.r)) {
            com.alipay.sdk.cons.a.s = true;
        }
        if (com.alipay.sdk.cons.a.s) {
            if (str.startsWith(com.alipay.sdk.cons.a.t)) {
                str = str.substring(str.indexOf(com.alipay.sdk.cons.a.t) + com.alipay.sdk.cons.a.t.length());
            } else if (str.startsWith(com.alipay.sdk.cons.a.u)) {
                str = str.substring(str.indexOf(com.alipay.sdk.cons.a.u) + com.alipay.sdk.cons.a.u.length());
            }
        }
        try {
            str2 = a(str);
            i.a(this.b.getApplicationContext(), str2);
            com.alipay.sdk.data.a.g().a(this.b.getApplicationContext());
            dismissLoading();
            com.alipay.sdk.app.statistic.a.b(this.b.getApplicationContext(), str);
        } catch (Throwable th) {
            try {
                String c2 = j.c();
                com.alipay.sdk.util.c.a(th);
                str2 = c2;
                return str2;
            } finally {
                com.alipay.sdk.data.a.g().a(this.b.getApplicationContext());
                dismissLoading();
                com.alipay.sdk.app.statistic.a.b(this.b.getApplicationContext(), str);
            }
        }
    }

    public synchronized Map<String, String> payV2(String str, boolean z) {
        return l.a(pay(str, z));
    }

    public synchronized String fetchTradeToken() {
        return i.a(this.b.getApplicationContext());
    }

    public synchronized boolean payInterceptorWithUrl(String str, boolean z, H5PayCallback h5PayCallback) {
        String fetchOrderInfoFromH5PayUrl;
        fetchOrderInfoFromH5PayUrl = fetchOrderInfoFromH5PayUrl(str);
        if (!TextUtils.isEmpty(fetchOrderInfoFromH5PayUrl)) {
            new Thread(new g(this, fetchOrderInfoFromH5PayUrl, z, h5PayCallback)).start();
        }
        return !TextUtils.isEmpty(fetchOrderInfoFromH5PayUrl);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x00dc, code lost:
        if (r9.startsWith("http://" + r8.e) != false) goto L_0x00de;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0179, code lost:
        if (r9.startsWith("http://" + r8.f) != false) goto L_0x017b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x003f, code lost:
        if (r9.startsWith("http://" + r8.d) != false) goto L_0x0041;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String fetchOrderInfoFromH5PayUrl(java.lang.String r20) {
        /*
            r19 = this;
            r8 = r19
            r1 = r20
            monitor-enter(r19)
            boolean r2 = android.text.TextUtils.isEmpty(r20)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x04d2
            java.lang.String r9 = r20.trim()     // Catch:{ Throwable -> 0x04cd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "https://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.d     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x0041
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "http://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.d     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x00ac
        L_0x0041:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "(http|https)://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.d     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "\\?"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = ""
            java.lang.String r2 = r9.replaceFirst(r2, r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.trim()     // Catch:{ Throwable -> 0x04cd }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r3 != 0) goto L_0x00ac
            java.util.Map r1 = com.alipay.sdk.util.n.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "req_data"
            java.lang.Object r1 = r1.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "<request_token>"
            java.lang.String r3 = "</request_token>"
            java.lang.String r1 = com.alipay.sdk.util.n.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "_input_charset=\"utf-8\"&ordertoken=\""
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "\"&pay_channel_id=\"alipay_sdk\"&bizcontext=\""
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            com.alipay.sdk.sys.a r1 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x04cd }
            android.app.Activity r3 = r8.b     // Catch:{ Throwable -> 0x04cd }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "sc"
            java.lang.String r4 = "h5tonative"
            java.lang.String r1 = r1.a(r3, r4)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "\""
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            monitor-exit(r19)
            return r1
        L_0x00ac:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "https://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.e     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x00de
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "http://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.e     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x0149
        L_0x00de:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "(http|https)://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.e     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "\\?"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = ""
            java.lang.String r2 = r9.replaceFirst(r2, r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.trim()     // Catch:{ Throwable -> 0x04cd }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r3 != 0) goto L_0x0149
            java.util.Map r1 = com.alipay.sdk.util.n.b((java.lang.String) r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "req_data"
            java.lang.Object r1 = r1.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "<request_token>"
            java.lang.String r3 = "</request_token>"
            java.lang.String r1 = com.alipay.sdk.util.n.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "_input_charset=\"utf-8\"&ordertoken=\""
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "\"&pay_channel_id=\"alipay_sdk\"&bizcontext=\""
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            com.alipay.sdk.sys.a r1 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x04cd }
            android.app.Activity r3 = r8.b     // Catch:{ Throwable -> 0x04cd }
            r1.<init>(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "sc"
            java.lang.String r4 = "h5tonative"
            java.lang.String r1 = r1.a(r3, r4)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "\""
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            monitor-exit(r19)
            return r1
        L_0x0149:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "https://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.f     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x017b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "http://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.f     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r9.startsWith(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x01ed
        L_0x017b:
            java.lang.String r2 = "alipay.wap.create.direct.pay.by.user"
            boolean r2 = r9.contains(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x018b
            java.lang.String r2 = "create_forex_trade_wap"
            boolean r2 = r9.contains(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x01ed
        L_0x018b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "(http|https)://"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r8.f     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "\\?"
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = ""
            java.lang.String r2 = r9.replaceFirst(r2, r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.trim()     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x01ed
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01e8 }
            r2.<init>()     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r3 = "url"
            r2.put(r3, r1)     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r3 = "bizcontext"
            com.alipay.sdk.sys.a r4 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x01e8 }
            android.app.Activity r5 = r8.b     // Catch:{ Throwable -> 0x01e8 }
            r4.<init>(r5)     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r5 = "sc"
            java.lang.String r6 = "h5tonative"
            java.lang.String r4 = r4.a(r5, r6)     // Catch:{ Throwable -> 0x01e8 }
            r2.put(r3, r4)     // Catch:{ Throwable -> 0x01e8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e8 }
            r3.<init>()     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r4 = "new_external_info=="
            r3.append(r4)     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x01e8 }
            r3.append(r2)     // Catch:{ Throwable -> 0x01e8 }
            java.lang.String r2 = r3.toString()     // Catch:{ Throwable -> 0x01e8 }
            monitor-exit(r19)
            return r2
        L_0x01e8:
            r0 = move-exception
            r2 = r0
            com.alipay.sdk.util.c.a(r2)     // Catch:{ Throwable -> 0x04cd }
        L_0x01ed:
            java.lang.String r2 = "^(http|https)://(maliprod\\.alipay\\.com/w/trade_pay\\.do.?|mali\\.alipay\\.com/w/trade_pay\\.do.?|mclient\\.alipay\\.com/w/trade_pay\\.do.?)"
            java.util.regex.Pattern r2 = java.util.regex.Pattern.compile(r2)     // Catch:{ Throwable -> 0x04cd }
            java.util.regex.Matcher r2 = r2.matcher(r1)     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = r2.find()     // Catch:{ Throwable -> 0x04cd }
            r10 = 0
            r11 = 4
            r12 = 3
            r13 = 2
            r14 = 1
            r15 = 0
            if (r2 == 0) goto L_0x0354
            java.lang.String r2 = "?"
            java.lang.String r3 = ""
            java.lang.String r1 = com.alipay.sdk.util.n.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r1)     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x0354
            java.util.Map r7 = com.alipay.sdk.util.n.b((java.lang.String) r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r6.<init>()     // Catch:{ Throwable -> 0x04cd }
            r2 = 0
            r3 = 1
            java.lang.String r4 = "trade_no"
            java.lang.String[] r5 = new java.lang.String[r13]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "trade_no"
            r5[r15] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "alipay_trade_no"
            r5[r14] = r1     // Catch:{ Throwable -> 0x04cd }
            r1 = r19
            r16 = r5
            r5 = r6
            r17 = r6
            r6 = r7
            r18 = r7
            r7 = r16
            boolean r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x04cd }
            if (r1 == 0) goto L_0x0354
            r2 = 1
            r3 = 0
            java.lang.String r4 = "pay_phase_id"
            java.lang.String[] r7 = new java.lang.String[r12]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "payPhaseId"
            r7[r15] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "pay_phase_id"
            r7[r14] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "out_relation_id"
            r7[r13] = r1     // Catch:{ Throwable -> 0x04cd }
            r1 = r19
            r5 = r17
            r6 = r18
            r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "&biz_sub_type=\"TRADE\""
            r9 = r17
            r9.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "&biz_type=\"trade\""
            r9.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "app_name"
            r7 = r18
            java.lang.Object r1 = r7.get(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x0282
            java.lang.String r2 = "cid"
            java.lang.Object r2 = r7.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x0282
            java.lang.String r1 = "ali1688"
            goto L_0x02a6
        L_0x0282:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x02a6
            java.lang.String r2 = "sid"
            java.lang.Object r2 = r7.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 == 0) goto L_0x02a4
            java.lang.String r2 = "s_id"
            java.lang.Object r2 = r7.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.CharSequence r2 = (java.lang.CharSequence) r2     // Catch:{ Throwable -> 0x04cd }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x04cd }
            if (r2 != 0) goto L_0x02a6
        L_0x02a4:
            java.lang.String r1 = "tb"
        L_0x02a6:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "&app_name=\""
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "\""
            r2.append(r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            r9.append(r1)     // Catch:{ Throwable -> 0x04cd }
            r2 = 1
            r3 = 1
            java.lang.String r4 = "extern_token"
            java.lang.String[] r11 = new java.lang.String[r11]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "extern_token"
            r11[r15] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "cid"
            r11[r14] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "sid"
            r11[r13] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "s_id"
            r11[r12] = r1     // Catch:{ Throwable -> 0x04cd }
            r1 = r19
            r5 = r9
            r6 = r7
            r12 = r7
            r7 = r11
            boolean r1 = r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x04cd }
            if (r1 != 0) goto L_0x02e5
            java.lang.String r1 = ""
            monitor-exit(r19)
            return r1
        L_0x02e5:
            r2 = 1
            r3 = 0
            java.lang.String r4 = "appenv"
            java.lang.String[] r7 = new java.lang.String[r14]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "appenv"
            r7[r15] = r1     // Catch:{ Throwable -> 0x04cd }
            r1 = r19
            r5 = r9
            r6 = r12
            r1.a(r2, r3, r4, r5, r6, r7)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "&pay_channel_id=\"alipay_sdk\""
            r9.append(r1)     // Catch:{ Throwable -> 0x04cd }
            com.alipay.sdk.app.PayTask$a r1 = new com.alipay.sdk.app.PayTask$a     // Catch:{ Throwable -> 0x04cd }
            r1.<init>(r8, r10)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "return_url"
            java.lang.Object r2 = r12.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x04cd }
            r1.a(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "show_url"
            java.lang.Object r2 = r12.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x04cd }
            r1.c(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "pay_order_id"
            java.lang.Object r2 = r12.get(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Throwable -> 0x04cd }
            r1.b(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = r9.toString()     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "&bizcontext=\""
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            com.alipay.sdk.sys.a r3 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x04cd }
            android.app.Activity r4 = r8.b     // Catch:{ Throwable -> 0x04cd }
            r3.<init>(r4)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r4 = "sc"
            java.lang.String r5 = "h5tonative"
            java.lang.String r3 = r3.a(r4, r5)     // Catch:{ Throwable -> 0x04cd }
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "\""
            r2.append(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            java.util.Map<java.lang.String, com.alipay.sdk.app.PayTask$a> r3 = r8.g     // Catch:{ Throwable -> 0x04cd }
            r3.put(r2, r1)     // Catch:{ Throwable -> 0x04cd }
            monitor-exit(r19)
            return r2
        L_0x0354:
            java.lang.String r1 = "mclient.alipay.com/cashier/mobilepay.htm"
            boolean r1 = r9.contains(r1)     // Catch:{ Throwable -> 0x04cd }
            if (r1 != 0) goto L_0x049c
            boolean r1 = com.alipay.sdk.app.EnvUtils.isSandBox()     // Catch:{ Throwable -> 0x04cd }
            if (r1 == 0) goto L_0x036c
            java.lang.String r1 = "mobileclientgw.alipaydev.com/cashier/mobilepay.htm"
            boolean r1 = r9.contains(r1)     // Catch:{ Throwable -> 0x04cd }
            if (r1 == 0) goto L_0x036c
            goto L_0x049c
        L_0x036c:
            com.alipay.sdk.data.a r1 = com.alipay.sdk.data.a.g()     // Catch:{ Throwable -> 0x04cd }
            boolean r1 = r1.c()     // Catch:{ Throwable -> 0x04cd }
            if (r1 == 0) goto L_0x04d2
            java.lang.String r1 = "^https?://(maliprod\\.alipay\\.com|mali\\.alipay\\.com)/batch_payment\\.do\\?"
            java.util.regex.Pattern r1 = java.util.regex.Pattern.compile(r1)     // Catch:{ Throwable -> 0x04cd }
            java.util.regex.Matcher r1 = r1.matcher(r9)     // Catch:{ Throwable -> 0x04cd }
            boolean r1 = r1.find()     // Catch:{ Throwable -> 0x04cd }
            if (r1 == 0) goto L_0x04d2
            android.net.Uri r1 = android.net.Uri.parse(r9)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "return_url"
            java.lang.String r2 = r1.getQueryParameter(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "show_url"
            java.lang.String r3 = r1.getQueryParameter(r3)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r4 = "pay_order_id"
            java.lang.String r4 = r1.getQueryParameter(r4)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String[] r5 = new java.lang.String[r13]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r6 = "trade_nos"
            java.lang.String r6 = r1.getQueryParameter(r6)     // Catch:{ Throwable -> 0x04cd }
            r5[r15] = r6     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r6 = "alipay_trade_no"
            java.lang.String r6 = r1.getQueryParameter(r6)     // Catch:{ Throwable -> 0x04cd }
            r5[r14] = r6     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r5 = a((java.lang.String[]) r5)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String[] r6 = new java.lang.String[r12]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r7 = "payPhaseId"
            java.lang.String r7 = r1.getQueryParameter(r7)     // Catch:{ Throwable -> 0x04cd }
            r6[r15] = r7     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r7 = "pay_phase_id"
            java.lang.String r7 = r1.getQueryParameter(r7)     // Catch:{ Throwable -> 0x04cd }
            r6[r14] = r7     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r7 = "out_relation_id"
            java.lang.String r7 = r1.getQueryParameter(r7)     // Catch:{ Throwable -> 0x04cd }
            r6[r13] = r7     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r6 = a((java.lang.String[]) r6)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String[] r7 = new java.lang.String[r11]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r9 = "app_name"
            java.lang.String r9 = r1.getQueryParameter(r9)     // Catch:{ Throwable -> 0x04cd }
            r7[r15] = r9     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r9 = "cid"
            java.lang.String r9 = r1.getQueryParameter(r9)     // Catch:{ Throwable -> 0x04cd }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x04cd }
            if (r9 != 0) goto L_0x03e9
            java.lang.String r9 = "ali1688"
            goto L_0x03eb
        L_0x03e9:
            java.lang.String r9 = ""
        L_0x03eb:
            r7[r14] = r9     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r9 = "sid"
            java.lang.String r9 = r1.getQueryParameter(r9)     // Catch:{ Throwable -> 0x04cd }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x04cd }
            if (r9 != 0) goto L_0x03fc
            java.lang.String r9 = "tb"
            goto L_0x03fe
        L_0x03fc:
            java.lang.String r9 = ""
        L_0x03fe:
            r7[r13] = r9     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r9 = "s_id"
            java.lang.String r9 = r1.getQueryParameter(r9)     // Catch:{ Throwable -> 0x04cd }
            boolean r9 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x04cd }
            if (r9 != 0) goto L_0x040f
            java.lang.String r9 = "tb"
            goto L_0x0411
        L_0x040f:
            java.lang.String r9 = ""
        L_0x0411:
            r7[r12] = r9     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r7 = a((java.lang.String[]) r7)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String[] r9 = new java.lang.String[r11]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r10 = "extern_token"
            java.lang.String r10 = r1.getQueryParameter(r10)     // Catch:{ Throwable -> 0x04cd }
            r9[r15] = r10     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r10 = "cid"
            java.lang.String r10 = r1.getQueryParameter(r10)     // Catch:{ Throwable -> 0x04cd }
            r9[r14] = r10     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r10 = "sid"
            java.lang.String r10 = r1.getQueryParameter(r10)     // Catch:{ Throwable -> 0x04cd }
            r9[r13] = r10     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r10 = "s_id"
            java.lang.String r10 = r1.getQueryParameter(r10)     // Catch:{ Throwable -> 0x04cd }
            r9[r12] = r10     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r9 = a((java.lang.String[]) r9)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String[] r10 = new java.lang.String[r14]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r11 = "appenv"
            java.lang.String r1 = r1.getQueryParameter(r11)     // Catch:{ Throwable -> 0x04cd }
            r10[r15] = r1     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = a((java.lang.String[]) r10)     // Catch:{ Throwable -> 0x04cd }
            boolean r10 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Throwable -> 0x04cd }
            if (r10 != 0) goto L_0x04d2
            boolean r10 = android.text.TextUtils.isEmpty(r7)     // Catch:{ Throwable -> 0x04cd }
            if (r10 != 0) goto L_0x04d2
            boolean r10 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x04cd }
            if (r10 != 0) goto L_0x04d2
            java.lang.String r10 = "trade_no=\"%s\"&pay_phase_id=\"%s\"&biz_type=\"trade\"&biz_sub_type=\"TRADE\"&app_name=\"%s\"&extern_token=\"%s\"&appenv=\"%s\"&pay_channel_id=\"alipay_sdk\"&bizcontext=\"%s\""
            r11 = 6
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Throwable -> 0x04cd }
            r11[r15] = r5     // Catch:{ Throwable -> 0x04cd }
            r11[r14] = r6     // Catch:{ Throwable -> 0x04cd }
            r11[r13] = r7     // Catch:{ Throwable -> 0x04cd }
            r11[r12] = r9     // Catch:{ Throwable -> 0x04cd }
            r6 = 4
            r11[r6] = r1     // Catch:{ Throwable -> 0x04cd }
            r1 = 5
            com.alipay.sdk.sys.a r6 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x04cd }
            android.app.Activity r7 = r8.b     // Catch:{ Throwable -> 0x04cd }
            r6.<init>(r7)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r7 = "sc"
            java.lang.String r9 = "h5tonative"
            java.lang.String r6 = r6.a(r7, r9)     // Catch:{ Throwable -> 0x04cd }
            r11[r1] = r6     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = java.lang.String.format(r10, r11)     // Catch:{ Throwable -> 0x04cd }
            com.alipay.sdk.app.PayTask$a r6 = new com.alipay.sdk.app.PayTask$a     // Catch:{ Throwable -> 0x04cd }
            r7 = 0
            r6.<init>(r8, r7)     // Catch:{ Throwable -> 0x04cd }
            r6.a(r2)     // Catch:{ Throwable -> 0x04cd }
            r6.c(r3)     // Catch:{ Throwable -> 0x04cd }
            r6.b(r4)     // Catch:{ Throwable -> 0x04cd }
            r6.d(r5)     // Catch:{ Throwable -> 0x04cd }
            java.util.Map<java.lang.String, com.alipay.sdk.app.PayTask$a> r2 = r8.g     // Catch:{ Throwable -> 0x04cd }
            r2.put(r1, r6)     // Catch:{ Throwable -> 0x04cd }
            monitor-exit(r19)
            return r1
        L_0x049c:
            com.alipay.sdk.sys.a r1 = new com.alipay.sdk.sys.a     // Catch:{ Throwable -> 0x04cd }
            android.app.Activity r2 = r8.b     // Catch:{ Throwable -> 0x04cd }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = "sc"
            java.lang.String r3 = "h5tonative"
            java.lang.String r1 = r1.a(r2, r3)     // Catch:{ Throwable -> 0x04cd }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Throwable -> 0x04cd }
            r2.<init>()     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "url"
            r2.put(r3, r9)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r3 = "bizcontext"
            r2.put(r3, r1)     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = "new_external_info==%s"
            java.lang.Object[] r3 = new java.lang.Object[r14]     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x04cd }
            r3[r15] = r2     // Catch:{ Throwable -> 0x04cd }
            java.lang.String r1 = java.lang.String.format(r1, r3)     // Catch:{ Throwable -> 0x04cd }
            monitor-exit(r19)
            return r1
        L_0x04ca:
            r0 = move-exception
            r1 = r0
            goto L_0x04d6
        L_0x04cd:
            r0 = move-exception
            r1 = r0
            com.alipay.sdk.util.c.a(r1)     // Catch:{ all -> 0x04ca }
        L_0x04d2:
            java.lang.String r1 = ""
            monitor-exit(r19)
            return r1
        L_0x04d6:
            monitor-exit(r19)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.PayTask.fetchOrderInfoFromH5PayUrl(java.lang.String):java.lang.String");
    }

    private static final String a(String... strArr) {
        if (strArr == null) {
            return "";
        }
        for (String str : strArr) {
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return "";
    }

    public static synchronized boolean fetchSdkConfig(Context context) {
        synchronized (PayTask.class) {
            try {
                b.a().a(context, c.b());
                long elapsedRealtime = SystemClock.elapsedRealtime() / 1000;
                if (elapsedRealtime - h < ((long) com.alipay.sdk.data.a.g().e())) {
                    return false;
                }
                h = elapsedRealtime;
                com.alipay.sdk.data.a.g().a(context.getApplicationContext());
                return true;
            } catch (Exception e2) {
                com.alipay.sdk.util.c.a(e2);
                return false;
            }
        }
    }

    private class a {
        private String b;
        private String c;
        private String d;
        private String e;

        private a() {
            this.b = "";
            this.c = "";
            this.d = "";
            this.e = "";
        }

        /* synthetic */ a(PayTask payTask, g gVar) {
            this();
        }

        public String a() {
            return this.b;
        }

        public void a(String str) {
            this.b = str;
        }

        public String b() {
            return this.d;
        }

        public void b(String str) {
            this.d = str;
        }

        public String c() {
            return this.c;
        }

        public void c(String str) {
            this.c = str;
        }

        public String d() {
            return this.e;
        }

        public void d(String str) {
            this.e = str;
        }
    }

    private boolean a(boolean z, boolean z2, String str, StringBuilder sb, Map<String, String> map, String... strArr) {
        String str2 = "";
        int length = strArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            String str3 = strArr[i2];
            if (!TextUtils.isEmpty(map.get(str3))) {
                str2 = map.get(str3);
                break;
            }
            i2++;
        }
        if (TextUtils.isEmpty(str2)) {
            if (z2) {
                return false;
            }
            return true;
        } else if (z) {
            sb.append("&");
            sb.append(str);
            sb.append("=\"");
            sb.append(str2);
            sb.append("\"");
            return true;
        } else {
            sb.append(str);
            sb.append("=\"");
            sb.append(str2);
            sb.append("\"");
            return true;
        }
    }

    public synchronized H5PayResultModel h5Pay(String str, boolean z) {
        H5PayResultModel h5PayResultModel;
        h5PayResultModel = new H5PayResultModel();
        try {
            String[] split = pay(str, z).split(";");
            HashMap hashMap = new HashMap();
            for (String str2 : split) {
                String substring = str2.substring(0, str2.indexOf("={"));
                hashMap.put(substring, a(str2, substring));
            }
            if (hashMap.containsKey(l.a)) {
                h5PayResultModel.setResultCode((String) hashMap.get(l.a));
            }
            h5PayResultModel.setReturnUrl(a(str, (Map<String, String>) hashMap));
            if (TextUtils.isEmpty(h5PayResultModel.getReturnUrl())) {
                com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.R, "");
            }
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.S, th);
            com.alipay.sdk.util.c.a(th);
        }
        return h5PayResultModel;
    }

    private String a(String str, Map<String, String> map) throws UnsupportedEncodingException {
        boolean equals = "9000".equals(map.get(l.a));
        String str2 = map.get("result");
        a remove = this.g.remove(str);
        String[] strArr = new String[2];
        strArr[0] = remove != null ? remove.b() : "";
        strArr[1] = remove != null ? remove.d() : "";
        a(strArr);
        if (map.containsKey("callBackUrl")) {
            return map.get("callBackUrl");
        }
        if (str2.length() > 15) {
            String a2 = a(n.a("&callBackUrl=\"", "\"", str2), n.a("&call_back_url=\"", "\"", str2), n.a((String) com.alipay.sdk.cons.a.p, "\"", str2), URLDecoder.decode(n.a((String) com.alipay.sdk.cons.a.q, "&", str2), "utf-8"), URLDecoder.decode(n.a("&callBackUrl=", "&", str2), "utf-8"), n.a("call_back_url=\"", "\"", str2));
            if (!TextUtils.isEmpty(a2)) {
                return a2;
            }
        }
        if (remove != null) {
            String a3 = equals ? remove.a() : remove.c();
            if (!TextUtils.isEmpty(a3)) {
                return a3;
            }
        }
        return com.alipay.sdk.data.a.g().d();
    }

    private String a(String str, String str2) {
        String str3 = str2 + "={";
        return str.substring(str.indexOf(str3) + str3.length(), str.lastIndexOf("}"));
    }

    private e.a a() {
        return new h(this);
    }

    public void showLoading() {
        if (this.c != null) {
            this.c.b();
        }
    }

    public void dismissLoading() {
        if (this.c != null) {
            this.c.c();
            this.c = null;
        }
    }

    private String a(String str) {
        String a2 = new com.alipay.sdk.sys.a(this.b).a(str);
        if (a2.contains("paymethod=\"expressGateway\"")) {
            return b(a2);
        }
        List<a.C0001a> f2 = com.alipay.sdk.data.a.g().f();
        if (!com.alipay.sdk.data.a.g().q || f2 == null) {
            f2 = i.a;
        }
        if (n.b((Context) this.b, f2)) {
            e eVar = new e(this.b, a());
            String a3 = eVar.a(a2);
            eVar.a();
            if (TextUtils.equals(a3, e.a) || TextUtils.equals(a3, e.b)) {
                com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.M, "");
                return b(a2);
            } else if (TextUtils.isEmpty(a3)) {
                return j.c();
            } else {
                if (!a3.contains(PayResultActivity.a)) {
                    return a3;
                }
                com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.O, "");
                return a(a2, f2, a3, this.b);
            }
        } else {
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.N, "");
            return b(a2);
        }
    }

    private static String a(String str, List<a.C0001a> list, String str2, Activity activity) {
        n.a a2 = n.a((Context) activity, list);
        if (a2 == null || a2.a() || a2.b() || !TextUtils.equals(a2.a.packageName, PayResultActivity.c)) {
            return str2;
        }
        com.alipay.sdk.util.c.b("msp", "PayTask:payResult: NOT_LOGIN");
        String valueOf = String.valueOf(str.hashCode());
        PayResultActivity.b.put(valueOf, new Object());
        Intent intent = new Intent(activity, PayResultActivity.class);
        intent.putExtra(PayResultActivity.e, str);
        intent.putExtra(PayResultActivity.f, activity.getPackageName());
        intent.putExtra(PayResultActivity.d, valueOf);
        activity.startActivity(intent);
        synchronized (PayResultActivity.b.get(valueOf)) {
            try {
                com.alipay.sdk.util.c.b("msp", "PayTask:payResult: wait");
                PayResultActivity.b.get(valueOf).wait();
            } catch (InterruptedException e2) {
                com.alipay.sdk.util.c.b("msp", "PayTask:payResult: InterruptedException:" + e2);
                return j.c();
            }
        }
        String str3 = PayResultActivity.a.b;
        com.alipay.sdk.util.c.b("msp", "PayTask:payResult: result:" + str3);
        return str3;
    }

    private String b(String str) {
        showLoading();
        k kVar = null;
        try {
            JSONObject c2 = new com.alipay.sdk.packet.impl.e().a(this.b.getApplicationContext(), str).c();
            String optString = c2.optString("end_code", (String) null);
            List<com.alipay.sdk.protocol.b> a2 = com.alipay.sdk.protocol.b.a(c2.optJSONObject(com.alipay.sdk.cons.c.c).optJSONObject(com.alipay.sdk.cons.c.d));
            int i2 = 0;
            for (int i3 = 0; i3 < a2.size(); i3++) {
                if (a2.get(i3).b() == com.alipay.sdk.protocol.a.Update) {
                    com.alipay.sdk.protocol.b.a(a2.get(i3));
                }
            }
            a(c2);
            dismissLoading();
            while (i2 < a2.size()) {
                com.alipay.sdk.protocol.b bVar = a2.get(i2);
                if (bVar.b() == com.alipay.sdk.protocol.a.WapPay) {
                    String a3 = a(bVar);
                    dismissLoading();
                    return a3;
                } else if (bVar.b() == com.alipay.sdk.protocol.a.OpenWeb) {
                    String a4 = a(bVar, optString);
                    dismissLoading();
                    return a4;
                } else {
                    i2++;
                }
            }
        } catch (IOException e2) {
            kVar = k.b(k.NETWORK_ERROR.a());
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.a, (Throwable) e2);
        } catch (Throwable th) {
            dismissLoading();
            throw th;
        }
        dismissLoading();
        if (kVar == null) {
            kVar = k.b(k.FAILED.a());
        }
        return j.a(kVar.a(), kVar.b(), "");
    }

    private void a(JSONObject jSONObject) {
        try {
            String optString = jSONObject.optString("tid");
            String optString2 = jSONObject.optString(com.alipay.sdk.tid.b.e);
            if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2)) {
                com.alipay.sdk.tid.b.a(b.a().b()).a(optString, optString2);
            }
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.F, th);
        }
    }

    private String a(com.alipay.sdk.protocol.b bVar, String str) {
        boolean b2;
        String a2;
        String[] c2 = bVar.c();
        Intent intent = new Intent(this.b, H5PayActivity.class);
        try {
            JSONObject d2 = n.d(new String(com.alipay.sdk.encrypt.a.a(c2[2])));
            intent.putExtra("url", c2[0]);
            intent.putExtra("title", c2[1]);
            intent.putExtra("version", "v2");
            intent.putExtra("method", d2.optString("method", "POST"));
            j.a(false);
            j.a((String) null);
            this.b.startActivity(intent);
            synchronized (a) {
                try {
                    a.wait();
                    b2 = j.b();
                    a2 = j.a();
                    j.a(false);
                    j.a((String) null);
                } catch (InterruptedException e2) {
                    com.alipay.sdk.util.c.a(e2);
                    return j.c();
                }
            }
            String str2 = "";
            if (b2) {
                try {
                    List<com.alipay.sdk.protocol.b> a3 = com.alipay.sdk.protocol.b.a(n.d(new String(com.alipay.sdk.encrypt.a.a(a2))));
                    int i2 = 0;
                    while (true) {
                        if (i2 >= a3.size()) {
                            break;
                        }
                        com.alipay.sdk.protocol.b bVar2 = a3.get(i2);
                        if (bVar2.b() == com.alipay.sdk.protocol.a.SetResult) {
                            String[] c3 = bVar2.c();
                            str2 = j.a(Integer.valueOf(c3[1]).intValue(), c3[0], n.e(c3[2]));
                            break;
                        }
                        i2++;
                    }
                } catch (Throwable th) {
                    com.alipay.sdk.util.c.a(th);
                    com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.s, th, a2);
                }
            }
            if (!TextUtils.isEmpty(str2)) {
                return str2;
            }
            try {
                return j.a(Integer.valueOf(str).intValue(), "", "");
            } catch (Throwable th2) {
                com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.s, th2, "endCode: " + str);
                return j.a(JosStatusCodes.RTN_CODE_COMMON_ERROR, "", "");
            }
        } catch (Throwable th3) {
            com.alipay.sdk.util.c.a(th3);
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.s, th3, Arrays.toString(c2));
            return j.c();
        }
    }

    private String a(com.alipay.sdk.protocol.b bVar) {
        String[] c2 = bVar.c();
        Intent intent = new Intent(this.b, H5PayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", c2[0]);
        if (c2.length == 2) {
            bundle.putString("cookie", c2[1]);
        }
        intent.putExtras(bundle);
        this.b.startActivity(intent);
        synchronized (a) {
            try {
                a.wait();
            } catch (InterruptedException e2) {
                com.alipay.sdk.util.c.a(e2);
                return j.c();
            }
        }
        String a2 = j.a();
        if (TextUtils.isEmpty(a2)) {
            return j.c();
        }
        return a2;
    }

    private static boolean b() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - j < 3000) {
            return true;
        }
        j = elapsedRealtime;
        return false;
    }
}
