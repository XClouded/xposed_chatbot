package com.alipay.auth.mobile.common;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.alipay.auth.mobile.api.IAlipayAuthMonitor;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.staticdataencrypt.IStaticDataEncryptComponent;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class AlipayAuthUtils {
    public static String strJoint(Map<String, String> map) {
        return strJoint((String) null, map);
    }

    public static String strJoint(String str, Map<String, String> map) {
        String str2;
        if (map == null || map.size() <= 0) {
            str2 = null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry next : map.entrySet()) {
                sb.append((String) next.getKey());
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append((String) next.getValue());
                sb.append("&");
            }
            str2 = sb.substring(0, sb.lastIndexOf("&"));
        }
        if (!TextUtils.isEmpty(str)) {
            str2 = str + "&" + str2;
        }
        LoggerUtils.d("AlipayAuthUtils", str2);
        return str2;
    }

    public static String encrypt(Context context, String str, String str2, IAlipayAuthMonitor iAlipayAuthMonitor) {
        String str3;
        try {
            LoggerUtils.d("AlipayAuthUtils", "encrypt param appkey=" + str + " data=" + str2);
            SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
            if (instance != null) {
                LoggerUtils.d("AlipayAuthUtils", "sgMgr != null");
                IStaticDataEncryptComponent staticDataEncryptComp = instance.getStaticDataEncryptComp();
                if (staticDataEncryptComp != null) {
                    LoggerUtils.d("AlipayAuthUtils", "comp != null");
                    str3 = staticDataEncryptComp.staticSafeEncrypt(16, str, str2);
                    try {
                        LoggerUtils.d("AlipayAuthUtils", "encryptData=" + str3);
                        return str3;
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            }
            return "";
        } catch (Throwable th2) {
            th = th2;
            str3 = "";
            LoggerUtils.e("AlipayAuthUtils", "encrypt error", th);
            MonitorAlipayAuth.getInstance().monitorAlipayAuth(iAlipayAuthMonitor, "AliPayAuth_EncError");
            return str3;
        }
    }

    public static String atlasSignData(Context context, String str, String str2, IAlipayAuthMonitor iAlipayAuthMonitor) {
        String str3;
        try {
            com.alibaba.wireless.security.open.SecurityGuardManager instance = com.alibaba.wireless.security.open.SecurityGuardManager.getInstance(context);
            LoggerUtils.d("AlipayAuthUtils", "获取sgMgr对象");
            if (instance != null) {
                LoggerUtils.d("AlipayAuthUtils", "sgMgr not null");
                ISecureSignatureComponent secureSignatureComp = instance.getSecureSignatureComp();
                HashMap hashMap = new HashMap();
                hashMap.put("INPUT", str2);
                hashMap.put("ATLAS", "a");
                SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                securityGuardParamContext.appKey = str;
                securityGuardParamContext.paramMap = hashMap;
                securityGuardParamContext.requestType = 5;
                str3 = secureSignatureComp.signRequest(securityGuardParamContext, "");
                try {
                    LoggerUtils.d("AlipayAuthUtils", "sign = " + str3);
                } catch (SecException e) {
                    e = e;
                } catch (Throwable th) {
                    th = th;
                    LoggerUtils.e("AlipayAuthUtils", "atlasSignData error", th);
                    MonitorAlipayAuth.getInstance().monitorAlipayAuth(iAlipayAuthMonitor, "AliPayAuth_SignError");
                    LoggerUtils.d("AlipayAuthUtils", "end sign = " + str3);
                    return str3;
                }
            } else {
                str3 = "";
            }
        } catch (SecException e2) {
            e = e2;
            str3 = "";
            LoggerUtils.e("AlipayAuthUtils", "atlasSignData SecException error", e);
            MonitorAlipayAuth.getInstance().monitorAlipayAuth(iAlipayAuthMonitor, "AliPayAuth_SignError");
            LoggerUtils.d("AlipayAuthUtils", "end sign = " + str3);
            return str3;
        } catch (Throwable th2) {
            th = th2;
            str3 = "";
            LoggerUtils.e("AlipayAuthUtils", "atlasSignData error", th);
            MonitorAlipayAuth.getInstance().monitorAlipayAuth(iAlipayAuthMonitor, "AliPayAuth_SignError");
            LoggerUtils.d("AlipayAuthUtils", "end sign = " + str3);
            return str3;
        }
        LoggerUtils.d("AlipayAuthUtils", "end sign = " + str3);
        return str3;
    }
}
