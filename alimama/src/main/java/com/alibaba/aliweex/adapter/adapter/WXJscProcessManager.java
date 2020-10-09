package com.alibaba.aliweex.adapter.adapter;

import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.aliweex.utils.WXUtil;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXJscProcessManager;

public class WXJscProcessManager implements IWXJscProcessManager {
    long DEFAULT_REBOOT_JSC_TIMEOUT = 5000;

    public boolean enableBackupThread() {
        IConfigAdapter configAdapter;
        if (!WXUtil.isTaobao() || (configAdapter = AliWeex.getInstance().getConfigAdapter()) == null) {
            return false;
        }
        WXInitConfigManager instance = WXInitConfigManager.getInstance();
        return "true".equals(configAdapter.getConfig("android_weex_ext_config", WXInitConfigManager.key_enableBackUpThread, instance.getFromConfigKV(instance.c_enableBackUpThread)));
    }

    public boolean enableBackUpThreadCache() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return true;
        }
        WXInitConfigManager instance = WXInitConfigManager.getInstance();
        return "true".equals(configAdapter.getConfig("android_weex_ext_config", WXInitConfigManager.key_enableBackUpThreadCache, instance.getFromConfigKV(instance.c_enableBackUpThreadCache)));
    }

    public boolean shouldReboot() {
        if (WXEnvironment.isApkDebugable()) {
            return false;
        }
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return true;
        }
        return "true".equals(configAdapter.getConfig("android_weex_ext_config", "enableRebootJsc", "true"));
    }

    public long rebootTimeout() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return this.DEFAULT_REBOOT_JSC_TIMEOUT;
        }
        try {
            return (long) Integer.parseInt(configAdapter.getConfig("android_weex_ext_config", "rebootJscTimeout", String.valueOf(this.DEFAULT_REBOOT_JSC_TIMEOUT)));
        } catch (Exception unused) {
            return this.DEFAULT_REBOOT_JSC_TIMEOUT;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027 A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean withException(com.taobao.weex.WXSDKInstance r3) {
        /*
            r2 = this;
            r0 = 0
            if (r3 == 0) goto L_0x0024
            android.content.Context r3 = r3.getContext()
            if (r3 == 0) goto L_0x0024
            java.lang.Class r1 = r3.getClass()
            if (r1 == 0) goto L_0x0024
            java.lang.Class r3 = r3.getClass()
            java.lang.String r3 = r3.getName()
            boolean r1 = android.text.TextUtils.isEmpty(r3)
            if (r1 != 0) goto L_0x0024
            java.lang.String r1 = "WXActivity"
            boolean r3 = r3.contains(r1)
            goto L_0x0025
        L_0x0024:
            r3 = 0
        L_0x0025:
            if (r3 != 0) goto L_0x0028
            r0 = 1
        L_0x0028:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.adapter.adapter.WXJscProcessManager.withException(com.taobao.weex.WXSDKInstance):boolean");
    }
}
