package com.taobao.android.sso.v2.security;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.log.TLogAdapter;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import mtopsdk.common.util.SymbolExpUtil;

public class SSOSecurityService {
    public static final String TAG = "Login.SSOSecurityService";
    private static SecurityGuardManager mSecurityGuardManager;
    private static ISecureSignatureComponent signComponent;
    private static SSOSecurityService ssoSecurityService;

    public static SSOSecurityService getInstace(Context context) throws SecException {
        if (ssoSecurityService == null) {
            ssoSecurityService = new SSOSecurityService();
            mSecurityGuardManager = SecurityGuardManager.getInstance(new ContextWrapper(context));
            if (mSecurityGuardManager != null) {
                signComponent = mSecurityGuardManager.getSecureSignatureComp();
            }
        }
        return ssoSecurityService;
    }

    private SSOSecurityService() {
    }

    private static ISecureSignatureComponent getSignComponent() {
        if (signComponent == null && mSecurityGuardManager != null) {
            signComponent = mSecurityGuardManager.getSecureSignatureComp();
        }
        return signComponent;
    }

    public String sign(String str, TreeMap<String, String> treeMap, String str2) throws SecException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : treeMap.entrySet()) {
            sb.append((String) next.getKey());
            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
            sb.append((String) next.getValue());
            sb.append("&");
        }
        return sign(str, sb.substring(0, sb.length() - 1), str2);
    }

    public static String sign(String str, String str2, String str3) throws SecException {
        HashMap hashMap = new HashMap();
        hashMap.put("INPUT", str2);
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("ATLAS", str3);
        }
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.appKey = str;
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 5;
        String str4 = "";
        if (getSignComponent() != null) {
            str4 = getSignComponent().signRequest(securityGuardParamContext, "");
        }
        if (Debuggable.isDebug()) {
            TLogAdapter.e(TAG, "sign = " + str4 + " ,appKey = " + str + ", beSigned = " + str2);
        }
        return str4;
    }

    public static String sign(String str, String str2) throws SecException {
        HashMap hashMap = new HashMap();
        hashMap.put("INPUT", str2);
        if (DataProviderFactory.getDataProvider().getEnvType() == 1) {
            hashMap.put("ATLAS", "daily");
        }
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.appKey = str;
        securityGuardParamContext.paramMap = hashMap;
        securityGuardParamContext.requestType = 5;
        String str3 = "";
        if (getSignComponent() != null) {
            str3 = getSignComponent().signRequest(securityGuardParamContext, "");
        }
        if (Debuggable.isDebug()) {
            TLogAdapter.d(TAG, "sign = " + str3 + " ,appKey = " + str + ", beSigned = " + str2);
        }
        return str3;
    }

    public String sign(String str, TreeMap<String, String> treeMap) throws SecException {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : treeMap.entrySet()) {
            sb.append((String) next.getKey());
            sb.append(SymbolExpUtil.SYMBOL_EQUAL);
            sb.append((String) next.getValue());
            sb.append("&");
        }
        return sign(str, sb.substring(0, sb.length() - 1));
    }
}
