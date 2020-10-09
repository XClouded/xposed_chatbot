package com.uc.webview.export.internal.utility;

import com.uc.webview.export.Build;
import java.util.HashMap;

/* compiled from: U4Source */
final class q extends HashMap<String, String> {
    q() {
        put("ucBuildVersion", String.format("ucbs %s.%s-impl %s.%s", new Object[]{Build.Version.NAME, Build.TIME, Build.UCM_VERSION, Build.CORE_TIME}));
    }
}
