package com.uploader.portal;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.ta.utdid2.device.UTDevice;
import com.uploader.export.UploaderEnvironment;
import com.uploader.export.UploaderGlobal;
import java.util.HashMap;

public class UploaderEnvironmentImpl2 extends UploaderEnvironment {
    private final Context context;
    private volatile int environment;
    private volatile String utdid;

    public boolean enableFlowControl() {
        return false;
    }

    public String getUserId() {
        return null;
    }

    public UploaderEnvironmentImpl2(Context context2, int i) {
        super(i);
        this.environment = 0;
        if (context2 == null) {
            this.context = UploaderGlobal.retrieveContext();
        } else {
            this.context = context2;
        }
    }

    public UploaderEnvironmentImpl2(Context context2) {
        this(context2, 0);
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
            return SecurityGuardManager.getInstance(this.context).getSecureSignatureComp().signRequest(securityGuardParamContext, getCurrentElement().authCode);
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
            return SecurityGuardManager.getInstance(context2).getStaticDataEncryptComp().staticBinarySafeDecryptNoB64(16, str, bArr, getCurrentElement().authCode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
