package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.content.SharedPreferences;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.k;
import com.uc.webview.export.utility.SetupTask;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/* compiled from: U4Source */
public class au extends UCSubSetupTask<au, au> {
    /* access modifiers changed from: private */
    public static final String a = "au";
    private static Object b = new Object();

    public void run() {
        Log.d(a, ".run");
        try {
            Context applicationContext = getContext().getApplicationContext();
            if (k.a((Boolean) getOption(UCCore.OPTION_ONLY_STAT_DEVICES_HAS_CORE_SHARE))) {
                synchronized (b) {
                    new a((byte) 0);
                    a.a(applicationContext, k.a((Boolean) getOption(UCCore.OPTION_CURRENT_IS_UC_CORE)), k.a((Boolean) getOption(UCCore.OPTION_HAS_UPDATE_SOURCE)), (Callable) getOption(UCCore.OPTION_DOWNLOAD_CHECKER));
                }
                return;
            }
            String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_HOST_COMPRESSED_CORE_FILE_PATH);
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_COPY_TASK_RUN_PV);
            synchronized (b) {
                if (!k.a(param)) {
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_COPY_TASK_RUN_CALL_PV);
                    new a((byte) 0);
                    a.a(applicationContext, param, UCCyclone.DecFileOrign.Other);
                }
                new a((byte) 0);
                a.a(applicationContext, k.a((Boolean) getOption(UCCore.OPTION_CURRENT_IS_UC_CORE)), k.a((Boolean) getOption(UCCore.OPTION_HAS_UPDATE_SOURCE)), (Callable) getOption(UCCore.OPTION_DOWNLOAD_CHECKER));
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void a(Context context, String str, String str2) {
        String str3 = a;
        Log.d(str3, ".shareDownloadFile(" + str + AVFSCacheConstants.COMMA_SEP + str2 + Operators.BRACKET_END_STR);
        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_COPY_TASK_UPD_PV);
        if (!k.a(str2)) {
            synchronized (b) {
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_COPY_TASK_UPD_CALL_PV);
                new a((byte) 0);
                a.a(context, str2, UCCyclone.DecFileOrign.Update);
            }
        }
    }

    /* compiled from: U4Source */
    static class a {
        private static long A = 0;
        private static long B = 0;
        private static long C = 0;
        private static long D = 0;
        private static long E = 0;
        private static long F = 0;
        private static long G = 0;
        private static long H = 0;
        private static long I = 0;
        private static long J = 0;
        private static long a = 1;
        private static long b = 2;
        private static long c = 4;
        private static long d = 8;
        private static long e = 16;
        private static long f = 32;
        private static long g = 64;
        private static long h = 128;
        private static long i = 256;
        private static long j = 512;
        private static long k = 1024;
        private static long l = 2048;
        private static long m = 4096;
        private static long n = 8192;
        private static long o = 16384;
        private static long p = 32768;
        private static long q;
        private static long r;
        private static long s;
        private static long t;
        private static long u;
        private static long v;
        private static long w;
        private static long x;
        private static long y;
        private static long z;

        private a() {
        }

        /* synthetic */ a(byte b2) {
            this();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:210:0x08eb, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:211:0x08ec, code lost:
            r20 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:212:0x08f0, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:213:0x08f1, code lost:
            r20 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:214:0x08f3, code lost:
            r26 = r2;
            r2 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:216:0x08fa, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:217:0x08fb, code lost:
            r20 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:219:?, code lost:
            r8 = m;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:221:?, code lost:
            com.uc.webview.export.internal.utility.Log.d(com.uc.webview.export.internal.setup.au.a(), ".shareCoreDecFile createAppSubFolder", r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:222:0x0908, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_PROCESS, java.lang.Long.toString(r8));
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_EXCEPIION, java.lang.Long.toString(r4));
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_AUTHORITY, java.lang.Long.toString(r2));
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_DELETE, java.lang.Long.toString(r10));
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_PRECONDITION, java.lang.Long.toString(r20));
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_TO_SDCARD_TASK_RESULT_COPY, java.lang.Long.toString(r12));
         */
        /* JADX WARNING: Code restructure failed: missing block: B:223:0x0942, code lost:
            if (r12 == B) goto L_0x0944;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:224:0x0944, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_SUCCESS_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:226:0x094e, code lost:
            if (r12 == G) goto L_0x0950;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:227:0x0950, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_HAS_EXISTS_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:228:0x0956, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_OTHER_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:229:0x095b, code lost:
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fProcessStat: " + r8);
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fProcessStatExp: " + r4);
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fSdcardAuthoryStat: " + r2);
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fDeleteStat: " + r10);
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fPreconditionStat: " + r20);
            r0 = com.uc.webview.export.internal.setup.au.a();
            com.uc.webview.export.internal.utility.Log.d(r0, ".shareCoreDecFile fCopyStat: " + r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:230:0x09db, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:231:0x09dc, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:232:0x09dd, code lost:
            r6 = r2;
            r2 = r8;
            r8 = r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:233:0x09e4, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:234:0x09e5, code lost:
            r6 = r2;
            r2 = r8;
            r8 = r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:235:0x09eb, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:236:0x09ec, code lost:
            r8 = r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:237:0x09f1, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:238:0x09f2, code lost:
            r8 = r20;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:239:0x09f5, code lost:
            r26 = r2;
            r2 = r6;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:257:0x0a53, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_SUCCESS_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:259:0x0a5d, code lost:
            if (r12 == G) goto L_0x0a5f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:260:0x0a5f, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_HAS_EXISTS_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:261:0x0a65, code lost:
            com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(com.uc.webview.export.internal.interfaces.IWaStat.SHARE_CORE_COPY_OTHER_PV);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x02e7, code lost:
            r0 = th;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x03f4, code lost:
            r0 = th;
         */
        /* JADX WARNING: Exception block dominator not found, dom blocks: [B:37:0x0213, B:65:0x031f] */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:212:0x08f0 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:34:0x01fc] */
        /* JADX WARNING: Removed duplicated region for block: B:216:0x08fa A[ExcHandler: Throwable (r0v32 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:34:0x01fc] */
        /* JADX WARNING: Removed duplicated region for block: B:257:0x0a53  */
        /* JADX WARNING: Removed duplicated region for block: B:258:0x0a59  */
        /* JADX WARNING: Removed duplicated region for block: B:267:0x0b1e  */
        /* JADX WARNING: Removed duplicated region for block: B:271:0x0b30  */
        /* JADX WARNING: Removed duplicated region for block: B:48:0x02e7 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:37:0x0213] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static void a(android.content.Context r28, java.lang.String r29, int r30) {
            /*
                r0 = r28
                r1 = r29
                java.lang.String r2 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile "
                r3.<init>(r4)
                r3.append(r1)
                java.lang.String r3 = r3.toString()
                com.uc.webview.export.internal.utility.Log.d(r2, r3)
                long r2 = a
                long r4 = a
                long r6 = a
                long r8 = a
                long r10 = a
                long r12 = a
                java.lang.String r14 = "csc_ctcp"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r14)     // Catch:{ Throwable -> 0x0a0b }
                java.lang.String r14 = "0"
                java.lang.String r15 = "process_private_data_dir_suffix"
                java.lang.Object r15 = com.uc.webview.export.extension.UCCore.getGlobalOption(r15)     // Catch:{ Throwable -> 0x0a0b }
                java.lang.String r15 = (java.lang.String) r15     // Catch:{ Throwable -> 0x0a0b }
                boolean r14 = r14.equals(r15)     // Catch:{ Throwable -> 0x0a0b }
                if (r14 != 0) goto L_0x010e
                long r0 = e     // Catch:{ Throwable -> 0x0a0b }
                java.lang.String r2 = "csc_cdrp"
                java.lang.String r3 = java.lang.Long.toString(r0)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                java.lang.String r2 = "csc_cdre"
                java.lang.String r3 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                java.lang.String r2 = "csc_cdra"
                java.lang.String r3 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                java.lang.String r2 = "csc_cdrd"
                java.lang.String r3 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                java.lang.String r2 = "csc_cdri"
                java.lang.String r3 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                java.lang.String r2 = "csc_cdrc"
                java.lang.String r3 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                long r2 = B
                int r14 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
                if (r14 != 0) goto L_0x007e
                java.lang.String r2 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)
                goto L_0x008f
            L_0x007e:
                long r2 = G
                int r14 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1))
                if (r14 != 0) goto L_0x008a
                java.lang.String r2 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)
                goto L_0x008f
            L_0x008a:
                java.lang.String r2 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r2)
            L_0x008f:
                java.lang.String r2 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r3.<init>(r14)
                r3.append(r0)
                java.lang.String r0 = r3.toString()
                com.uc.webview.export.internal.utility.Log.d(r2, r0)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r2)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x010e:
                java.lang.String r14 = "csc_cmcp"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r14)     // Catch:{ Throwable -> 0x0a0b }
                long r14 = g     // Catch:{ Throwable -> 0x0a0b }
                long r2 = com.uc.webview.export.internal.utility.e.a((android.content.Context) r28)     // Catch:{ Throwable -> 0x0a05, all -> 0x0a01 }
                long r6 = com.uc.webview.export.internal.utility.e.a     // Catch:{ Throwable -> 0x09fe, all -> 0x09fb }
                int r16 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
                if (r16 == 0) goto L_0x01fa
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x09fe, all -> 0x09fb }
                java.lang.String r1 = ".run Sdcard权限检查失败"
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x09fe, all -> 0x09fb }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r14)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r6 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r6 != 0) goto L_0x016a
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x017b
            L_0x016a:
                long r0 = G
                int r6 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r6 != 0) goto L_0x0176
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x017b
            L_0x0176:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x017b:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r6)
                r1.append(r14)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r6)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x01fa:
                long r6 = h     // Catch:{ Throwable -> 0x09fe, all -> 0x09fb }
                java.io.File r14 = new java.io.File     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                java.lang.String r15 = com.uc.webview.export.internal.utility.e.a()     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                r14.<init>(r15)     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                java.lang.String r15 = r28.getPackageName()     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                java.io.File r14 = com.uc.webview.export.internal.utility.e.a((java.io.File) r14, (java.lang.String) r15)     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                boolean r15 = r14.exists()     // Catch:{ Throwable -> 0x08fa, all -> 0x08f0 }
                if (r15 != 0) goto L_0x02ea
                long r0 = d     // Catch:{ Throwable -> 0x08fa, all -> 0x02e7 }
                java.lang.String r6 = "csc_cdrp"
                java.lang.String r7 = java.lang.Long.toString(r0)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdre"
                java.lang.String r7 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdra"
                java.lang.String r7 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdrd"
                java.lang.String r7 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdri"
                java.lang.String r7 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdrc"
                java.lang.String r7 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                long r6 = B
                int r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
                if (r14 != 0) goto L_0x0257
                java.lang.String r6 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
                goto L_0x0268
            L_0x0257:
                long r6 = G
                int r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
                if (r14 != 0) goto L_0x0263
                java.lang.String r6 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
                goto L_0x0268
            L_0x0263:
                java.lang.String r6 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
            L_0x0268:
                java.lang.String r6 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r7 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r7.<init>(r14)
                r7.append(r0)
                java.lang.String r0 = r7.toString()
                com.uc.webview.export.internal.utility.Log.d(r6, r0)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r6)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x02e7:
                r0 = move-exception
                goto L_0x08f3
            L_0x02ea:
                long r15 = i     // Catch:{ Throwable -> 0x08eb, all -> 0x08f0 }
                java.lang.String r6 = "sc_cpy"
                java.lang.String r6 = com.uc.webview.export.extension.UCCore.getParam(r6)     // Catch:{ Throwable -> 0x08e1, all -> 0x08d7 }
                boolean r6 = java.lang.Boolean.parseBoolean(r6)     // Catch:{ Throwable -> 0x08e1, all -> 0x08d7 }
                java.lang.Boolean r6 = java.lang.Boolean.valueOf(r6)     // Catch:{ Throwable -> 0x08e1, all -> 0x08d7 }
                if (r6 != 0) goto L_0x02fe
                r6 = 0
                goto L_0x0302
            L_0x02fe:
                boolean r6 = r6.booleanValue()     // Catch:{ Throwable -> 0x08e1, all -> 0x08d7 }
            L_0x0302:
                java.lang.String r7 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x08e1, all -> 0x08d7 }
                r18 = r15
                java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                java.lang.String r1 = "配置允许内核共享:"
                r15.<init>(r1)     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                r15.append(r6)     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                java.lang.String r1 = r15.toString()     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                com.uc.webview.export.internal.utility.Log.d(r7, r1)     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                if (r6 != 0) goto L_0x03fd
                long r6 = c     // Catch:{ Throwable -> 0x03fa, all -> 0x03f7 }
                r0 = 0
                r1 = 0
                com.uc.webview.export.cyclone.UCCyclone.recursiveDelete(r14, r1, r0)     // Catch:{ Throwable -> 0x03f4, all -> 0x02e7 }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x0364
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0375
            L_0x0364:
                long r0 = G
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x0370
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0375
            L_0x0370:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x0375:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r14)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r6)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x03f4:
                r0 = move-exception
                goto L_0x09f5
            L_0x03f7:
                r0 = move-exception
                goto L_0x08dc
            L_0x03fa:
                r0 = move-exception
                goto L_0x08e6
            L_0x03fd:
                long r6 = o     // Catch:{ Throwable -> 0x08d3, all -> 0x08cf }
                java.lang.String r1 = "sc_hucmv"
                java.lang.String r1 = com.uc.webview.export.extension.UCCore.getParam(r1)     // Catch:{ Throwable -> 0x08eb, all -> 0x08f0 }
                boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r1)     // Catch:{ Throwable -> 0x08eb, all -> 0x08f0 }
                if (r1 == 0) goto L_0x04df
                long r0 = p     // Catch:{ Throwable -> 0x03f4, all -> 0x02e7 }
                java.lang.String r6 = "csc_cdrp"
                java.lang.String r7 = java.lang.Long.toString(r0)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdre"
                java.lang.String r7 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdra"
                java.lang.String r7 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdrd"
                java.lang.String r7 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdri"
                java.lang.String r7 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                java.lang.String r6 = "csc_cdrc"
                java.lang.String r7 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r6, r7)
                long r6 = B
                int r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
                if (r14 != 0) goto L_0x044f
                java.lang.String r6 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
                goto L_0x0460
            L_0x044f:
                long r6 = G
                int r14 = (r12 > r6 ? 1 : (r12 == r6 ? 0 : -1))
                if (r14 != 0) goto L_0x045b
                java.lang.String r6 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
                goto L_0x0460
            L_0x045b:
                java.lang.String r6 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r6)
            L_0x0460:
                java.lang.String r6 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r7 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r7.<init>(r14)
                r7.append(r0)
                java.lang.String r0 = r7.toString()
                com.uc.webview.export.internal.utility.Log.d(r6, r0)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r6)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x04df:
                long r15 = j     // Catch:{ Throwable -> 0x08eb, all -> 0x08f0 }
                long r6 = a(r0, r14)     // Catch:{ Throwable -> 0x08c8, all -> 0x08c1 }
                long r10 = k     // Catch:{ Throwable -> 0x08bc, all -> 0x08b7 }
                r20 = r8
                long r8 = a(r30)     // Catch:{ Throwable -> 0x08ac, all -> 0x08a1 }
                long r15 = q     // Catch:{ Throwable -> 0x089c, all -> 0x0897 }
                int r1 = (r15 > r8 ? 1 : (r15 == r8 ? 0 : -1))
                if (r1 == 0) goto L_0x05dc
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                java.lang.String r1 = ".run 先决条件判断失败！"
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x053e
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x054f
            L_0x053e:
                long r0 = G
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x054a
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x054f
            L_0x054a:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x054f:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r14)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r10 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r10)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x05ce:
                r0 = move-exception
            L_0x05cf:
                r26 = r2
                r2 = r10
                r10 = r6
                goto L_0x08f6
            L_0x05d5:
                r0 = move-exception
            L_0x05d6:
                r26 = r2
                r2 = r10
                r10 = r6
                goto L_0x09f8
            L_0x05dc:
                r22 = r10
                long r10 = l     // Catch:{ Throwable -> 0x0895, all -> 0x0893 }
                boolean r1 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r29)     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                if (r1 == 0) goto L_0x06ba
                long r0 = n     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                java.lang.String r10 = "csc_cdrp"
                java.lang.String r11 = java.lang.Long.toString(r0)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                java.lang.String r10 = "csc_cdre"
                java.lang.String r11 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                java.lang.String r10 = "csc_cdra"
                java.lang.String r11 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                java.lang.String r10 = "csc_cdrd"
                java.lang.String r11 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                java.lang.String r10 = "csc_cdri"
                java.lang.String r11 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                java.lang.String r10 = "csc_cdrc"
                java.lang.String r11 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r10, r11)
                long r10 = B
                int r14 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
                if (r14 != 0) goto L_0x062a
                java.lang.String r10 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r10)
                goto L_0x063b
            L_0x062a:
                long r10 = G
                int r14 = (r12 > r10 ? 1 : (r12 == r10 ? 0 : -1))
                if (r14 != 0) goto L_0x0636
                java.lang.String r10 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r10)
                goto L_0x063b
            L_0x0636:
                java.lang.String r10 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r10)
            L_0x063b:
                java.lang.String r10 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r11 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r11.<init>(r14)
                r11.append(r0)
                java.lang.String r0 = r11.toString()
                com.uc.webview.export.internal.utility.Log.d(r10, r0)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r10 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r10)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x06ba:
                r15 = 0
            L_0x06bc:
                r17 = 1
                long r15 = r15 + r17
                r24 = r12
                r1 = r29
                long r12 = a((android.content.Context) r0, (java.io.File) r14, (java.lang.String) r1)     // Catch:{ Throwable -> 0x088e, all -> 0x0889 }
                long r17 = B     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                int r19 = (r12 > r17 ? 1 : (r12 == r17 ? 0 : -1))
                if (r19 == 0) goto L_0x07b7
                long r17 = G     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                int r19 = (r12 > r17 ? 1 : (r12 == r17 ? 0 : -1))
                if (r19 != 0) goto L_0x06d6
                goto L_0x07b7
            L_0x06d6:
                r17 = 3
                int r19 = (r15 > r17 ? 1 : (r15 == r17 ? 0 : -1))
                if (r19 <= 0) goto L_0x06bc
                java.lang.String r0 = "csc_cdrt"
                java.lang.String r1 = java.lang.Long.toString(r15)     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)     // Catch:{ Throwable -> 0x05d5, all -> 0x05ce }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x0727
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0738
            L_0x0727:
                long r0 = G
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x0733
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0738
            L_0x0733:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x0738:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r14)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r10 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r10)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
            L_0x07ac:
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x07b7:
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x07f9
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x080a
            L_0x07f9:
                long r0 = G
                int r14 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r14 != 0) goto L_0x0805
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x080a
            L_0x0805:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x080a:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r14 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r14)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r10 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r10)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x0889:
                r0 = move-exception
                r12 = r24
                goto L_0x05cf
            L_0x088e:
                r0 = move-exception
                r12 = r24
                goto L_0x05d6
            L_0x0893:
                r0 = move-exception
                goto L_0x089a
            L_0x0895:
                r0 = move-exception
                goto L_0x089f
            L_0x0897:
                r0 = move-exception
                r22 = r10
            L_0x089a:
                r10 = r6
                goto L_0x08a7
            L_0x089c:
                r0 = move-exception
                r22 = r10
            L_0x089f:
                r10 = r6
                goto L_0x08b2
            L_0x08a1:
                r0 = move-exception
                r22 = r10
                r10 = r6
                r8 = r20
            L_0x08a7:
                r6 = r2
                r2 = r22
                goto L_0x0ae2
            L_0x08ac:
                r0 = move-exception
                r22 = r10
                r10 = r6
                r8 = r20
            L_0x08b2:
                r6 = r2
                r2 = r22
                goto L_0x0a0c
            L_0x08b7:
                r0 = move-exception
                r20 = r8
                r10 = r6
                goto L_0x08c4
            L_0x08bc:
                r0 = move-exception
                r20 = r8
                r10 = r6
                goto L_0x08cb
            L_0x08c1:
                r0 = move-exception
                r20 = r8
            L_0x08c4:
                r6 = r2
                r2 = r15
                goto L_0x0ae2
            L_0x08c8:
                r0 = move-exception
                r20 = r8
            L_0x08cb:
                r6 = r2
                r2 = r15
                goto L_0x0a0c
            L_0x08cf:
                r0 = move-exception
                r20 = r8
                goto L_0x08dc
            L_0x08d3:
                r0 = move-exception
                r20 = r8
                goto L_0x08e6
            L_0x08d7:
                r0 = move-exception
                r20 = r8
                r18 = r15
            L_0x08dc:
                r6 = r2
                r2 = r18
                goto L_0x0ae2
            L_0x08e1:
                r0 = move-exception
                r20 = r8
                r18 = r15
            L_0x08e6:
                r6 = r2
                r2 = r18
                goto L_0x0a0c
            L_0x08eb:
                r0 = move-exception
                r20 = r8
                goto L_0x09f5
            L_0x08f0:
                r0 = move-exception
                r20 = r8
            L_0x08f3:
                r26 = r2
                r2 = r6
            L_0x08f6:
                r6 = r26
                goto L_0x0ae2
            L_0x08fa:
                r0 = move-exception
                r20 = r8
                long r8 = m     // Catch:{ Throwable -> 0x09f1, all -> 0x09eb }
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x09e4, all -> 0x09dc }
                java.lang.String r6 = ".shareCoreDecFile createAppSubFolder"
                com.uc.webview.export.internal.utility.Log.d(r1, r6, r0)     // Catch:{ Throwable -> 0x09e4, all -> 0x09dc }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r20)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r6 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r6 != 0) goto L_0x094a
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x095b
            L_0x094a:
                long r0 = G
                int r6 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r6 != 0) goto L_0x0956
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x095b
            L_0x0956:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x095b:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r6)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r6 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r6)
                r1.append(r4)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r14 = r20
                r1.append(r14)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                r1.append(r12)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                return
            L_0x09dc:
                r0 = move-exception
                r14 = r20
                r6 = r2
                r2 = r8
                r8 = r14
                goto L_0x0ae2
            L_0x09e4:
                r0 = move-exception
                r14 = r20
                r6 = r2
                r2 = r8
                r8 = r14
                goto L_0x0a0c
            L_0x09eb:
                r0 = move-exception
                r14 = r20
                r8 = r14
                goto L_0x08f3
            L_0x09f1:
                r0 = move-exception
                r14 = r20
                r8 = r14
            L_0x09f5:
                r26 = r2
                r2 = r6
            L_0x09f8:
                r6 = r26
                goto L_0x0a0c
            L_0x09fb:
                r0 = move-exception
                r6 = r2
                goto L_0x0a02
            L_0x09fe:
                r0 = move-exception
                r6 = r2
                goto L_0x0a06
            L_0x0a01:
                r0 = move-exception
            L_0x0a02:
                r2 = r14
                goto L_0x0ae2
            L_0x0a05:
                r0 = move-exception
            L_0x0a06:
                r2 = r14
                goto L_0x0a0c
            L_0x0a08:
                r0 = move-exception
                goto L_0x0ae2
            L_0x0a0b:
                r0 = move-exception
            L_0x0a0c:
                long r14 = f     // Catch:{ all -> 0x0a08 }
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ all -> 0x0ae0 }
                java.lang.String r4 = ".shareCoreDecFile"
                com.uc.webview.export.internal.utility.Log.d(r1, r4, r0)     // Catch:{ all -> 0x0ae0 }
                java.lang.String r0 = "csc_cdrp"
                java.lang.String r1 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdre"
                java.lang.String r1 = java.lang.Long.toString(r14)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdra"
                java.lang.String r1 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrd"
                java.lang.String r1 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdri"
                java.lang.String r1 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                java.lang.String r0 = "csc_cdrc"
                java.lang.String r1 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                long r0 = B
                int r4 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r4 != 0) goto L_0x0a59
                java.lang.String r0 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0a6a
            L_0x0a59:
                long r0 = G
                int r4 = (r12 > r0 ? 1 : (r12 == r0 ? 0 : -1))
                if (r4 != 0) goto L_0x0a65
                java.lang.String r0 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
                goto L_0x0a6a
            L_0x0a65:
                java.lang.String r0 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r0)
            L_0x0a6a:
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r4 = ".shareCoreDecFile fProcessStat: "
                r1.<init>(r4)
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fProcessStatExp: "
                r1.<init>(r2)
                r1.append(r14)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r1.<init>(r2)
                r1.append(r6)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fDeleteStat: "
                r1.<init>(r2)
                r1.append(r10)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fPreconditionStat: "
                r1.<init>(r2)
                r1.append(r8)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".shareCoreDecFile fCopyStat: "
                r1.<init>(r2)
                goto L_0x07ac
            L_0x0ae0:
                r0 = move-exception
                r4 = r14
            L_0x0ae2:
                java.lang.String r1 = "csc_cdrp"
                java.lang.String r14 = java.lang.Long.toString(r2)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                java.lang.String r1 = "csc_cdre"
                java.lang.String r14 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                java.lang.String r1 = "csc_cdra"
                java.lang.String r14 = java.lang.Long.toString(r6)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                java.lang.String r1 = "csc_cdrd"
                java.lang.String r14 = java.lang.Long.toString(r10)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                java.lang.String r1 = "csc_cdri"
                java.lang.String r14 = java.lang.Long.toString(r8)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                java.lang.String r1 = "csc_cdrc"
                java.lang.String r14 = java.lang.Long.toString(r12)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r1, r14)
                long r14 = B
                int r1 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r1 == 0) goto L_0x0b30
                long r14 = G
                int r1 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1))
                if (r1 != 0) goto L_0x0b2a
                java.lang.String r1 = "csc_chev"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)
                goto L_0x0b35
            L_0x0b2a:
                java.lang.String r1 = "csc_corv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)
                goto L_0x0b35
            L_0x0b30:
                java.lang.String r1 = "csc_cspv"
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat((java.lang.String) r1)
            L_0x0b35:
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r14 = new java.lang.StringBuilder
                java.lang.String r15 = ".shareCoreDecFile fProcessStat: "
                r14.<init>(r15)
                r14.append(r2)
                java.lang.String r2 = r14.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = ".shareCoreDecFile fProcessStatExp: "
                r2.<init>(r3)
                r2.append(r4)
                java.lang.String r2 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = ".shareCoreDecFile fSdcardAuthoryStat: "
                r2.<init>(r3)
                r2.append(r6)
                java.lang.String r2 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = ".shareCoreDecFile fDeleteStat: "
                r2.<init>(r3)
                r2.append(r10)
                java.lang.String r2 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = ".shareCoreDecFile fPreconditionStat: "
                r2.<init>(r3)
                r2.append(r8)
                java.lang.String r2 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = ".shareCoreDecFile fCopyStat: "
                r2.<init>(r3)
                r2.append(r12)
                java.lang.String r2 = r2.toString()
                com.uc.webview.export.internal.utility.Log.d(r1, r2)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.au.a.a(android.content.Context, java.lang.String, int):void");
        }

        static {
            long j2 = a << 1;
            q = j2;
            long j3 = j2 << 1;
            r = j3;
            long j4 = j3 << 1;
            s = j4;
            long j5 = j4 << 1;
            t = j5;
            u = j5 << 1;
            long j6 = a << 1;
            v = j6;
            long j7 = j6 << 1;
            w = j7;
            long j8 = j7 << 1;
            x = j8;
            long j9 = j8 << 1;
            y = j9;
            z = j9 << 1;
            long j10 = a << 1;
            A = j10;
            long j11 = j10 << 1;
            B = j11;
            long j12 = j11 << 1;
            C = j12;
            long j13 = j12 << 1;
            D = j13;
            long j14 = j13 << 1;
            E = j14;
            long j15 = j14 << 1;
            F = j15;
            long j16 = j15 << 1;
            G = j16;
            long j17 = j16 << 1;
            H = j17;
            long j18 = j17 << 1;
            I = j18;
            J = j18 << 1;
        }

        private static long a(int i2) {
            long j2;
            long j3 = q;
            try {
                if (UCCyclone.DecFileOrign.Update == i2) {
                    return j3;
                }
                if (k.g()) {
                    j2 = r;
                } else {
                    UCMRunningInfo totalLoadedUCM = SetupTask.getTotalLoadedUCM();
                    if (totalLoadedUCM == null) {
                        j2 = s;
                    } else if (!totalLoadedUCM.isShareCore) {
                        return j3;
                    } else {
                        j2 = u;
                    }
                }
                return j2;
            } catch (Throwable th) {
                Log.d(au.a, ".checkPrecondition", th);
                return j3;
            }
        }

        private static boolean a(Context context, File file, e.a aVar) {
            if (aVar != null) {
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDK_VERSION_CONFIG, UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_HOST_PUSH_UCM_VERSIONS));
            }
            return !k.a(e.a(context, file, UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_HOST_PUSH_UCM_VERSIONS), aVar));
        }

        /* JADX WARNING: Unknown top exception splitter block from list: {B:31:0x0091=Splitter:B:31:0x0091, B:18:0x0062=Splitter:B:18:0x0062} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static long a(android.content.Context r8, java.io.File r9) {
            /*
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                java.lang.String r2 = ".deleteHistoryCoreFiles hostSubFolder:"
                r1.<init>(r2)
                java.lang.String r2 = r9.getAbsolutePath()
                r1.append(r2)
                java.lang.String r1 = r1.toString()
                com.uc.webview.export.internal.utility.Log.d(r0, r1)
                long r0 = v
                java.io.File[] r9 = r9.listFiles()     // Catch:{ Throwable -> 0x0094 }
                if (r9 == 0) goto L_0x0091
                int r2 = r9.length     // Catch:{ Throwable -> 0x0094 }
                if (r2 != 0) goto L_0x0026
                goto L_0x0091
            L_0x0026:
                int r2 = r9.length     // Catch:{ Throwable -> 0x0094 }
                r3 = 0
            L_0x0028:
                if (r3 >= r2) goto L_0x0090
                r4 = r9[r3]     // Catch:{ Throwable -> 0x0094 }
                java.lang.String r5 = r4.getAbsolutePath()     // Catch:{ Throwable -> 0x0094 }
                boolean r5 = com.uc.webview.export.cyclone.UCCyclone.detectZipByFileType(r5)     // Catch:{ Throwable -> 0x0094 }
                r6 = 0
                if (r5 == 0) goto L_0x0062
                boolean r5 = com.uc.webview.export.internal.utility.e.a((java.io.File) r4)     // Catch:{ Throwable -> 0x0094 }
                if (r5 != 0) goto L_0x0062
                boolean r5 = com.uc.webview.export.internal.utility.e.a((android.content.Context) r8, (java.io.File) r4, (com.uc.webview.export.internal.utility.e.a) r6)     // Catch:{ Throwable -> 0x0094 }
                if (r5 != 0) goto L_0x0062
                r4.delete()     // Catch:{ Throwable -> 0x0094 }
                long r5 = x     // Catch:{ Throwable -> 0x0094 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r7 = ".deleteHistoryCoreFiles verifySignature failure! file: "
                r1.<init>(r7)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r4 = r4.getAbsolutePath()     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                r1.append(r4)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                goto L_0x0086
            L_0x0062:
                boolean r5 = a((android.content.Context) r8, (java.io.File) r4, (com.uc.webview.export.internal.utility.e.a) r6)     // Catch:{ Throwable -> 0x0094 }
                if (r5 != 0) goto L_0x008d
                r4.delete()     // Catch:{ Throwable -> 0x0094 }
                long r5 = y     // Catch:{ Throwable -> 0x0094 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r7 = ".deleteHistoryCoreFiles verifyCoreCompressFileVersion failure! file: "
                r1.<init>(r7)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r4 = r4.getAbsolutePath()     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                r1.append(r4)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x008a, all -> 0x0088 }
            L_0x0086:
                r0 = r5
                goto L_0x008d
            L_0x0088:
                r0 = r5
                goto L_0x00a2
            L_0x008a:
                r8 = move-exception
                r0 = r5
                goto L_0x0095
            L_0x008d:
                int r3 = r3 + 1
                goto L_0x0028
            L_0x0090:
                return r0
            L_0x0091:
                long r8 = z     // Catch:{ Throwable -> 0x0094 }
                return r8
            L_0x0094:
                r8 = move-exception
            L_0x0095:
                long r2 = w     // Catch:{ all -> 0x00a2 }
                java.lang.String r9 = com.uc.webview.export.internal.setup.au.a     // Catch:{ all -> 0x00a1 }
                java.lang.String r0 = ".deleteHistoryCoreFiles"
                com.uc.webview.export.internal.utility.Log.d(r9, r0, r8)     // Catch:{ all -> 0x00a1 }
                return r2
            L_0x00a1:
                r0 = r2
            L_0x00a2:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.au.a.a(android.content.Context, java.io.File):long");
        }

        /* JADX WARNING: Removed duplicated region for block: B:113:0x0279 A[Catch:{ Throwable -> 0x027d }] */
        /* JADX WARNING: Removed duplicated region for block: B:119:0x028b A[Catch:{ Throwable -> 0x0239, Throwable -> 0x02b2 }] */
        /* JADX WARNING: Removed duplicated region for block: B:120:0x028e  */
        /* JADX WARNING: Removed duplicated region for block: B:125:0x029b A[Catch:{ Throwable -> 0x029f }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static long a(android.content.Context r16, java.io.File r17, java.lang.String r18) {
            /*
                r0 = r16
                r1 = r18
                java.lang.String r2 = com.uc.webview.export.internal.setup.au.a
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                java.lang.String r4 = ".copyCoreFileToSdcard("
                r3.<init>(r4)
                r3.append(r0)
                java.lang.String r4 = r17.getAbsolutePath()
                r3.append(r4)
                java.lang.String r4 = ", "
                r3.append(r4)
                r3.append(r1)
                java.lang.String r4 = ")"
                r3.append(r4)
                java.lang.String r3 = r3.toString()
                com.uc.webview.export.internal.utility.Log.d(r2, r3)
                long r2 = A
                com.uc.webview.export.internal.utility.e$a r4 = new com.uc.webview.export.internal.utility.e$a
                r4.<init>()
                java.lang.String r5 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x02b2 }
                java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r7 = ".copyCoreFileToSdcard copy file path : "
                r6.<init>(r7)     // Catch:{ Throwable -> 0x02b2 }
                r6.append(r1)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r6 = r6.toString()     // Catch:{ Throwable -> 0x02b2 }
                com.uc.webview.export.internal.utility.Log.d(r5, r6)     // Catch:{ Throwable -> 0x02b2 }
                boolean r5 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r18)     // Catch:{ Throwable -> 0x02b2 }
                if (r5 == 0) goto L_0x005d
                long r0 = D     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r2 = "csc_vvfsv"
                long r3 = r4.s
            L_0x0055:
                java.lang.String r3 = java.lang.Long.toString(r3)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r2, r3)
                return r0
            L_0x005d:
                java.io.File r5 = new java.io.File     // Catch:{ Throwable -> 0x02b2 }
                r5.<init>(r1)     // Catch:{ Throwable -> 0x02b2 }
                boolean r1 = r5.exists()     // Catch:{ Throwable -> 0x02b2 }
                if (r1 == 0) goto L_0x02aa
                boolean r1 = r5.isFile()     // Catch:{ Throwable -> 0x02b2 }
                if (r1 != 0) goto L_0x0070
                goto L_0x02aa
            L_0x0070:
                java.lang.String r1 = r5.getAbsolutePath()     // Catch:{ Throwable -> 0x02b2 }
                boolean r1 = com.uc.webview.export.cyclone.UCCyclone.detectZipByFileType(r1)     // Catch:{ Throwable -> 0x02b2 }
                r6 = 0
                if (r1 == 0) goto L_0x00ae
                boolean r1 = com.uc.webview.export.internal.utility.e.c((android.content.Context) r16)     // Catch:{ Throwable -> 0x02b2 }
                if (r1 != 0) goto L_0x00ae
                boolean r1 = com.uc.webview.export.internal.utility.e.a((android.content.Context) r0, (java.io.File) r5, (com.uc.webview.export.internal.utility.e.a) r6)     // Catch:{ Throwable -> 0x02b2 }
                if (r1 != 0) goto L_0x00ae
                long r6 = I     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r2 = ".copyCoreFileToSdcard verifySignature failure! file: "
                r1.<init>(r2)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r2 = r5.getAbsolutePath()     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                r1.append(r2)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r0 = "csc_vvfsv"
                long r1 = r4.s
            L_0x00a6:
                java.lang.String r1 = java.lang.Long.toString(r1)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                return r6
            L_0x00ae:
                boolean r0 = a((android.content.Context) r0, (java.io.File) r5, (com.uc.webview.export.internal.utility.e.a) r4)     // Catch:{ Throwable -> 0x02b2 }
                if (r0 != 0) goto L_0x00e0
                long r6 = J     // Catch:{ Throwable -> 0x02b2 }
                long r0 = com.uc.webview.export.internal.utility.e.a.n     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                r4.a(r0)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r2 = ".copyCoreFileToSdcard verifyCoreCompressFileVersion failure! file: "
                r1.<init>(r2)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r2 = r5.getAbsolutePath()     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                r1.append(r2)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x00dc, all -> 0x00d9 }
                java.lang.String r0 = "csc_vvfsv"
                long r1 = r4.s
                goto L_0x00a6
            L_0x00d9:
                r2 = r6
                goto L_0x02c5
            L_0x00dc:
                r0 = move-exception
                r2 = r6
                goto L_0x02b3
            L_0x00e0:
                java.io.File[] r0 = r17.listFiles()     // Catch:{ Throwable -> 0x02b2 }
                r1 = 1
                r7 = 0
                if (r0 == 0) goto L_0x018e
                int r8 = r0.length     // Catch:{ Throwable -> 0x02b2 }
                if (r8 <= 0) goto L_0x018e
                long r8 = r5.length()     // Catch:{ Throwable -> 0x02b2 }
                int r10 = r0.length     // Catch:{ Throwable -> 0x02b2 }
                r11 = r6
                r6 = 0
            L_0x00f2:
                if (r6 >= r10) goto L_0x018e
                r12 = r0[r6]     // Catch:{ Throwable -> 0x02b2 }
                long r13 = r12.length()     // Catch:{ Throwable -> 0x02b2 }
                int r15 = (r8 > r13 ? 1 : (r8 == r13 ? 0 : -1))
                if (r15 != 0) goto L_0x018a
                boolean r13 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r11)     // Catch:{ Throwable -> 0x02b2 }
                r14 = 1013(0x3f5, float:1.42E-42)
                if (r13 == 0) goto L_0x0131
                com.uc.webview.export.cyclone.UCCyclone$MessageDigestType r11 = com.uc.webview.export.cyclone.UCCyclone.MessageDigestType.SHA1     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r11 = com.uc.webview.export.cyclone.UCCyclone.hashFileContents(r5, r11)     // Catch:{ Throwable -> 0x02b2 }
                boolean r13 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r11)     // Catch:{ Throwable -> 0x02b2 }
                if (r13 != 0) goto L_0x0113
                goto L_0x0131
            L_0x0113:
                long r8 = E     // Catch:{ Throwable -> 0x02b2 }
                long r2 = com.uc.webview.export.internal.utility.e.a.o     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                r4.a(r2)     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                java.lang.String r2 = "SHA1 [%s] failed."
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                r1[r7] = r5     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                java.lang.String r1 = java.lang.String.format(r2, r1)     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                r0.<init>((int) r14, (java.lang.String) r1)     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
                throw r0     // Catch:{ Throwable -> 0x012d, all -> 0x012a }
            L_0x012a:
                r2 = r8
                goto L_0x02c5
            L_0x012d:
                r0 = move-exception
                r2 = r8
                goto L_0x02b3
            L_0x0131:
                com.uc.webview.export.cyclone.UCCyclone$MessageDigestType r13 = com.uc.webview.export.cyclone.UCCyclone.MessageDigestType.SHA1     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r13 = com.uc.webview.export.cyclone.UCCyclone.hashFileContents(r12, r13)     // Catch:{ Throwable -> 0x02b2 }
                boolean r15 = com.uc.webview.export.internal.utility.k.a((java.lang.String) r13)     // Catch:{ Throwable -> 0x02b2 }
                if (r15 != 0) goto L_0x016f
                boolean r13 = r13.equals(r11)     // Catch:{ Throwable -> 0x02b2 }
                if (r13 == 0) goto L_0x018a
                long r5 = G     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x0186 }
                java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r2 = ".copyCoreFileToSdcard "
                r1.<init>(r2)     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r2 = r12.getAbsolutePath()     // Catch:{ Throwable -> 0x0186 }
                r1.append(r2)     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r2 = " had exists."
                r1.append(r2)     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0186 }
                com.uc.webview.export.internal.utility.Log.d(r0, r1)     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r0 = "csc_vvfsv"
                long r1 = r4.s
            L_0x0167:
                java.lang.String r1 = java.lang.Long.toString(r1)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                return r5
            L_0x016f:
                long r5 = E     // Catch:{ Throwable -> 0x02b2 }
                long r2 = com.uc.webview.export.internal.utility.e.a.o     // Catch:{ Throwable -> 0x0186 }
                r4.a(r2)     // Catch:{ Throwable -> 0x0186 }
                com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r2 = "SHA1 [%s] failed."
                java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x0186 }
                r1[r7] = r12     // Catch:{ Throwable -> 0x0186 }
                java.lang.String r1 = java.lang.String.format(r2, r1)     // Catch:{ Throwable -> 0x0186 }
                r0.<init>((int) r14, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0186 }
                throw r0     // Catch:{ Throwable -> 0x0186 }
            L_0x0186:
                r0 = move-exception
                r2 = r5
                goto L_0x02b3
            L_0x018a:
                int r6 = r6 + 1
                goto L_0x00f2
            L_0x018e:
                java.lang.String r0 = r5.getName()     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r6 = "."
                int r6 = r0.lastIndexOf(r6)     // Catch:{ Throwable -> 0x02b2 }
                r8 = 2
                if (r6 <= 0) goto L_0x01cc
                java.lang.String r6 = "."
                int r6 = r0.lastIndexOf(r6)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r6 = r0.substring(r7, r6)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r9 = "."
                int r9 = r0.lastIndexOf(r9)     // Catch:{ Throwable -> 0x02b2 }
                int r9 = r9 + r1
                int r10 = r0.length()     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = r0.substring(r9, r10)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r9 = "%s_%s.%s"
                r10 = 3
                java.lang.Object[] r10 = new java.lang.Object[r10]     // Catch:{ Throwable -> 0x02b2 }
                r10[r7] = r6     // Catch:{ Throwable -> 0x02b2 }
                long r11 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r6 = java.lang.String.valueOf(r11)     // Catch:{ Throwable -> 0x02b2 }
                r10[r1] = r6     // Catch:{ Throwable -> 0x02b2 }
                r10[r8] = r0     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = java.lang.String.format(r9, r10)     // Catch:{ Throwable -> 0x02b2 }
                goto L_0x01e0
            L_0x01cc:
                java.lang.String r6 = "%s_%s"
                java.lang.Object[] r9 = new java.lang.Object[r8]     // Catch:{ Throwable -> 0x02b2 }
                r9[r7] = r0     // Catch:{ Throwable -> 0x02b2 }
                long r10 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = java.lang.String.valueOf(r10)     // Catch:{ Throwable -> 0x02b2 }
                r9[r1] = r0     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = java.lang.String.format(r6, r9)     // Catch:{ Throwable -> 0x02b2 }
            L_0x01e0:
                java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r9 = r17.getAbsolutePath()     // Catch:{ Throwable -> 0x02b2 }
                r6.<init>(r9, r0)     // Catch:{ Throwable -> 0x02b2 }
                java.io.File r9 = new java.io.File     // Catch:{ Throwable -> 0x02b2 }
                java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b2 }
                r0.<init>()     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r10 = r6.getAbsolutePath()     // Catch:{ Throwable -> 0x02b2 }
                r0.append(r10)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r10 = ".tmp"
                r0.append(r10)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = r0.toString()     // Catch:{ Throwable -> 0x02b2 }
                r9.<init>(r0)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r0 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x02b2 }
                java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r11 = ".copyCoreFileToSdcard targetFile: "
                r10.<init>(r11)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r11 = r6.getAbsolutePath()     // Catch:{ Throwable -> 0x02b2 }
                r10.append(r11)     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r10 = r10.toString()     // Catch:{ Throwable -> 0x02b2 }
                com.uc.webview.export.internal.utility.Log.d(r0, r10)     // Catch:{ Throwable -> 0x02b2 }
                r9.createNewFile()     // Catch:{ Exception -> 0x0269 }
                com.uc.webview.export.internal.utility.k.a((java.io.File) r5, (java.io.File) r9)     // Catch:{ Exception -> 0x0269 }
                boolean r0 = r9.renameTo(r6)     // Catch:{ Exception -> 0x0269 }
                if (r0 == 0) goto L_0x0244
                long r0 = r5.lastModified()     // Catch:{ Exception -> 0x0269 }
                r6.setLastModified(r0)     // Catch:{ Exception -> 0x0269 }
                boolean r0 = r9.exists()     // Catch:{ Throwable -> 0x0239 }
                if (r0 == 0) goto L_0x0285
                r9.delete()     // Catch:{ Throwable -> 0x0239 }
                goto L_0x0285
            L_0x0239:
                r0 = move-exception
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r5 = ".copyCoreFileToSdcard"
            L_0x0240:
                com.uc.webview.export.internal.utility.Log.d(r1, r5, r0)     // Catch:{ Throwable -> 0x02b2 }
                goto L_0x0285
            L_0x0244:
                long r10 = F     // Catch:{ Exception -> 0x0269 }
                long r2 = com.uc.webview.export.internal.utility.e.a.p     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                r4.a(r2)     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                com.uc.webview.export.internal.setup.UCSetupException r0 = new com.uc.webview.export.internal.setup.UCSetupException     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                r2 = 1005(0x3ed, float:1.408E-42)
                java.lang.String r3 = "Rename [%s] to [%s] failed."
                java.lang.Object[] r5 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                r5[r7] = r9     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                r5[r1] = r6     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                java.lang.String r1 = java.lang.String.format(r3, r5)     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                r0.<init>((int) r2, (java.lang.String) r1)     // Catch:{ Exception -> 0x0263, all -> 0x025f }
                throw r0     // Catch:{ Exception -> 0x0263, all -> 0x025f }
            L_0x025f:
                r0 = move-exception
                r1 = r0
                r2 = r10
                goto L_0x0295
            L_0x0263:
                r0 = move-exception
                r2 = r10
                goto L_0x026a
            L_0x0266:
                r0 = move-exception
                r1 = r0
                goto L_0x0295
            L_0x0269:
                r0 = move-exception
            L_0x026a:
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ all -> 0x0266 }
                java.lang.String r5 = ".copyCoreFileToSdcard"
                com.uc.webview.export.internal.utility.Log.d(r1, r5, r0)     // Catch:{ all -> 0x0266 }
                boolean r0 = r9.exists()     // Catch:{ Throwable -> 0x027d }
                if (r0 == 0) goto L_0x0285
                r9.delete()     // Catch:{ Throwable -> 0x027d }
                goto L_0x0285
            L_0x027d:
                r0 = move-exception
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r5 = ".copyCoreFileToSdcard"
                goto L_0x0240
            L_0x0285:
                long r0 = F     // Catch:{ Throwable -> 0x02b2 }
                int r5 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
                if (r5 == 0) goto L_0x028e
                long r0 = B     // Catch:{ Throwable -> 0x02b2 }
                goto L_0x028f
            L_0x028e:
                r0 = r2
            L_0x028f:
                java.lang.String r2 = "csc_vvfsv"
                long r3 = r4.s
                goto L_0x0055
            L_0x0295:
                boolean r0 = r9.exists()     // Catch:{ Throwable -> 0x029f }
                if (r0 == 0) goto L_0x02a9
                r9.delete()     // Catch:{ Throwable -> 0x029f }
                goto L_0x02a9
            L_0x029f:
                r0 = move-exception
                java.lang.String r5 = com.uc.webview.export.internal.setup.au.a     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r6 = ".copyCoreFileToSdcard"
                com.uc.webview.export.internal.utility.Log.d(r5, r6, r0)     // Catch:{ Throwable -> 0x02b2 }
            L_0x02a9:
                throw r1     // Catch:{ Throwable -> 0x02b2 }
            L_0x02aa:
                long r0 = H     // Catch:{ Throwable -> 0x02b2 }
                java.lang.String r2 = "csc_vvfsv"
                long r3 = r4.s
                goto L_0x0055
            L_0x02b2:
                r0 = move-exception
            L_0x02b3:
                long r5 = C     // Catch:{ all -> 0x02c5 }
                java.lang.String r1 = com.uc.webview.export.internal.setup.au.a     // Catch:{ all -> 0x02c4 }
                java.lang.String r2 = ".copyCoreFileToSdcard"
                com.uc.webview.export.internal.utility.Log.d(r1, r2, r0)     // Catch:{ all -> 0x02c4 }
                java.lang.String r0 = "csc_vvfsv"
                long r1 = r4.s
                goto L_0x0167
            L_0x02c4:
                r2 = r5
            L_0x02c5:
                java.lang.String r0 = "csc_vvfsv"
                long r4 = r4.s
                java.lang.String r1 = java.lang.Long.toString(r4)
                com.uc.webview.export.internal.interfaces.IWaStat.WaStat.stat(r0, r1)
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.au.a.a(android.content.Context, java.io.File, java.lang.String):long");
        }

        public static void a(Context context, boolean z2, boolean z3, Callable<Boolean> callable) {
            int i2;
            Log.d(au.a, ".statDevicesHasShareCore isUCCore: " + z2 + ", hasUpdSource: " + z3 + ", wifiChecker: " + callable);
            try {
                if ("0".equals((String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION))) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("PREF_SC_HIS", 4);
                    int i3 = (callable == null || !callable.call().booleanValue()) ? sharedPreferences.getInt("NO_WIFI_HISTORY", 0) + 1 : 0;
                    if (!z3 || !z2) {
                        i2 = sharedPreferences.getInt("INVALID_UPD_HISTORY", 0) + 1;
                    } else {
                        i2 = 0;
                    }
                    Log.d(au.a, ".statDevicesHasShareCore noWifiTimes: " + i3 + ", invalidUpdateTimes: " + i2);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putInt("NO_WIFI_HISTORY", i3);
                    edit.putInt("INVALID_UPD_HISTORY", i2);
                    edit.apply();
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_NO_WIFI_TIMES, Integer.toString(i3));
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_INVALID_UPDATE_TIMES, Integer.toString(i2));
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_CURRENT_IS_UC_CORE, z2 ? "1" : "0");
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_HAS_UPD_SOURCE, z3 ? "1" : "0");
                    if (!e.b(context)) {
                        Log.d(au.a, ".statDevicesHasShareCore Sdcard配置及权限校验失败");
                        return;
                    }
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(context.getPackageName());
                    String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_SPECIAL_HOST_PKG_NAME_LIST);
                    if (!k.a(param)) {
                        for (String str : param.split(CDParamKeys.CD_VALUE_STRING_SPLITER)) {
                            if (!k.a(str) && !str.equals(context.getPackageName())) {
                                arrayList.add(str);
                            }
                        }
                    }
                    for (String str2 : arrayList) {
                        File c2 = e.c(str2);
                        if (!c2.exists()) {
                            Log.d(au.a, ".statDevicesHasShareCore " + c2.getAbsolutePath() + " not exists.");
                        } else {
                            File[] listFiles = c2.listFiles();
                            if (listFiles != null) {
                                if (listFiles.length != 0) {
                                    Log.d(au.a, ".statDevicesHasShareCore " + str2 + "," + str2.hashCode() + AVFSCacheConstants.COMMA_SEP + listFiles.length);
                                    StringBuilder sb = new StringBuilder("csc_dhsc_");
                                    sb.append(str2.hashCode());
                                    IWaStat.WaStat.stat(sb.toString(), Integer.toString(listFiles.length));
                                }
                            }
                            Log.d(au.a, ".statDevicesHasShareCore " + c2.getAbsolutePath() + " empty.");
                        }
                    }
                }
            } catch (Throwable th) {
                Log.d(au.a, ".statDevicesHasShareCore", th);
            }
        }
    }
}
