package com.uc.webview.export.internal.utility;

import com.uc.webview.export.internal.setup.br;
import java.io.File;
import java.util.Comparator;

/* compiled from: U4Source */
final class p implements Comparator<br> {
    p() {
    }

    public final /* synthetic */ int compare(Object obj, Object obj2) {
        long lastModified = new File((String) ((br) obj2).coreImplModule.first).lastModified() - new File((String) ((br) obj).coreImplModule.first).lastModified();
        if (lastModified > 0) {
            return 1;
        }
        return lastModified == 0 ? 0 : -1;
    }
}
