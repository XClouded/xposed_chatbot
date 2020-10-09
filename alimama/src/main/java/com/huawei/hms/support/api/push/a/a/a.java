package com.huawei.hms.support.api.push.a.a;

import alimama.com.unwetaologger.base.LogContent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import java.util.ArrayList;

/* compiled from: SelfShowType */
public class a {
    private static final String[] a = {"phone", "url", "email", LogContent.LOG_VALUE_SOURCE_DEFAULT, "cosa", "rp"};
    private Context b;
    private com.huawei.hms.support.api.push.a.b.a c;

    public a(Context context, com.huawei.hms.support.api.push.a.b.a aVar) {
        this.b = context;
        this.c = aVar;
    }

    public static boolean a(String str) {
        for (String equals : a) {
            if (equals.equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void a() {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter launchNotify()");
        if (this.b == null || this.c == null) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "launchNotify  context or msg is null");
        } else if (LogContent.LOG_VALUE_SOURCE_DEFAULT.equals(this.c.k())) {
            d();
        } else if ("cosa".equals(this.c.k())) {
            g();
        } else if ("email".equals(this.c.k())) {
            c();
        } else if ("phone".equals(this.c.k())) {
            b();
        } else if ("rp".equals(this.c.k())) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", this.c.k() + " not support rich message.");
        } else if ("url".equals(this.c.k())) {
            com.huawei.hms.support.log.a.c("PushSelfShowLog", this.c.k() + " not support URL.");
        } else {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", this.c.k() + " is not exist in hShowType");
        }
    }

