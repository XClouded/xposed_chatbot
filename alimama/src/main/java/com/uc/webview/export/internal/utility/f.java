package com.uc.webview.export.internal.utility;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: U4Source */
final class f implements FilenameFilter {
    f() {
    }

    public final boolean accept(File file, String str) {
        return str.contains("sdk_shell");
    }
}
