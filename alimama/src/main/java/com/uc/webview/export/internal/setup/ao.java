package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.e;
import com.uc.webview.export.internal.utility.k;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class ao extends bf {
    private static String b = "ao";
    private static long c = 1;
    private static long d = 2;
    private static long e = 4;
    private static long f = 8;
    private static long g = 16;
    private static long h = 32;

    public void run() {
        long j;
        long j2;
        long j3 = d;
        try {
            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_TASK_RUN);
            if (!k.a(getTotalLoadedUCM())) {
                IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_TASK_HAD_INIT);
                String str = b;
                Log.d(str, ".run stat: " + j3);
                return;
            }
            String str2 = (String) getOption(UCCore.OPTION_LOCAL_DIR);
            if (k.a(str2)) {
                str2 = e.d(getContext().getApplicationContext());
            }
            String str3 = b;
            Log.d(str3, ".run locationDecDir: " + str2);
            if (!k.a(str2)) {
                j2 = e;
                try {
                    IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_TASK_LOCATION_DEC);
                    ((l) ((l) ((l) ((l) ((l) setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) str2);
                    super.run();
                } catch (Throwable th) {
                    th = th;
                }
            } else {
                ValueCallback callback = getCallback(UCCore.EVENT_DELAY_SEARCH_CORE_FILE);
                String str4 = b;
                Log.d(str4, ".run delaySeareCoreFileCB: " + callback);
                if (callback == null) {
                    String str5 = (String) getOption(UCCore.OPTION_DEC_FILE);
                    if (k.a(str5)) {
                        str5 = e.e(getContext().getApplicationContext());
                    }
                    String str6 = b;
                    Log.d(str6, ".run sdCoreDecFilePath: " + str5);
                    if (!k.a(str5)) {
                        j2 = f;
                        Integer num = (Integer) this.mOptions.get(UCCore.OPTION_VERIFY_POLICY);
                        String param = UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_VERIFY_POLICY);
                        if (CDParamKeys.CD_VALUE_VERIFY_ALL_HASH_ASYNC.equals(param)) {
                            num = Integer.valueOf(num.intValue() | UCCore.VERIFY_POLICY_ALL_FULL_HASH);
                        } else if (CDParamKeys.CD_VALUE_VERIFY_ALL_HASH_SYNC.equals(param)) {
                            num = Integer.valueOf(Integer.valueOf(num.intValue() | UCCore.VERIFY_POLICY_ALL_FULL_HASH).intValue() & Integer.MAX_VALUE);
                        }
                        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_TASK_SDCARD);
                        this.mCallbacks = null;
                        resetCrashFlag();
                        ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) new b().setParent(this)).setCallbacks(this.mCallbacks)).setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).setup(UCCore.OPTION_VERIFY_POLICY, (Object) num)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) str5)).start();
                    } else {
                        long j4 = g;
                        try {
                            IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_TASK_EXCEPTION);
                            throw new UCSetupException(3022, b + " not found uc core");
                        } catch (Throwable th2) {
                            j = j4;
                            th = th2;
                            String str7 = b;
                            Log.d(str7, ".run stat: " + j);
                            throw th;
                        }
                    }
                } else {
                    try {
                        IWaStat.WaStat.stat(IWaStat.SHARE_CORE_SDCARD_SETUP_DELAY_SEARCH_CORE_FILE);
                        callback.onReceiveValue(this);
                        throw new UCSetupException(3035, b + " delay search sdcard core file.");
                    } catch (Throwable th3) {
                        th = th3;
                        j = h;
                        String str72 = b;
                        Log.d(str72, ".run stat: " + j);
                        throw th;
                    }
                }
            }
            String str8 = b;
            Log.d(str8, ".run stat: " + j2);
        } catch (Throwable th4) {
            th = th4;
            j = j3;
            String str722 = b;
            Log.d(str722, ".run stat: " + j);
            throw th;
        }
    }
}
