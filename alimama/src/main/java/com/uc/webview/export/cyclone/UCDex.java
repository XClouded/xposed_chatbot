package com.uc.webview.export.cyclone;

import android.content.Context;
import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;
import java.io.IOException;

@Constant
/* compiled from: U4Source */
public class UCDex implements Runnable {
    public static DexClassLoader createDexClassLoader(Context context, Boolean bool, String str, String str2, String str3, ClassLoader classLoader) {
        return getUCDex().createDexClassLoader(context, bool, str, str2, str3, classLoader);
    }

    public static DexFile createDexFile(Context context, Boolean bool, String str, String str2, int i) throws IOException {
        return getUCDex().createDexFile(context, bool, str, str2, i);
    }

    private static com.uc.webview.export.cyclone.service.UCDex getUCDex() {
        com.uc.webview.export.cyclone.service.UCDex uCDex = (com.uc.webview.export.cyclone.service.UCDex) UCService.initImpl(com.uc.webview.export.cyclone.service.UCDex.class);
        if (uCDex != null) {
            return uCDex;
        }
        throw new UCKnownException((int) ErrorCode.UCSERVICE_UCDEX_IMPL_NOT_FOUND, "The implement of UCDex service is not registered.");
    }

    public void run() {
        getUCDex().run();
    }
}
