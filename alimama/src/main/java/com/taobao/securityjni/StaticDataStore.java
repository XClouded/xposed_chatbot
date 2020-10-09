package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent;

@Deprecated
public class StaticDataStore {
    public static final int APP_KEY_TYPE = 1;
    public static final int EXTRA_KEY_TYPE = 3;
    public static final int INVALID_KEY_TYPE = 4;
    public static final int SECURITY_KEY_TYPE = 2;
    private IStaticDataStoreComponent proxy;

    @Deprecated
    public String getMMPid() {
        return null;
    }

    @Deprecated
    public String getTtid() {
        return null;
    }

    public StaticDataStore(ContextWrapper contextWrapper) {
        SecurityGuardManager instance = SecurityGuardManager.getInstance(contextWrapper);
        if (instance != null) {
            this.proxy = instance.getStaticDataStoreComp();
        }
    }

    @Deprecated
    public String getAppKey() {
        return getAppKey(new DataContext(0, (byte[]) null));
    }

    public String getAppKey(DataContext dataContext) {
        if (dataContext == null) {
            return null;
        }
        return getAppKeyByIndex(dataContext.index < 0 ? 0 : dataContext.index);
    }

    public String getExtraData(String str) {
        if (this.proxy == null || str == null) {
            return null;
        }
        return this.proxy.getExtraData(str);
    }

    public int getKeyType(String str) {
        if (this.proxy == null || str == null) {
            return 4;
        }
        return this.proxy.getKeyType(str);
    }

    public String getAppKeyByIndex(int i) {
        if (this.proxy == null || i < 0 || i > 8) {
            return null;
        }
        return this.proxy.getAppKeyByIndex(i);
    }
}
