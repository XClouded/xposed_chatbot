package com.uc.crashsdk;

import java.io.File;
import java.io.FilenameFilter;

/* compiled from: ProGuard */
final class g implements FilenameFilter {
    g() {
    }

    public final boolean accept(File file, String str) {
        return com.uc.crashsdk.a.g.b(str) && str.endsWith(".st");
    }
}
