package com.alibaba.motu.tbrest.rest;

import android.content.Context;
import com.alibaba.motu.tbrest.utils.LogUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class RestSecuritySDKRequestAuthentication {
    private String mAppkey = null;
    private boolean mBInitSecurityCheck = false;
    private Context mContext;
    private int s_secureIndex = 1;
    private Object s_secureSignatureCompObj = null;
    private Object s_securityGuardManagerObj = null;
    private Class s_securityGuardParamContextClz = null;
    private Field s_securityGuardParamContext_appKey = null;
    private Field s_securityGuardParamContext_paramMap = null;
    private Field s_securityGuardParamContext_requestType = null;
    private Method s_signRequestMethod = null;

    public String getAppkey() {
        return this.mAppkey;
    }

    public RestSecuritySDKRequestAuthentication(Context context, String str) {
        this.mContext = context;
        this.mAppkey = str;
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0043  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void _initSecurityCheck() {
        /*
            r7 = this;
            monitor-enter(r7)
            boolean r0 = r7.mBInitSecurityCheck     // Catch:{ all -> 0x00c1 }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r7)
            return
        L_0x0007:
            r0 = 0
            r1 = 1
            r2 = 0
            java.lang.String r3 = "com.taobao.wireless.security.sdk.SecurityGuardManager"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Throwable -> 0x003b }
            java.lang.String r4 = "getInstance"
            java.lang.Class[] r5 = new java.lang.Class[r1]     // Catch:{ Throwable -> 0x003c }
            java.lang.Class<android.content.Context> r6 = android.content.Context.class
            r5[r2] = r6     // Catch:{ Throwable -> 0x003c }
            java.lang.reflect.Method r4 = r3.getMethod(r4, r5)     // Catch:{ Throwable -> 0x003c }
            java.lang.Object[] r5 = new java.lang.Object[r1]     // Catch:{ Throwable -> 0x003c }
            android.content.Context r6 = r7.mContext     // Catch:{ Throwable -> 0x003c }
            r5[r2] = r6     // Catch:{ Throwable -> 0x003c }
            java.lang.Object r4 = r4.invoke(r0, r5)     // Catch:{ Throwable -> 0x003c }
            r7.s_securityGuardManagerObj = r4     // Catch:{ Throwable -> 0x003c }
            java.lang.String r4 = "getSecureSignatureComp"
            java.lang.Class[] r5 = new java.lang.Class[r2]     // Catch:{ Throwable -> 0x003c }
            java.lang.reflect.Method r4 = r3.getMethod(r4, r5)     // Catch:{ Throwable -> 0x003c }
            java.lang.Object r5 = r7.s_securityGuardManagerObj     // Catch:{ Throwable -> 0x003c }
            java.lang.Object[] r6 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x003c }
            java.lang.Object r4 = r4.invoke(r5, r6)     // Catch:{ Throwable -> 0x003c }
            r7.s_secureSignatureCompObj = r4     // Catch:{ Throwable -> 0x003c }
            goto L_0x0041
        L_0x003b:
            r3 = r0
        L_0x003c:
            java.lang.String r4 = "initSecurityCheck failure, It's ok "
            com.alibaba.motu.tbrest.utils.LogUtil.i(r4)     // Catch:{ all -> 0x00c1 }
        L_0x0041:
            if (r3 == 0) goto L_0x00bd
            java.lang.String r4 = "com.taobao.wireless.security.sdk.SecurityGuardParamContext"
            java.lang.Class r4 = java.lang.Class.forName(r4)     // Catch:{ Throwable -> 0x00b8 }
            r7.s_securityGuardParamContextClz = r4     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Class r4 = r7.s_securityGuardParamContextClz     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r5 = "appKey"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x00b8 }
            r7.s_securityGuardParamContext_appKey = r4     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Class r4 = r7.s_securityGuardParamContextClz     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r5 = "paramMap"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x00b8 }
            r7.s_securityGuardParamContext_paramMap = r4     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Class r4 = r7.s_securityGuardParamContextClz     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r5 = "requestType"
            java.lang.reflect.Field r4 = r4.getDeclaredField(r5)     // Catch:{ Throwable -> 0x00b8 }
            r7.s_securityGuardParamContext_requestType = r4     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r4 = "isOpen"
            java.lang.Class[] r5 = new java.lang.Class[r2]     // Catch:{ Throwable -> 0x0072 }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r5)     // Catch:{ Throwable -> 0x0072 }
            goto L_0x0078
        L_0x0072:
            java.lang.String r3 = "initSecurityCheck failure, It's ok"
            com.alibaba.motu.tbrest.utils.LogUtil.i(r3)     // Catch:{ Throwable -> 0x00b8 }
            r3 = r0
        L_0x0078:
            if (r3 == 0) goto L_0x0089
            java.lang.Object r0 = r7.s_securityGuardManagerObj     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Object r0 = r3.invoke(r0, r4)     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ Throwable -> 0x00b8 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x00b8 }
            goto L_0x009b
        L_0x0089:
            java.lang.String r3 = "com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent"
            java.lang.Class r3 = java.lang.Class.forName(r3)     // Catch:{ Throwable -> 0x0091 }
            r0 = r3
            goto L_0x0096
        L_0x0091:
            java.lang.String r3 = "initSecurityCheck failure, It's ok"
            com.alibaba.motu.tbrest.utils.LogUtil.i(r3)     // Catch:{ Throwable -> 0x00b8 }
        L_0x0096:
            if (r0 != 0) goto L_0x009a
            r0 = 1
            goto L_0x009b
        L_0x009a:
            r0 = 0
        L_0x009b:
            if (r0 == 0) goto L_0x009f
            r0 = 1
            goto L_0x00a1
        L_0x009f:
            r0 = 12
        L_0x00a1:
            r7.s_secureIndex = r0     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r0 = "com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent"
            java.lang.Class r0 = java.lang.Class.forName(r0)     // Catch:{ Throwable -> 0x00b8 }
            java.lang.String r3 = "signRequest"
            java.lang.Class[] r4 = new java.lang.Class[r1]     // Catch:{ Throwable -> 0x00b8 }
            java.lang.Class r5 = r7.s_securityGuardParamContextClz     // Catch:{ Throwable -> 0x00b8 }
            r4[r2] = r5     // Catch:{ Throwable -> 0x00b8 }
            java.lang.reflect.Method r0 = r0.getMethod(r3, r4)     // Catch:{ Throwable -> 0x00b8 }
            r7.s_signRequestMethod = r0     // Catch:{ Throwable -> 0x00b8 }
            goto L_0x00bd
        L_0x00b8:
            java.lang.String r0 = "initSecurityCheck failure, It's ok"
            com.alibaba.motu.tbrest.utils.LogUtil.i(r0)     // Catch:{ all -> 0x00c1 }
        L_0x00bd:
            r7.mBInitSecurityCheck = r1     // Catch:{ all -> 0x00c1 }
            monitor-exit(r7)
            return
        L_0x00c1:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.rest.RestSecuritySDKRequestAuthentication._initSecurityCheck():void");
    }

    public String getSign(String str) {
        if (!this.mBInitSecurityCheck) {
            _initSecurityCheck();
        }
        if (this.mAppkey == null) {
            LogUtil.e("RestSecuritySDKRequestAuthentication:getSign There is no appkey,please check it!");
            return null;
        } else if (str == null) {
            return null;
        } else {
            if (!(this.s_securityGuardManagerObj == null || this.s_securityGuardParamContextClz == null || this.s_securityGuardParamContext_appKey == null || this.s_securityGuardParamContext_paramMap == null || this.s_securityGuardParamContext_requestType == null || this.s_signRequestMethod == null || this.s_secureSignatureCompObj == null)) {
                try {
                    Object newInstance = this.s_securityGuardParamContextClz.newInstance();
                    this.s_securityGuardParamContext_appKey.set(newInstance, this.mAppkey);
                    ((Map) this.s_securityGuardParamContext_paramMap.get(newInstance)).put("INPUT", str);
                    this.s_securityGuardParamContext_requestType.set(newInstance, Integer.valueOf(this.s_secureIndex));
                    return (String) this.s_signRequestMethod.invoke(this.s_secureSignatureCompObj, new Object[]{newInstance});
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                } catch (IllegalArgumentException e3) {
                    e3.printStackTrace();
                } catch (InvocationTargetException e4) {
                    e4.printStackTrace();
                }
            }
            return null;
        }
    }
}
