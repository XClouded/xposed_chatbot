package com.uc.webview.export.internal.utility;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: U4Source */
final class o implements FilenameFilter {
    o() {
    }

    public final boolean accept(File file, String str) {
        return str.startsWith("lib") && str.endsWith("_kr_uc.so");
    }
}
