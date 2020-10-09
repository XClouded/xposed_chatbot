package com.uc.webview.export.internal.utility;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: U4Source */
final class r implements FilenameFilter {
    r() {
    }

    public final boolean accept(File file, String str) {
        return str.startsWith("uc_temp_dec_");
    }
}