    private void b() {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter launchCall");
        try {
            Intent intent = new Intent();
            Intent action = intent.setAction("android.intent.action.DIAL");
            action.setData(Uri.parse("tel:" + this.c.o())).setFlags(268435456);
            this.b.startActivity(intent);
        } catch (Exception e) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", e.toString(), (Throwable) e);
        }
    }

    private void c() {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter launchMail");
        try {
            if (this.c.p() != null) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SENDTO").setFlags(268435456).setData(Uri.fromParts("mailto", this.c.p(), (String) null)).putExtra("android.intent.extra.SUBJECT", this.c.q()).putExtra("android.intent.extra.TEXT", this.c.r()).setPackage("com.android.email");
                this.b.startActivity(intent);
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", e.toString(), (Throwable) e);
        }
    }

    private void d() {
        try {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "enter launchApp, appPackageName =" + this.c.s() + ",and msg.intentUri is " + this.c.g());
            if (com.huawei.hms.support.api.push.a.d.a.c(this.b, this.c.s())) {
                g();
                return;
            }
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "enter launch app, appPackageName =" + this.c.s() + ",and msg.intentUri is " + this.c.g());
            e();
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "launchApp error:" + e.toString());
        }
    }

    private String b(String str) {
        String str2;
        try {
            int indexOf = str.indexOf(63);
            if (indexOf == -1) {
                return str;
            }
            int i = indexOf + 1;
            String[] split = str.substring(i).split("&");
            ArrayList arrayList = new ArrayList();
            for (String str3 : split) {
                if (!str3.startsWith("h_w_hiapp_referrer") && !str3.startsWith("h_w_gp_referrer")) {
                    arrayList.add(str3);
                }
            }
            StringBuilder sb = new StringBuilder();
            int size = arrayList.size();
            for (int i2 = 0; i2 < size; i2++) {
                sb.append((String) arrayList.get(i2));
                if (i2 < arrayList.size() - 1) {
                    sb.append("&");
                }
            }
            if (arrayList.size() == 0) {
                str2 = str.substring(0, indexOf);
            } else {
                str2 = str.substring(0, i) + sb.toString();
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "after delete referrer, the new IntentUri is:" + str2);
            return str2;
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "self show receiver exception");
            return str;
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "delete referrer exception");
            return str;
        }
    }

    private void e() {
        try {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(this.c.g())) {
                sb.append("&referrer=");
                sb.append(Uri.encode(b(this.c.g())));
            }
            String str = "market://details?id=" + this.c.s() + sb;
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(str));
            intent.setPackage("com.huawei.appmarket");
            Intent intent2 = new Intent("android.intent.action.VIEW");
            intent2.setData(Uri.parse(str));
            intent2.setPackage("com.android.vending");
            if (com.huawei.hms.support.api.push.a.d.a.a(this.b, "com.android.vending", intent2).booleanValue()) {
                intent2.setFlags(402653184);
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "open google play store's app detail, IntentUrl is:" + intent2.toURI());
                this.b.startActivity(intent2);
            } else if (com.huawei.hms.support.api.push.a.d.a.a(this.b, "com.huawei.appmarket", intent).booleanValue()) {
                intent.setFlags(402653184);
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "open HiApp's app detail, IntentUrl is:" + intent.toURI());
                this.b.startActivity(intent);
            } else {
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "open app detail by browser.");
                f();
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "open market app detail failed,exception:" + e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        com.huawei.hms.support.log.a.b("PushSelfShowLog", "parse h_w_hiapp_referrer faied");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0031, code lost:
        com.huawei.hms.support.log.a.c("PushSelfShowLog", "parse intentUri error");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
        com.huawei.hms.support.log.a.c("PushSelfShowLog", "parse intentUri error");
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:22:? A[ExcHandler: RuntimeException (unused java.lang.RuntimeException), PHI: r0 
  PHI: (r0v9 java.lang.String) = (r0v0 java.lang.String), (r0v0 java.lang.String), (r0v11 java.lang.String), (r0v11 java.lang.String), (r0v11 java.lang.String), (r0v0 java.lang.String), (r0v0 java.lang.String) binds: [B:1:0x0004, B:4:0x0014, B:12:0x0023, B:17:0x002d, B:18:?, B:9:0x001e, B:10:?] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC, Splitter:B:1:0x0004] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void f() {
        /*
            r5 = this;
            java.lang.String r0 = ""
            java.lang.String r1 = ""
            com.huawei.hms.support.api.push.a.b.a r2 = r5.c     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
            java.lang.String r2 = r2.g()     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
            java.lang.String r2 = android.net.Uri.decode(r2)     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
            android.net.Uri r2 = android.net.Uri.parse(r2)     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
            java.lang.String r3 = "h_w_hiapp_referrer"
            java.lang.String r3 = r2.getQueryParameter(r3)     // Catch:{ Exception -> 0x001a, RuntimeException -> 0x0039 }
            r0 = r3
            goto L_0x0021
        L_0x001a:
            java.lang.String r3 = "PushSelfShowLog"
            java.lang.String r4 = "parse h_w_hiapp_referrer faied"
            com.huawei.hms.support.log.a.b(r3, r4)     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
        L_0x0021:
            java.lang.String r3 = "h_w_gp_referrer"
            java.lang.String r2 = r2.getQueryParameter(r3)     // Catch:{ Exception -> 0x0029, RuntimeException -> 0x0039 }
            r1 = r2
            goto L_0x0040
        L_0x0029:
            java.lang.String r2 = "PushSelfShowLog"
            java.lang.String r3 = "parse h_w_hiapp_referrer faied"
            com.huawei.hms.support.log.a.b(r2, r3)     // Catch:{ RuntimeException -> 0x0039, Exception -> 0x0031 }
            goto L_0x0040
        L_0x0031:
            java.lang.String r2 = "PushSelfShowLog"
            java.lang.String r3 = "parse intentUri error"
            com.huawei.hms.support.log.a.c(r2, r3)
            goto L_0x0040
        L_0x0039:
            java.lang.String r2 = "PushSelfShowLog"
            java.lang.String r3 = "parse intentUri error"
            com.huawei.hms.support.log.a.c(r2, r3)
        L_0x0040:
            boolean r2 = com.huawei.hms.c.j.b()
            if (r2 == 0) goto L_0x0071
            boolean r2 = com.huawei.hms.c.j.a()
            if (r2 != 0) goto L_0x004d
            goto L_0x0071
        L_0x004d:
            java.lang.String r1 = "PushSelfShowLog"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "It is China device, open Huawei market web, referrer: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.huawei.hms.support.log.a.b(r1, r2)
            java.lang.String r0 = android.net.Uri.decode(r0)
            boolean r1 = android.webkit.URLUtil.isValidUrl(r0)
            if (r1 == 0) goto L_0x006e
            goto L_0x00a9
        L_0x006e:
            java.lang.String r0 = "https://a.vmall.com/"
            goto L_0x00a9
        L_0x0071:
            java.lang.String r0 = "PushSelfShowLog"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "not EMUI system or not in China, open google play web, referrer: "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            com.huawei.hms.support.log.a.b(r0, r2)
            java.lang.String r0 = android.net.Uri.decode(r1)
            boolean r1 = android.webkit.URLUtil.isValidUrl(r0)
            if (r1 == 0) goto L_0x0092
            goto L_0x00a9
        L_0x0092:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "https://play.google.com/store/apps/details?id="
            r0.append(r1)
            com.huawei.hms.support.api.push.a.b.a r1 = r5.c
            java.lang.String r1 = r1.s()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L_0x00a9:
            java.lang.String r1 = "PushSelfShowLog"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "open the URL by browser: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.huawei.hms.support.log.a.b(r1, r2)
            android.content.Context r1 = r5.b
            com.huawei.hms.support.api.push.a.d.a.d(r1, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.huawei.hms.support.api.push.a.a.a.f():void");
    }

    private void g() {
        com.huawei.hms.support.log.a.b("PushSelfShowLog", "run into launchCosaApp ");
        try {
            com.huawei.hms.support.log.a.b("PushSelfShowLog", "enter launchExistApp cosa, appPackageName =" + this.c.s() + ",and msg.intentUri is " + this.c.g());
            Intent b2 = com.huawei.hms.support.api.push.a.d.a.b(this.b, this.c.s());
            boolean z = false;
            if (this.c.g() != null) {
                try {
                    Intent parseUri = Intent.parseUri(this.c.g(), 0);
                    com.huawei.hms.support.log.a.b("PushSelfShowLog", "Intent.parseUri(msg.intentUri, 0)," + parseUri.toURI());
                    boolean booleanValue = com.huawei.hms.support.api.push.a.d.a.a(this.b, this.c.s(), parseUri).booleanValue();
                    if (booleanValue) {
                        b2 = parseUri;
                    }
                    z = booleanValue;
                } catch (RuntimeException unused) {
                    com.huawei.hms.support.log.a.c("PushSelfShowLog", "intentUri error");
                } catch (Exception unused2) {
                    com.huawei.hms.support.log.a.c("PushSelfShowLog", "intentUri error");
                }
            } else {
                if (this.c.t() != null) {
                    Intent intent = new Intent(this.c.t());
                    if (com.huawei.hms.support.api.push.a.d.a.a(this.b, this.c.s(), intent).booleanValue()) {
                        b2 = intent;
                    }
                }
                b2.setPackage(this.c.s());
            }
            if (b2 == null) {
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "launchCosaApp,intent == null");
            } else if (!com.huawei.hms.support.api.push.a.d.a.a(this.b, b2)) {
                com.huawei.hms.support.log.a.c("PushSelfShowLog", "no permission to start Activity");
            } else {
                if (z) {
                    b2.addFlags(268435456);
                } else {
                    b2.setFlags(805437440);
                }
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "start " + b2.toURI());
                this.b.startActivity(b2);
            }
        } catch (RuntimeException unused3) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "launch Cosa App exception");
        } catch (Exception unused4) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", "launch Cosa App exception");
        }
    }
}
