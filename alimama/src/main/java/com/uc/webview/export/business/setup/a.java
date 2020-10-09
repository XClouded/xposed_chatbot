package com.uc.webview.export.business.setup;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.business.BusinessWrapper;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.BaseSetupTask;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.setup.h;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.utility.SetupTask;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class a extends SetupTask {
    /* access modifiers changed from: private */
    public static final String a = "a";
    /* access modifiers changed from: private */
    public com.uc.webview.export.business.a b = new com.uc.webview.export.business.a();
    /* access modifiers changed from: private */
    public com.uc.webview.export.business.a c = new com.uc.webview.export.business.a();
    /* access modifiers changed from: private */
    public com.uc.webview.export.business.a d = new com.uc.webview.export.business.a();
    /* access modifiers changed from: private */
    public com.uc.webview.export.business.a e = new com.uc.webview.export.business.a();
    /* access modifiers changed from: private */
    public com.uc.webview.export.business.a f = new com.uc.webview.export.business.a();
    /* access modifiers changed from: private */
    public C0023a g;
    /* access modifiers changed from: private */
    public ValueCallback<BaseSetupTask> h = new h(this);
    /* access modifiers changed from: private */
    public ValueCallback<BaseSetupTask> i = new i(this);
    /* access modifiers changed from: private */
    public ValueCallback<BaseSetupTask> j = new j(this);
    /* access modifiers changed from: private */
    public ValueCallback<BaseSetupTask> k = new k(this);
    private Map<String, Pair<ValueCallback<BaseSetupTask>, ValueCallback<BaseSetupTask>>> l = new l(this);
    private Map<String, String> m = new m(this);

    static /* synthetic */ void b() {
    }

    private void c() {
        for (Map.Entry next : this.l.entrySet()) {
            ValueCallback callback = getCallback((String) next.getKey());
            if (callback != null) {
                next.setValue(new Pair(callback, ((Pair) next.getValue()).second));
            }
        }
    }

    private void a(BaseSetupTask baseSetupTask) {
        for (Map.Entry next : this.l.entrySet()) {
            String str = (String) next.getKey();
            Iterator<Map.Entry<String, String>> it = this.m.entrySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Map.Entry next2 = it.next();
                if (((String) next2.getKey()).equals(str)) {
                    str = (String) next2.getValue();
                    break;
                }
            }
            baseSetupTask.onEvent(str, (ValueCallback) ((Pair) next.getValue()).second);
        }
    }

    private SetupTask e() {
        if (!k.a((Boolean) getOption(UCCore.OPTION_UCMOBILE_INIT))) {
            return UCCore.setup("CONTEXT", this.mOptions.get("CONTEXT"));
        }
        try {
            return (SetupTask) UCCyclone.invoke((Object) null, Class.forName("com.uc.webview.browser.BrowserCore"), UCCore.LEGACY_EVENT_SETUP, new Class[]{String.class, Object.class}, new Object[]{"CONTEXT", this.mOptions.get("CONTEXT")});
        } catch (Exception e2) {
            throw new UCSetupException((Throwable) e2);
        }
    }

    /* access modifiers changed from: private */
    public String f() {
        File file = new File(UCCore.getExtractDirPath((String) this.mOptions.get(UCCore.OPTION_BUSINESS_DECOMPRESS_ROOT_PATH), (String) this.mOptions.get(UCCore.OPTION_NEW_UCM_ZIP_FILE)));
        if (!file.exists()) {
            file = new File(UCCore.getExtractDirPath(k.a(getContext(), "decompresses2").getAbsolutePath(), (String) this.mOptions.get(UCCore.OPTION_NEW_UCM_ZIP_FILE)));
        }
        return file.getAbsolutePath();
    }

    private void g() {
        a((Map<String, Object>) new n(this));
    }

    private void h() {
        a((Map<String, Object>) new d(this));
    }

    private void i() {
        a((Map<String, Object>) new e(this));
    }

    private void j() {
        a((Map<String, Object>) new f(this));
    }

    private static final int[] a(String str) {
        if (str == null) {
            return null;
        }
        String[] split = str.split("\\.");
        if (split.length <= 3) {
            return null;
        }
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])};
    }

    private static final boolean a(String str, String str2, String str3, String str4) {
        try {
            String str5 = a;
            Log.d(str5, "sdk版本:" + str);
            String str6 = a;
            Log.d(str6, "sdk支持的最小内核版本:" + str2);
            String str7 = a;
            Log.d(str7, "内核版本:" + str3);
            String str8 = a;
            Log.d(str8, "内核支持的最小sdk版本:" + str4);
            int[] a2 = a(str3);
            int[] a3 = a(str2);
            if (a2 != null) {
                if (a3 != null) {
                    if (a2[0] >= a3[0]) {
                        if (a2[0] == a3[0]) {
                            if (a2[1] >= a3[1]) {
                                if (a2[1] == a3[1]) {
                                    if (a2[2] >= a3[2]) {
                                        if (a2[2] == a3[2] && a2[3] < a3[3]) {
                                        }
                                    }
                                }
                            }
                        }
                        int[] a4 = a(str);
                        int[] a5 = a(str4);
                        if (a4 != null) {
                            if (a5 != null) {
                                if (a4[0] >= a5[0]) {
                                    if (a4[0] == a5[0]) {
                                        if (a4[1] >= a5[1]) {
                                            if (a4[1] == a5[1]) {
                                                if (a4[2] >= a5[2]) {
                                                    if (a4[2] == a5[2] && a4[3] < a5[3]) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    return true;
                                }
                                Log.d(a, "最小SDK版本不通过");
                                return false;
                            }
                        }
                        return false;
                    }
                    Log.d(a, "最小内核版本不通过");
                    return false;
                }
            }
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:136:0x0280  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x029a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long a(java.io.File r7, java.lang.String[] r8) {
        /*
            r0 = 0
            int r1 = r8.length     // Catch:{ Throwable -> 0x0265 }
            if (r1 > 0) goto L_0x002a
            java.lang.String r7 = "so file array is empty."
            long r0 = com.uc.webview.export.business.a.C0022a.k     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0021
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0021:
            return r0
        L_0x0022:
            r8 = move-exception
            r0 = r7
            goto L_0x0294
        L_0x0026:
            r8 = move-exception
            r0 = r7
            goto L_0x0266
        L_0x002a:
            boolean r1 = com.uc.webview.export.internal.utility.k.b((java.io.File) r7, (java.io.File) r7)     // Catch:{ Throwable -> 0x0265 }
            if (r1 != 0) goto L_0x004e
            java.lang.String r7 = "root dir modifyFilePermissionsDirFromTo failure."
            long r0 = com.uc.webview.export.business.a.C0022a.l     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x004d
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x004d:
            return r0
        L_0x004e:
            r1 = 2
            java.lang.String[] r2 = new java.lang.String[r1]     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r3 = "core.jar"
            r4 = 0
            r2[r4] = r3     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r3 = "sdk_shell.jar"
            r5 = 1
            r2[r5] = r3     // Catch:{ Throwable -> 0x0265 }
            r3 = 0
        L_0x005c:
            if (r3 >= r1) goto L_0x00a6
            r5 = r2[r3]     // Catch:{ Throwable -> 0x0265 }
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x0265 }
            r6.<init>(r7, r5)     // Catch:{ Throwable -> 0x0265 }
            boolean r5 = r6.exists()     // Catch:{ Throwable -> 0x0265 }
            if (r5 == 0) goto L_0x0075
            boolean r5 = com.uc.webview.export.internal.utility.k.a((java.io.File) r6)     // Catch:{ Throwable -> 0x0265 }
            if (r5 != 0) goto L_0x0072
            goto L_0x0075
        L_0x0072:
            int r3 = r3 + 1
            goto L_0x005c
        L_0x0075:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r6.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " not exists or setReadable failure."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.m     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x00a5
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x00a5:
            return r0
        L_0x00a6:
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r2 = "assets"
            r1.<init>(r7, r2)     // Catch:{ Throwable -> 0x0265 }
            boolean r2 = com.uc.webview.export.internal.utility.k.b((java.io.File) r1, (java.io.File) r7)     // Catch:{ Throwable -> 0x0265 }
            if (r2 != 0) goto L_0x00d1
            java.lang.String r7 = "resource dir modifyFilePermissionsDirFromTo failure."
            long r0 = com.uc.webview.export.business.a.C0022a.n     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x00d0
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x00d0:
            return r0
        L_0x00d1:
            java.lang.String r2 = "paks"
            java.io.File r1 = com.uc.webview.export.internal.utility.k.b((java.io.File) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0265 }
            java.io.File[] r1 = r1.listFiles()     // Catch:{ Throwable -> 0x0265 }
            int r2 = r1.length     // Catch:{ Throwable -> 0x0265 }
            r3 = 0
        L_0x00dd:
            if (r3 >= r2) goto L_0x0122
            r5 = r1[r3]     // Catch:{ Throwable -> 0x0265 }
            boolean r6 = r5.exists()     // Catch:{ Throwable -> 0x0265 }
            if (r6 == 0) goto L_0x00f1
            boolean r6 = com.uc.webview.export.internal.utility.k.a((java.io.File) r5)     // Catch:{ Throwable -> 0x0265 }
            if (r6 != 0) goto L_0x00ee
            goto L_0x00f1
        L_0x00ee:
            int r3 = r3 + 1
            goto L_0x00dd
        L_0x00f1:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r5.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " not exists or setReadable failure."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.o     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0121
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0121:
            return r0
        L_0x0122:
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0265 }
            r2 = r8[r4]     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r2 = com.uc.webview.export.internal.utility.k.a((java.io.File) r7, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0265 }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0265 }
            java.io.File r1 = r1.getParentFile()     // Catch:{ Throwable -> 0x0265 }
            boolean r7 = com.uc.webview.export.internal.utility.k.b((java.io.File) r1, (java.io.File) r7)     // Catch:{ Throwable -> 0x0265 }
            if (r7 != 0) goto L_0x0155
            java.lang.String r7 = "so dir modifyFilePermissionsDirFromTo failure."
            long r0 = com.uc.webview.export.business.a.C0022a.p     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0154
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0154:
            return r0
        L_0x0155:
            boolean r7 = r1.exists()     // Catch:{ Throwable -> 0x0265 }
            if (r7 == 0) goto L_0x0232
            boolean r7 = com.uc.webview.export.internal.utility.k.a((java.io.File) r1)     // Catch:{ Throwable -> 0x0265 }
            if (r7 != 0) goto L_0x0163
            goto L_0x0232
        L_0x0163:
            int r7 = r8.length     // Catch:{ Throwable -> 0x0265 }
        L_0x0164:
            if (r4 >= r7) goto L_0x0216
            r2 = r8[r4]     // Catch:{ Throwable -> 0x0265 }
            java.io.File r3 = new java.io.File     // Catch:{ Throwable -> 0x0265 }
            r3.<init>(r1, r2)     // Catch:{ Throwable -> 0x0265 }
            boolean r2 = r3.exists()     // Catch:{ Throwable -> 0x0265 }
            if (r2 != 0) goto L_0x01a4
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r3.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " not exists."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.r     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x01a3
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x01a3:
            return r0
        L_0x01a4:
            boolean r2 = com.uc.webview.export.internal.utility.k.b((java.io.File) r3)     // Catch:{ Throwable -> 0x0265 }
            if (r2 != 0) goto L_0x01db
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r3.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " setExecutable failure."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.s     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x01da
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x01da:
            return r0
        L_0x01db:
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.io.File) r3)     // Catch:{ Throwable -> 0x0265 }
            if (r2 != 0) goto L_0x0212
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r3.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " setReadable failure."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.t     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0211
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0211:
            return r0
        L_0x0212:
            int r4 = r4 + 1
            goto L_0x0164
        L_0x0216:
            boolean r7 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r7 != 0) goto L_0x022f
            java.lang.String r7 = a
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r1 = ".checkFilesExistsAndPermissions failure, because "
            r8.<init>(r1)
            r8.append(r0)
            java.lang.String r8 = r8.toString()
            com.uc.webview.export.internal.utility.Log.d(r7, r8)
        L_0x022f:
            r7 = 0
            return r7
        L_0x0232:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0265 }
            r7.<init>()     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = r1.getName()     // Catch:{ Throwable -> 0x0265 }
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r8 = " not exists or setReadable failure."
            r7.append(r8)     // Catch:{ Throwable -> 0x0265 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0265 }
            long r0 = com.uc.webview.export.business.a.C0022a.q     // Catch:{ Throwable -> 0x0026 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0262
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0262:
            return r0
        L_0x0263:
            r8 = move-exception
            goto L_0x0294
        L_0x0265:
            r8 = move-exception
        L_0x0266:
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ all -> 0x0263 }
            java.lang.String r1 = "exception "
            r7.<init>(r1)     // Catch:{ all -> 0x0263 }
            java.lang.String r8 = r8.getMessage()     // Catch:{ all -> 0x0263 }
            r7.append(r8)     // Catch:{ all -> 0x0263 }
            java.lang.String r7 = r7.toString()     // Catch:{ all -> 0x0263 }
            long r0 = com.uc.webview.export.business.a.C0022a.u     // Catch:{ all -> 0x0022 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r8 != 0) goto L_0x0293
            java.lang.String r8 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkFilesExistsAndPermissions failure, because "
            r2.<init>(r3)
            r2.append(r7)
            java.lang.String r7 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r7)
        L_0x0293:
            return r0
        L_0x0294:
            boolean r7 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r7 != 0) goto L_0x02ad
            java.lang.String r7 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".checkFilesExistsAndPermissions failure, because "
            r1.<init>(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r7, r0)
        L_0x02ad:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.a.a(java.io.File, java.lang.String[]):long");
    }

    private static long a(File file, DexClassLoader dexClassLoader) {
        return a(file, k.a((ClassLoader) dexClassLoader));
    }

    private DexClassLoader b(String str) {
        Context context = (Context) getOption("CONTEXT");
        File file = new File(str);
        try {
            String absolutePath = new File(file, br.SDK_SHELL_DEX_FILE).getAbsolutePath();
            Integer num = (Integer) this.mOptions.get(UCCore.OPTION_VERIFY_POLICY);
            if (!(num == null || (num.intValue() & 1) == 0)) {
                h.a(context, num, absolutePath);
            }
            return new DexClassLoader(absolutePath, UCCore.getODexDirPath(context, file.getAbsolutePath()), "", a.class.getClassLoader());
        } catch (Throwable unused) {
            Log.d(a, "create sdk_shell dexLoader failure!");
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:82:0x0176  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0193  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long c(java.lang.String r8) {
        /*
            r7 = this;
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".checkCoreCompatibleAndFileExistsPermissions "
            r1.<init>(r2)
            r1.append(r8)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0158 }
            r1.<init>(r8)     // Catch:{ Throwable -> 0x0158 }
            boolean r2 = r1.exists()     // Catch:{ Throwable -> 0x0158 }
            if (r2 != 0) goto L_0x004e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0158 }
            java.lang.String r2 = "check new core files, "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0158 }
            r1.append(r8)     // Catch:{ Throwable -> 0x0158 }
            java.lang.String r8 = " not exists!"
            r1.append(r8)     // Catch:{ Throwable -> 0x0158 }
            java.lang.String r8 = r1.toString()     // Catch:{ Throwable -> 0x0158 }
            long r0 = com.uc.webview.export.business.a.C0022a.e     // Catch:{ Throwable -> 0x0078, all -> 0x0072 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r2 != 0) goto L_0x004d
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r8)
        L_0x004d:
            return r0
        L_0x004e:
            dalvik.system.DexClassLoader r8 = r7.b((java.lang.String) r8)     // Catch:{ Throwable -> 0x0158 }
            if (r8 != 0) goto L_0x007e
            java.lang.String r8 = "check old core files, create sdk_shell dexLoader failure!"
            long r0 = com.uc.webview.export.business.a.C0022a.f     // Catch:{ Throwable -> 0x0078, all -> 0x0072 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r2 != 0) goto L_0x0071
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r8)
        L_0x0071:
            return r0
        L_0x0072:
            r0 = move-exception
            r6 = r0
            r0 = r8
            r8 = r6
            goto L_0x018d
        L_0x0078:
            r0 = move-exception
            r6 = r0
            r0 = r8
            r8 = r6
            goto L_0x0159
        L_0x007e:
            java.lang.String r2 = com.uc.webview.export.internal.utility.k.b((java.lang.ClassLoader) r8)     // Catch:{ Throwable -> 0x0158 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r2)     // Catch:{ Throwable -> 0x0158 }
            if (r3 == 0) goto L_0x00a6
            java.lang.String r8 = "check old core files, get core version failure!"
            long r0 = com.uc.webview.export.business.a.C0022a.g     // Catch:{ Throwable -> 0x0078, all -> 0x0072 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r2 != 0) goto L_0x00a5
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r8)
        L_0x00a5:
            return r0
        L_0x00a6:
            java.lang.String r3 = "bo_prom_sp_v_c_i"
            java.lang.Object r3 = r7.getOption(r3)     // Catch:{ Throwable -> 0x0158 }
            com.uc.webview.export.extension.UCCore$Callable r3 = (com.uc.webview.export.extension.UCCore.Callable) r3     // Catch:{ Throwable -> 0x0158 }
            if (r3 == 0) goto L_0x0138
            java.lang.Object r3 = r3.call(r2)     // Catch:{ Throwable -> 0x0158 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ Throwable -> 0x0158 }
            boolean r3 = r3.booleanValue()     // Catch:{ Throwable -> 0x0158 }
            if (r3 != 0) goto L_0x00be
            goto L_0x0138
        L_0x00be:
            java.lang.String r3 = com.uc.webview.export.internal.utility.k.c((java.lang.ClassLoader) r8)     // Catch:{ Throwable -> 0x0158 }
            java.lang.String r4 = com.uc.webview.export.Build.Version.NAME     // Catch:{ Throwable -> 0x0158 }
            java.lang.String r5 = com.uc.webview.export.Build.Version.SUPPORT_U4_MIN     // Catch:{ Throwable -> 0x0158 }
            boolean r2 = a(r4, r5, r2, r3)     // Catch:{ Throwable -> 0x0158 }
            if (r2 != 0) goto L_0x00ea
            java.lang.String r8 = "check old core files, version compatible failure!"
            long r0 = com.uc.webview.export.business.a.C0022a.i     // Catch:{ Throwable -> 0x0078, all -> 0x0072 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r2 != 0) goto L_0x00e9
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r8)
        L_0x00e9:
            return r0
        L_0x00ea:
            java.lang.String r2 = "bo_skip_io_dc"
            java.lang.Object r2 = r7.getOption(r2)     // Catch:{ Throwable -> 0x0158 }
            java.lang.Boolean r2 = (java.lang.Boolean) r2     // Catch:{ Throwable -> 0x0158 }
            boolean r2 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r2)     // Catch:{ Throwable -> 0x0158 }
            r3 = 0
            if (r2 == 0) goto L_0x011e
            long r1 = a((java.io.File) r1, (dalvik.system.DexClassLoader) r8)     // Catch:{ Throwable -> 0x0158 }
            int r8 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r8 == 0) goto L_0x011e
            java.lang.String r8 = "check old core files, file exists and permission failure!"
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r0 != 0) goto L_0x011d
            java.lang.String r0 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r8)
        L_0x011d:
            return r1
        L_0x011e:
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r8 != 0) goto L_0x0137
            java.lang.String r8 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = ".checkCoreCompatibleAndFileExistsPermissions "
            r1.<init>(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r0)
        L_0x0137:
            return r3
        L_0x0138:
            java.lang.String r8 = "check callable permission failure!"
            long r0 = com.uc.webview.export.business.a.C0022a.h     // Catch:{ Throwable -> 0x0078, all -> 0x0072 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r2 != 0) goto L_0x0155
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r8)
            java.lang.String r8 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r8)
        L_0x0155:
            return r0
        L_0x0156:
            r8 = move-exception
            goto L_0x018d
        L_0x0158:
            r8 = move-exception
        L_0x0159:
            java.lang.String r1 = "check old core files exception!"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x018a }
            r0.<init>()     // Catch:{ all -> 0x018a }
            r0.append(r1)     // Catch:{ all -> 0x018a }
            java.lang.String r8 = r8.getMessage()     // Catch:{ all -> 0x018a }
            r0.append(r8)     // Catch:{ all -> 0x018a }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x018a }
            long r1 = com.uc.webview.export.business.a.C0022a.j     // Catch:{ all -> 0x0156 }
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r8 != 0) goto L_0x0189
            java.lang.String r8 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = ".checkCoreCompatibleAndFileExistsPermissions "
            r3.<init>(r4)
            r3.append(r0)
            java.lang.String r0 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r8, r0)
        L_0x0189:
            return r1
        L_0x018a:
            r0 = move-exception
            r8 = r0
            r0 = r1
        L_0x018d:
            boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r1 != 0) goto L_0x01a6
            java.lang.String r1 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = ".checkCoreCompatibleAndFileExistsPermissions "
            r2.<init>(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r0)
        L_0x01a6:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.a.c(java.lang.String):long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x00a0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private long d(java.lang.String r7) {
        /*
            r6 = this;
            r0 = 0
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x0073 }
            r1.<init>(r7)     // Catch:{ Throwable -> 0x0073 }
            boolean r2 = r1.exists()     // Catch:{ Throwable -> 0x0073 }
            if (r2 != 0) goto L_0x002d
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r2 = "check new core files, "
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0073 }
            r1.append(r7)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r7 = " not exists!"
            r1.append(r7)     // Catch:{ Throwable -> 0x0073 }
            java.lang.String r7 = r1.toString()     // Catch:{ Throwable -> 0x0073 }
            long r0 = com.uc.webview.export.business.a.C0022a.e     // Catch:{ Throwable -> 0x0048, all -> 0x0043 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r2 != 0) goto L_0x002c
            java.lang.String r2 = a
            com.uc.webview.export.internal.utility.Log.d(r2, r7)
        L_0x002c:
            return r0
        L_0x002d:
            dalvik.system.DexClassLoader r7 = r6.b((java.lang.String) r7)     // Catch:{ Throwable -> 0x0073 }
            if (r7 != 0) goto L_0x004d
            java.lang.String r7 = "check new core files, create sdk_shell dexLoader failure!"
            long r0 = com.uc.webview.export.business.a.C0022a.f     // Catch:{ Throwable -> 0x0048, all -> 0x0043 }
            boolean r2 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r2 != 0) goto L_0x0042
            java.lang.String r2 = a
            com.uc.webview.export.internal.utility.Log.d(r2, r7)
        L_0x0042:
            return r0
        L_0x0043:
            r0 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L_0x009a
        L_0x0048:
            r0 = move-exception
            r5 = r0
            r0 = r7
            r7 = r5
            goto L_0x0074
        L_0x004d:
            long r1 = a((java.io.File) r1, (dalvik.system.DexClassLoader) r7)     // Catch:{ Throwable -> 0x0073 }
            r3 = 0
            int r7 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1))
            if (r7 == 0) goto L_0x0065
            java.lang.String r7 = "check new core files, file exists and permission failure!"
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r0 != 0) goto L_0x0064
            java.lang.String r0 = a
            com.uc.webview.export.internal.utility.Log.d(r0, r7)
        L_0x0064:
            return r1
        L_0x0065:
            boolean r7 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r7 != 0) goto L_0x0070
            java.lang.String r7 = a
            com.uc.webview.export.internal.utility.Log.d(r7, r0)
        L_0x0070:
            return r3
        L_0x0071:
            r7 = move-exception
            goto L_0x009a
        L_0x0073:
            r7 = move-exception
        L_0x0074:
            java.lang.String r1 = "check new core files exception!"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0097 }
            r0.<init>()     // Catch:{ all -> 0x0097 }
            r0.append(r1)     // Catch:{ all -> 0x0097 }
            java.lang.String r7 = r7.getMessage()     // Catch:{ all -> 0x0097 }
            r0.append(r7)     // Catch:{ all -> 0x0097 }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0097 }
            long r1 = com.uc.webview.export.business.a.C0022a.j     // Catch:{ all -> 0x0071 }
            boolean r7 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r7 != 0) goto L_0x0096
            java.lang.String r7 = a
            com.uc.webview.export.internal.utility.Log.d(r7, r0)
        L_0x0096:
            return r1
        L_0x0097:
            r0 = move-exception
            r7 = r0
            r0 = r1
        L_0x009a:
            boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r1 != 0) goto L_0x00a5
            java.lang.String r1 = a
            com.uc.webview.export.internal.utility.Log.d(r1, r0)
        L_0x00a5:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.a.d(java.lang.String):long");
    }

    /* JADX WARNING: Removed duplicated region for block: B:41:0x011b A[Catch:{ UCSetupException -> 0x0268, all -> 0x0374, UCSetupException -> 0x0319 }] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0180 A[SYNTHETIC, Splitter:B:44:0x0180] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r9 = this;
            java.lang.String r0 = a
            java.lang.String r1 = ".run begin."
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            com.uc.webview.export.business.setup.a$a r0 = new com.uc.webview.export.business.setup.a$a
            r1 = 0
            r0.<init>(r9, r1)
            r9.g = r0
            com.uc.webview.export.business.a r0 = r9.b     // Catch:{ all -> 0x0374 }
            long r2 = com.uc.webview.export.business.a.d.a     // Catch:{ all -> 0x0374 }
            r0.a(r2)     // Catch:{ all -> 0x0374 }
            r0 = 7001(0x1b59, float:9.81E-42)
            r2 = 0
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.a     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "CONTEXT"
            java.lang.Object r3 = r9.getOption(r3)     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Object) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x0037
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.e     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_CONTEXT is null."
            goto L_0x00c9
        L_0x0037:
            java.util.concurrent.ConcurrentHashMap r3 = r9.mOptions     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = "bo_new_ucm_zf"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x0052
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.b     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_NEW_UCM_ZIP_FILE is empty."
            goto L_0x00c9
        L_0x0052:
            java.util.concurrent.ConcurrentHashMap r3 = r9.mOptions     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = "bo_dec_r_p"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x006c
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.c     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_BUSINESS_DECOMPRESS_ROOT_PATH is empty."
            goto L_0x00c9
        L_0x006c:
            java.util.concurrent.ConcurrentHashMap r3 = r9.mOptions     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = "bo_f_u_dec_r_p"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Object) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x0084
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.d     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_FORCE_USE_BUSINESS_DECOMPRESS_ROOT_PATH is null."
            goto L_0x00c9
        L_0x0084:
            java.util.concurrent.ConcurrentHashMap r3 = r9.mOptions     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = "bo_old_dex_dp"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 != 0) goto L_0x00aa
            java.lang.String r3 = "bo_prom_sp_v_c_i"
            java.lang.Object r3 = r9.getOption(r3)     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Object) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x00aa
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.f     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_OLD_DEX_DIR_PATH not empty but OPTION_PROMISE_SPECIAL_VERSION_CORE_INIT is null."
            goto L_0x00c9
        L_0x00aa:
            java.lang.String r3 = "bo_ucm_init"
            java.lang.Object r3 = r9.getOption(r3)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x00c8
            boolean r3 = com.uc.webview.export.internal.utility.k.i()     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 != 0) goto L_0x00c8
            com.uc.webview.export.business.a r3 = r9.d     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.b.g     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "OPTION_UCMOBILE_INIT is true but Class.forName(\"com.uc.webview.browser.BrowserCore\") exception."
            goto L_0x00c9
        L_0x00c8:
            r3 = r2
        L_0x00c9:
            boolean r4 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r4 == 0) goto L_0x0262
            com.uc.webview.export.business.a r3 = r9.e     // Catch:{ UCSetupException -> 0x0268 }
            long r4 = com.uc.webview.export.business.a.C0022a.b     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "bo_new_ucm_zf"
            java.lang.Object r3 = r9.getOption(r3)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = "bo_dec_r_p"
            java.lang.Object r4 = r9.getOption(r4)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.business.setup.o.b(r4, r3)     // Catch:{ UCSetupException -> 0x0268 }
            r4 = 0
            r6 = 1
            if (r3 == 0) goto L_0x0118
            com.uc.webview.export.business.a r3 = r9.e     // Catch:{ UCSetupException -> 0x0268 }
            long r7 = com.uc.webview.export.business.a.C0022a.d     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r7)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = "bo_skip_io_dc"
            java.lang.Object r3 = r9.getOption(r3)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.Boolean r3 = (java.lang.Boolean) r3     // Catch:{ UCSetupException -> 0x0268 }
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r3)     // Catch:{ UCSetupException -> 0x0268 }
            if (r3 == 0) goto L_0x0106
        L_0x0104:
            r3 = 1
            goto L_0x0119
        L_0x0106:
            java.lang.String r3 = r9.f()     // Catch:{ UCSetupException -> 0x0268 }
            long r7 = r9.d((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            int r3 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r3 != 0) goto L_0x0113
            goto L_0x0104
        L_0x0113:
            com.uc.webview.export.business.a r3 = r9.e     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r7)     // Catch:{ UCSetupException -> 0x0268 }
        L_0x0118:
            r3 = 0
        L_0x0119:
            if (r3 == 0) goto L_0x0180
            java.lang.String r1 = a     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = ".run readyDecompressAndODex && checkNewCoreFileExistsAndPermissions."
            com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.setup.c r1 = new com.uc.webview.export.business.setup.c     // Catch:{ UCSetupException -> 0x0268 }
            r1.<init>(r9)     // Catch:{ UCSetupException -> 0x0268 }
            r9.a((java.util.Map<java.lang.String, java.lang.Object>) r1)     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.a r1 = r9.b     // Catch:{ UCSetupException -> 0x0268 }
            long r3 = com.uc.webview.export.business.a.d.b     // Catch:{ UCSetupException -> 0x0268 }
            r1.a(r3)     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilis()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.b = r1
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilisCpu()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.c = r1
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "mInitStat："
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r9.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "checkMillis："
            r1.<init>(r2)
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            java.lang.String r2 = r2.b
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return
        L_0x0180:
            com.uc.webview.export.business.a r3 = r9.f     // Catch:{ UCSetupException -> 0x0268 }
            long r7 = com.uc.webview.export.business.a.C0022a.c     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r7)     // Catch:{ UCSetupException -> 0x0268 }
            java.util.concurrent.ConcurrentHashMap r3 = r9.mOptions     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r7 = "bo_old_dex_dp"
            java.lang.Object r3 = r3.get(r7)     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ UCSetupException -> 0x0268 }
            long r7 = r9.c((java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            int r3 = (r4 > r7 ? 1 : (r4 == r7 ? 0 : -1))
            if (r3 != 0) goto L_0x019b
            r1 = 1
            goto L_0x01a0
        L_0x019b:
            com.uc.webview.export.business.a r3 = r9.f     // Catch:{ UCSetupException -> 0x0268 }
            r3.a(r7)     // Catch:{ UCSetupException -> 0x0268 }
        L_0x01a0:
            if (r1 == 0) goto L_0x0202
            java.lang.String r1 = a     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = ".run checkOldCoreCompatibleAndFileExistsPermissions."
            com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ UCSetupException -> 0x0268 }
            r9.h()     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.a r1 = r9.b     // Catch:{ UCSetupException -> 0x0268 }
            long r3 = com.uc.webview.export.business.a.d.c     // Catch:{ UCSetupException -> 0x0268 }
            r1.a(r3)     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilis()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.b = r1
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilisCpu()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.c = r1
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "mInitStat："
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r9.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "checkMillis："
            r1.<init>(r2)
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            java.lang.String r2 = r2.b
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return
        L_0x0202:
            java.lang.String r1 = a     // Catch:{ UCSetupException -> 0x0268 }
            java.lang.String r3 = ".run initNewCoreByZipFile."
            com.uc.webview.export.internal.utility.Log.d(r1, r3)     // Catch:{ UCSetupException -> 0x0268 }
            r9.g()     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.a r1 = r9.b     // Catch:{ UCSetupException -> 0x0268 }
            long r3 = com.uc.webview.export.business.a.d.d     // Catch:{ UCSetupException -> 0x0268 }
            r1.a(r3)     // Catch:{ UCSetupException -> 0x0268 }
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilis()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.b = r1
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilisCpu()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.c = r1
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "mInitStat："
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r9.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "checkMillis："
            r1.<init>(r2)
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            java.lang.String r2 = r2.b
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return
        L_0x0262:
            com.uc.webview.export.internal.setup.UCSetupException r1 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x0268 }
            r1.<init>((int) r0, (java.lang.String) r3)     // Catch:{ UCSetupException -> 0x0268 }
            throw r1     // Catch:{ UCSetupException -> 0x0268 }
        L_0x0268:
            r1 = move-exception
            java.lang.String r3 = a     // Catch:{ all -> 0x0374 }
            java.lang.String r4 = "checkInputConditions failure message: "
            com.uc.webview.export.internal.utility.Log.d(r3, r4, r1)     // Catch:{ all -> 0x0374 }
            int r3 = r1.errCode()     // Catch:{ all -> 0x0374 }
            if (r3 != r0) goto L_0x0373
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r9.getOption(r0)     // Catch:{ UCSetupException -> 0x0319 }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.Object) r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 == 0) goto L_0x028c
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.e     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "OPTION_CONTEXT is null."
            goto L_0x02f9
        L_0x028c:
            java.lang.String r0 = "sc_ldpl"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r1 = "sc_lshco"
            boolean r0 = r1.equals(r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 != 0) goto L_0x02a4
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.h     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "shareCoreLoadPolicy not equals sc_lshco"
            goto L_0x02f9
        L_0x02a4:
            java.lang.String r0 = "sc_ta_fp"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ UCSetupException -> 0x0319 }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 == 0) goto L_0x02ba
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.i     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "CD_KEY_SHARE_CORE_COMMONALITY_TARGET_FPATH is empty."
            goto L_0x02f9
        L_0x02ba:
            java.lang.String r0 = "sc_pkgl"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ UCSetupException -> 0x0319 }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 == 0) goto L_0x02d0
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.j     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "CD_KEY_SHARE_CORE_CLIENT_SPECIAL_HOST_PKG_NAME_LIST is empty."
            goto L_0x02f9
        L_0x02d0:
            java.lang.String r0 = "sc_taucmv"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ UCSetupException -> 0x0319 }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 == 0) goto L_0x02e6
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.k     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "CD_KEY_SHARE_CORE_CLIENT_UCM_VERSIONS is empty."
            goto L_0x02f9
        L_0x02e6:
            android.content.Context r0 = r9.getContext()     // Catch:{ UCSetupException -> 0x0319 }
            boolean r0 = com.uc.webview.export.internal.utility.e.b((android.content.Context) r0)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 != 0) goto L_0x02f9
            com.uc.webview.export.business.a r0 = r9.d     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.b.l     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r2 = "Sdcard配置及权限校验失败."
        L_0x02f9:
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r2)     // Catch:{ UCSetupException -> 0x0319 }
            if (r0 == 0) goto L_0x0311
            java.lang.String r0 = a     // Catch:{ UCSetupException -> 0x0319 }
            java.lang.String r1 = ".run initShareCore."
            com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ UCSetupException -> 0x0319 }
            r9.i()     // Catch:{ UCSetupException -> 0x0319 }
            com.uc.webview.export.business.a r0 = r9.b     // Catch:{ UCSetupException -> 0x0319 }
            long r1 = com.uc.webview.export.business.a.d.j     // Catch:{ UCSetupException -> 0x0319 }
            r0.a(r1)     // Catch:{ UCSetupException -> 0x0319 }
            goto L_0x0324
        L_0x0311:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ UCSetupException -> 0x0319 }
            r1 = 7003(0x1b5b, float:9.813E-42)
            r0.<init>((int) r1, (java.lang.String) r2)     // Catch:{ UCSetupException -> 0x0319 }
            throw r0     // Catch:{ UCSetupException -> 0x0319 }
        L_0x0319:
            r0 = move-exception
            java.lang.String r1 = a     // Catch:{ all -> 0x0374 }
            java.lang.String r2 = "checkShareCore failure message: "
            com.uc.webview.export.internal.utility.Log.d(r1, r2, r0)     // Catch:{ all -> 0x0374 }
            r9.j()     // Catch:{ all -> 0x0374 }
        L_0x0324:
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilis()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.b = r1
            com.uc.webview.export.business.setup.a$a r0 = r9.g
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r1 = r1.a
            long r1 = r1.getMilisCpu()
            java.lang.String r1 = java.lang.String.valueOf(r1)
            r0.c = r1
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "mInitStat："
            r1.<init>(r2)
            com.uc.webview.export.business.a r2 = r9.b
            long r2 = r2.a
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            java.lang.String r0 = a
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "checkMillis："
            r1.<init>(r2)
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            java.lang.String r2 = r2.b
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.uc.webview.export.internal.utility.Log.d(r0, r1)
            return
        L_0x0373:
            throw r1     // Catch:{ all -> 0x0374 }
        L_0x0374:
            r0 = move-exception
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r2 = r2.a
            long r2 = r2.getMilis()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r1.b = r2
            com.uc.webview.export.business.setup.a$a r1 = r9.g
            com.uc.webview.export.business.setup.a$a r2 = r9.g
            com.uc.webview.export.cyclone.UCElapseTime r2 = r2.a
            long r2 = r2.getMilisCpu()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r1.c = r2
            java.lang.String r1 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "mInitStat："
            r2.<init>(r3)
            com.uc.webview.export.business.a r3 = r9.b
            long r3 = r3.a
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r2)
            java.lang.String r1 = a
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "checkMillis："
            r2.<init>(r3)
            com.uc.webview.export.business.setup.a$a r3 = r9.g
            java.lang.String r3 = r3.b
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.uc.webview.export.internal.utility.Log.d(r1, r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.a.run():void");
    }

    /* renamed from: com.uc.webview.export.business.setup.a$a  reason: collision with other inner class name */
    /* compiled from: U4Source */
    class C0023a {
        UCElapseTime a;
        String b;
        String c;
        String d;
        String e;
        String f;
        String g;

        private C0023a() {
            this.a = new UCElapseTime();
        }

        /* synthetic */ C0023a(a aVar, byte b2) {
            this();
        }
    }

    private void a(Map<String, Object> map) {
        String str;
        String str2;
        try {
            ValueCallback valueCallback = (ValueCallback) getOption(UCCore.OPTION_START_INIT_UC_CORE);
            if (valueCallback != null) {
                Bundle bundle = new Bundle();
                for (Map.Entry next : map.entrySet()) {
                    if (next.getValue() instanceof String) {
                        str = (String) next.getKey();
                        str2 = (String) next.getValue();
                    } else {
                        str = (String) next.getKey();
                        str2 = next.getValue() == null ? BuildConfig.buildJavascriptFrameworkVersion : next.getValue().toString();
                    }
                    bundle.putString(str, str2);
                }
                valueCallback.onReceiveValue(bundle);
            }
        } catch (Throwable th) {
            Log.d(a, "init core callback", th);
        }
        String str3 = a;
        Log.d(str3, "initCore options: " + map);
        c();
        SetupTask e2 = e();
        ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) e2.setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).setParent(this)).setCallbacks(this.mCallbacks)).setup(UCCore.OPTION_DECOMPRESS_ROOT_DIR, this.mOptions.get(UCCore.OPTION_BUSINESS_DECOMPRESS_ROOT_PATH))).setAsDefault();
        a((BaseSetupTask) e2);
        for (Map.Entry next2 : map.entrySet()) {
            e2.setup((String) next2.getKey(), next2.getValue());
        }
        e2.start();
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [com.uc.webview.export.internal.setup.BaseSetupTask] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void a(com.uc.webview.export.business.setup.a r5, java.lang.String r6, com.uc.webview.export.internal.setup.BaseSetupTask r7) {
        /*
            java.util.Map<java.lang.String, android.util.Pair<android.webkit.ValueCallback<com.uc.webview.export.internal.setup.BaseSetupTask>, android.webkit.ValueCallback<com.uc.webview.export.internal.setup.BaseSetupTask>>> r0 = r5.l
            java.util.Set r0 = r0.entrySet()
            java.util.Iterator r0 = r0.iterator()
        L_0x000a:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L_0x004d
            java.lang.Object r1 = r0.next()
            java.util.Map$Entry r1 = (java.util.Map.Entry) r1
            java.lang.Object r2 = r1.getKey()
            boolean r2 = r6.equals(r2)
            if (r2 == 0) goto L_0x000a
            java.lang.Object r1 = r1.getValue()
            android.util.Pair r1 = (android.util.Pair) r1
            java.lang.Object r1 = r1.first
            android.webkit.ValueCallback r1 = (android.webkit.ValueCallback) r1
            if (r1 == 0) goto L_0x000a
            if (r7 == 0) goto L_0x0030
            r2 = r7
            goto L_0x0031
        L_0x0030:
            r2 = r5
        L_0x0031:
            r1.onReceiveValue(r2)     // Catch:{ Throwable -> 0x0035 }
            goto L_0x000a
        L_0x0035:
            r1 = move-exception
            java.lang.String r2 = a
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            r3.append(r6)
            java.lang.String r4 = " callback"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            com.uc.webview.export.internal.utility.Log.d(r2, r3, r1)
            goto L_0x000a
        L_0x004d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.business.setup.a.a(com.uc.webview.export.business.setup.a, java.lang.String, com.uc.webview.export.internal.setup.BaseSetupTask):void");
    }

    static /* synthetic */ void g(a aVar) {
        long j2;
        Object option = aVar.getOption(UCCore.OPTION_DECOMPRESS_AND_ODEX_TASK_WAIT_MILIS);
        if (option != null) {
            if (option instanceof Long) {
                j2 = ((Long) option).longValue();
            } else if (option instanceof Integer) {
                j2 = ((Integer) option).longValue();
            }
            ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) BusinessWrapper.decompressAndODex("CONTEXT", aVar.getContext()).setup("o_flag_odex_done", (Object) true)).setup(UCCore.OPTION_UCM_ZIP_FILE, aVar.getOption(UCCore.OPTION_NEW_UCM_ZIP_FILE))).setup(UCCore.OPTION_ZIP_FILE_TYPE, aVar.getOption(UCCore.OPTION_NEW_UCM_ZIP_TYPE))).setup(UCCore.OPTION_DECOMPRESS_ROOT_DIR, aVar.getOption(UCCore.OPTION_BUSINESS_DECOMPRESS_ROOT_PATH))).setup(UCCore.OPTION_DELETE_AFTER_EXTRACT, (Object) false)).setup(UCCore.OPTION_PROVIDED_KEYS, aVar.getOption(UCCore.OPTION_PROVIDED_KEYS))).setup(UCCore.OPTION_DECOMPRESS_CALLBACK, aVar.getOption(UCCore.OPTION_DECOMPRESS_CALLBACK))).setup(UCCore.OPTION_DECOMPRESS_AND_ODEX_CALLBACK, aVar.getOption(UCCore.OPTION_DECOMPRESS_AND_ODEX_CALLBACK))).setup(UCCore.OPTION_VERIFY_POLICY, aVar.getOption(UCCore.OPTION_VERIFY_POLICY))).start(Long.valueOf(j2).longValue());
        }
        j2 = TBToast.Duration.MEDIUM;
        ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) ((SetupTask) BusinessWrapper.decompressAndODex("CONTEXT", aVar.getContext()).setup("o_flag_odex_done", (Object) true)).setup(UCCore.OPTION_UCM_ZIP_FILE, aVar.getOption(UCCore.OPTION_NEW_UCM_ZIP_FILE))).setup(UCCore.OPTION_ZIP_FILE_TYPE, aVar.getOption(UCCore.OPTION_NEW_UCM_ZIP_TYPE))).setup(UCCore.OPTION_DECOMPRESS_ROOT_DIR, aVar.getOption(UCCore.OPTION_BUSINESS_DECOMPRESS_ROOT_PATH))).setup(UCCore.OPTION_DELETE_AFTER_EXTRACT, (Object) false)).setup(UCCore.OPTION_PROVIDED_KEYS, aVar.getOption(UCCore.OPTION_PROVIDED_KEYS))).setup(UCCore.OPTION_DECOMPRESS_CALLBACK, aVar.getOption(UCCore.OPTION_DECOMPRESS_CALLBACK))).setup(UCCore.OPTION_DECOMPRESS_AND_ODEX_CALLBACK, aVar.getOption(UCCore.OPTION_DECOMPRESS_AND_ODEX_CALLBACK))).setup(UCCore.OPTION_VERIFY_POLICY, aVar.getOption(UCCore.OPTION_VERIFY_POLICY))).start(Long.valueOf(j2).longValue());
    }

    static /* synthetic */ void h(a aVar) {
        aVar.c.a(aVar.b.a);
        b bVar = new b(aVar);
        for (Map.Entry entry : bVar.entrySet()) {
            IWaStat.WaStat.stat((String) entry.getKey(), (String) entry.getValue());
        }
        String str = a;
        Log.d(str, "processStatMaps: " + bVar);
    }

    static /* synthetic */ void c(a aVar, String str) {
        g gVar = new g(aVar, str);
        IWaStat.WaStat.statAKV(new Pair(IWaStat.BUSINESS_ELAPSE_KEY, gVar));
        String str2 = a;
        Log.d(str2, "elapseStatMaps: " + gVar);
    }
}
