package com.uc.webview.export.internal.setup;

import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.k;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class ap extends bf {
    /* access modifiers changed from: private */
    public static String b = "ap";
    private static long c = 1;
    private static long d = 2;
    private static long e = 4;
    private static long f = 8;
    private static long g = 16;

    public void run() {
        long j;
        long j2 = d;
        try {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SEARCH_CORE_FILE_TASK_RUN);
            if (!"0".equals((String) UCCore.getGlobalOption(UCCore.PROCESS_PRIVATE_DATA_DIR_SUFFIX_OPTION))) {
                long j3 = g;
                String str = b;
                Log.d(str, ".run stat: " + j3);
                return;
            }
            ((l) ((l) ((l) onEvent("success", new at(this))).onEvent(UCCore.EVENT_EXCEPTION, new as(this))).onEvent(UCCore.LEGACY_EVENT_SETUP, new ar(this))).onEvent("switch", new aq(this));
            String e2 = e.e(getContext().getApplicationContext());
            String str2 = b;
            Log.d(str2, ".run sdCoreDecFilePath: " + e2);
            if (!k.a(e2)) {
                j = e;
                try {
                    Integer num = (Integer) this.mOptions.get(UCCore.OPTION_VERIFY_POLICY);
                    String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_VERIFY_POLICY);
                    if (CDParamKeys.CD_VALUE_VERIFY_ALL_HASH_ASYNC.equals(param)) {
                        num = Integer.valueOf(num.intValue() | UCCore.VERIFY_POLICY_ALL_FULL_HASH);
                    } else if (CDParamKeys.CD_VALUE_VERIFY_ALL_HASH_SYNC.equals(param)) {
                        num = Integer.valueOf(Integer.valueOf(num.intValue() | UCCore.VERIFY_POLICY_ALL_FULL_HASH).intValue() & Integer.MAX_VALUE);
                    }
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SEARCH_CORE_FILE_SDCARD);
                    this.mCallbacks = null;
                    resetCrashFlag();
                    ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) new b().setParent(this)).setCallbacks(this.mCallbacks)).setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).setup(UCCore.OPTION_VERIFY_POLICY, (Object) num)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) e2)).setup(UCCore.OPTION_SHARE_CORE_SETUP_TASK_FLAG, (Object) true)).setup(UCCore.OPTION_ENABLE_LOAD_CLASS, (Object) false)).start();
                    String str3 = b;
                    Log.d(str3, ".run stat: " + j);
                } catch (Throwable th) {
                    th = th;
                    String str4 = b;
                    Log.d(str4, ".run stat: " + j);
                    throw th;
                }
            } else {
                long j4 = f;
                try {
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SEARCH_CORE_FILE_EXCEPTION);
                    throw new UCSetupException(3036, b + " not found uc core");
                } catch (Throwable th2) {
                    j = j4;
                    th = th2;
                    String str42 = b;
                    Log.d(str42, ".run stat: " + j);
                    throw th;
                }
            }
        } catch (Throwable th3) {
            th = th3;
            th.printStackTrace();
            String str5 = b;
            Log.d(str5, ".run stat: " + j2);
        }
    }
}
