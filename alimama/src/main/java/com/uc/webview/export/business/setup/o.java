package com.uc.webview.export.business.setup;

import android.os.Bundle;
import android.webkit.ValueCallback;
import com.uc.webview.export.business.a;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.setup.l;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.utility.SetupTask;
import java.io.File;

/* compiled from: U4Source */
public class o extends SetupTask {
    /* access modifiers changed from: private */
    public static final String a = "o";
    private static String c = "_odex_ready";
    /* access modifiers changed from: private */
    public a b = new a();

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x023d, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:?, code lost:
        r7.b.a(com.uc.webview.export.business.a.c.c);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0246, code lost:
        r0 = a;
        com.uc.webview.export.internal.utility.Log.d(r0, ".run stat: " + r7.b.a);
        r0 = com.uc.webview.export.internal.interfaces.IWaStat.BUSINESS_DECOMPRESS_AND_ODEX;
        r1 = r7.b.a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0264, code lost:
        r1 = a;
        com.uc.webview.export.internal.utility.Log.d(r1, ".run stat: " + r7.b.a);
        com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.BUSINESS_DECOMPRESS_AND_ODEX, java.lang.Long.toString(r7.b.a));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0288, code lost:
        throw r0;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:51:0x023f */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r7 = this;
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ Throwable -> 0x023f }
            long r1 = com.uc.webview.export.business.a.c.a     // Catch:{ Throwable -> 0x023f }
            r0.a(r1)     // Catch:{ Throwable -> 0x023f }
            boolean r0 = com.uc.webview.export.internal.utility.k.i()     // Catch:{ Throwable -> 0x023f }
            if (r0 == 0) goto L_0x0029
            java.lang.String r0 = "process_private_data_dir_suffix"
            java.lang.Object r0 = com.uc.webview.export.extension.UCCore.getGlobalOption(r0)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x023f }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x023f }
            if (r0 == 0) goto L_0x0029
            java.lang.String r0 = "process_private_data_dir_suffix"
            java.lang.String r1 = "1"
            com.uc.webview.export.extension.UCCore.setGlobalOption(r0, r1)     // Catch:{ Throwable -> 0x023f }
            android.content.Context r0 = r7.getContext()     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.SDKFactory.c((android.content.Context) r0)     // Catch:{ Throwable -> 0x023f }
        L_0x0029:
            java.lang.String r0 = "process_private_data_dir_suffix"
            java.lang.Object r0 = com.uc.webview.export.extension.UCCore.getGlobalOption(r0)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x023f }
            if (r0 == 0) goto L_0x0075
            java.lang.String r1 = "0"
            boolean r1 = r1.equals(r0)     // Catch:{ Throwable -> 0x023f }
            if (r1 != 0) goto L_0x0075
            boolean r1 = com.uc.webview.export.internal.utility.k.i()     // Catch:{ Throwable -> 0x023f }
            if (r1 == 0) goto L_0x0049
            java.lang.String r1 = "1"
            boolean r0 = r1.equals(r0)     // Catch:{ Throwable -> 0x023f }
            if (r0 != 0) goto L_0x0075
        L_0x0049:
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ Throwable -> 0x023f }
            long r1 = com.uc.webview.export.business.a.c.d     // Catch:{ Throwable -> 0x023f }
            r0.a(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".run stat: "
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r7.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = "bs_dec_od"
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
            java.lang.String r1 = java.lang.Long.toString(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
            return
        L_0x0075:
            java.lang.String r0 = "ucmZipFile"
            java.lang.Object r0 = r7.getOption(r0)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "bo_dec_root_dir"
            java.lang.Object r1 = r7.getOption(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = (java.lang.String) r1     // Catch:{ Throwable -> 0x023f }
            java.lang.String r2 = a     // Catch:{ Throwable -> 0x023f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x023f }
            java.lang.String r4 = ".run decFilePath: "
            r3.<init>(r4)     // Catch:{ Throwable -> 0x023f }
            r3.append(r0)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r4 = " decRootDirPath: "
            r3.append(r4)     // Catch:{ Throwable -> 0x023f }
            r3.append(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r3 = r3.toString()     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.utility.Log.d(r2, r3)     // Catch:{ Throwable -> 0x023f }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x023f }
            if (r2 != 0) goto L_0x0211
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x023f }
            if (r2 == 0) goto L_0x00ae
            goto L_0x0211
        L_0x00ae:
            boolean r2 = b(r1, r0)     // Catch:{ Throwable -> 0x023f }
            if (r2 == 0) goto L_0x00e7
            java.lang.String r0 = a     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "readyDecompressAndODex"
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ Throwable -> 0x023f }
            long r1 = com.uc.webview.export.business.a.c.f     // Catch:{ Throwable -> 0x023f }
            r0.a(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".run stat: "
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r7.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = "bs_dec_od"
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
            java.lang.String r1 = java.lang.Long.toString(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
            return
        L_0x00e7:
            com.uc.webview.export.internal.setup.b r2 = new com.uc.webview.export.internal.setup.b     // Catch:{ Throwable -> 0x023f }
            r2.<init>()     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r2.setParent(r7)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r2 = (com.uc.webview.export.internal.setup.l) r2     // Catch:{ Throwable -> 0x023f }
            java.util.concurrent.ConcurrentHashMap r3 = r7.mCallbacks     // Catch:{ Throwable -> 0x023f }
            if (r3 == 0) goto L_0x011b
            java.util.concurrent.ConcurrentHashMap r3 = r7.mCallbacks     // Catch:{ Throwable -> 0x023f }
            java.util.Set r3 = r3.entrySet()     // Catch:{ Throwable -> 0x023f }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Throwable -> 0x023f }
        L_0x0100:
            boolean r4 = r3.hasNext()     // Catch:{ Throwable -> 0x023f }
            if (r4 == 0) goto L_0x011b
            java.lang.Object r4 = r3.next()     // Catch:{ Throwable -> 0x023f }
            java.util.Map$Entry r4 = (java.util.Map.Entry) r4     // Catch:{ Throwable -> 0x023f }
            java.lang.Object r5 = r4.getKey()     // Catch:{ Throwable -> 0x023f }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.business.setup.p r6 = new com.uc.webview.export.business.setup.p     // Catch:{ Throwable -> 0x023f }
            r6.<init>(r7, r4)     // Catch:{ Throwable -> 0x023f }
            r2.onEvent((java.lang.String) r5, r6)     // Catch:{ Throwable -> 0x023f }
            goto L_0x0100
        L_0x011b:
            java.lang.String r3 = "exception"
            com.uc.webview.export.business.setup.s r4 = new com.uc.webview.export.business.setup.s     // Catch:{ Throwable -> 0x023f }
            r4.<init>(r7)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r2.onEvent((java.lang.String) r3, r4)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r3 = (com.uc.webview.export.internal.setup.l) r3     // Catch:{ Throwable -> 0x023f }
            java.lang.String r4 = "die"
            com.uc.webview.export.business.setup.r r5 = new com.uc.webview.export.business.setup.r     // Catch:{ Throwable -> 0x023f }
            r5.<init>(r7)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.onEvent((java.lang.String) r4, r5)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r3 = (com.uc.webview.export.internal.setup.l) r3     // Catch:{ Throwable -> 0x023f }
            java.lang.String r4 = "setup"
            com.uc.webview.export.business.setup.q r5 = new com.uc.webview.export.business.setup.q     // Catch:{ Throwable -> 0x023f }
            r5.<init>(r7, r1, r0)     // Catch:{ Throwable -> 0x023f }
            r3.onEvent((java.lang.String) r4, r5)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ Throwable -> 0x023f }
            long r3 = com.uc.webview.export.business.a.c.b     // Catch:{ Throwable -> 0x023f }
            r0.a(r3)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = "VERIFY_POLICY"
            java.lang.Object r0 = r7.getOption(r0)     // Catch:{ Throwable -> 0x023f }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "sc_vrfplc"
            java.lang.String r1 = com.uc.webview.export.extension.UCCore.getParam(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r3 = "sc_vrfaha"
            boolean r3 = r3.equals(r1)     // Catch:{ Throwable -> 0x023f }
            r4 = -1073741697(0xffffffffc000007f, float:-2.0000303)
            if (r3 == 0) goto L_0x0169
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x023f }
            r0 = r0 | r4
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x023f }
            goto L_0x0186
        L_0x0169:
            java.lang.String r3 = "sc_vrfahs"
            boolean r1 = r3.equals(r1)     // Catch:{ Throwable -> 0x023f }
            if (r1 == 0) goto L_0x0186
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x023f }
            r0 = r0 | r4
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x023f }
            int r0 = r0.intValue()     // Catch:{ Throwable -> 0x023f }
            r1 = 2147483647(0x7fffffff, float:NaN)
            r0 = r0 & r1
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)     // Catch:{ Throwable -> 0x023f }
        L_0x0186:
            java.util.concurrent.ConcurrentHashMap r1 = r7.mOptions     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.BaseSetupTask r1 = r2.setOptions((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r1)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r1 = (com.uc.webview.export.internal.setup.l) r1     // Catch:{ Throwable -> 0x023f }
            java.lang.String r3 = "VERIFY_POLICY"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r1.setup((java.lang.String) r3, (java.lang.Object) r0)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "dexFilePath"
            r3 = 0
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "soFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "resFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "ucmCfgFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "ucmKrlDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "sdk_setup"
            boolean r4 = com.uc.webview.export.internal.utility.k.i()     // Catch:{ Throwable -> 0x023f }
            r5 = 1
            r4 = r4 ^ r5
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r4)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "chkMultiCore"
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r4)     // Catch:{ Throwable -> 0x023f }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x023f }
            java.lang.String r1 = "bo_enable_load_class"
            r4 = 0
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r4)     // Catch:{ Throwable -> 0x023f }
            r0.setup((java.lang.String) r1, (java.lang.Object) r4)     // Catch:{ Throwable -> 0x023f }
            r7.mCallbacks = r3     // Catch:{ Throwable -> 0x023f }
            r2.start()     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".run stat: "
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r7.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = "bs_dec_od"
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
        L_0x0209:
            java.lang.String r1 = java.lang.Long.toString(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
            return
        L_0x0211:
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ Throwable -> 0x023f }
            long r1 = com.uc.webview.export.business.a.c.e     // Catch:{ Throwable -> 0x023f }
            r0.a(r1)     // Catch:{ Throwable -> 0x023f }
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".run stat: "
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r7.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = "bs_dec_od"
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
            java.lang.String r1 = java.lang.Long.toString(r1)
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
            return
        L_0x023d:
            r0 = move-exception
            goto L_0x0264
        L_0x023f:
            com.uc.webview.export.business.a r0 = r7.b     // Catch:{ all -> 0x023d }
            long r1 = com.uc.webview.export.business.a.c.c     // Catch:{ all -> 0x023d }
            r0.a(r1)     // Catch:{ all -> 0x023d }
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".run stat: "
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r7.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = "bs_dec_od"
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
            goto L_0x0209
        L_0x0264:
            java.lang.String r1 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".run stat: "
            r2.<init>(r3)
            com.uc.webview.export.business.a r3 = r7.b
            long r3 = r3.a
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r2)
            com.uc.webview.export.business.a r1 = r7.b
            long r1 = r1.a
            java.lang.String r1 = java.lang.Long.toString(r1)
            java.lang.String r2 = "bs_dec_od"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.o.run():void");
    }

    public static void a(String str, String str2) {
        try {
            String extractDirPath = UCCore.getExtractDirPath(str, str2);
            File file = new File(str2);
            String decompressSourceHash = UCCyclone.getDecompressSourceHash(str2, file.length(), file.lastModified(), false);
            new File(extractDirPath, decompressSourceHash + c).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean b(String str, String str2) {
        try {
            File file = new File(UCCore.getExtractDirPath(str, str2));
            if (!file.exists() || k.a(file, c, false) == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static /* synthetic */ void a(o oVar, l lVar) {
        ValueCallback valueCallback = (ValueCallback) oVar.getOption(UCCore.OPTION_DECOMPRESS_AND_ODEX_CALLBACK);
        if (valueCallback != null) {
            String event = lVar.getEvent();
            Bundle bundle = new Bundle();
            bundle.putString("event", event);
            if (lVar.getException() != null) {
                bundle.putInt("errorCode", lVar.getException().errCode());
                bundle.putString("msg", lVar.getException().getMessage());
            }
            String str = a;
            Log.d(str, "decompressAndODex bundle: " + bundle);
            valueCallback.onReceiveValue(bundle);
        }
    }
}
