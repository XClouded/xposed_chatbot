package com.alibaba.wireless.security.open.middletier.fc.ui;

import android.app.Activity;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.text.TextUtils;
import com.alibaba.wireless.security.SecExceptionCode;
import com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ContainerActivity extends Activity {
    /* access modifiers changed from: private */
    public static IUIBridge m;
    WVUCWebView a = null;
    long b = 0;
    String c = "";
    String d = "?action=close";
    long e;
    String f = "";
    String g = "";
    boolean h = false;
    final int i = 7;
    final int j = 0;
    final int k = 100107;
    final int l = SecExceptionCode.SEC_ERROR_MIDDLE_TIER_INIT_FAILED;

    private String a(String str) throws MalformedURLException {
        String query = new URL(str).getQuery();
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(query)) {
            String str2 = null;
            String[] split = query.split("&");
            for (String str3 : split) {
                if (str3.startsWith("http_referer=")) {
                    this.c = str3.substring("http_referer=".length());
                    str2 = str3;
                } else if (!str3.equalsIgnoreCase("native=1")) {
                    sb.append(str3);
                    sb.append("&");
                }
            }
            sb.append("tmd_nc=1");
            if (str2 != null) {
                sb.append("&");
                sb.append(str2);
            }
            return str.replace(query, sb.toString());
        }
        sb.append(str);
        if (!str.endsWith("?")) {
            sb.append("?");
        }
        sb.append("tmd_nc=1");
        return sb.toString();
    }

    public static void setUIBridge(IUIBridge iUIBridge) {
        m = iUIBridge;
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (m != null) {
            m.sendUIResult(this.b, 4, (HashMap) null);
        }
        if (this.h) {
            long currentTimeMillis = System.currentTimeMillis();
            String str = this.f;
            long j2 = currentTimeMillis - this.e;
            UserTrackMethodJniBridge.addUtRecord("100107", 0, 7, str, j2, "", "onBackPressed", (String) null, "" + this.b, this.g);
        }
        finish();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0094  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onCreate(android.os.Bundle r27) {
        /*
            r26 = this;
            r1 = r26
            super.onCreate(r27)
            long r2 = java.lang.System.currentTimeMillis()
            r1.e = r2
            java.lang.String r2 = "onCreate"
            java.lang.String r3 = ""
            android.content.Intent r0 = r26.getIntent()     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = "needUT"
            r5 = 0
            boolean r4 = r0.getBooleanExtra(r4, r5)     // Catch:{ Exception -> 0x008f }
            r1.h = r4     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = "sessionId"
            r5 = 0
            long r4 = r0.getLongExtra(r4, r5)     // Catch:{ Exception -> 0x008f }
            r1.b = r4     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = "pluginVersion"
            java.lang.String r4 = r0.getStringExtra(r4)     // Catch:{ Exception -> 0x008f }
            r1.f = r4     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = "bxUUID"
            java.lang.String r4 = r0.getStringExtra(r4)     // Catch:{ Exception -> 0x008f }
            r1.g = r4     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = "location"
            java.lang.String r0 = r0.getStringExtra(r4)     // Catch:{ Exception -> 0x008f }
            java.lang.String r4 = r1.a(r0)     // Catch:{ Exception -> 0x008f }
            android.widget.LinearLayout r0 = new android.widget.LinearLayout     // Catch:{ Exception -> 0x008c }
            r0.<init>(r1)     // Catch:{ Exception -> 0x008c }
            r3 = 1
            r0.setOrientation(r3)     // Catch:{ Exception -> 0x008c }
            android.widget.LinearLayout$LayoutParams r3 = new android.widget.LinearLayout$LayoutParams     // Catch:{ Exception -> 0x008c }
            r5 = -1
            r3.<init>(r5, r5)     // Catch:{ Exception -> 0x008c }
            r0.setLayoutParams(r3)     // Catch:{ Exception -> 0x008c }
            r1.setContentView(r0)     // Catch:{ Exception -> 0x008c }
            android.taobao.windvane.extra.uc.WVUCWebView r3 = new android.taobao.windvane.extra.uc.WVUCWebView     // Catch:{ Exception -> 0x008c }
            r3.<init>(r1)     // Catch:{ Exception -> 0x008c }
            r1.a = r3     // Catch:{ Exception -> 0x008c }
            android.taobao.windvane.extra.uc.WVUCWebView r3 = r1.a     // Catch:{ Exception -> 0x008c }
            android.view.ViewGroup$LayoutParams r6 = new android.view.ViewGroup$LayoutParams     // Catch:{ Exception -> 0x008c }
            r6.<init>(r5, r5)     // Catch:{ Exception -> 0x008c }
            r0.addView(r3, r6)     // Catch:{ Exception -> 0x008c }
            android.taobao.windvane.extra.uc.WVUCWebView r0 = r1.a     // Catch:{ Exception -> 0x008c }
            com.alibaba.wireless.security.open.middletier.fc.ui.ContainerActivity$1 r3 = new com.alibaba.wireless.security.open.middletier.fc.ui.ContainerActivity$1     // Catch:{ Exception -> 0x008c }
            r3.<init>(r1)     // Catch:{ Exception -> 0x008c }
            r0.setWebViewClient(r3)     // Catch:{ Exception -> 0x008c }
            android.taobao.windvane.extra.uc.WVUCWebView r0 = r1.a     // Catch:{ Exception -> 0x008c }
            r0.loadUrl(r4)     // Catch:{ Exception -> 0x008c }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x008c }
            r0.<init>()     // Catch:{ Exception -> 0x008c }
            r0.append(r2)     // Catch:{ Exception -> 0x008c }
            java.lang.String r3 = "||loadUrl"
            r0.append(r3)     // Catch:{ Exception -> 0x008c }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x008c }
            r22 = r0
            r23 = r4
            goto L_0x00e8
        L_0x008c:
            r0 = move-exception
            r3 = r4
            goto L_0x0090
        L_0x008f:
            r0 = move-exception
        L_0x0090:
            com.alibaba.wireless.security.open.middletier.fc.ui.IUIBridge r4 = m
            if (r4 == 0) goto L_0x009d
            com.alibaba.wireless.security.open.middletier.fc.ui.IUIBridge r4 = m
            long r5 = r1.b
            r7 = 2
            r8 = 0
            r4.sendUIResult(r5, r7, r8)
        L_0x009d:
            long r4 = java.lang.System.currentTimeMillis()
            java.lang.String r6 = "100107"
            r7 = 2303(0x8ff, float:3.227E-42)
            r8 = 7
            java.lang.String r9 = r1.f
            long r10 = r1.e
            long r10 = r4 - r10
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = ""
            r4.append(r5)
            java.lang.String r0 = r0.getMessage()
            r4.append(r0)
            java.lang.String r0 = r4.toString()
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = ""
            r4.append(r5)
            long r12 = r1.b
            r4.append(r12)
            java.lang.String r13 = r4.toString()
            java.lang.String r14 = r1.g
            r4 = r6
            r5 = r7
            r6 = r8
            r7 = r9
            r8 = r10
            r10 = r0
            r11 = r2
            r12 = r3
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r4, r5, r6, r7, r8, r10, r11, r12, r13, r14)
            r26.finish()
            r22 = r2
            r23 = r3
        L_0x00e8:
            boolean r0 = r1.h
            if (r0 == 0) goto L_0x011a
            long r2 = java.lang.System.currentTimeMillis()
            java.lang.String r15 = "100107"
            r16 = 0
            r17 = 7
            java.lang.String r0 = r1.f
            long r4 = r1.e
            long r19 = r2 - r4
            java.lang.String r21 = ""
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            r2.append(r3)
            long r3 = r1.b
            r2.append(r3)
            java.lang.String r24 = r2.toString()
            java.lang.String r2 = r1.g
            r18 = r0
            r25 = r2
            com.alibaba.wireless.security.framework.utils.UserTrackMethodJniBridge.addUtRecord(r15, r16, r17, r18, r19, r21, r22, r23, r24, r25)
        L_0x011a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.open.middletier.fc.ui.ContainerActivity.onCreate(android.os.Bundle):void");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.a != null) {
            try {
                this.a.setVisibility(8);
                this.a.removeAllViews();
                this.a.coreDestroy();
                this.a = null;
            } catch (Exception e2) {
                UserTrackMethodJniBridge.addUtRecord("100107", SecExceptionCode.SEC_ERROR_MIDDLE_TIER_INIT_FAILED, 7, this.f, 0, "" + e2.getMessage(), "onDestroy", (String) null, "" + this.b, this.g);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (m != null) {
            m.removeUIItem(this.b);
        }
    }
}
