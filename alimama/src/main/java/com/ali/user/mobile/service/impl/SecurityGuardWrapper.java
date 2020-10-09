package com.ali.user.mobile.service.impl;

import android.content.Context;
import android.content.ContextWrapper;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.exception.SecRuntimeException;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.service.StorageService;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;

public class SecurityGuardWrapper implements StorageService {
    public static final String TAG = "SecurityGuardWrapper";

    public void init(Context context) {
        try {
            SecurityGuardManager.getInstance(new ContextWrapper(context));
        } catch (SecException unused) {
        }
    }

    private SecurityGuardManager getSecurityGuardManager() {
        try {
            return SecurityGuardManager.getInstance(DataProviderFactory.getApplicationContext());
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public String getAppKey(int i) {
        try {
            return getSecurityGuardManager().getStaticDataStoreComp().getAppKeyByIndex(i, "");
        } catch (SecException e) {
            TLogAdapter.d(TAG, "can't get appkey from blackbox." + e.getMessage());
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public void put(String str, String str2) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        try {
            SecurityGuardManager instance = SecurityGuardManager.getInstance(DataProviderFactory.getApplicationContext());
            if (instance != null && (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) != null) {
                dynamicDataStoreComp.putStringDDpEx(str, str2, 0);
            }
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x000a, code lost:
        r0 = r0.getDynamicDataStoreComp();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String get(java.lang.String r3) {
        /*
            r2 = this;
            android.content.Context r0 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getApplicationContext()     // Catch:{ SecException -> 0x0019 }
            com.alibaba.wireless.security.open.SecurityGuardManager r0 = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(r0)     // Catch:{ SecException -> 0x0019 }
            if (r0 == 0) goto L_0x0016
            com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent r0 = r0.getDynamicDataStoreComp()     // Catch:{ SecException -> 0x0019 }
            if (r0 == 0) goto L_0x0016
            r1 = 0
            java.lang.String r3 = r0.getStringDDpEx(r3, r1)     // Catch:{ SecException -> 0x0019 }
            return r3
        L_0x0016:
            java.lang.String r3 = ""
            return r3
        L_0x0019:
            r3 = move-exception
            com.ali.user.mobile.exception.SecRuntimeException r0 = new com.ali.user.mobile.exception.SecRuntimeException
            int r1 = r3.getErrorCode()
            r0.<init>(r1, r3)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.service.impl.SecurityGuardWrapper.get(java.lang.String):java.lang.String");
    }

    public void remove(String str) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        try {
            SecurityGuardManager instance = SecurityGuardManager.getInstance(DataProviderFactory.getApplicationContext());
            if (instance != null && (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) != null) {
                dynamicDataStoreComp.removeStringDDpEx(str, 0);
            }
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }
}
