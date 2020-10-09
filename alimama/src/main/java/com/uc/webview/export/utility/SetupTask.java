package com.uc.webview.export.utility;

import android.util.Pair;
import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.cyclone.UCHashMap;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.setup.UCSetupException;
import com.uc.webview.export.internal.setup.UCSetupTask;
import com.uc.webview.export.internal.setup.br;
import com.uc.webview.export.internal.utility.Log;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

@Api
/* compiled from: U4Source */
public abstract class SetupTask extends UCSetupTask<SetupTask, SetupTask> {
    public static br sFirstUCM;
    private final String a = "SetupTask";

    public void startSync() {
        start();
        try {
            Thread.sleep(200);
        } catch (Throwable unused) {
        }
        SDKFactory.g();
    }

    public void callbackFinishStat(String str) {
        UCLogger create = UCLogger.create(UploadQueueMgr.MSGTYPE_INTERVAL, "SetupTask");
        if (create != null) {
            create.print("finish: core=" + str, new Throwable[0]);
        }
    }

    public void setException(UCSetupException uCSetupException) {
        setException(uCSetupException, true);
    }

    public void setException(UCSetupException uCSetupException, boolean z) {
        super.setException(uCSetupException);
        callStatException(IWaStat.SETUP_TOTAL_EXCEPTION, uCSetupException);
        callbackFinishStat("0");
        if (z) {
            SDKFactory.a(uCSetupException.toRunnable());
        }
    }

    /* access modifiers changed from: protected */
    public void callStatException(String str, UCSetupException uCSetupException) {
        String str2;
        String str3;
        try {
            str2 = uCSetupException.getRootCause().getClass().getName();
            try {
                String message = uCSetupException.getRootCause().getMessage();
                int i = 256;
                if (256 > message.length()) {
                    i = message.length();
                }
                str3 = message.substring(0, i);
            } catch (Exception unused) {
                str3 = "";
                callbackStat(new Pair(str, new UCHashMap().set("cnt", "1").set("err", String.valueOf(uCSetupException.errCode())).set("cls", str2).set("msg", str3)));
            }
        } catch (Exception unused2) {
            str2 = "";
            str3 = "";
            callbackStat(new Pair(str, new UCHashMap().set("cnt", "1").set("err", String.valueOf(uCSetupException.errCode())).set("cls", str2).set("msg", str3)));
        }
        try {
            callbackStat(new Pair(str, new UCHashMap().set("cnt", "1").set("err", String.valueOf(uCSetupException.errCode())).set("cls", str2).set("msg", str3)));
        } catch (Throwable unused3) {
        }
    }

    public SetupTask setAsDefault() {
        SDKFactory.s = this;
        SDKFactory.k = true;
        return this;
    }

    public String getFirstUCMFileHashs() {
        StringBuilder sb = new StringBuilder();
        try {
            if (sFirstUCM != null) {
                for (Map.Entry next : sFirstUCM.getFileHashs().entrySet()) {
                    sb.append((String) next.getKey());
                    sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                    sb.append((String) next.getValue());
                    sb.append(";");
                }
            } else {
                sb.append("first_ucm=null;");
            }
        } catch (Throwable th) {
            Log.d("SetupTask", "getFirstUCMFileHashs error", th);
        }
        return sb.toString();
    }

    public int getPercent() {
        return super.getPercent();
    }
}
