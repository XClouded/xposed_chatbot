package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.SecurityGuardParamContext;
import com.taobao.wireless.security.sdk.indiekit.IIndieKitComponent;
import java.util.HashMap;

@Deprecated
public class SecurityCheck {
    private SecurityGuardManager manager;
    private IIndieKitComponent proxy;

    public int validateFileSignature(String str, String str2, String str3) {
        return -1;
    }

    public SecurityCheck(ContextWrapper contextWrapper) {
        this.manager = SecurityGuardManager.getInstance(contextWrapper);
        if (this.manager != null) {
            this.proxy = this.manager.getIndieKitComp();
        }
    }

    public String getCheckSignature(String str) {
        DataContext dataContext = new DataContext();
        dataContext.index = 0;
        return getCheckSignature(str, dataContext);
    }

    public String getCheckSignature(String str, DataContext dataContext) {
        if (this.proxy == null || str == null || dataContext == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("timestamp", str);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 1;
        if (dataContext.extData == null || "".equals(dataContext.extData)) {
            dataContext.index = dataContext.index < 0 ? 0 : dataContext.index;
            String appKeyByIndex = this.manager.getStaticDataStoreComp().getAppKeyByIndex(dataContext.index);
            if (appKeyByIndex == null || "".equals(appKeyByIndex)) {
                return null;
            }
            securityGuardParamContext.appKey = appKeyByIndex;
        } else {
            securityGuardParamContext.appKey = new String(dataContext.extData);
        }
        return this.proxy.indieKitRequest(securityGuardParamContext);
    }

    public String indieKitRequest(SecurityGuardParamContext securityGuardParamContext) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.indieKitRequest(securityGuardParamContext);
    }

    public int reportSusText(String str, String str2) {
        throw new UnsupportedOperationException();
    }
}
