package anet.channel.security;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.ALog;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.taobao.orange.OConstant;
import java.util.HashMap;
import java.util.Map;

class SecurityGuardImpl implements ISecurity {
    private static String TAG = "awcn.DefaultSecurityGuard";
    private static Map<String, Integer> algorithMap = null;
    private static boolean mSecurityGuardValid = false;
    private String authCode = null;

    public boolean isSecOff() {
        return false;
    }

    static {
        try {
            Class.forName(OConstant.REFLECT_SECURITYGUARD);
            mSecurityGuardValid = true;
            algorithMap = new HashMap();
            algorithMap.put(ISecurity.SIGN_ALGORITHM_HMAC_SHA1, 3);
            algorithMap.put(ISecurity.CIPHER_ALGORITHM_AES128, 16);
        } catch (Throwable unused) {
            mSecurityGuardValid = false;
        }
    }

    SecurityGuardImpl(String str) {
        this.authCode = str;
    }

    public String sign(Context context, String str, String str2, String str3) {
        if (!mSecurityGuardValid || context == null || TextUtils.isEmpty(str2) || !algorithMap.containsKey(str)) {
            return null;
        }
        try {
            ISecureSignatureComponent secureSignatureComp = SecurityGuardManager.getInstance(context).getSecureSignatureComp();
            if (secureSignatureComp != null) {
                SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                securityGuardParamContext.appKey = str2;
                securityGuardParamContext.paramMap.put("INPUT", str3);
                securityGuardParamContext.requestType = algorithMap.get(str).intValue();
                return secureSignatureComp.signRequest(securityGuardParamContext, this.authCode);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "Securityguard sign request failed.", (String) null, th, new Object[0]);
        }
        return null;
    }

    public byte[] decrypt(Context context, String str, String str2, byte[] bArr) {
        Integer num;
        IStaticDataEncryptComponent staticDataEncryptComp;
        if (!mSecurityGuardValid || context == null || bArr == null || TextUtils.isEmpty(str2) || !algorithMap.containsKey(str) || (num = algorithMap.get(str)) == null) {
            return null;
        }
        try {
            SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
            if (!(instance == null || (staticDataEncryptComp = instance.getStaticDataEncryptComp()) == null)) {
                return staticDataEncryptComp.staticBinarySafeDecryptNoB64(num.intValue(), str2, bArr, this.authCode);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "staticBinarySafeDecryptNoB64", (String) null, th, new Object[0]);
        }
        return null;
    }

    public boolean saveBytes(Context context, String str, byte[] bArr) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        if (context == null || bArr == null || TextUtils.isEmpty(str)) {
            return false;
        }
        try {
            SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
            if (instance == null || (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) == null || dynamicDataStoreComp.putByteArray(str, bArr) == 0) {
                return false;
            }
            return true;
        } catch (Throwable th) {
            ALog.e(TAG, "saveBytes", (String) null, th, new Object[0]);
            return false;
        }
    }

    public byte[] getBytes(Context context, String str) {
        IDynamicDataStoreComponent dynamicDataStoreComp;
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
            if (instance == null || (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) == null) {
                return null;
            }
            return dynamicDataStoreComp.getByteArray(str);
        } catch (Throwable th) {
            ALog.e(TAG, "getBytes", (String) null, th, new Object[0]);
            return null;
        }
    }
}
