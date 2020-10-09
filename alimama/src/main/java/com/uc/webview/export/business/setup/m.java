package com.uc.webview.export.business.setup;

import com.uc.webview.export.extension.UCCore;
import java.util.HashMap;

/* compiled from: U4Source */
final class m extends HashMap<String, String> {
    final /* synthetic */ a a;

    m(a aVar) {
        this.a = aVar;
        put(UCCore.EVENT_INIT_CORE_SUCCESS, "success");
        put("die_delegate", UCCore.EVENT_DIE);
    }
}
