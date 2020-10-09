package com.uc.webview.export.cyclone.service;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import com.taobao.weex.ui.component.WXComponent;
import com.uc.webview.export.cyclone.Constant;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.cyclone.UCKnownException;
import com.uc.webview.export.cyclone.UCLibrary;
import com.uc.webview.export.cyclone.UCLogger;
import com.uc.webview.export.cyclone.UCService;
import java.io.File;
import java.io.IOException;

@Constant
/* compiled from: U4Source */
public class UCUnSevenZipMultiThreadImpl implements UCUnSevenZip {
    private static final String LOG_TAG = "UCUnSevenZipMultiThreadImplConstant";
    private static String mFailedFilePath = null;
    private static boolean mSoIsLoaded = false;
    private static UCKnownException mSoIsLoadedException = null;
    private static final boolean sSupportArm64 = true;

    /* compiled from: U4Source */
    enum ArchType {
        Arm,
        Arm64,
        Unknown
    }

    private static native int dec7z(String str, String str2, String str3);

    public int getServiceVersion() {
        return 0;
    }

    static {
        try {
            UCService.registerImpl(UCUnSevenZip.class, new UCUnSevenZipMultiThreadImpl());
        } catch (Throwable th) {
            UCLogger create = UCLogger.create(WXComponent.PROP_FS_WRAP_CONTENT, LOG_TAG);
            if (create != null) {
                create.print("UCUnSevenZipMultiThreadImplConstant register exception:", th);
            }
        }
    }

    public int deccompress(Context context, String str, String str2) {
        loadSo(context, getCurrentArch(str));
        int dec7z = dec7z(str, str2, "");
        UCLogger create = !UCCyclone.enableDebugLog ? null : UCLogger.create("d", LOG_TAG);
        if (create != null) {
            create.print("UCUnSevenZipMultiThreadImpl.dec ret=" + dec7z, new Throwable[0]);
        }
        return dec7z;
    }

    public int deccompress(Context context, String str, String str2, String str3) {
        loadSo(context, getCurrentArch(str));
        int dec7z = dec7z(str, str2, str3);
        UCLogger create = !UCCyclone.enableDebugLog ? null : UCLogger.create("d", LOG_TAG);
        if (create != null) {
            create.print("UCUnSevenZipMultiThreadImpl.dec ret=" + dec7z, new Throwable[0]);
        }
        return dec7z;
    }

    private static ArchType getCurrentArch(String str) {
        ArchType archType = ArchType.Unknown;
        if (Build.VERSION.SDK_INT >= 23) {
            if (Process.is64Bit()) {
                return ArchType.Arm64;
            }
            return ArchType.Arm;
        } else if (str.indexOf("/lib/arm64/") > 0) {
            return ArchType.Arm64;
        } else {
            return str.indexOf("/lib/arm/") > 0 ? ArchType.Arm : archType;
        }
    }

    private static void loadSoImpl(Context context, ArchType archType) throws IOException {
        File file;
        try {
            if (ArchType.Arm == archType) {
                file = UCCyclone.genFile(context, (String) null, "libdec7zmt-arm", ".so", 25977809, "b815846a1a67c293fea7d09625a9ced1", UCUnSevenZipMultiThreadImplConstantArm.genCodes(), new Object[0]);
            } else if (ArchType.Arm64 == archType) {
                file = UCCyclone.genFile(context, (String) null, "libdec7zmt-arm64", ".so", 25936580, "8e50b80c2ff7c7f2f62825e4e4ca4101", UCUnSevenZipMultiThreadImplConstantArm64.genCodes(), new Object[0]);
            } else {
                file = null;
            }
            if (file != null) {
                UCLibrary.load(context, file.getAbsolutePath(), (ClassLoader) null);
                return;
            }
            throw new UCKnownException("arch not support");
        } catch (IOException e) {
            throw e;
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:15|16|17|18) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x001c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static synchronized void loadSo(android.content.Context r2, com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.ArchType r3) {
        /*
            java.lang.Class<com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl> r0 = com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.class
            monitor-enter(r0)
            boolean r1 = mSoIsLoaded     // Catch:{ all -> 0x003d }
            if (r1 == 0) goto L_0x0009
            monitor-exit(r0)
            return
        L_0x0009:
            com.uc.webview.export.cyclone.UCKnownException r1 = mSoIsLoadedException     // Catch:{ all -> 0x003d }
            if (r1 != 0) goto L_0x003a
            com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl$ArchType r1 = com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.ArchType.Arm     // Catch:{ Throwable -> 0x0031 }
            if (r1 == r3) goto L_0x0029
            com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl$ArchType r1 = com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.ArchType.Arm64     // Catch:{ Throwable -> 0x0031 }
            if (r1 != r3) goto L_0x0016
            goto L_0x0029
        L_0x0016:
            com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl$ArchType r3 = com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.ArchType.Arm     // Catch:{ Throwable -> 0x001c }
            loadSoImpl(r2, r3)     // Catch:{ Throwable -> 0x001c }
            goto L_0x002c
        L_0x001c:
            com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl$ArchType r3 = com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.ArchType.Arm64     // Catch:{ Throwable -> 0x0022 }
            loadSoImpl(r2, r3)     // Catch:{ Throwable -> 0x0022 }
            goto L_0x002c
        L_0x0022:
            r2 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ Throwable -> 0x0031 }
            r3.<init>((java.lang.Throwable) r2)     // Catch:{ Throwable -> 0x0031 }
            throw r3     // Catch:{ Throwable -> 0x0031 }
        L_0x0029:
            loadSoImpl(r2, r3)     // Catch:{ Throwable -> 0x0031 }
        L_0x002c:
            r2 = 1
            mSoIsLoaded = r2     // Catch:{ all -> 0x003d }
            monitor-exit(r0)
            return
        L_0x0031:
            r2 = move-exception
            com.uc.webview.export.cyclone.UCKnownException r3 = new com.uc.webview.export.cyclone.UCKnownException     // Catch:{ all -> 0x003d }
            r3.<init>((java.lang.Throwable) r2)     // Catch:{ all -> 0x003d }
            mSoIsLoadedException = r3     // Catch:{ all -> 0x003d }
            throw r3     // Catch:{ all -> 0x003d }
        L_0x003a:
            com.uc.webview.export.cyclone.UCKnownException r2 = mSoIsLoadedException     // Catch:{ all -> 0x003d }
            throw r2     // Catch:{ all -> 0x003d }
        L_0x003d:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl.loadSo(android.content.Context, com.uc.webview.export.cyclone.service.UCUnSevenZipMultiThreadImpl$ArchType):void");
    }

    public static void saveFailedFilePath(String str) {
        mFailedFilePath = str;
    }

    public String failedFilePath() {
        return mFailedFilePath;
    }
}
