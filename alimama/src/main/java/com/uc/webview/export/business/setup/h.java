package com.uc.webview.export.business.setup;

import android.webkit.ValueCallback;
import com.uc.webview.export.business.a;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.SDKFactory;
import com.uc.webview.export.internal.setup.BaseSetupTask;
import com.uc.webview.export.internal.utility.Log;

/* compiled from: U4Source */
final class h implements ValueCallback<BaseSetupTask> {
    final /* synthetic */ a a;

    h(a aVar) {
        this.a = aVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        BaseSetupTask baseSetupTask = (BaseSetupTask) obj;
        String a2 = a.a;
        Log.d(a2, "mSuccessCallback " + baseSetupTask.toString() + " init type: " + baseSetupTask.getInitType());
        this.a.g.f = String.valueOf(this.a.g.a.getMilis());
        this.a.g.g = String.valueOf(this.a.g.a.getMilisCpu());
        if (SDKFactory.d() != 2) {
            this.a.c.a(a.d.h);
            a.a(this.a, UCCore.EVENT_INIT_CORE_SUCCESS, baseSetupTask);
        } else {
            this.a.c.a(a.d.i);
        }
        if (UCCore.BUSINESS_INIT_BY_NEW_CORE_ZIP_FILE.equals(baseSetupTask.getInitType())) {
            o.a((String) this.a.getOption(UCCore.OPTION_BUSINESS_DECOMPRESS_ROOT_PATH), (String) this.a.getOption(UCCore.OPTION_NEW_UCM_ZIP_FILE));
        } else if (UCCore.BUSINESS_INIT_BY_OLD_CORE_DEX_DIR.equals(baseSetupTask.getInitType())) {
            a.g(this.a);
        }
    }
}
