package com.uc.webview.export.internal.setup;

import android.webkit.ValueCallback;
import com.taobao.android.dxcontainer.DXContainerErrorConstant;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.uc.startup.b;
import com.uc.webview.export.internal.utility.k;

/* compiled from: U4Source */
final class v implements ValueCallback<l> {
    final /* synthetic */ o a;

    v(o oVar) {
        this.a = oVar;
    }

    public final /* synthetic */ void onReceiveValue(Object obj) {
        String str;
        l lVar = (l) obj;
        if (k.g()) {
            str = "ThickSetupTask_" + ((String) lVar.getOption(UCCore.OPTION_SO_FILE_PATH));
        } else {
            str = "";
        }
        if (str == null) {
            str = (String) lVar.getOption(UCCore.OPTION_DEX_FILE_PATH);
        }
        if (str == null) {
            str = (String) lVar.getOption(UCCore.OPTION_UCM_ZIP_FILE);
        }
        if (str == null) {
            str = (String) lVar.getOption(UCCore.OPTION_UCM_LIB_DIR);
        }
        if (str == null) {
            str = (String) lVar.getOption(UCCore.OPTION_UCM_KRL_DIR);
        }
        if (str == null) {
            str = (String) lVar.getOption(UCCore.OPTION_UCM_CFG_FILE);
        }
        lVar.setException(new UCSetupException((int) DXContainerErrorConstant.DX_CONTAINER_ERROR_INSERT_MODELS_NO_CHILD, String.format("Multi crash detected in [%s].", new Object[]{str})));
        lVar.onEvent(UCCore.EVENT_DIE, (ValueCallback) null);
        b.a(197, lVar.getException().toString());
    }
}
