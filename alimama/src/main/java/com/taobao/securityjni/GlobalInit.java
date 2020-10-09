package com.taobao.securityjni;

import android.content.Context;
import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent;

@Deprecated
public final class GlobalInit {
    private static Context globalContext;
    private static String sAppKey;

    public static void setEnableOutPutExpInfo(boolean z) {
    }

    public static synchronized void SetGlobalAppKey(String str) {
        synchronized (GlobalInit.class) {
            sAppKey = str;
        }
    }

    public static synchronized String GetGlobalAppKey() {
        String str;
        synchronized (GlobalInit.class) {
            str = sAppKey;
        }
        return str;
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    public static void GlobalSecurityInitSync(ContextWrapper contextWrapper) {
        GlobalSecurityInitSync(contextWrapper, (String) null);
    }

    public static void GlobalSecurityInitSync(ContextWrapper contextWrapper, String str) {
        globalContext = contextWrapper;
        SecurityGuardManager.getInitializer().loadLibrarySync(contextWrapper, str);
    }

    public static void GlobalSecurityInitSyncSo(ContextWrapper contextWrapper) {
        GlobalSecurityInitSyncSo(contextWrapper, (String) null);
    }

    public static void GlobalSecurityInitSyncSo(ContextWrapper contextWrapper, String str) {
        globalContext = contextWrapper;
        if (SecurityGuardManager.getInitializer().loadLibrarySync(contextWrapper, str) != 0 || SecurityGuardManager.getInstance(contextWrapper) != null) {
        }
    }

    public static void GlobalSecurityInitSyncSDK(ContextWrapper contextWrapper) {
        globalContext = contextWrapper;
        if (SecurityGuardManager.getInitializer().loadLibrarySync(contextWrapper, (String) null) != 0 || SecurityGuardManager.getInstance(contextWrapper) != null) {
        }
    }

    public static void GlobalSecurityInitAsync(ContextWrapper contextWrapper) {
        GlobalSecurityInitAsync(contextWrapper, (String) null);
    }

    public static void GlobalSecurityInitAsync(ContextWrapper contextWrapper, String str) {
        globalContext = contextWrapper;
        SecurityGuardManager.getInitializer().loadLibraryAsync(contextWrapper, str);
    }

    public static void GlobalSecurityInitAsyncSo(ContextWrapper contextWrapper) {
        globalContext = contextWrapper;
    }

    public static void GlobalSecurityInitAsyncSo(ContextWrapper contextWrapper, String str) {
        globalContext = contextWrapper;
        SecurityGuardManager.getInitializer().loadLibraryAsync(contextWrapper, str);
        SecurityGuardManager.getInstance(contextWrapper);
    }

    public static void GlobalSecurityInitAsyncSDK(ContextWrapper contextWrapper) {
        globalContext = contextWrapper;
        SecurityGuardManager.getInitializer().loadLibraryAsync(contextWrapper, (String) null);
        SecurityGuardManager.getInstance(contextWrapper);
    }

    private static void initSecBody(ContextWrapper contextWrapper) {
        ISecurityBodyComponent securityBodyComp;
        SecurityGuardManager instance = SecurityGuardManager.getInstance(contextWrapper);
        if (instance != null && (securityBodyComp = instance.getSecurityBodyComp()) != null) {
            String GetGlobalAppKey = GetGlobalAppKey();
            if (GetGlobalAppKey == null) {
                GetGlobalAppKey = instance.getStaticDataStoreComp().getAppKeyByIndex(0);
            }
            securityBodyComp.initSecurityBody(GetGlobalAppKey);
        }
    }
}
