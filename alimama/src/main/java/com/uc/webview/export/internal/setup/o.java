package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.webkit.ValueCallback;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCElapseTime;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSubSetupTask;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.utility.SetupTask;
import java.io.File;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class o extends SetupTask {
    private static o b;
    /* access modifiers changed from: private */
    public static Stack<UCSetupTask> c = new Stack<>();
    public l a;
    /* access modifiers changed from: private */
    public l d;
    /* access modifiers changed from: private */
    public l e;
    private l f;
    private Context g;
    private UCElapseTime h;
    /* access modifiers changed from: private */
    public UCSetupException i;
    /* access modifiers changed from: private */
    public UCSetupTask j;
    private ValueCallback<l> k;
    private ValueCallback<l> l;
    /* access modifiers changed from: private */
    public List<av> m;
    private boolean n = false;
    /* access modifiers changed from: private */
    public final ValueCallback<l> o = new p(this);
    /* access modifiers changed from: private */
    public final ValueCallback<l> p = new u(this);
    private final ValueCallback<l> q = new v(this);
    private final ValueCallback<l> r = new w(this);
    /* access modifiers changed from: private */
    public final ValueCallback<l> s = new ad(this);
    private Object t = new Object();
    private az u = null;

    private o() {
    }

    public static synchronized o a() {
        o oVar;
        synchronized (o.class) {
            if (b == null) {
                b.a(327);
                b = new o();
                b.a(331);
            }
            oVar = b;
        }
        return oVar;
    }

    /* access modifiers changed from: private */
    public l e() {
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_NEW_SC_TASK);
        return (l) a((l) new ao(), "ShareCoreSdcardSetupTask").setup(UCCore.OPTION_SHARE_CORE_SETUP_TASK_FLAG, (Object) true);
    }

    /* access modifiers changed from: private */
    public l b(l lVar) {
        ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) lVar.setParent(this)).setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).onEvent(UCCore.LEGACY_EVENT_SETUP, this.l)).onEvent("load", this.l)).onEvent("init", this.l)).onEvent("switch", this.l)).onEvent(UCCore.EVENT_STAT, this.k)).onEvent("success", this.o)).onEvent(UCCore.EVENT_EXCEPTION, this.p);
        return lVar;
    }

    private l a(l lVar, String str) {
        ((l) ((l) ((l) ((l) ((l) ((l) b(lVar).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null);
        if (!k.a(str)) {
            ah setupCrashImprover = lVar.getSetupCrashImprover(getContext(), str);
            ((l) ((l) ((l) ((l) lVar.onEvent("start", setupCrashImprover.b)).onEvent(UCCore.EVENT_DIE, setupCrashImprover.c)).onEvent("crash_none", (ValueCallback) null)).onEvent("crash_seen", (ValueCallback) null)).onEvent("crash_repeat", this.q);
        }
        return lVar;
    }

    public final void a(String str, Callable<Boolean> callable) {
        l lVar = (l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) new bw().setParent(UCSetupTask.getRoot())).setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).setup(UCCore.OPTION_UCM_ZIP_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) null)).setup(UCCore.OPTION_USE_SDK_SETUP, (Object) true)).setup(UCCore.OPTION_CHECK_MULTI_CORE, (Object) true)).onEvent(UCCore.EVENT_STAT, this.k != null ? this.k : new UCSubSetupTask.a())).onEvent("switch", this.s)).onEvent(UCCore.EVENT_DOWNLOAD_EXCEPTION, new ac(this))).onEvent(UCCore.EVENT_DOWNLOAD_FILE_DELETE, new ab(this, str))).onEvent(UCCore.EVENT_UPDATE_PROGRESS, new aa(this));
        this.a = lVar;
        this.d = lVar;
        if (callable != null) {
            this.d.setup(UCCore.OPTION_DOWNLOAD_CHECKER, (Object) callable);
        }
        if (!k.a(str)) {
            this.d.setup(UCCore.OPTION_UCM_UPD_URL, (Object) str);
        }
        if (CDParamKeys.CD_VALUE_LOAD_POLICY_SHARE_CORE.equals(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_LOAD_POLICY))) {
            this.d.onEvent(UCCore.EVENT_UPDATE_SHARE_CORE, this.r);
        }
    }

    private l a(l lVar, boolean z) {
        String str = (String) getOption(UCCore.OPTION_UCM_UPD_URL);
        if (k.a(str)) {
            return null;
        }
        a((String) getOption(UCCore.OPTION_UCM_UPD_URL), (Callable<Boolean>) null);
        try {
            File a2 = k.a(this.g, "updates");
            if (a2.list().length > 0) {
                if (z) {
                    String sourceHash = UCCyclone.getSourceHash(str);
                    String[] list = a2.list();
                    Log.d("SdkSetupTask", "hashcode: " + sourceHash + " list: " + list);
                    int length = list.length;
                    boolean z2 = false;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= length) {
                            break;
                        } else if (list[i2].equals(sourceHash)) {
                            z2 = true;
                            break;
                        } else {
                            i2++;
                        }
                    }
                    if (!z2) {
                        return null;
                    }
                }
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_NEW_UPD_TASK);
                this.n = true;
                if (lVar != null) {
                    c.push(lVar);
                }
                String absolutePath = a2.getAbsolutePath();
                if (z || a2.list().length > 1) {
                    absolutePath = new File(a2, UCCyclone.getSourceHash(str)).getAbsolutePath();
                }
                return (l) ((l) a(k.g() ? new az() : new bf(), absolutePath).setup(UCCore.OPTION_CHECK_DECOMPRESS_FINISH, (Object) true)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) absolutePath);
            }
        } catch (Exception e2) {
            Log.d("SdkSetupTask", "UCMPackageInfo.getUpdateRoot exception: " + e2);
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void f() {
        try {
            if (getTotalLoadedUCM().coreType != 2) {
                k.d(getTotalLoadedUCM().shellClassLoader);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        try {
            SDKFactory.c(SDKFactory.a(getTotalLoadedUCM().ucmPackageInfo, (String) getOption(UCCore.OPTION_LOAD_POLICY)));
        } catch (Throwable th2) {
            th2.printStackTrace();
        }
        try {
            e.a((String) getOption(UCCore.OPTION_LOAD_SHARE_CORE_HOST));
        } catch (Throwable th3) {
            th3.printStackTrace();
        }
    }

    /* JADX WARNING: type inference failed for: r0v88, types: [com.uc.webview.export.internal.setup.BaseSetupTask] */
    /* JADX WARNING: type inference failed for: r0v94, types: [com.uc.webview.export.internal.setup.BaseSetupTask] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
            r11 = this;
            r0 = 5
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            com.uc.webview.export.cyclone.UCElapseTime r0 = new com.uc.webview.export.cyclone.UCElapseTime
            r0.<init>()
            r11.h = r0
            java.lang.String r0 = "ucmZipDir"
            r1 = 0
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r11.setup((java.lang.String) r0, (java.lang.Object) r1)
            com.uc.webview.export.utility.SetupTask r0 = (com.uc.webview.export.utility.SetupTask) r0
            java.lang.String r2 = "sdk_setup"
            r3 = 1
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r3)
            r0.setup((java.lang.String) r2, (java.lang.Object) r4)
            r11.setupGlobalOnce()
            java.lang.String r0 = "stat"
            android.webkit.ValueCallback r0 = r11.getCallback(r0)
            java.lang.String r2 = "stat"
            com.uc.webview.export.internal.setup.r r4 = new com.uc.webview.export.internal.setup.r
            r4.<init>(r11, r0)
            r11.onEvent((java.lang.String) r2, r4)
            android.util.Pair r0 = new android.util.Pair
            java.lang.String r2 = "sdk_stp"
            r0.<init>(r2, r1)
            r11.callbackStat(r0)
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r2 = "SYSTEM_WEBVIEW"
            java.lang.Boolean r0 = com.uc.webview.export.internal.utility.k.a((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r0, (java.lang.String) r2)
            boolean r0 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r0)
            if (r0 == 0) goto L_0x004e
            java.util.Stack<com.uc.webview.export.internal.setup.UCSetupTask> r0 = c
            r0.push(r1)
        L_0x004e:
            r0 = 287(0x11f, float:4.02E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            java.lang.String r0 = "CONTEXT"
            java.lang.Object r0 = r11.getOption(r0)
            android.content.Context r0 = (android.content.Context) r0
            r11.g = r0
            com.uc.webview.export.internal.setup.UCAsyncTask$a r0 = new com.uc.webview.export.internal.setup.UCAsyncTask$a
            r0.<init>()
            r11.l = r0
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r0 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a
            r0.<init>()
            r11.k = r0
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            com.uc.webview.export.internal.setup.af.a((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r0)
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r2 = "SYSTEM_WEBVIEW"
            java.lang.Boolean r0 = com.uc.webview.export.internal.utility.k.a((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r0, (java.lang.String) r2)
            boolean r0 = com.uc.webview.export.internal.utility.k.b((java.lang.Boolean) r0)
            if (r0 != 0) goto L_0x008c
            com.uc.webview.export.internal.setup.ay r0 = new com.uc.webview.export.internal.setup.ay
            r0.<init>()
            com.uc.webview.export.internal.setup.l r0 = r11.b((com.uc.webview.export.internal.setup.l) r0)
            r0.start()
            goto L_0x0361
        L_0x008c:
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r0)
            r2 = 3017(0xbc9, float:4.228E-42)
            r4 = 500(0x1f4, float:7.0E-43)
            if (r0 == 0) goto L_0x01aa
            java.lang.String r0 = "SdkSetupTask"
            java.lang.String r5 = "isThickSDK"
            com.uc.webview.export.internal.utility.Log.i(r0, r5)
            r0 = 314(0x13a, float:4.4E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r5 = "ucmKrlDir"
            java.lang.Object r0 = r0.get(r5)
            java.lang.String r0 = (java.lang.String) r0
            java.util.concurrent.ConcurrentHashMap r5 = r11.mOptions
            java.lang.String r6 = "soFilePath"
            java.lang.Object r5 = r5.get(r6)
            java.lang.String r5 = (java.lang.String) r5
            java.util.concurrent.ConcurrentHashMap r6 = r11.mOptions
            java.lang.String r7 = "resFilePath"
            java.lang.Object r6 = r6.get(r7)
            java.lang.String r6 = (java.lang.String) r6
            java.util.concurrent.ConcurrentHashMap r7 = r11.mOptions
            java.lang.String r8 = "ucmZipFile"
            java.lang.Object r7 = r7.get(r8)
            java.lang.String r7 = (java.lang.String) r7
            boolean r8 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r8 == 0) goto L_0x00d4
            r8 = r5
            goto L_0x00d5
        L_0x00d4:
            r8 = r0
        L_0x00d5:
            android.content.Context r9 = r11.g
            java.util.concurrent.ConcurrentHashMap r10 = r11.mOptions
            boolean r9 = com.uc.webview.export.internal.utility.h.b((android.content.Context) r9, (java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r10)
            if (r9 == 0) goto L_0x0104
            com.uc.webview.export.internal.setup.br r0 = com.uc.webview.export.internal.utility.h.a()
            java.lang.String r0 = r0.soDirPath
            r1 = 509(0x1fd, float:7.13E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r1)
            com.uc.webview.export.internal.setup.az r1 = r11.b()
            com.uc.webview.export.internal.setup.l r1 = r11.a((com.uc.webview.export.internal.setup.l) r1, (java.lang.String) r0)
            r0 = 510(0x1fe, float:7.15E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            com.uc.webview.export.internal.setup.br r0 = com.uc.webview.export.internal.utility.h.a()
            r1.setUCM(r0)
            java.lang.String r0 = "1"
            com.uc.webview.export.internal.uc.startup.b.a((int) r4, (java.lang.String) r0)
            goto L_0x0164
        L_0x0104:
            boolean r4 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r8)
            if (r4 != 0) goto L_0x0131
            com.uc.webview.export.internal.setup.az r1 = new com.uc.webview.export.internal.setup.az
            r1.<init>()
            java.lang.String r4 = com.uc.webview.export.internal.setup.ah.a((java.lang.String) r8)
            com.uc.webview.export.internal.setup.l r1 = r11.a((com.uc.webview.export.internal.setup.l) r1, (java.lang.String) r4)
            java.lang.String r4 = "soFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r1 = r1.setup((java.lang.String) r4, (java.lang.Object) r5)
            com.uc.webview.export.internal.setup.l r1 = (com.uc.webview.export.internal.setup.l) r1
            java.lang.String r4 = "resFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r1 = r1.setup((java.lang.String) r4, (java.lang.Object) r6)
            com.uc.webview.export.internal.setup.l r1 = (com.uc.webview.export.internal.setup.l) r1
            java.lang.String r4 = "ucmKrlDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r1.setup((java.lang.String) r4, (java.lang.Object) r0)
            r1 = r0
            com.uc.webview.export.internal.setup.l r1 = (com.uc.webview.export.internal.setup.l) r1
            goto L_0x0164
        L_0x0131:
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r7)
            if (r0 != 0) goto L_0x0164
            com.uc.webview.export.internal.setup.b r0 = new com.uc.webview.export.internal.setup.b
            r0.<init>()
            com.uc.webview.export.internal.setup.l r0 = r11.a((com.uc.webview.export.internal.setup.l) r0, (java.lang.String) r7)
            java.lang.String r1 = "ucmZipFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r1, (java.lang.Object) r7)
            r1 = r0
            com.uc.webview.export.internal.setup.l r1 = (com.uc.webview.export.internal.setup.l) r1
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r4 = "first_use_sw"
            java.lang.Object r0 = r0.get(r4)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r0)
            if (r0 == 0) goto L_0x0164
            r11.f = r1
            com.uc.webview.export.internal.setup.ay r0 = new com.uc.webview.export.internal.setup.ay
            r0.<init>()
            com.uc.webview.export.internal.setup.l r1 = r11.b((com.uc.webview.export.internal.setup.l) r0)
        L_0x0164:
            android.content.Context r0 = r11.g
            java.util.concurrent.ConcurrentHashMap r4 = r11.mOptions
            boolean r0 = com.uc.webview.export.internal.utility.h.a((android.content.Context) r0, (java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r4)
            if (r0 == 0) goto L_0x0175
            com.uc.webview.export.internal.setup.l r0 = r11.a((com.uc.webview.export.internal.setup.l) r1, (boolean) r3)
            if (r0 == 0) goto L_0x0175
            goto L_0x0176
        L_0x0175:
            r0 = r1
        L_0x0176:
            r1 = 315(0x13b, float:4.41E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r1)
            r1 = 291(0x123, float:4.08E-43)
            if (r0 == 0) goto L_0x0186
            com.uc.webview.export.internal.uc.startup.b.a(r1)
            r0.start()
            goto L_0x019b
        L_0x0186:
            com.uc.webview.export.internal.setup.l r0 = r11.d
            if (r0 == 0) goto L_0x01a2
            com.uc.webview.export.internal.uc.startup.b.a(r1)
            com.uc.webview.export.internal.setup.az r0 = new com.uc.webview.export.internal.setup.az
            r0.<init>()
            java.lang.String r1 = ""
            com.uc.webview.export.internal.setup.l r0 = r11.a((com.uc.webview.export.internal.setup.l) r0, (java.lang.String) r1)
            r0.start()
        L_0x019b:
            java.lang.String r0 = "Thick SDK"
            com.uc.webview.export.internal.SDKFactory.c((java.lang.String) r0)
            goto L_0x0361
        L_0x01a2:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            java.lang.String r1 = "At least 1 of OPTION_SO_FILE_PATH |OPTION_UCM_KRL_DIR |OPTION_UCM_UPD_URL  should be given."
            r0.<init>((int) r2, (java.lang.String) r1)
            throw r0
        L_0x01aa:
            r0 = 288(0x120, float:4.04E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            r0 = 141(0x8d, float:1.98E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            java.lang.String r0 = "csc_ndft"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r3 = "dexFilePath"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            android.content.Context r3 = r11.g
            java.util.concurrent.ConcurrentHashMap r5 = r11.mOptions
            boolean r3 = com.uc.webview.export.internal.utility.h.b((android.content.Context) r3, (java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r5)
            if (r3 == 0) goto L_0x01f1
            com.uc.webview.export.internal.setup.bf r0 = new com.uc.webview.export.internal.setup.bf
            r0.<init>()
            com.uc.webview.export.internal.setup.br r3 = com.uc.webview.export.internal.utility.h.a()
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setUCM(r3)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            com.uc.webview.export.internal.setup.br r3 = com.uc.webview.export.internal.utility.h.a()
            android.util.Pair<java.lang.String, java.lang.String> r3 = r3.coreImplModule
            java.lang.Object r3 = r3.second
            java.lang.String r3 = (java.lang.String) r3
            com.uc.webview.export.internal.setup.l r0 = r11.a((com.uc.webview.export.internal.setup.l) r0, (java.lang.String) r3)
            java.lang.String r3 = "1"
            com.uc.webview.export.internal.uc.startup.b.a((int) r4, (java.lang.String) r3)
            goto L_0x02d1
        L_0x01f1:
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r3 != 0) goto L_0x0226
            com.uc.webview.export.internal.setup.bf r3 = new com.uc.webview.export.internal.setup.bf
            r3.<init>()
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r3, (java.lang.String) r0)
            java.lang.String r4 = "dexFilePath"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r3.setup((java.lang.String) r4, (java.lang.Object) r0)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r3 = "soFilePath"
            java.lang.String r4 = "soFilePath"
            java.lang.Object r4 = r11.getOption(r4)
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r3, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.lang.String r3 = "resFilePath"
            java.lang.String r4 = "resFilePath"
            java.lang.Object r4 = r11.getOption(r4)
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r3, (java.lang.Object) r4)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            goto L_0x02d1
        L_0x0226:
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r3 = "ucmZipFile"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r3 != 0) goto L_0x0263
            com.uc.webview.export.internal.setup.b r3 = new com.uc.webview.export.internal.setup.b
            r3.<init>()
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r3, (java.lang.String) r0)
            java.lang.String r4 = "ucmZipFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r3.setup((java.lang.String) r4, (java.lang.Object) r0)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            java.util.concurrent.ConcurrentHashMap r3 = r11.mOptions
            java.lang.String r4 = "first_use_sw"
            java.lang.Object r3 = r3.get(r4)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r3)
            if (r3 == 0) goto L_0x02d1
            r11.f = r0
            com.uc.webview.export.internal.setup.ay r0 = new com.uc.webview.export.internal.setup.ay
            r0.<init>()
            com.uc.webview.export.internal.setup.l r0 = r11.b((com.uc.webview.export.internal.setup.l) r0)
            goto L_0x02d1
        L_0x0263:
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r3 = "ucmLibDir"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r3 != 0) goto L_0x028c
            java.util.concurrent.ConcurrentHashMap r3 = r11.mOptions
            java.lang.String r4 = "forbid_repair"
            r3.get(r4)
            com.uc.webview.export.internal.setup.bf r3 = new com.uc.webview.export.internal.setup.bf
            r3.<init>()
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r3, (java.lang.String) r0)
            java.lang.String r4 = "ucmLibDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r3.setup((java.lang.String) r4, (java.lang.Object) r0)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            goto L_0x02d1
        L_0x028c:
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r3 = "ucmKrlDir"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r3 != 0) goto L_0x02ae
            com.uc.webview.export.internal.setup.bf r3 = new com.uc.webview.export.internal.setup.bf
            r3.<init>()
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r3, (java.lang.String) r0)
            java.lang.String r4 = "ucmKrlDir"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r3.setup((java.lang.String) r4, (java.lang.Object) r0)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            goto L_0x02d1
        L_0x02ae:
            java.util.concurrent.ConcurrentHashMap r0 = r11.mOptions
            java.lang.String r3 = "ucmCfgFile"
            java.lang.Object r0 = r0.get(r3)
            java.lang.String r0 = (java.lang.String) r0
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)
            if (r3 != 0) goto L_0x02d0
            com.uc.webview.export.internal.setup.bf r3 = new com.uc.webview.export.internal.setup.bf
            r3.<init>()
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r3, (java.lang.String) r0)
            java.lang.String r4 = "ucmCfgFile"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r3.setup((java.lang.String) r4, (java.lang.Object) r0)
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0
            goto L_0x02d1
        L_0x02d0:
            r0 = r1
        L_0x02d1:
            android.content.Context r3 = r11.g
            java.util.concurrent.ConcurrentHashMap r4 = r11.mOptions
            boolean r3 = com.uc.webview.export.internal.utility.h.a((android.content.Context) r3, (java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r4)
            if (r3 == 0) goto L_0x02ee
            java.lang.String r3 = "skip_old_extra_kernel"
            java.lang.Object r3 = r11.getOption(r3)
            java.lang.Boolean r3 = (java.lang.Boolean) r3
            boolean r3 = com.uc.webview.export.internal.utility.k.a((java.lang.Boolean) r3)
            com.uc.webview.export.internal.setup.l r3 = r11.a((com.uc.webview.export.internal.setup.l) r0, (boolean) r3)
            if (r3 == 0) goto L_0x02ee
            r0 = r3
        L_0x02ee:
            r3 = 142(0x8e, float:1.99E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r3)
            java.lang.String r3 = "sc_ldpl"
            java.lang.String r3 = com.uc.webview.export.extension.UCCore.getParam(r3)
            java.lang.String r4 = "sc_lshco"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L_0x0305
            com.uc.webview.export.internal.setup.l r1 = r11.e()
        L_0x0305:
            java.lang.String r3 = "SdkSetupTask"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "mUpdateTask: "
            r4.<init>(r5)
            com.uc.webview.export.internal.setup.l r5 = r11.d
            r4.append(r5)
            java.lang.String r5 = " shareCoreTask: "
            r4.append(r5)
            r4.append(r1)
            java.lang.String r4 = r4.toString()
            com.uc.webview.export.internal.utility.Log.d(r3, r4)
            r3 = 289(0x121, float:4.05E-43)
            com.uc.webview.export.internal.uc.startup.b.a(r3)
            r3 = 290(0x122, float:4.06E-43)
            if (r0 == 0) goto L_0x0340
            com.uc.webview.export.internal.setup.l r2 = r11.d
            if (r2 == 0) goto L_0x0339
            if (r1 == 0) goto L_0x0339
            r11.a((com.uc.webview.export.internal.setup.l) r1)
            java.util.Stack<com.uc.webview.export.internal.setup.UCSetupTask> r2 = c
            r2.push(r1)
        L_0x0339:
            com.uc.webview.export.internal.uc.startup.b.a(r3)
            r0.start()
            goto L_0x0361
        L_0x0340:
            com.uc.webview.export.internal.setup.l r0 = r11.d
            if (r0 == 0) goto L_0x0356
            com.uc.webview.export.internal.uc.startup.b.a(r3)
            com.uc.webview.export.internal.setup.bf r0 = new com.uc.webview.export.internal.setup.bf
            r0.<init>()
            java.lang.String r1 = ""
            com.uc.webview.export.internal.setup.l r0 = r11.a((com.uc.webview.export.internal.setup.l) r0, (java.lang.String) r1)
            r0.start()
            goto L_0x0361
        L_0x0356:
            if (r1 == 0) goto L_0x0366
            r11.a((com.uc.webview.export.internal.setup.l) r1)
            com.uc.webview.export.internal.uc.startup.b.a(r3)
            r1.start()
        L_0x0361:
            r0 = 6
            com.uc.webview.export.internal.uc.startup.b.a(r0)
            return
        L_0x0366:
            com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException
            java.lang.String r1 = "At least 1 of OPTION_DEX_FILE_PATH|OPTION_UCM_LIB_DIR|OPTION_UCM_KRL_DIR|OPTION_UCM_CFG_FILE|OPTION_UCM_UPD_URL and CD_KEY_SHARE_CORE_CLIENT_LOAD_POLICY should be given."
            r0.<init>((int) r2, (java.lang.String) r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.o.run():void");
    }

    public final az b() {
        if (this.u == null) {
            synchronized (this.t) {
                if (this.u == null) {
                    this.u = new az();
                }
            }
        }
        return this.u;
    }

    private void a(l lVar) {
        l lVar2;
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_FAULT_TOLERANCE_TASK);
        String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_KRL_DIR);
        String param2 = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_ZIP_FPATH);
        if (!k.a(param) || !k.a(param2)) {
            lVar2 = a((l) new an(), "ShareCoreFaultToleranceTask");
        } else {
            lVar2 = null;
        }
        if (lVar2 != null) {
            c.push(lVar2);
            lVar.onEvent(UCCore.EVENT_DELAY_SEARCH_CORE_FILE, new z(this));
        }
    }

    static /* synthetic */ void a(o oVar, UCMRunningInfo uCMRunningInfo) {
        oVar.setLoadedUCM(uCMRunningInfo);
        oVar.setTotalLoadedUCM(uCMRunningInfo);
        SDKFactory.h = uCMRunningInfo.loadType;
        Log.d("SdkSetupTask", "initLoadUcm sLoadType: " + SDKFactory.h + ", isShareCore:" + uCMRunningInfo.isShareCore);
        if (uCMRunningInfo.isShareCore) {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_SUCCESS);
        }
        if (uCMRunningInfo.isOldExtraKernel) {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_OLD_KERNAL_SETUP_SUCCESS);
        }
        if (uCMRunningInfo.isFirstTimeOdex) {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_FIRST_KERNAL_SETUP_SUCCESS);
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:80|81|82|83|84|85|86|(2:87|88)) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:84:0x01d8 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:87:0x01e3 */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x015b A[Catch:{ Throwable -> 0x0189 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static /* synthetic */ void b(com.uc.webview.export.internal.setup.o r17, com.uc.webview.export.internal.setup.UCMRunningInfo r18) {
        /*
            r1 = r17
            r2 = r18
            java.lang.String r0 = "csc_ssctp"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            java.lang.String r0 = "d"
            java.lang.String r3 = "SdkSetupTask"
            com.uc.webview.export.cyclone.UCLogger r3 = com.uc.webview.export.cyclone.UCLogger.create(r0, r3)     // Catch:{ Throwable -> 0x0520 }
            r4 = 2
            r5 = 1
            r6 = 0
            if (r3 == 0) goto L_0x0050
            java.lang.String r0 = "mSuccessCB: dataDir is [%s] core type: [%d] isShareCore{%b}."
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r9 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.br r9 = r9.ucmPackageInfo     // Catch:{ Throwable -> 0x0520 }
            if (r9 == 0) goto L_0x002c
            com.uc.webview.export.internal.setup.UCMRunningInfo r9 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.br r9 = r9.ucmPackageInfo     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r9 = r9.dataDir     // Catch:{ Throwable -> 0x0520 }
            goto L_0x002d
        L_0x002c:
            r9 = 0
        L_0x002d:
            r8[r6] = r9     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r9 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0520 }
            int r9 = r9.coreType     // Catch:{ Throwable -> 0x0520 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r9)     // Catch:{ Throwable -> 0x0520 }
            r8[r5] = r9     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r9 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0520 }
            boolean r9 = r9.isShareCore     // Catch:{ Throwable -> 0x0520 }
            java.lang.Boolean r9 = java.lang.Boolean.valueOf(r9)     // Catch:{ Throwable -> 0x0520 }
            r8[r4] = r9     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r0 = java.lang.String.format(r0, r8)     // Catch:{ Throwable -> 0x0520 }
            java.lang.Throwable[] r8 = new java.lang.Throwable[r6]     // Catch:{ Throwable -> 0x0520 }
            r3.print(r0, r8)     // Catch:{ Throwable -> 0x0520 }
        L_0x0050:
            boolean r0 = com.uc.webview.export.internal.setup.af.b()     // Catch:{ Throwable -> 0x0520 }
            if (r0 == 0) goto L_0x0070
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.br r0 = r0.ucmPackageInfo     // Catch:{ Throwable -> 0x0520 }
            if (r0 == 0) goto L_0x0070
            android.content.Context r8 = r1.g     // Catch:{ Throwable -> 0x0520 }
            if (r8 == 0) goto L_0x0070
            java.util.concurrent.ConcurrentHashMap r8 = r1.mOptions     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.utility.h.a((com.uc.webview.export.internal.setup.br) r0, (java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Object>) r8)     // Catch:{ Throwable -> 0x0520 }
            android.content.Context r8 = r1.g     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r8 = com.uc.webview.export.internal.utility.h.a((android.content.Context) r8)     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.utility.h.a((com.uc.webview.export.internal.setup.br) r0, (java.lang.String) r8)     // Catch:{ Throwable -> 0x0520 }
        L_0x0070:
            java.lang.String r0 = "csc_sscip"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0083 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0083 }
            int r0 = r0.coreType     // Catch:{ Throwable -> 0x0083 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x0083 }
            r1.callbackFinishStat(r0)     // Catch:{ Throwable -> 0x0083 }
            goto L_0x0087
        L_0x0083:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x0087:
            java.lang.String r0 = "ucmUpdUrl"
            java.lang.Object r0 = r1.getOption(r0)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Throwable -> 0x0189 }
            if (r0 == 0) goto L_0x018d
            android.content.Context r8 = r1.g     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r9 = "flags"
            java.io.File r8 = com.uc.webview.export.internal.utility.k.a((android.content.Context) r8, (java.lang.String) r9)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r9 = "uc_upd"
            java.io.File r8 = com.uc.webview.export.internal.utility.k.b((java.io.File) r8, (java.lang.String) r9)     // Catch:{ Throwable -> 0x0189 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0189 }
            r9.<init>()     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r10 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r0)     // Catch:{ Throwable -> 0x0189 }
            r9.append(r10)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r10 = "_frun"
            r9.append(r10)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0189 }
            java.io.File r10 = new java.io.File     // Catch:{ Throwable -> 0x0189 }
            r10.<init>(r8, r9)     // Catch:{ Throwable -> 0x0189 }
            boolean r9 = r10.exists()     // Catch:{ Throwable -> 0x0189 }
            if (r9 != 0) goto L_0x00ce
            boolean r9 = r10.createNewFile()     // Catch:{ Throwable -> 0x0189 }
            if (r9 == 0) goto L_0x00c6
            goto L_0x00ce
        L_0x00c6:
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r2 = "createNewFile firstTimeRunFlagFile failed"
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0189 }
            throw r0     // Catch:{ Throwable -> 0x0189 }
        L_0x00ce:
            int r9 = r2.coreType     // Catch:{ Throwable -> 0x0189 }
            r11 = 0
            if (r9 == r4) goto L_0x014b
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0189 }
            r9.<init>()     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r13 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r0)     // Catch:{ Throwable -> 0x0189 }
            r9.append(r13)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r13 = "_ucrun"
            r9.append(r13)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r9 = r9.toString()     // Catch:{ Throwable -> 0x0189 }
            java.io.File r13 = new java.io.File     // Catch:{ Throwable -> 0x0189 }
            r13.<init>(r8, r9)     // Catch:{ Throwable -> 0x0189 }
            boolean r9 = r13.exists()     // Catch:{ Throwable -> 0x0189 }
            if (r9 != 0) goto L_0x0103
            boolean r9 = r13.createNewFile()     // Catch:{ Throwable -> 0x0189 }
            if (r9 == 0) goto L_0x00fb
            goto L_0x0103
        L_0x00fb:
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r2 = "createNewFile ucrunFlagFile failed"
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0189 }
            throw r0     // Catch:{ Throwable -> 0x0189 }
        L_0x0103:
            long r13 = r13.lastModified()     // Catch:{ Throwable -> 0x0189 }
            long r15 = r10.lastModified()     // Catch:{ Throwable -> 0x0189 }
            r9 = 0
            long r13 = r13 - r15
            boolean r2 = r2.isOldExtraKernel     // Catch:{ Throwable -> 0x0189 }
            if (r2 != 0) goto L_0x0156
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0189 }
            r2.<init>()     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r0 = com.uc.webview.export.cyclone.UCCyclone.getSourceHash(r0)     // Catch:{ Throwable -> 0x0189 }
            r2.append(r0)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r0 = "_curucrun"
            r2.append(r0)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r0 = r2.toString()     // Catch:{ Throwable -> 0x0189 }
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x0189 }
            r2.<init>(r8, r0)     // Catch:{ Throwable -> 0x0189 }
            boolean r0 = r2.exists()     // Catch:{ Throwable -> 0x0189 }
            if (r0 != 0) goto L_0x0140
            boolean r0 = r2.createNewFile()     // Catch:{ Throwable -> 0x0189 }
            if (r0 == 0) goto L_0x0138
            goto L_0x0140
        L_0x0138:
            java.lang.Exception r0 = new java.lang.Exception     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r2 = "createNewFile currentUcRunFlagFile failed"
            r0.<init>(r2)     // Catch:{ Throwable -> 0x0189 }
            throw r0     // Catch:{ Throwable -> 0x0189 }
        L_0x0140:
            long r8 = r2.lastModified()     // Catch:{ Throwable -> 0x0189 }
            long r15 = r10.lastModified()     // Catch:{ Throwable -> 0x0189 }
            r0 = 0
            long r8 = r8 - r15
            goto L_0x0157
        L_0x014b:
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0189 }
            long r13 = r10.lastModified()     // Catch:{ Throwable -> 0x0189 }
            r0 = 0
            long r13 = r8 - r13
        L_0x0156:
            r8 = r11
        L_0x0157:
            int r0 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1))
            if (r0 != 0) goto L_0x0165
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x0189 }
            long r10 = r10.lastModified()     // Catch:{ Throwable -> 0x0189 }
            r0 = 0
            long r8 = r8 - r10
        L_0x0165:
            java.lang.String r0 = "stp_uc_hour"
            r10 = 3600000(0x36ee80, double:1.7786363E-317)
            long r13 = r13 / r10
            double r12 = (double) r13     // Catch:{ Throwable -> 0x0189 }
            double r12 = java.lang.Math.ceil(r12)     // Catch:{ Throwable -> 0x0189 }
            int r2 = (int) r12     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0189 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r2)     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r0 = "stp_curuc_hour"
            long r8 = r8 / r10
            double r8 = (double) r8     // Catch:{ Throwable -> 0x0189 }
            double r8 = java.lang.Math.ceil(r8)     // Catch:{ Throwable -> 0x0189 }
            int r2 = (int) r8     // Catch:{ Throwable -> 0x0189 }
            java.lang.String r2 = java.lang.String.valueOf(r2)     // Catch:{ Throwable -> 0x0189 }
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r2)     // Catch:{ Throwable -> 0x0189 }
            goto L_0x018d
        L_0x0189:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x018d:
            java.lang.String r0 = "sdk_ucm_old"
            com.uc.webview.export.internal.setup.UCMRunningInfo r2 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x01a0 }
            boolean r2 = r2.isOldExtraKernel     // Catch:{ Throwable -> 0x01a0 }
            if (r2 == 0) goto L_0x019a
            java.lang.String r2 = "1"
            goto L_0x019c
        L_0x019a:
            java.lang.String r2 = "0"
        L_0x019c:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r2)     // Catch:{ Throwable -> 0x01a0 }
            goto L_0x01a4
        L_0x01a0:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x01a4:
            java.lang.String r0 = ""
            java.lang.String r2 = ""
            java.lang.String r8 = ""
            java.lang.String r9 = ""
            java.lang.String r10 = ""
            com.uc.webview.export.internal.setup.UCMRunningInfo r11 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            int r11 = r11.coreType     // Catch:{ Throwable -> 0x0349 }
            if (r11 != r4) goto L_0x01bc
            com.uc.webview.export.internal.setup.UCSetupException r4 = r1.i     // Catch:{ Throwable -> 0x0349 }
            if (r4 == 0) goto L_0x01bc
            r4 = 1
            goto L_0x01bd
        L_0x01bc:
            r4 = 0
        L_0x01bd:
            if (r4 == 0) goto L_0x01f3
            com.uc.webview.export.internal.setup.UCSetupException r0 = r1.i     // Catch:{ Throwable -> 0x0349 }
            int r0 = r0.errCode()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r0 = java.lang.String.valueOf(r0)     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.internal.setup.UCSetupException r9 = r1.i     // Catch:{ Throwable -> 0x01d8 }
            java.lang.Throwable r9 = r9.getRootCause()     // Catch:{ Throwable -> 0x01d8 }
            java.lang.Class r9 = r9.getClass()     // Catch:{ Throwable -> 0x01d8 }
            java.lang.String r9 = r9.getSimpleName()     // Catch:{ Throwable -> 0x01d8 }
            r2 = r9
        L_0x01d8:
            com.uc.webview.export.internal.setup.UCSetupException r9 = r1.i     // Catch:{ Throwable -> 0x01e3 }
            java.lang.Throwable r9 = r9.getRootCause()     // Catch:{ Throwable -> 0x01e3 }
            java.lang.String r9 = com.uc.webview.export.internal.utility.k.a((java.lang.Throwable) r9)     // Catch:{ Throwable -> 0x01e3 }
            r8 = r9
        L_0x01e3:
            com.uc.webview.export.internal.setup.UCSetupTask r9 = r1.j     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r9 = r9.getCrashCode()     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.internal.setup.UCSetupTask r10 = r1.j     // Catch:{ Throwable -> 0x0349 }
            java.lang.Class r10 = r10.getClass()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r10 = r10.getSimpleName()     // Catch:{ Throwable -> 0x0349 }
        L_0x01f3:
            java.lang.String r11 = "setup_priority"
            java.lang.Object r11 = r1.getOption(r11)     // Catch:{ Throwable -> 0x0349 }
            java.lang.Integer r11 = (java.lang.Integer) r11     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r12 = "dlChecker"
            java.lang.Object r12 = r1.getOption(r12)     // Catch:{ Throwable -> 0x021a }
            java.util.concurrent.Callable r12 = (java.util.concurrent.Callable) r12     // Catch:{ Throwable -> 0x021a }
            if (r12 != 0) goto L_0x0208
            java.lang.String r12 = "N"
            goto L_0x021c
        L_0x0208:
            java.lang.Object r12 = r12.call()     // Catch:{ Throwable -> 0x021a }
            java.lang.Boolean r12 = (java.lang.Boolean) r12     // Catch:{ Throwable -> 0x021a }
            boolean r12 = r12.booleanValue()     // Catch:{ Throwable -> 0x021a }
            if (r12 == 0) goto L_0x0217
            java.lang.String r12 = "T"
            goto L_0x021c
        L_0x0217:
            java.lang.String r12 = "F"
            goto L_0x021c
        L_0x021a:
            java.lang.String r12 = "E"
        L_0x021c:
            com.uc.webview.export.cyclone.UCHashMap r13 = new com.uc.webview.export.cyclone.UCHashMap     // Catch:{ Throwable -> 0x0349 }
            r13.<init>()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r14 = "cnt"
            java.lang.String r15 = "1"
            com.uc.webview.export.cyclone.UCHashMap r13 = r13.set(r14, r15)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r14 = "code"
            com.uc.webview.export.internal.setup.UCMRunningInfo r15 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            int r15 = r15.coreType     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r15 = java.lang.String.valueOf(r15)     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.cyclone.UCHashMap r13 = r13.set(r14, r15)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r14 = "dir"
            com.uc.webview.export.internal.setup.UCMRunningInfo r15 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.internal.setup.br r15 = r15.ucmPackageInfo     // Catch:{ Throwable -> 0x0349 }
            if (r15 != 0) goto L_0x0246
            java.lang.String r15 = "null"
            goto L_0x0252
        L_0x0246:
            com.uc.webview.export.internal.setup.UCMRunningInfo r15 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.internal.setup.br r15 = r15.ucmPackageInfo     // Catch:{ Throwable -> 0x0349 }
            android.content.Context r7 = r1.g     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r15 = r15.getDirAlias(r7)     // Catch:{ Throwable -> 0x0349 }
        L_0x0252:
            com.uc.webview.export.cyclone.UCHashMap r7 = r13.set(r14, r15)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "old"
            com.uc.webview.export.internal.setup.UCMRunningInfo r14 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            boolean r14 = r14.isOldExtraKernel     // Catch:{ Throwable -> 0x0349 }
            if (r14 == 0) goto L_0x0263
            java.lang.String r14 = "T"
            goto L_0x0265
        L_0x0263:
            java.lang.String r14 = "F"
        L_0x0265:
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "frun"
            com.uc.webview.export.internal.setup.UCMRunningInfo r14 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            boolean r14 = r14.isFirstTimeOdex     // Catch:{ Throwable -> 0x0349 }
            if (r14 == 0) goto L_0x0276
            java.lang.String r14 = "T"
            goto L_0x0278
        L_0x0276:
            java.lang.String r14 = "F"
        L_0x0278:
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "cpu_cnt"
            java.lang.String r14 = com.uc.webview.export.internal.utility.k.a()     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "cpu_freq"
            java.lang.String r14 = com.uc.webview.export.internal.utility.k.b()     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "cost_cpu"
            com.uc.webview.export.cyclone.UCElapseTime r14 = r1.h     // Catch:{ Throwable -> 0x0349 }
            long r14 = r14.getMilisCpu()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r14 = java.lang.String.valueOf(r14)     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "cost"
            com.uc.webview.export.cyclone.UCElapseTime r14 = r1.h     // Catch:{ Throwable -> 0x0349 }
            long r14 = r14.getMilis()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r14 = java.lang.String.valueOf(r14)     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "pri"
            if (r11 != 0) goto L_0x02b7
            java.lang.String r11 = "n"
            goto L_0x02bb
        L_0x02b7:
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch:{ Throwable -> 0x0349 }
        L_0x02bb:
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r11)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r11 = "wifi"
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r11, r12)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r11 = "csc_tsu"
            com.uc.webview.export.internal.setup.UCMRunningInfo r12 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            boolean r12 = r12.isShareCore     // Catch:{ Throwable -> 0x0349 }
            if (r12 == 0) goto L_0x02d2
            java.lang.String r12 = "csc_tis"
            goto L_0x02d4
        L_0x02d2:
            java.lang.String r12 = "csc_tns"
        L_0x02d4:
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r11, r12)     // Catch:{ Throwable -> 0x0349 }
            android.util.Pair r11 = new android.util.Pair     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r12 = "sdk_stp_suc"
            if (r4 == 0) goto L_0x031b
            java.lang.String r13 = "multi_core"
            com.uc.webview.export.internal.utility.d r14 = com.uc.webview.export.internal.utility.d.a()     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r15 = "MULTI_CORE_TYPE"
            boolean r14 = r14.b(r15)     // Catch:{ Throwable -> 0x0349 }
            if (r14 == 0) goto L_0x02ef
            java.lang.String r14 = "1"
            goto L_0x02f1
        L_0x02ef:
            java.lang.String r14 = "0"
        L_0x02f1:
            com.uc.webview.export.cyclone.UCHashMap r7 = r7.set(r13, r14)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r13 = "err"
            com.uc.webview.export.cyclone.UCHashMap r0 = r7.set(r13, r0)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r7 = "cls"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r7, r2)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r2 = "msg"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r2, r8)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r2 = "crash"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r2, r9)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r2 = "task"
            com.uc.webview.export.cyclone.UCHashMap r0 = r0.set(r2, r10)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r2 = "csc_tsu"
            java.lang.String r7 = "csc_tfi"
            com.uc.webview.export.cyclone.UCHashMap r7 = r0.set(r2, r7)     // Catch:{ Throwable -> 0x0349 }
        L_0x031b:
            r11.<init>(r12, r7)     // Catch:{ Throwable -> 0x0349 }
            r1.callbackStat(r11)     // Catch:{ Throwable -> 0x0349 }
            java.lang.String r0 = "sdk_ucm_old"
            com.uc.webview.export.internal.setup.UCMRunningInfo r2 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            boolean r2 = r2.isOldExtraKernel     // Catch:{ Throwable -> 0x0349 }
            if (r2 == 0) goto L_0x032e
            java.lang.String r2 = "1"
            goto L_0x0330
        L_0x032e:
            java.lang.String r2 = "0"
        L_0x0330:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r2)     // Catch:{ Throwable -> 0x0349 }
            com.uc.webview.export.internal.setup.UCMRunningInfo r0 = getTotalLoadedUCM()     // Catch:{ Throwable -> 0x0349 }
            boolean r0 = r0.isShareCore     // Catch:{ Throwable -> 0x0349 }
            if (r0 == 0) goto L_0x034d
            if (r4 == 0) goto L_0x0343
            java.lang.String r0 = "csc_nsifp"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0349 }
            goto L_0x034d
        L_0x0343:
            java.lang.String r0 = "csc_nsisp"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x0349 }
            goto L_0x034d
        L_0x0349:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x034d:
            r17.f()     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.setup.n r0 = new com.uc.webview.export.internal.setup.n     // Catch:{ Throwable -> 0x0374 }
            r0.<init>()     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.cyclone.UCCyclone.statCallback = r0     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r2)     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x0374 }
            java.lang.String r2 = "stat"
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r4 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a     // Catch:{ Throwable -> 0x0374 }
            r4.<init>()     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r2, r4)     // Catch:{ Throwable -> 0x0374 }
            com.uc.webview.export.internal.setup.n r0 = (com.uc.webview.export.internal.setup.n) r0     // Catch:{ Throwable -> 0x0374 }
            r0.start()     // Catch:{ Throwable -> 0x0374 }
            goto L_0x0378
        L_0x0374:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x0378:
            java.lang.String r0 = "vmsize_saving"
            java.lang.Object r0 = r1.getOption(r0)     // Catch:{ Throwable -> 0x03c9 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x03c9 }
            if (r0 == 0) goto L_0x038a
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x03c9 }
            if (r0 == 0) goto L_0x038a
            r0 = 1
            goto L_0x038b
        L_0x038a:
            r0 = 0
        L_0x038b:
            com.uc.webview.export.internal.setup.s r2 = new com.uc.webview.export.internal.setup.s     // Catch:{ Throwable -> 0x03c9 }
            r2.<init>(r1, r0)     // Catch:{ Throwable -> 0x03c9 }
            com.uc.webview.export.internal.utility.k.a((java.util.Map<java.lang.String, java.lang.String>) r2)     // Catch:{ Throwable -> 0x03c9 }
            if (r3 == 0) goto L_0x03af
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x03c9 }
            java.lang.String r4 = "mSuccessCB: vmsize_saving_enable="
            r2.<init>(r4)     // Catch:{ Throwable -> 0x03c9 }
            if (r0 == 0) goto L_0x03a1
            java.lang.String r4 = "true"
            goto L_0x03a3
        L_0x03a1:
            java.lang.String r4 = "false"
        L_0x03a3:
            r2.append(r4)     // Catch:{ Throwable -> 0x03c9 }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x03c9 }
            java.lang.Throwable[] r4 = new java.lang.Throwable[r6]     // Catch:{ Throwable -> 0x03c9 }
            r3.print(r2, r4)     // Catch:{ Throwable -> 0x03c9 }
        L_0x03af:
            if (r0 == 0) goto L_0x03e1
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = new com.uc.webview.export.internal.setup.UCAsyncTask     // Catch:{ Throwable -> 0x03c9 }
            com.uc.webview.export.cyclone.UCVmsize r2 = new com.uc.webview.export.cyclone.UCVmsize     // Catch:{ Throwable -> 0x03c9 }
            android.content.Context r3 = r1.g     // Catch:{ Throwable -> 0x03c9 }
            r2.<init>(r3)     // Catch:{ Throwable -> 0x03c9 }
            r0.<init>((java.lang.Runnable) r2)     // Catch:{ Throwable -> 0x03c9 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x03c9 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r2)     // Catch:{ Throwable -> 0x03c9 }
            r0.start()     // Catch:{ Throwable -> 0x03c9 }
            goto L_0x03e1
        L_0x03c9:
            r0 = move-exception
            java.lang.String r2 = "SdkSetupTask"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r4 = "successCallbackImpl "
            r3.<init>(r4)     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r0 = r0.getMessage()     // Catch:{ Throwable -> 0x0520 }
            r3.append(r0)     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r0 = r3.toString()     // Catch:{ Throwable -> 0x0520 }
            com.uc.webview.export.internal.utility.Log.i(r2, r0)     // Catch:{ Throwable -> 0x0520 }
        L_0x03e1:
            com.uc.webview.export.internal.setup.f r0 = new com.uc.webview.export.internal.setup.f     // Catch:{ Throwable -> 0x0431 }
            r0.<init>()     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r2)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            java.lang.String r2 = "CONTEXT"
            android.content.Context r3 = r1.g     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            java.lang.String r2 = "del_dec_fil"
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            java.lang.String r2 = "del_upd_fil"
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r6)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            java.lang.String r2 = "bo_dex_old_dex_dir"
            java.lang.String r3 = "bo_dex_old_dex_dir"
            java.lang.Object r3 = r1.getOption(r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setup(r2, r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            java.lang.String r2 = "die"
            com.uc.webview.export.internal.setup.t r3 = new com.uc.webview.export.internal.setup.t     // Catch:{ Throwable -> 0x0431 }
            r3.<init>(r1)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r2, r3)     // Catch:{ Throwable -> 0x0431 }
            com.uc.webview.export.internal.setup.f r0 = (com.uc.webview.export.internal.setup.f) r0     // Catch:{ Throwable -> 0x0431 }
            r0.start()     // Catch:{ Throwable -> 0x0431 }
            goto L_0x0435
        L_0x0431:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x0435:
            java.util.List<com.uc.webview.export.internal.setup.av> r0 = r1.m     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            if (r0 == 0) goto L_0x0462
            com.uc.webview.export.internal.setup.e r0 = new com.uc.webview.export.internal.setup.e     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            java.util.List<com.uc.webview.export.internal.setup.av> r2 = r1.m     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            r0.<init>(r2)     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r2)     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.e r0 = (com.uc.webview.export.internal.setup.e) r0     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            java.util.concurrent.ConcurrentHashMap r2 = r1.mOptions     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.UCSubSetupTask r0 = r0.setOptions(r2)     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.e r0 = (com.uc.webview.export.internal.setup.e) r0     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            java.lang.String r2 = "stat"
            com.uc.webview.export.internal.setup.UCSubSetupTask$a r3 = new com.uc.webview.export.internal.setup.UCSubSetupTask$a     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            r3.<init>()     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.onEvent(r2, r3)     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            com.uc.webview.export.internal.setup.e r0 = (com.uc.webview.export.internal.setup.e) r0     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
            r0.start()     // Catch:{ Throwable -> 0x046a, all -> 0x0466 }
        L_0x0462:
            r2 = 0
        L_0x0463:
            r1.m = r2     // Catch:{ Throwable -> 0x0520 }
            goto L_0x0470
        L_0x0466:
            r0 = move-exception
            r2 = 0
            goto L_0x051d
        L_0x046a:
            r0 = move-exception
            r2 = 0
            r0.printStackTrace()     // Catch:{ all -> 0x051c }
            goto L_0x0463
        L_0x0470:
            com.uc.webview.export.internal.setup.l r0 = r1.f     // Catch:{ Throwable -> 0x04a4 }
            if (r0 == 0) goto L_0x04a8
            com.uc.webview.export.internal.setup.l r0 = r1.f     // Catch:{ Throwable -> 0x04a4 }
            java.lang.String r2 = "chkMultiCore"
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r5)     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.setup((java.lang.String) r2, (java.lang.Object) r3)     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x04a4 }
            java.lang.String r2 = "success"
            r3 = 0
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r3)     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x04a4 }
            java.lang.String r2 = "exception"
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r3)     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x04a4 }
            java.lang.String r2 = "switch"
            android.webkit.ValueCallback<com.uc.webview.export.internal.setup.l> r3 = r1.s     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.BaseSetupTask r0 = r0.onEvent((java.lang.String) r2, r3)     // Catch:{ Throwable -> 0x04a4 }
            com.uc.webview.export.internal.setup.l r0 = (com.uc.webview.export.internal.setup.l) r0     // Catch:{ Throwable -> 0x04a4 }
            r0.start()     // Catch:{ Throwable -> 0x04a4 }
            r2 = 0
            r1.f = r2     // Catch:{ Throwable -> 0x04a4 }
            goto L_0x04a8
        L_0x04a4:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x04a8:
            r2 = 2000(0x7d0, double:9.88E-321)
            java.lang.String r0 = "sc_ustwm"
            java.lang.String r0 = com.uc.webview.export.extension.UCCore.getParam(r0)     // Catch:{ Throwable -> 0x04c8 }
            boolean r0 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x04c8 }
            if (r0 == 0) goto L_0x04cc
            com.uc.webview.export.internal.setup.l r0 = r1.e     // Catch:{ Throwable -> 0x04c8 }
            if (r0 == 0) goto L_0x04cc
            java.lang.String r0 = "csc_ddspv"
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)     // Catch:{ Throwable -> 0x04c8 }
            com.uc.webview.export.internal.setup.l r0 = r1.e     // Catch:{ Throwable -> 0x04c8 }
            r0.start(r2)     // Catch:{ Throwable -> 0x04c8 }
            r4 = 0
            r1.e = r4     // Catch:{ Throwable -> 0x04c8 }
            goto L_0x04cc
        L_0x04c8:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x04cc:
            getTotalLoadedUCM()     // Catch:{ Throwable -> 0x04d0 }
            goto L_0x04d5
        L_0x04d0:
            r0 = move-exception
            r4 = r0
            r4.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x04d5:
            com.uc.webview.export.internal.setup.l r0 = r1.d     // Catch:{ Throwable -> 0x04f7 }
            if (r0 == 0) goto L_0x04fb
            com.uc.webview.export.internal.setup.l r0 = r1.d     // Catch:{ Throwable -> 0x04f7 }
            r0.start(r2)     // Catch:{ Throwable -> 0x04f7 }
            r2 = 0
            r1.d = r2     // Catch:{ Throwable -> 0x04f7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = new com.uc.webview.export.internal.setup.UCAsyncTask     // Catch:{ Throwable -> 0x04f7 }
            com.uc.webview.export.cyclone.UCDex r2 = new com.uc.webview.export.cyclone.UCDex     // Catch:{ Throwable -> 0x04f7 }
            r2.<init>()     // Catch:{ Throwable -> 0x04f7 }
            r0.<init>((java.lang.Runnable) r2)     // Catch:{ Throwable -> 0x04f7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = com.uc.webview.export.utility.SetupTask.getRoot()     // Catch:{ Throwable -> 0x04f7 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.setParent(r2)     // Catch:{ Throwable -> 0x04f7 }
            r0.start()     // Catch:{ Throwable -> 0x04f7 }
            goto L_0x04fb
        L_0x04f7:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x04fb:
            java.util.concurrent.ConcurrentHashMap r0 = r1.mOptions     // Catch:{ Throwable -> 0x0520 }
            java.lang.String r2 = "distinguish_js_error"
            java.lang.Object r0 = r0.get(r2)     // Catch:{ Throwable -> 0x0520 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x0520 }
            if (r0 == 0) goto L_0x051b
            com.uc.webview.export.internal.interfaces.IGlobalSettings r2 = com.uc.webview.export.internal.SDKFactory.e()     // Catch:{ Throwable -> 0x0517 }
            if (r2 == 0) goto L_0x0516
            java.lang.String r3 = "DistinguishJSError"
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0517 }
            r2.setBoolValue(r3, r0)     // Catch:{ Throwable -> 0x0517 }
        L_0x0516:
            return
        L_0x0517:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Throwable -> 0x0520 }
        L_0x051b:
            return
        L_0x051c:
            r0 = move-exception
        L_0x051d:
            r1.m = r2     // Catch:{ Throwable -> 0x0520 }
            throw r0     // Catch:{ Throwable -> 0x0520 }
        L_0x0520:
            r0 = move-exception
            com.uc.webview.export.internal.setup.UCSetupException r2 = new com.uc.webview.export.internal.setup.UCSetupException
            r3 = 4004(0xfa4, float:5.611E-42)
            r2.<init>((int) r3, (java.lang.Throwable) r0)
            r1.setException(r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.o.b(com.uc.webview.export.internal.setup.o, com.uc.webview.export.internal.setup.UCMRunningInfo):void");
    }

    static /* synthetic */ l i(o oVar) {
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_CREATE_DELAY_SEARE_CORE_FILE_TASK_PV);
        l a2 = oVar.a((l) new ap(), "ShareCoreSearchCoreFileTask");
        a2.setParent(UCSetupTask.getRoot());
        return a2;
    }
}
