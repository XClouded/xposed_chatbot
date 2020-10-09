package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class w implements ValueCallback<l> {
    final /* synthetic */ o a;

    w(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        l lVar = (l) obj;
        Log.d("SdkSetupTask", "mShareCoreCB " + lVar);
        try {
            bw bwVar = (bw) lVar;
            if (CDParamKeys.CD_VALUE_LOAD_POLICY_SHARE_CORE.equals(UCCore.getParam(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_LOAD_POLICY))) {
                ((l) ((l) ((l) ((l) ((l) ((l) ((l) this.a.e().setParent(UCSetupTask.getRoot())).setup(UCCore.OPTION_LOCAL_DIR, (Object) bwVar.c)).setup(UCCore.OPTION_DEC_FILE, (Object) bwVar.d)).onEvent("switch", this.a.s)).onEvent("success", new y(this))).onEvent(UCCore.EVENT_EXCEPTION, new x(this))).onEvent(UCCore.EVENT_DELAY_SEARCH_CORE_FILE, (ValueCallback) null)).start();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
