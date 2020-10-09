package mtopsdk.security;

import android.content.Context;
import androidx.annotation.NonNull;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent;
import com.alibaba.wireless.security.open.middletier.IMiddleTierGenericComponent;
import com.alibaba.wireless.security.open.middletier.IUnifiedSecurityComponent;
import com.alibaba.wireless.security.open.middletier.fc.IFCComponent;
import com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.security.ISign;
import mtopsdk.security.util.SecurityUtils;
import mtopsdk.security.util.SignConstants;
import mtopsdk.security.util.SignStatistics;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class InnerSignImpl extends AbstractSignImpl {
    private static final String TAG = "mtopsdk.InnerSignImpl";
    private volatile IAVMPGenericComponent.IAVMPGenericInstance mAVMPInstance;
    private IMiddleTierGenericComponent mMiddleTier = null;
    private IUnifiedSecurityComponent mUnifiedSign = null;
    private SecurityGuardManager sgMgr = null;

    public void init(@NonNull MtopConfig mtopConfig) {
        super.init(mtopConfig);
        final String instanceId = getInstanceId();
        try {
            SignStatistics.setIUploadStats(mtopConfig.uploadStats);
            long currentTimeMillis = System.currentTimeMillis();
            this.sgMgr = SecurityGuardManager.getInstance(this.mtopConfig.context);
            initUmidToken(StringUtils.isEmpty(mtopConfig.appKey) ? getAppKeyByIndex(mtopConfig.appKeyIndex, getAuthCode()) : mtopConfig.appKey, getAuthCode());
            final Context context = this.mtopConfig.context;
            MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                public void run() {
                    try {
                        InnerSignImpl.this.getAVMPInstance(context);
                    } catch (Throwable th) {
                        TBSdkLog.e(InnerSignImpl.TAG, instanceId + " [init]getAVMPInstance error when async init AVMP.", th);
                    }
                }
            });
            initMiddleTier(mtopConfig);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, instanceId + " [init]ISign init SecurityGuard succeed.init time=" + (System.currentTimeMillis() - currentTimeMillis));
            }
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SG_MANAGER, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + " [init]ISign init SecurityGuard error.errorCode=" + errorCode, (Throwable) e);
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + " [init]ISign init SecurityGuard error.", (Throwable) e2);
        }
    }

    private void initUmidToken(String str, String str2) {
        final String instanceId = getInstanceId();
        try {
            IUMIDComponent uMIDComp = this.sgMgr.getUMIDComp();
            if (uMIDComp != null) {
                int env = getEnv();
                if (str2 == null) {
                    str2 = "";
                }
                uMIDComp.initUMID(str, env, str2, new IUMIDInitListenerEx() {
                    public void onUMIDInitFinishedEx(String str, int i) {
                        if (i == 200) {
                            XState.setValue(instanceId, XStateConstants.KEY_UMID_TOKEN, str);
                            TBSdkLog.i(InnerSignImpl.TAG, instanceId + " [initUmidToken]IUMIDComponent initUMID succeed,UMID token=" + str);
                            return;
                        }
                        TBSdkLog.w(InnerSignImpl.TAG, instanceId + " [initUmidToken]IUMIDComponent initUMID error,resultCode :" + i);
                    }
                });
            }
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_INIT_UMID, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + "[initUmidToken]IUMIDComponent initUMID error,errorCode=" + errorCode, (Throwable) e);
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + "[initUmidToken]IUMIDComponent initUMID error.", (Throwable) e2);
        }
    }

    public String getAppKey(ISign.SignCtx signCtx) {
        if (signCtx == null) {
            return null;
        }
        return getAppKeyByIndex(signCtx.index, signCtx.authCode);
    }

    public String getMtopApiSign(HashMap<String, String> hashMap, String str, String str2) {
        String instanceId = getInstanceId();
        String str3 = null;
        if (hashMap == null) {
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] params is null.appKey=" + str);
            return null;
        } else if (str == null) {
            hashMap.put(XStateConstants.KEY_SG_ERROR_CODE, "AppKey is null");
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] AppKey is null.");
            return null;
        } else if (this.sgMgr == null) {
            hashMap.put(XStateConstants.KEY_SG_ERROR_CODE, "SGManager is null");
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign]SecurityGuardManager is null,please call ISign init()");
            return null;
        } else {
            try {
                if ((SwitchConfig.getInstance().getUseSecurityAdapter() & 1) == 1) {
                    str3 = getSign(hashMap, str);
                }
                if (!StringUtils.isBlank(str3)) {
                    return str3;
                }
                SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                securityGuardParamContext.appKey = str;
                securityGuardParamContext.requestType = 7;
                Map<String, String> convertInnerBaseStrMap = convertInnerBaseStrMap(hashMap, str, false);
                if (convertInnerBaseStrMap != null && 2 == getEnv()) {
                    convertInnerBaseStrMap.put("ATLAS", "daily");
                }
                securityGuardParamContext.paramMap = convertInnerBaseStrMap;
                return this.sgMgr.getSecureSignatureComp().signRequest(securityGuardParamContext, str2);
            } catch (SecException e) {
                int errorCode = e.getErrorCode();
                SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SIGN_MTOP_REQUEST, String.valueOf(errorCode), "");
                hashMap.put(XStateConstants.KEY_SG_ERROR_CODE, String.valueOf(errorCode));
                TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] ISecureSignatureComponent signRequest error,errorCode=" + errorCode, (Throwable) e);
                return null;
            } catch (Exception e2) {
                TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] ISecureSignatureComponent signRequest error", (Throwable) e2);
                return null;
            }
        }
    }

    public Map<String, String> convertInnerBaseStrMap(Map<String, String> map, String str, boolean z) {
        Map<String, String> map2 = map;
        if (map2 == null || map.size() < 1) {
            return null;
        }
        String str2 = map2.get("extdata");
        String str3 = map2.get("x-features");
        String str4 = map2.get(XStateConstants.KEY_ROUTER_ID);
        String str5 = map2.get(XStateConstants.KEY_PLACE_ID);
        String str6 = map2.get(XStateConstants.KEY_OPEN_BIZ);
        String str7 = map2.get(XStateConstants.KEY_MINI_APPKEY);
        String str8 = map2.get(XStateConstants.KEY_REQ_APPKEY);
        String str9 = map2.get(XStateConstants.KEY_ACCESS_TOKEN);
        String str10 = map2.get(XStateConstants.KEY_OPEN_BIZ_DATA);
        StringBuilder sb = new StringBuilder(64);
        sb.append(SecurityUtils.convertNull2Default(map2.get("utdid")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("uid")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get(XStateConstants.KEY_REQBIZ_EXT)));
        sb.append("&");
        sb.append(str);
        sb.append("&");
        sb.append(SecurityUtils.getMd5(map2.get("data")));
        sb.append("&");
        sb.append(map2.get("t"));
        sb.append("&");
        sb.append(map2.get("api"));
        sb.append("&");
        sb.append(map2.get("v"));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("sid")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("ttid")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("deviceId")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("lat")));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(map2.get("lng")));
        sb.append("&");
        if (z) {
            sb.append(SecurityUtils.convertNull2Default(str2));
            sb.append("&");
        } else if (StringUtils.isNotBlank(str2)) {
            sb.append(str2);
            sb.append("&");
        }
        sb.append(str3);
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str4));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str5));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str6));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str7));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str8));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str9));
        sb.append("&");
        sb.append(SecurityUtils.convertNull2Default(str10));
        HashMap hashMap = new HashMap(2);
        hashMap.put("INPUT", sb.toString());
        return hashMap;
    }

    public String getCommonHmacSha1Sign(String str, String str2) {
        String instanceId = getInstanceId();
        if (str == null || str2 == null) {
            return null;
        }
        if (this.sgMgr == null) {
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign]SecurityGuardManager is null,please call ISign init()");
            return null;
        }
        try {
            HashMap hashMap = new HashMap(1);
            hashMap.put("INPUT", str);
            SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
            securityGuardParamContext.appKey = str2;
            securityGuardParamContext.requestType = 3;
            securityGuardParamContext.paramMap = hashMap;
            return this.sgMgr.getSecureSignatureComp().signRequest(securityGuardParamContext, getAuthCode());
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SIGN_HMAC_SHA1, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign] ISecureSignatureComponent signRequest error,errorCode=" + errorCode, (Throwable) e);
            return null;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign] ISecureSignatureComponent signRequest error", (Throwable) e2);
            return null;
        }
    }

    public String getSecBodyDataEx(String str, String str2, String str3, HashMap<String, String> hashMap, int i) {
        try {
            return ((ISecurityBodyComponent) this.sgMgr.getInterface(ISecurityBodyComponent.class)).getSecurityBodyDataEx(str, str2, str3, hashMap, i, getEnv());
        } catch (SecException e) {
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_GET_SECBODY, String.valueOf(e.getErrorCode()), String.valueOf(i));
            TBSdkLog.e(TAG, getInstanceId() + " [getSecBodyDataEx] ISecurityBodyComponent getSecurityBodyDataEx  error.errorCode=" + e.getErrorCode() + ", flag=" + i, (Throwable) e);
            return null;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, getInstanceId() + " [getSecBodyDataEx] ISecurityBodyComponent getSecurityBodyDataEx  error,flag=" + i, (Throwable) e2);
            return null;
        }
    }

    public String getAvmpSign(String str, String str2, int i) {
        String avmpSign = avmpSign(str);
        if (!StringUtils.isBlank(avmpSign)) {
            return avmpSign;
        }
        TBSdkLog.e(TAG, getInstanceId() + " [getAvmpSign] call avmpSign return null.degrade call getSecBodyDataEx ");
        return getSecBodyDataEx("", "", str2, (HashMap<String, String>) null, i);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized java.lang.String avmpSign(java.lang.String r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            r0 = 0
            java.lang.String r1 = ""
            r2 = 4
            byte[] r3 = new byte[r2]     // Catch:{ all -> 0x00bd }
            r4 = 0
            if (r12 != 0) goto L_0x0029
            java.lang.String r12 = ""
            java.lang.String r5 = "mtopsdk.InnerSignImpl"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0027 }
            r6.<init>()     // Catch:{ Exception -> 0x0027 }
            java.lang.String r7 = r11.getInstanceId()     // Catch:{ Exception -> 0x0027 }
            r6.append(r7)     // Catch:{ Exception -> 0x0027 }
            java.lang.String r7 = " [avmpSign] input is null"
            r6.append(r7)     // Catch:{ Exception -> 0x0027 }
            java.lang.String r6 = r6.toString()     // Catch:{ Exception -> 0x0027 }
            mtopsdk.common.util.TBSdkLog.e(r5, r6)     // Catch:{ Exception -> 0x0027 }
            goto L_0x0029
        L_0x0027:
            r12 = move-exception
            goto L_0x0083
        L_0x0029:
            mtopsdk.mtop.global.MtopConfig r5 = r11.mtopConfig     // Catch:{ Exception -> 0x0027 }
            if (r5 == 0) goto L_0x0032
            mtopsdk.mtop.global.MtopConfig r5 = r11.mtopConfig     // Catch:{ Exception -> 0x0027 }
            android.content.Context r5 = r5.context     // Catch:{ Exception -> 0x0027 }
            goto L_0x0036
        L_0x0032:
            android.content.Context r5 = mtopsdk.common.util.MtopUtils.getContext()     // Catch:{ Exception -> 0x0027 }
        L_0x0036:
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r5 = r11.getAVMPInstance(r5)     // Catch:{ Exception -> 0x0027 }
            if (r5 != 0) goto L_0x003e
            monitor-exit(r11)
            return r0
        L_0x003e:
            java.lang.String r6 = "sign"
            byte[] r7 = new byte[r4]     // Catch:{ Exception -> 0x0027 }
            java.lang.Class r7 = r7.getClass()     // Catch:{ Exception -> 0x0027 }
            r8 = 6
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x0027 }
            java.lang.Integer r9 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0027 }
            r8[r4] = r9     // Catch:{ Exception -> 0x0027 }
            r9 = 1
            byte[] r10 = r12.getBytes()     // Catch:{ Exception -> 0x0027 }
            r8[r9] = r10     // Catch:{ Exception -> 0x0027 }
            r9 = 2
            byte[] r12 = r12.getBytes()     // Catch:{ Exception -> 0x0027 }
            int r12 = r12.length     // Catch:{ Exception -> 0x0027 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0027 }
            r8[r9] = r12     // Catch:{ Exception -> 0x0027 }
            r12 = 3
            r8[r12] = r1     // Catch:{ Exception -> 0x0027 }
            r8[r2] = r3     // Catch:{ Exception -> 0x0027 }
            r12 = 5
            int r1 = r11.getEnv()     // Catch:{ Exception -> 0x0027 }
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)     // Catch:{ Exception -> 0x0027 }
            r8[r12] = r1     // Catch:{ Exception -> 0x0027 }
            java.lang.Object r12 = r5.invokeAVMP(r6, r7, r8)     // Catch:{ Exception -> 0x0027 }
            byte[] r12 = (byte[]) r12     // Catch:{ Exception -> 0x0027 }
            byte[] r12 = (byte[]) r12     // Catch:{ Exception -> 0x0027 }
            if (r12 == 0) goto L_0x00bb
            java.lang.String r1 = new java.lang.String     // Catch:{ Exception -> 0x0027 }
            r1.<init>(r12)     // Catch:{ Exception -> 0x0027 }
            r0 = r1
            goto L_0x00bb
        L_0x0083:
            java.nio.ByteBuffer r1 = java.nio.ByteBuffer.wrap(r3)     // Catch:{ Throwable -> 0x009d }
            java.nio.ByteOrder r2 = java.nio.ByteOrder.LITTLE_ENDIAN     // Catch:{ Throwable -> 0x009d }
            java.nio.ByteBuffer r1 = r1.order(r2)     // Catch:{ Throwable -> 0x009d }
            int r1 = r1.getInt()     // Catch:{ Throwable -> 0x009d }
            java.lang.String r2 = "InvokeAVMP"
            java.lang.String r3 = java.lang.String.valueOf(r1)     // Catch:{ Throwable -> 0x009e }
            java.lang.String r4 = ""
            mtopsdk.security.util.SignStatistics.commitStats(r2, r3, r4)     // Catch:{ Throwable -> 0x009e }
            goto L_0x009e
        L_0x009d:
            r1 = 0
        L_0x009e:
            java.lang.String r2 = "mtopsdk.InnerSignImpl"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bd }
            r3.<init>()     // Catch:{ all -> 0x00bd }
            java.lang.String r4 = r11.getInstanceId()     // Catch:{ all -> 0x00bd }
            r3.append(r4)     // Catch:{ all -> 0x00bd }
            java.lang.String r4 = " [avmpSign] call avmpInstance.invokeAVMP error.errorCode="
            r3.append(r4)     // Catch:{ all -> 0x00bd }
            r3.append(r1)     // Catch:{ all -> 0x00bd }
            java.lang.String r1 = r3.toString()     // Catch:{ all -> 0x00bd }
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r2, (java.lang.String) r1, (java.lang.Throwable) r12)     // Catch:{ all -> 0x00bd }
        L_0x00bb:
            monitor-exit(r11)
            return r0
        L_0x00bd:
            r12 = move-exception
            monitor-exit(r11)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.security.InnerSignImpl.avmpSign(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public IAVMPGenericComponent.IAVMPGenericInstance getAVMPInstance(@NonNull Context context) {
        if (this.mAVMPInstance == null) {
            synchronized (InnerSignImpl.class) {
                if (this.mAVMPInstance == null) {
                    try {
                        this.mAVMPInstance = ((IAVMPGenericComponent) SecurityGuardManager.getInstance(context).getInterface(IAVMPGenericComponent.class)).createAVMPInstance("mwua", "sgcipher");
                        if (this.mAVMPInstance == null) {
                            TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance return null.");
                        }
                    } catch (SecException e) {
                        int errorCode = e.getErrorCode();
                        SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_AVMP_INSTANCE, String.valueOf(errorCode), "");
                        TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance error,errorCode=" + errorCode, (Throwable) e);
                    } catch (Exception e2) {
                        TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance error.", (Throwable) e2);
                    }
                }
            }
        }
        return this.mAVMPInstance;
    }

    private String getAppKeyByIndex(int i, String str) {
        String str2;
        SecException e;
        Exception e2;
        String instanceId = getInstanceId();
        try {
            str2 = this.sgMgr.getStaticDataStoreComp().getAppKeyByIndex(i, str);
            try {
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex  appKey=" + str2 + ",appKeyIndex=" + i + ",authCode=" + str);
                }
            } catch (SecException e3) {
                e = e3;
                int errorCode = e.getErrorCode();
                SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_GET_APPKEY, String.valueOf(errorCode), "");
                TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.errorCode=" + errorCode + ",appKeyIndex=" + i + ",authCode=" + str, (Throwable) e);
                return str2;
            } catch (Exception e4) {
                e2 = e4;
                TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.appKeyIndex=" + i + ",authCode=" + str, (Throwable) e2);
                return str2;
            }
        } catch (SecException e5) {
            SecException secException = e5;
            str2 = null;
            e = secException;
            int errorCode2 = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_GET_APPKEY, String.valueOf(errorCode2), "");
            TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.errorCode=" + errorCode2 + ",appKeyIndex=" + i + ",authCode=" + str, (Throwable) e);
            return str2;
        } catch (Exception e6) {
            Exception exc = e6;
            str2 = null;
            e2 = exc;
            TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.appKeyIndex=" + i + ",authCode=" + str, (Throwable) e2);
            return str2;
        }
        return str2;
    }

    private void initMiddleTier(MtopConfig mtopConfig) {
        if (mtopConfig != null) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                HashMap hashMap = new HashMap();
                hashMap.put("auth_code", getAuthCode());
                if (this.mMiddleTier == null) {
                    this.mMiddleTier = (IMiddleTierGenericComponent) SecurityGuardManager.getInstance(mtopConfig.context).getInterface(IMiddleTierGenericComponent.class);
                    if (this.mMiddleTier != null && !this.mMiddleTier.init(hashMap) && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.e(TAG, getInstanceId() + " [initMiddleTier]init middle tier failed");
                    }
                }
                HashMap hashMap2 = new HashMap();
                hashMap2.put(SignConstants.MIDDLE_PARAM_AUTHCODE, getAuthCode());
                if (this.mUnifiedSign == null) {
                    this.mUnifiedSign = (IUnifiedSecurityComponent) SecurityGuardManager.getInstance(mtopConfig.context).getInterface(IUnifiedSecurityComponent.class);
                    if (this.mUnifiedSign != null) {
                        this.mUnifiedSign.init(hashMap2);
                    } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.e(TAG, getInstanceId() + " [initMiddleTier]init sign failed");
                    }
                }
                HashMap hashMap3 = new HashMap();
                hashMap3.put(SignConstants.MIDDLE_PARAM_LOGIN_VALUE, Boolean.valueOf(RemoteLogin.getLogin(mtopConfig.mtopInstance) != null));
                IFCComponent iFCComponent = (IFCComponent) SecurityGuardManager.getInstance(mtopConfig.context).getInterface(IFCComponent.class);
                if (iFCComponent != null) {
                    iFCComponent.setUp(mtopConfig.context, hashMap3);
                    mtopConfig.mtopGlobalHeaders.put(SignConstants.BX_VERSION, iFCComponent.getFCPluginVersion());
                }
                TBSdkLog.e(TAG, "[initMiddleTier] execute initMiddleTier cost time ", String.valueOf(System.currentTimeMillis() - currentTimeMillis));
            } catch (SecException e) {
                TBSdkLog.e(TAG, getInstanceId() + " [initMiddleTier]init middleTier failed with errorCode " + e.getErrorCode() + ",appKeyIndex=" + mtopConfig.appKeyIndex + ",authCode=" + mtopConfig.authCode, (Throwable) e);
            } catch (Exception e2) {
                TBSdkLog.e(TAG, getInstanceId() + " [initMiddleTier]init middleTier failed with unknown exception, appKeyIndex=" + mtopConfig.appKeyIndex + ",authCode=" + mtopConfig.authCode, (Throwable) e2);
            }
        }
    }

    public String getSign(HashMap<String, String> hashMap, String str) {
        String str2;
        String str3 = convertInnerBaseStrMap(hashMap, str, false).get("INPUT");
        try {
            if (this.mMiddleTier != null) {
                if (!StringUtils.isBlank(str3)) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("data", str3.getBytes("UTF-8"));
                    hashMap2.put("env", Integer.valueOf(getMiddleTierEnv()));
                    hashMap2.put("appkey", str);
                    HashMap<String, String> sign = this.mMiddleTier.getSign(hashMap2);
                    if (sign != null) {
                        if (!sign.isEmpty()) {
                            str2 = sign.remove("x-sign");
                            try {
                                if (StringUtils.isNotBlank(str2)) {
                                    hashMap.putAll(sign);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e = e;
                                TBSdkLog.e(TAG, getInstanceId() + " [getSign]your input data transfer to byte utf-8 failed ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, e);
                                return str2;
                            } catch (SecException e2) {
                                e = e2;
                                TBSdkLog.e(TAG, getInstanceId() + " [getSign]get sign failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
                                return str2;
                            }
                            return str2;
                        }
                    }
                    TBSdkLog.e(TAG, getInstanceId() + " [getSign]get sign failed with no output ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
                    return "";
                }
            }
            TBSdkLog.e(TAG, getInstanceId() + " [getSign]middleTier null or data data ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
            return "";
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            str2 = "";
            TBSdkLog.e(TAG, getInstanceId() + " [getSign]your input data transfer to byte utf-8 failed ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, e);
            return str2;
        } catch (SecException e4) {
            e = e4;
            str2 = "";
            TBSdkLog.e(TAG, getInstanceId() + " [getSign]get sign failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
            return str2;
        }
    }

    public String getWua(HashMap<String, String> hashMap, String str) {
        String str2;
        String str3 = hashMap.get("sign");
        try {
            if (this.mMiddleTier != null) {
                if (!StringUtils.isBlank(str3)) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("data", str3.getBytes("UTF-8"));
                    hashMap2.put("env", Integer.valueOf(getMiddleTierEnv()));
                    HashMap<String, String> wua = this.mMiddleTier.getWua(hashMap2);
                    if (wua != null) {
                        if (!wua.isEmpty()) {
                            str2 = wua.remove("wua");
                            try {
                                if (StringUtils.isNotBlank(str2)) {
                                    hashMap.putAll(wua);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e = e;
                                TBSdkLog.e(TAG, getInstanceId() + " [getWua]your input data transfer to byte utf-8 failed ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, e);
                                return str2;
                            } catch (SecException e2) {
                                e = e2;
                                TBSdkLog.e(TAG, getInstanceId() + " [getWua]get wua failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
                                return str2;
                            }
                            return str2;
                        }
                    }
                    TBSdkLog.e(TAG, getInstanceId() + " [getWua]get wua failed with no output ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
                    return "";
                }
            }
            TBSdkLog.e(TAG, getInstanceId() + " [getWua]middleTier null or data data ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
            return "";
        } catch (UnsupportedEncodingException e3) {
            e = e3;
            str2 = "";
            TBSdkLog.e(TAG, getInstanceId() + " [getWua]your input data transfer to byte utf-8 failed ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, e);
            return str2;
        } catch (SecException e4) {
            e = e4;
            str2 = "";
            TBSdkLog.e(TAG, getInstanceId() + " [getWua]get wua failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
            return str2;
        }
    }

    public String getMiniWua(HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        String str;
        try {
            if (this.mMiddleTier == null) {
                TBSdkLog.e(TAG, getInstanceId() + " [getMiniWua]middleTier ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
                return "";
            }
            HashMap hashMap3 = new HashMap();
            hashMap3.put("env", Integer.valueOf(getMiddleTierEnv()));
            if (hashMap2 == null) {
                hashMap2 = new HashMap<>();
            }
            hashMap2.put("api_name", hashMap.get("api"));
            hashMap3.put(SignConstants.MIDDLE_PARAM_EXT, hashMap2);
            HashMap<String, String> miniWua = this.mMiddleTier.getMiniWua(hashMap3);
            if (miniWua != null) {
                if (!miniWua.isEmpty()) {
                    str = miniWua.remove(SignConstants.MIDDLE_OUTPUT_X_MINI_WUA);
                    try {
                        if (StringUtils.isNotBlank(str)) {
                            hashMap.putAll(miniWua);
                        }
                    } catch (SecException e) {
                        e = e;
                        TBSdkLog.e(TAG, getInstanceId() + " [getMiniWua]get miniwua failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
                        return str;
                    }
                    return str;
                }
            }
            TBSdkLog.e(TAG, getInstanceId() + " [getMiniWua]get miniwua failed with no output ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
            return "";
        } catch (SecException e2) {
            e = e2;
            str = "";
            TBSdkLog.e(TAG, getInstanceId() + " [getMiniWua]get miniwua failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
            return str;
        }
    }

    public HashMap<String, String> getUnifiedSign(HashMap<String, String> hashMap, HashMap<String, String> hashMap2, String str, String str2, boolean z) {
        String instanceId = getInstanceId();
        if (str == null) {
            hashMap.put(XStateConstants.KEY_SG_ERROR_CODE, "AppKey is null");
            TBSdkLog.e(TAG, instanceId + " [getUnifiedSign] AppKey is null.");
            return null;
        } else if (hashMap == null) {
            TBSdkLog.e(TAG, instanceId + " [getUnifiedSign] params is null.appKey=" + str);
            return null;
        } else if (this.mUnifiedSign == null) {
            hashMap.put(XStateConstants.KEY_SG_ERROR_CODE, "unified is null");
            TBSdkLog.e(TAG, instanceId + " [getUnifiedSign]sg unified sign is null, please call ISign init()");
            return null;
        } else {
            try {
                HashMap hashMap3 = new HashMap();
                String str3 = convertInnerBaseStrMap(hashMap, str, true).get("INPUT");
                if (StringUtils.isBlank(str3)) {
                    TBSdkLog.e(TAG, getInstanceId() + " [getUnifiedSign]get sign failed with sign data empty ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
                    return null;
                }
                hashMap3.put("appkey", str);
                hashMap3.put("data", str3);
                hashMap3.put(SignConstants.MIDDLE_PARAM_USE_WUA, Boolean.valueOf(z));
                hashMap3.put("env", Integer.valueOf(getMiddleTierEnv()));
                hashMap3.put(SignConstants.MIDDLE_PARAM_AUTHCODE, str2);
                hashMap3.put(SignConstants.MIDDLE_PARAM_EXT_PARAM, hashMap2);
                hashMap3.put("api", hashMap.get("api"));
                HashMap<String, String> securityFactors = this.mUnifiedSign.getSecurityFactors(hashMap3);
                if (securityFactors != null) {
                    if (!securityFactors.isEmpty()) {
                        return securityFactors;
                    }
                }
                TBSdkLog.e(TAG, getInstanceId() + " [getUnifiedSign]get sign failed with no output ", "appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode);
                return null;
            } catch (SecException e) {
                TBSdkLog.e(TAG, getInstanceId() + " [getUnifiedSign]get sign failed and SecException errorCode " + e.getErrorCode() + ",appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, (Throwable) e);
                return null;
            } catch (Throwable th) {
                TBSdkLog.e(TAG, getInstanceId() + " [getUnifiedSign]get sign failed exception ,appKeyIndex=" + this.mtopConfig.appKeyIndex + ",authCode=" + this.mtopConfig.authCode, th);
                return null;
            }
        }
    }

    private int getMiddleTierEnv() {
        if (getEnv() == EnvModeEnum.PREPARE.getEnvMode()) {
            return 1;
        }
        return (getEnv() == EnvModeEnum.TEST.getEnvMode() || getEnv() == EnvModeEnum.TEST_SANDBOX.getEnvMode()) ? 2 : 0;
    }
}
