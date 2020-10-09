package com.uploader.portal;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.ta.utdid2.device.UTDevice;
import com.uploader.export.IUploaderEnvironment;
import java.util.HashMap;

@Deprecated
public class UploaderEnvironmentImpl implements IUploaderEnvironment {
    private volatile String authCode;
    private final Context context;
    private volatile String dailyAppKey = "4272";
    private volatile int environment = 0;
    private final int instanceType;
    private volatile String onlineAppKey = "21646297";
    private volatile String prepareAppKey = "21646297";
    private volatile String utdid;

    public boolean enableFlowControl() {
        return false;
    }

    public String getDomain() {
        return null;
    }

    public int getInstanceType() {
        return 0;
    }

    public String getUserId() {
        return null;
    }

    public UploaderEnvironmentImpl(Context context2) {
        this.context = context2;
        this.instanceType = 0;
    }

    public int getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(int i) {
        this.environment = i;
    }

    public String getUtdid() {
        if (this.utdid != null) {
            return this.utdid;
        }
        try {
            this.utdid = UTDevice.getUtdid(this.context);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return this.utdid;
    }

    public String getAppKey() {
        switch (getEnvironment()) {
            case 0:
                return this.onlineAppKey;
            case 1:
                return this.prepareAppKey;
            case 2:
                return this.dailyAppKey;
            default:
                return this.onlineAppKey;
        }
    }

    public void setAppKey(String str, String str2, String str3) {
        this.onlineAppKey = str;
        this.prepareAppKey = str2;
        this.dailyAppKey = str3;
    }

    public String getAppVersion() {
        try {
            String str = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
            return TextUtils.isEmpty(str) ? "0" : str;
        } catch (Throwable unused) {
            return "0";
        }
    }

    public String signature(String str) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("INPUT", str);
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.appKey = getAppKey();
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 3;
        try {
            return SecurityGuardManager.getInstance(this.context).getSecureSignatureComp().signRequest(securityGuardParamContext, this.authCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int putSslTicket(Context context2, String str, byte[] bArr) {
        try {
            return SecurityGuardManager.getInstance(context2).getDynamicDataStoreComp().putByteArray(str, bArr);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] getSslTicket(Context context2, String str) {
        try {
            return SecurityGuardManager.getInstance(context2).getDynamicDataStoreComp().getByteArray(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] decrypt(Context context2, String str, byte[] bArr) {
        try {
            return SecurityGuardManager.getInstance(context2).getStaticDataEncryptComp().staticBinarySafeDecryptNoB64(16, str, bArr, this.authCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAuthCode(String str) {
        this.authCode = str;
    }
}
