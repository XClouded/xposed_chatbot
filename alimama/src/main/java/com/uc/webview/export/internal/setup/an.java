package com.uc.webview.export.internal.setup;

import android.util.Pair;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;
import java.util.concurrent.ConcurrentHashMap;

/* compiled from: U4Source */
public class an extends bf {
    private static String b = "an";
    private static long c = 1;
    private static long d = 2;
    private static long e = 4;
    private static long f = 8;
    private static long g = (e << 1);

    public void run() {
        Throwable th;
        long j;
        Log.d(b, "==run.");
        long j2 = d;
        callbackStat(new Pair(IWaStat.SHARE_CORE_FAULTTOLERANCE_SETUP_TASK_RUN, (Object) null));
        try {
            if (!k.a(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_KRL_DIR))) {
                j = e;
                try {
                    String str = b;
                    Log.d(str, ".run bak krl dir: " + UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_KRL_DIR));
                    callbackStat(new Pair(IWaStat.SHARE_CORE_FAULTTOLERANCE_SETUP_TASK_KRL, (Object) null));
                    ((l) ((l) ((l) ((l) ((l) setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_LIB_DIR, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_KRL_DIR))).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null);
                    super.run();
                } catch (Throwable th2) {
                    th = th2;
                }
            } else if (!k.a(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_ZIP_FPATH))) {
                j = f;
                String str2 = b;
                Log.d(str2, ".run bak core file: " + UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_ZIP_FPATH));
                callbackStat(new Pair(IWaStat.SHARE_CORE_FAULTTOLERANCE_SETUP_TASK_ZIP, (Object) null));
                this.mCallbacks = null;
                resetCrashFlag();
                ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) ((l) new b().setParent(this)).setCallbacks(this.mCallbacks)).setOptions((ConcurrentHashMap<String, Object>) this.mOptions)).setup(UCCore.OPTION_DEX_FILE_PATH, (Object) null)).setup(UCCore.OPTION_SO_FILE_PATH, (Object) null)).setup(UCCore.OPTION_RES_FILE_PATH, (Object) null)).setup(UCCore.OPTION_UCM_CFG_FILE, (Object) null)).setup(UCCore.OPTION_UCM_KRL_DIR, (Object) null)).setup(UCCore.OPTION_UCM_ZIP_FILE, (Object) UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_ZIP_FPATH))).start();
            } else {
                long j3 = g;
                callbackStat(new Pair(IWaStat.SHARE_CORE_FAULTTOLERANCE_SETUP_TASK_EXCEPTION, (Object) null));
                throw new UCSetupException(3033, String.format(b + " not config (%s or %s)", new Object[]{CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_KRL_DIR, CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_BAK_ZIP_FPATH}));
            }
            String str3 = b;
            Log.d(str3, ".run stat: " + j);
        } catch (Throwable th3) {
            long j4 = j2;
            th = th3;
            long j5 = j4;
            String str4 = b;
            Log.d(str4, ".run stat: " + j5);
            throw th;
        }
    }
}
