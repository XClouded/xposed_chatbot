package com.uc.webview.export.internal.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.Signature;
import android.os.Build;
import android.util.DisplayMetrics;
import android.webkit.ValueCallback;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.utility.e;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/* compiled from: U4Source */
public final class g {
    public static boolean a(String str, Context context, Context context2, String str2, ValueCallback<Object[]> valueCallback) {
        try {
            s.a(str, context);
            boolean a2 = a(str, context, context2, str2, valueCallback, (e.a) null);
            if (!a2) {
                s.a(str, context, a2);
            }
            return a2;
        } catch (UCSetupException unused) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0045 A[Catch:{ Throwable -> 0x0077, Throwable -> 0x02b1 }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00a0 A[SYNTHETIC, Splitter:B:24:0x00a0] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean a(java.lang.String r18, android.content.Context r19, android.content.Context r20, java.lang.String r21, android.webkit.ValueCallback<java.lang.Object[]> r22, com.uc.webview.export.internal.utility.e.a r23) {
        /*
            r0 = r18
            r1 = r19
            r2 = r21
            r3 = r22
            r4 = r23
            java.io.File r5 = new java.io.File
            r5.<init>(r0)
            boolean r5 = r5.exists()
            r6 = 0
            if (r5 != 0) goto L_0x0017
            return r6
        L_0x0017:
            java.lang.String r5 = "SignatureVerifier"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r8 = "verify: file = "
            r7.<init>(r8)
            r7.append(r0)
            java.lang.String r7 = r7.toString()
            com.uc.webview.export.internal.utility.Log.d(r5, r7)
            long r7 = java.lang.System.currentTimeMillis()
            r5 = 8
            r9 = 2
            r10 = 1
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            android.content.pm.Signature[] r0 = com.uc.webview.export.internal.utility.g.a.a(r1, r0, r3)     // Catch:{ Throwable -> 0x02b1 }
            if (r0 == 0) goto L_0x0042
            int r13 = r0.length     // Catch:{ Throwable -> 0x02b1 }
            if (r13 > 0) goto L_0x0040
            goto L_0x0042
        L_0x0040:
            r13 = 0
            goto L_0x0043
        L_0x0042:
            r13 = 1
        L_0x0043:
            if (r13 == 0) goto L_0x00a0
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "verify: failed: Signatures of archive is empty. Costs "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x02b1 }
            long r13 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r2 = 0
            long r13 = r13 - r11
            r1.append(r13)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "ms."
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x02b1 }
            if (r3 == 0) goto L_0x007b
            java.lang.Object[] r0 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x0077 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0077 }
            r0[r6] = r1     // Catch:{ Throwable -> 0x0077 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0077 }
            r0[r10] = r1     // Catch:{ Throwable -> 0x0077 }
            r3.onReceiveValue(r0)     // Catch:{ Throwable -> 0x0077 }
            goto L_0x007b
        L_0x0077:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x02b1 }
        L_0x007b:
            if (r4 == 0) goto L_0x0082
            long r0 = com.uc.webview.export.internal.utility.e.a.i     // Catch:{ Throwable -> 0x02b1 }
            r4.a(r0)     // Catch:{ Throwable -> 0x02b1 }
        L_0x0082:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return r6
        L_0x00a0:
            java.security.PublicKey[] r13 = com.uc.webview.export.internal.utility.g.a.a((android.content.pm.Signature[]) r0)     // Catch:{ Throwable -> 0x02b1 }
            boolean r0 = com.uc.webview.export.internal.utility.g.a.a((java.security.PublicKey[]) r13)     // Catch:{ Throwable -> 0x02b1 }
            if (r0 == 0) goto L_0x0105
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "verify: failed: PublicKeys of archive is empty. Costs "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x02b1 }
            long r13 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r2 = 0
            long r13 = r13 - r11
            r1.append(r13)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "ms."
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x02b1 }
            if (r3 == 0) goto L_0x00e0
            java.lang.Object[] r0 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x00dc }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x00dc }
            r0[r6] = r1     // Catch:{ Throwable -> 0x00dc }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x00dc }
            r0[r10] = r1     // Catch:{ Throwable -> 0x00dc }
            r3.onReceiveValue(r0)     // Catch:{ Throwable -> 0x00dc }
            goto L_0x00e0
        L_0x00dc:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x02b1 }
        L_0x00e0:
            if (r4 == 0) goto L_0x00e7
            long r0 = com.uc.webview.export.internal.utility.e.a.j     // Catch:{ Throwable -> 0x02b1 }
            r4.a(r0)     // Catch:{ Throwable -> 0x02b1 }
        L_0x00e7:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return r6
        L_0x0105:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r15 = "verify: step 0: get PublicKeys of archive ok. Costs "
            r14.<init>(r15)     // Catch:{ Throwable -> 0x02b1 }
            long r15 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r17 = 0
            long r11 = r15 - r11
            r14.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = "ms."
            r14.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = r14.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r11)     // Catch:{ Throwable -> 0x02b1 }
            if (r20 == 0) goto L_0x0195
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r0 = r20.getPackageName()     // Catch:{ Throwable -> 0x02b1 }
            android.content.pm.Signature[] r0 = com.uc.webview.export.internal.utility.g.a.a(r1, r0)     // Catch:{ Throwable -> 0x02b1 }
            boolean r0 = a(r13, r0)     // Catch:{ Throwable -> 0x02b1 }
            if (r0 == 0) goto L_0x0175
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "verify: step 1: get Signatures of app from current context and verify ok. Costs "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x02b1 }
            long r13 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r2 = 0
            long r13 = r13 - r11
            r1.append(r13)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "ms."
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return r10
        L_0x0175:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r15 = "verify: step 1: get Signatures of app from current context and verify failed. Costs "
            r14.<init>(r15)     // Catch:{ Throwable -> 0x02b1 }
            long r15 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r17 = 0
            long r11 = r15 - r11
            r14.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = "ms."
            r14.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = r14.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r11)     // Catch:{ Throwable -> 0x02b1 }
        L_0x0195:
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r0 = "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"
            byte[] r0 = android.util.Base64.decode(r0, r9)     // Catch:{ Throwable -> 0x02b1 }
            android.content.pm.Signature[] r0 = com.uc.webview.export.internal.utility.g.a.a((byte[]) r0)     // Catch:{ Throwable -> 0x01e7 }
            boolean r0 = a(r13, r0)     // Catch:{ Throwable -> 0x01e7 }
            if (r0 == 0) goto L_0x01eb
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e7 }
            java.lang.String r15 = "verify: step 2: get Signatures of app from hardcode app and verify ok. Costs "
            r14.<init>(r15)     // Catch:{ Throwable -> 0x01e7 }
            long r15 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x01e7 }
            r17 = 0
            long r5 = r15 - r11
            r14.append(r5)     // Catch:{ Throwable -> 0x01e7 }
            java.lang.String r5 = "ms."
            r14.append(r5)     // Catch:{ Throwable -> 0x01e7 }
            java.lang.String r5 = r14.toString()     // Catch:{ Throwable -> 0x01e7 }
            com.uc.webview.export.internal.utility.Log.d(r0, r5)     // Catch:{ Throwable -> 0x01e7 }
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return r10
        L_0x01e7:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x02b1 }
        L_0x01eb:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r6 = "verify: step 2: get Signatures of app from hardcode app and verify failed. Costs "
            r5.<init>(r6)     // Catch:{ Throwable -> 0x02b1 }
            long r14 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r6 = 0
            long r14 = r14 - r11
            r5.append(r14)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r6 = "ms."
            r5.append(r6)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r5)     // Catch:{ Throwable -> 0x02b1 }
            if (r2 == 0) goto L_0x0289
            int r0 = r21.length()     // Catch:{ Throwable -> 0x02b1 }
            if (r0 <= 0) goto L_0x0289
            long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            android.content.pm.Signature[] r0 = com.uc.webview.export.internal.utility.g.a.a(r1, r2)     // Catch:{ Throwable -> 0x02b1 }
            boolean r0 = a(r13, r0)     // Catch:{ Throwable -> 0x02b1 }
            if (r0 == 0) goto L_0x0263
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = "verify: step 3: get Signatures of app from "
            r1.<init>(r11)     // Catch:{ Throwable -> 0x02b1 }
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = " and verify ok. Costs "
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r2 = 0
            long r11 = r11 - r5
            r1.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "ms."
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return r10
        L_0x0263:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r11 = "verify: step 3: get Signatures of app from "
            r1.<init>(r11)     // Catch:{ Throwable -> 0x02b1 }
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = " and verify failed. Costs "
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b1 }
            r2 = 0
            long r11 = r11 - r5
            r1.append(r11)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r2 = "ms."
            r1.append(r2)     // Catch:{ Throwable -> 0x02b1 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x02b1 }
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x02b1 }
        L_0x0289:
            if (r3 == 0) goto L_0x02a5
            java.lang.Object[] r0 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x02a1 }
            r1 = 8
            java.lang.Integer r2 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x02a1 }
            r1 = 0
            r0[r1] = r2     // Catch:{ Throwable -> 0x02a1 }
            r1 = 3
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Throwable -> 0x02a1 }
            r0[r10] = r1     // Catch:{ Throwable -> 0x02a1 }
            r3.onReceiveValue(r0)     // Catch:{ Throwable -> 0x02a1 }
            goto L_0x02a5
        L_0x02a1:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x02b1 }
        L_0x02a5:
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            goto L_0x0303
        L_0x02af:
            r0 = move-exception
            goto L_0x0320
        L_0x02b1:
            r0 = move-exception
            r1 = r0
            if (r3 == 0) goto L_0x02da
            java.lang.Object[] r0 = new java.lang.Object[r9]     // Catch:{ Throwable -> 0x02d6 }
            r2 = 8
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x02d6 }
            r5 = 0
            r0[r5] = r2     // Catch:{ Throwable -> 0x02d6 }
            java.lang.Class r2 = r1.getClass()     // Catch:{ Throwable -> 0x02d6 }
            java.lang.String r2 = r2.getName()     // Catch:{ Throwable -> 0x02d6 }
            int r2 = r2.hashCode()     // Catch:{ Throwable -> 0x02d6 }
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)     // Catch:{ Throwable -> 0x02d6 }
            r0[r10] = r2     // Catch:{ Throwable -> 0x02d6 }
            r3.onReceiveValue(r0)     // Catch:{ Throwable -> 0x02d6 }
            goto L_0x02da
        L_0x02d6:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ all -> 0x02af }
        L_0x02da:
            if (r4 == 0) goto L_0x02e1
            long r2 = com.uc.webview.export.internal.utility.e.a.m     // Catch:{ all -> 0x02af }
            r4.a(r2)     // Catch:{ all -> 0x02af }
        L_0x02e1:
            if (r4 == 0) goto L_0x02e8
            java.lang.String r0 = "csc_sigvrfe"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ all -> 0x02af }
        L_0x02e8:
            if (r4 == 0) goto L_0x02f7
            java.lang.String r0 = "csc_sigvrfe_v"
            java.lang.String r2 = r1.toString()     // Catch:{ all -> 0x02af }
            java.lang.String r2 = com.uc.webview.export.internal.utility.e.b((java.lang.String) r2)     // Catch:{ all -> 0x02af }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r2)     // Catch:{ all -> 0x02af }
        L_0x02f7:
            r1.printStackTrace()     // Catch:{ all -> 0x02af }
            java.lang.String r0 = "SignatureVerifier"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
        L_0x0303:
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            if (r4 == 0) goto L_0x031e
            long r0 = com.uc.webview.export.internal.utility.e.a.l
            r4.a(r0)
        L_0x031e:
            r1 = 0
            return r1
        L_0x0320:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Verify: total costs:"
            r1.<init>(r2)
            long r2 = java.lang.System.currentTimeMillis()
            long r2 = r2 - r7
            r1.append(r2)
            java.lang.String r2 = "ms"
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "SignatureVerifier"
            com.uc.webview.export.internal.utility.Log.d(r2, r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.g.a(java.lang.String, android.content.Context, android.content.Context, java.lang.String, android.webkit.ValueCallback, com.uc.webview.export.internal.utility.e$a):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x004d A[Catch:{ Throwable -> 0x005d }] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0055  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final boolean a(java.security.PublicKey[] r6, android.content.pm.Signature[] r7) {
        /*
            r0 = 0
            java.security.PublicKey[] r7 = com.uc.webview.export.internal.utility.g.a.a((android.content.pm.Signature[]) r7)     // Catch:{ Throwable -> 0x005d }
            boolean r1 = com.uc.webview.export.internal.utility.g.a.a((java.security.PublicKey[]) r7)     // Catch:{ Throwable -> 0x005d }
            if (r1 == 0) goto L_0x0013
            java.lang.String r6 = "SignatureVerifier"
            java.lang.String r7 = "公钥校验错误：Implement.isEmpty(appPublicKeys) == true"
            com.uc.webview.export.internal.utility.Log.d(r6, r7)     // Catch:{ Throwable -> 0x005d }
            return r0
        L_0x0013:
            r1 = 1
            if (r7 == 0) goto L_0x0043
            if (r6 != 0) goto L_0x0019
            goto L_0x0043
        L_0x0019:
            java.util.HashSet r2 = new java.util.HashSet     // Catch:{ Throwable -> 0x005d }
            r2.<init>()     // Catch:{ Throwable -> 0x005d }
            int r3 = r7.length     // Catch:{ Throwable -> 0x005d }
            r4 = 0
        L_0x0020:
            if (r4 >= r3) goto L_0x002a
            r5 = r7[r4]     // Catch:{ Throwable -> 0x005d }
            r2.add(r5)     // Catch:{ Throwable -> 0x005d }
            int r4 = r4 + 1
            goto L_0x0020
        L_0x002a:
            java.util.HashSet r7 = new java.util.HashSet     // Catch:{ Throwable -> 0x005d }
            r7.<init>()     // Catch:{ Throwable -> 0x005d }
            int r3 = r6.length     // Catch:{ Throwable -> 0x005d }
            r4 = 0
        L_0x0031:
            if (r4 >= r3) goto L_0x003b
            r5 = r6[r4]     // Catch:{ Throwable -> 0x005d }
            r7.add(r5)     // Catch:{ Throwable -> 0x005d }
            int r4 = r4 + 1
            goto L_0x0031
        L_0x003b:
            boolean r6 = r2.equals(r7)     // Catch:{ Throwable -> 0x005d }
            if (r6 == 0) goto L_0x004a
            r6 = 1
            goto L_0x004b
        L_0x0043:
            java.lang.String r6 = "SignatureVerifier"
            java.lang.String r7 = "Sign.equals: s1 == null || s2 == null"
            com.uc.webview.export.internal.utility.Log.e(r6, r7)     // Catch:{ Throwable -> 0x005d }
        L_0x004a:
            r6 = 0
        L_0x004b:
            if (r6 != 0) goto L_0x0055
            java.lang.String r6 = "SignatureVerifier"
            java.lang.String r7 = "公钥校验错误：Implement.equals(appPublicKeys, archiveKeys) == false"
            com.uc.webview.export.internal.utility.Log.d(r6, r7)     // Catch:{ Throwable -> 0x005d }
            return r0
        L_0x0055:
            java.lang.String r6 = "SignatureVerifier"
            java.lang.String r7 = "公钥校验正确!"
            com.uc.webview.export.internal.utility.Log.d(r6, r7)
            return r1
        L_0x005d:
            r6 = move-exception
            r6.printStackTrace()
            java.lang.String r6 = "SignatureVerifier"
            java.lang.String r7 = "公钥校验错误：Implement.isEmpty(appPublicKeys) == true"
            com.uc.webview.export.internal.utility.Log.d(r6, r7)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.g.a(java.security.PublicKey[], android.content.pm.Signature[]):boolean");
    }

    /* compiled from: U4Source */
    static class a {
        public static Signature[] a(Context context, String str) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 64);
                if (packageInfo == null) {
                    return null;
                }
                return packageInfo.signatures;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("SignatureVerifier", e.getMessage());
                return null;
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:24:0x0086 A[SYNTHETIC, Splitter:B:24:0x0086] */
        /* JADX WARNING: Removed duplicated region for block: B:28:0x009b A[SYNTHETIC, Splitter:B:28:0x009b] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static android.content.pm.Signature[] a(android.content.Context r10, java.lang.String r11, android.webkit.ValueCallback<java.lang.Object[]> r12) {
            /*
                java.lang.String r0 = "SignatureVerifier"
                java.lang.String r1 = "getUninstalledAPKSignature(): archiveApkFilePath = %1s"
                r2 = 1
                java.lang.Object[] r3 = new java.lang.Object[r2]
                r4 = 0
                r3[r4] = r11
                java.lang.String r1 = java.lang.String.format(r1, r3)
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                r0 = 0
                long r5 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r1 = "sc_vrfv1"
                java.lang.String r1 = com.uc.webview.export.extension.UCCore.getParam(r1)     // Catch:{ Throwable -> 0x00d0 }
                boolean r1 = java.lang.Boolean.parseBoolean(r1)     // Catch:{ Throwable -> 0x00d0 }
                java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Throwable -> 0x00d0 }
                if (r1 != 0) goto L_0x0028
                r1 = 0
                goto L_0x002c
            L_0x0028:
                boolean r1 = r1.booleanValue()     // Catch:{ Throwable -> 0x00d0 }
            L_0x002c:
                r3 = 2
                if (r1 != 0) goto L_0x006b
                boolean r7 = com.uc.webview.export.cyclone.UCCyclone.detectZipByFileType(r11)     // Catch:{ Throwable -> 0x00d0 }
                if (r7 != 0) goto L_0x0037
                r7 = 0
                goto L_0x0043
            L_0x0037:
                boolean r7 = com.uc.webview.export.internal.utility.a.a((java.lang.String) r11)     // Catch:{ Throwable -> 0x00d0 }
                if (r7 == 0) goto L_0x003f
                r7 = 1
                goto L_0x0043
            L_0x003f:
                boolean r7 = com.uc.webview.export.internal.utility.t.a((java.lang.String) r11)     // Catch:{ Throwable -> 0x00d0 }
            L_0x0043:
                if (r7 == 0) goto L_0x006b
                java.lang.String r10 = "SignatureVerifier"
                java.lang.String r1 = "摘要校验V2!"
                com.uc.webview.export.internal.utility.Log.d(r10, r1)     // Catch:{ Throwable -> 0x00d0 }
                java.security.cert.X509Certificate[][] r10 = com.uc.webview.export.internal.utility.a.b((java.lang.String) r11)     // Catch:{ Throwable -> 0x00d0 }
                int r11 = r10.length     // Catch:{ Throwable -> 0x00d0 }
                android.content.pm.Signature[] r11 = new android.content.pm.Signature[r11]     // Catch:{ Throwable -> 0x00d0 }
                r1 = 0
            L_0x0054:
                int r7 = r10.length     // Catch:{ Throwable -> 0x00d0 }
                if (r1 >= r7) goto L_0x0069
                android.content.pm.Signature r7 = new android.content.pm.Signature     // Catch:{ Throwable -> 0x00d0 }
                r8 = r10[r1]     // Catch:{ Throwable -> 0x00d0 }
                r8 = r8[r4]     // Catch:{ Throwable -> 0x00d0 }
                byte[] r8 = r8.getEncoded()     // Catch:{ Throwable -> 0x00d0 }
                r7.<init>(r8)     // Catch:{ Throwable -> 0x00d0 }
                r11[r1] = r7     // Catch:{ Throwable -> 0x00d0 }
                int r1 = r1 + 1
                goto L_0x0054
            L_0x0069:
                r10 = 2
                goto L_0x0084
            L_0x006b:
                java.lang.String r7 = "SignatureVerifier"
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r9 = "摘要校验V1! 强制V1:"
                r8.<init>(r9)     // Catch:{ Throwable -> 0x00d0 }
                r8.append(r1)     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r1 = r8.toString()     // Catch:{ Throwable -> 0x00d0 }
                com.uc.webview.export.internal.utility.Log.d(r7, r1)     // Catch:{ Throwable -> 0x00d0 }
                android.content.pm.Signature[] r10 = b(r10, r11)     // Catch:{ Throwable -> 0x00d0 }
                r11 = r10
                r10 = 1
            L_0x0084:
                if (r12 == 0) goto L_0x0099
                java.lang.Object[] r1 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0099 }
                r3 = 10
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Throwable -> 0x0099 }
                r1[r4] = r3     // Catch:{ Throwable -> 0x0099 }
                java.lang.Integer r10 = java.lang.Integer.valueOf(r10)     // Catch:{ Throwable -> 0x0099 }
                r1[r2] = r10     // Catch:{ Throwable -> 0x0099 }
                r12.onReceiveValue(r1)     // Catch:{ Throwable -> 0x0099 }
            L_0x0099:
                if (r11 == 0) goto L_0x00aa
                int r10 = r11.length     // Catch:{ Throwable -> 0x00d0 }
                if (r10 <= 0) goto L_0x00aa
                java.lang.String r10 = "SignatureVerifier"
                java.lang.String r12 = "摘要校验成功!"
                com.uc.webview.export.internal.utility.Log.d(r10, r12)     // Catch:{ Throwable -> 0x00a7 }
                r0 = r11
                goto L_0x00b1
            L_0x00a7:
                r10 = move-exception
                r0 = r11
                goto L_0x00d1
            L_0x00aa:
                java.lang.String r10 = "SignatureVerifier"
                java.lang.String r11 = "摘要校验失败"
                com.uc.webview.export.internal.utility.Log.e(r10, r11)     // Catch:{ Throwable -> 0x00d0 }
            L_0x00b1:
                java.lang.String r10 = "SignatureVerifier"
                java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r12 = "耗时："
                r11.<init>(r12)     // Catch:{ Throwable -> 0x00d0 }
                long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x00d0 }
                r12 = 0
                long r1 = r1 - r5
                r11.append(r1)     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r12 = "ms"
                r11.append(r12)     // Catch:{ Throwable -> 0x00d0 }
                java.lang.String r11 = r11.toString()     // Catch:{ Throwable -> 0x00d0 }
                com.uc.webview.export.internal.utility.Log.i(r10, r11)     // Catch:{ Throwable -> 0x00d0 }
                goto L_0x00dd
            L_0x00d0:
                r10 = move-exception
            L_0x00d1:
                r10.printStackTrace()
                java.lang.String r11 = "SignatureVerifier"
                java.lang.String r10 = r10.getMessage()
                com.uc.webview.export.internal.utility.Log.e(r11, r10)
            L_0x00dd:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.utility.g.a.a(android.content.Context, java.lang.String, android.webkit.ValueCallback):android.content.pm.Signature[]");
        }

        private static Signature[] b(Context context, String str) throws Exception {
            Object obj;
            Object obj2;
            Class<?> cls = Class.forName("android.content.pm.PackageParser");
            if (Build.VERSION.SDK_INT >= 21) {
                obj = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            } else {
                obj = cls.getConstructor(new Class[]{String.class}).newInstance(new Object[]{""});
            }
            if (Build.VERSION.SDK_INT >= 21) {
                obj2 = cls.getMethod("parsePackage", new Class[]{File.class, Integer.TYPE}).invoke(obj, new Object[]{new File(str), 0});
            } else {
                obj2 = cls.getMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE}).invoke(obj, new Object[]{new File(str), null, context.getResources().getDisplayMetrics(), 0});
            }
            try {
                cls.getMethod("collectCertificates", new Class[]{Class.forName("android.content.pm.PackageParser$Package"), Integer.TYPE}).invoke(obj, new Object[]{obj2, 64});
                return (Signature[]) obj2.getClass().getField("mSignatures").get(obj2);
            } catch (Throwable unused) {
                cls.getMethod("collectCertificates", new Class[]{Class.forName("android.content.pm.PackageParser$Package"), Boolean.TYPE}).invoke(obj, new Object[]{obj2, true});
                Object obj3 = obj2.getClass().getField("mSigningDetails").get(obj2);
                return (Signature[]) obj3.getClass().getField("signatures").get(obj3);
            }
        }

        static Signature[] a(byte[] bArr) throws Exception {
            X509Certificate[][] a = a.a(ByteBuffer.wrap(bArr));
            Signature[] signatureArr = new Signature[a.length];
            for (int i = 0; i < a.length; i++) {
                signatureArr[i] = new Signature(a[i][0].getEncoded());
            }
            return signatureArr;
        }

        public static PublicKey[] a(Signature[] signatureArr) {
            if (signatureArr != null) {
                try {
                    if (signatureArr.length != 0) {
                        int length = signatureArr.length;
                        PublicKey[] publicKeyArr = new PublicKey[length];
                        for (int i = 0; i < length; i++) {
                            publicKeyArr[i] = ((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signatureArr[i].toByteArray()))).getPublicKey();
                        }
                        return publicKeyArr;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("SignatureVerifier", "获取公钥异常：\n" + e.getMessage());
                    return null;
                }
            }
            return null;
        }

        public static final boolean a(PublicKey[] publicKeyArr) {
            return publicKeyArr == null || publicKeyArr.length <= 0;
        }
    }

    /* compiled from: U4Source */
    public static class b implements ValueCallback<Object[]> {
        private String a;

        public final /* synthetic */ void onReceiveValue(Object obj) {
            Object[] objArr = (Object[]) obj;
            Log.d("VerifyStat", "orign: " + this.a + " objects: " + Arrays.toString(objArr));
            if (objArr != null && objArr.length == 2 && (objArr[0] instanceof Integer) && (objArr[1] instanceof Integer)) {
                String num = ((Integer) objArr[1]).toString();
                int intValue = ((Integer) objArr[0]).intValue();
                if (intValue == 8) {
                    IWaStat.WaStat.stat(String.format("%s_err_%s", new Object[]{this.a, num}));
                } else if (intValue == 10) {
                    IWaStat.WaStat.stat(String.format("%s_ver_%s", new Object[]{this.a, num}));
                }
            }
        }

        public b(String str) {
            this.a = str;
        }
    }
}
