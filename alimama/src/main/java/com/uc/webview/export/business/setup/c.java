package com.uc.webview.export.business.setup;

import com.uc.webview.export.extension.UCCore;
import java.util.HashMap;

/* compiled from: U4Source */
final class c extends HashMap<String, Object> {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
        put(UCCore.OPTION_BUSINESS_INIT_TYPE, UCCore.BUSINESS_INIT_BY_NEW_CORE_DEX_DIR);
        put(UCCore.OPTION_UCM_ZIP_FILE, (Object) null);
        put(UCCore.OPTION_NOT_USE_7Z_CORE, true);
        put(UCCore.OPTION_DEX_FILE_PATH, this.a.f());
    }
}
