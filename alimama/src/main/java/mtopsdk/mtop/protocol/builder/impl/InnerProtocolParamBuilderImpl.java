package mtopsdk.mtop.protocol.builder.impl;

import android.text.TextUtils;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.network.NetParam;
import mtopsdk.mtop.protocol.builder.ProtocolParamBuilder;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.network.NetworkStateReceiver;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class InnerProtocolParamBuilderImpl implements ProtocolParamBuilder {
    private static final String TAG = "mtopsdk.InnerProtocolParamBuilderImpl";
    private MtopConfig mtopConfig = null;

    /* JADX WARNING: Removed duplicated region for block: B:110:0x03b7  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x03ba  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0155  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0174  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x017f  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0193  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x01fd  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0261  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0273  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x0276  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0289  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.String, java.lang.String> buildParams(mtopsdk.framework.domain.MtopContext r21) {
        /*
            r20 = this;
            r1 = r20
            r2 = r21
            mtopsdk.mtop.util.MtopStatistics r0 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r3 = r2.stats
            long r3 = r3.currentTimeMillis()
            r0.buildParamsStartTime = r3
            mtopsdk.mtop.intf.Mtop r3 = r2.mtopInstance
            mtopsdk.mtop.global.MtopConfig r0 = r3.getMtopConfig()
            r1.mtopConfig = r0
            mtopsdk.mtop.global.MtopConfig r0 = r1.mtopConfig
            mtopsdk.security.ISign r10 = r0.sign
            if (r10 != 0) goto L_0x0027
            java.lang.String r0 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r2 = r2.seqNo
            java.lang.String r3 = "ISign of mtopConfig in mtopInstance is null"
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r0, (java.lang.String) r2, (java.lang.String) r3)
            r0 = 0
            return r0
        L_0x0027:
            mtopsdk.mtop.domain.MtopRequest r11 = r2.mtopRequest
            mtopsdk.mtop.common.MtopNetworkProp r12 = r2.property
            java.util.HashMap r13 = new java.util.HashMap
            r4 = 64
            r13.<init>(r4)
            java.lang.String r0 = "utdid"
            java.lang.String r5 = r3.getUtdid()
            r13.put(r0, r5)
            java.lang.String r0 = r12.reqUserId
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x0046
            java.lang.String r0 = r12.reqUserId
            goto L_0x004c
        L_0x0046:
            java.lang.String r0 = r12.userInfo
            java.lang.String r0 = r3.getMultiAccountUserId(r0)
        L_0x004c:
            java.lang.String r5 = "uid"
            r13.put(r5, r0)
            java.lang.String r0 = r12.reqBizExt
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x0060
            java.lang.String r0 = "reqbiz-ext"
            java.lang.String r5 = r12.reqBizExt
            r13.put(r0, r5)
        L_0x0060:
            java.lang.String r0 = r12.reqAppKey
            boolean r0 = mtopsdk.common.util.StringUtils.isBlank(r0)
            if (r0 == 0) goto L_0x0074
            mtopsdk.mtop.global.MtopConfig r0 = r1.mtopConfig
            java.lang.String r0 = r0.appKey
            r12.reqAppKey = r0
            mtopsdk.mtop.global.MtopConfig r0 = r1.mtopConfig
            java.lang.String r0 = r0.authCode
            r12.authCode = r0
        L_0x0074:
            java.lang.String r14 = r12.reqAppKey
            java.lang.String r15 = r12.authCode
            mtopsdk.mtop.global.MtopConfig r0 = r1.mtopConfig
            java.lang.String r0 = r0.routerId
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x008b
            java.lang.String r0 = "routerId"
            mtopsdk.mtop.global.MtopConfig r5 = r1.mtopConfig
            java.lang.String r5 = r5.routerId
            r13.put(r0, r5)
        L_0x008b:
            java.lang.String r0 = r12.routerId
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x009a
            java.lang.String r0 = "routerId"
            java.lang.String r5 = r12.routerId
            r13.put(r0, r5)
        L_0x009a:
            mtopsdk.mtop.global.MtopConfig r0 = r1.mtopConfig
            java.lang.String r0 = r0.placeId
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x00ad
            java.lang.String r0 = "placeId"
            mtopsdk.mtop.global.MtopConfig r5 = r1.mtopConfig
            java.lang.String r5 = r5.placeId
            r13.put(r0, r5)
        L_0x00ad:
            java.lang.String r0 = r12.placeId
            boolean r0 = mtopsdk.common.util.StringUtils.isNotBlank(r0)
            if (r0 == 0) goto L_0x00bc
            java.lang.String r0 = "placeId"
            java.lang.String r5 = r12.placeId
            r13.put(r0, r5)
        L_0x00bc:
            java.lang.String r0 = "appKey"
            r13.put(r0, r14)
            java.lang.String r5 = r11.getData()
            boolean r0 = r12.priorityFlag
            if (r0 == 0) goto L_0x00fd
            java.util.Map<java.lang.String, java.lang.String> r0 = r12.priorityData
            if (r0 == 0) goto L_0x00fd
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ Exception -> 0x00e2 }
            r0.<init>(r5)     // Catch:{ Exception -> 0x00e2 }
            java.lang.String r6 = "x-priority-data"
            java.util.Map<java.lang.String, java.lang.String> r7 = r12.priorityData     // Catch:{ Exception -> 0x00e2 }
            java.lang.String r7 = com.alibaba.fastjson.JSON.toJSONString(r7)     // Catch:{ Exception -> 0x00e2 }
            r0.putOpt(r6, r7)     // Catch:{ Exception -> 0x00e2 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x00e2 }
            goto L_0x00fe
        L_0x00e2:
            r0 = move-exception
            java.lang.String r6 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r7 = r2.seqNo
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "set api priority data error, priorityData:"
            r8.append(r9)
            java.util.Map<java.lang.String, java.lang.String> r9 = r12.priorityData
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            mtopsdk.common.util.TBSdkLog.e(r6, r7, r8, r0)
        L_0x00fd:
            r0 = r5
        L_0x00fe:
            java.lang.String r5 = "data"
            r13.put(r5, r0)
            long r5 = mtopsdk.mtop.global.SDKUtils.getCorrectionTime()
            java.lang.String r0 = java.lang.String.valueOf(r5)
            java.lang.String r5 = "t"
            r13.put(r5, r0)
            java.lang.String r5 = "api"
            java.lang.String r6 = r11.getApiName()
            java.util.Locale r7 = java.util.Locale.US
            java.lang.String r6 = r6.toLowerCase(r7)
            r13.put(r5, r6)
            java.lang.String r5 = "v"
            java.lang.String r6 = r11.getVersion()
            java.util.Locale r7 = java.util.Locale.US
            java.lang.String r6 = r6.toLowerCase(r7)
            r13.put(r5, r6)
            java.lang.String r5 = "sid"
            java.lang.String r6 = r12.userInfo
            java.lang.String r6 = r3.getMultiAccountSid(r6)
            r13.put(r5, r6)
            java.lang.String r5 = "ttid"
            java.lang.String r6 = r12.ttid
            r13.put(r5, r6)
            java.lang.String r5 = "deviceId"
            java.lang.String r6 = r3.getDeviceId()
            r13.put(r5, r6)
            java.lang.String r5 = "lat"
            java.lang.String r5 = mtopsdk.xstate.XState.getValue(r5)
            boolean r6 = mtopsdk.common.util.StringUtils.isNotBlank(r5)
            if (r6 == 0) goto L_0x016b
            java.lang.String r6 = "lng"
            java.lang.String r6 = mtopsdk.xstate.XState.getValue(r6)
            boolean r7 = mtopsdk.common.util.StringUtils.isNotBlank(r6)
            if (r7 == 0) goto L_0x016b
            java.lang.String r7 = "lat"
            r13.put(r7, r5)
            java.lang.String r5 = "lng"
            r13.put(r5, r6)
        L_0x016b:
            long r5 = mtopsdk.mtop.features.MtopFeatureManager.getMtopTotalFeatures(r3)
            int r7 = r12.reqSource
            r9 = 1
            if (r7 != r9) goto L_0x017b
            r7 = 11
            long r7 = mtopsdk.mtop.features.MtopFeatureManager.getMtopFeatureValue(r7)
            long r5 = r5 | r7
        L_0x017b:
            boolean r7 = r12.priorityFlag
            if (r7 == 0) goto L_0x0186
            r7 = 12
            long r7 = mtopsdk.mtop.features.MtopFeatureManager.getMtopFeatureValue(r7)
            long r5 = r5 | r7
        L_0x0186:
            java.lang.String r7 = "x-features"
            java.lang.String r5 = java.lang.String.valueOf(r5)
            r13.put(r7, r5)
            mtopsdk.mtop.domain.ApiTypeEnum r5 = r12.apiType
            if (r5 == 0) goto L_0x01f5
            boolean r5 = r12.isInnerOpen
            if (r5 == 0) goto L_0x01a9
            java.lang.String r5 = r3.getInstanceId()
            java.lang.String r6 = r12.openAppKey
            java.lang.String r5 = mtopsdk.common.util.StringUtils.concatStr(r5, r6)
            java.lang.String r6 = "accessToken"
            java.lang.String r5 = mtopsdk.xstate.XState.getValue(r5, r6)
            r12.accessToken = r5
        L_0x01a9:
            java.lang.String r5 = "exttype"
            mtopsdk.mtop.domain.ApiTypeEnum r6 = r12.apiType
            java.lang.String r6 = r6.getApiType()
            r13.put(r5, r6)
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>(r4)
            java.lang.String r4 = r12.openAppKey
            boolean r4 = mtopsdk.common.util.StringUtils.isNotBlank(r4)
            if (r4 == 0) goto L_0x01d0
            java.lang.String r4 = "openappkey"
            r5.append(r4)
            java.lang.String r4 = "="
            r5.append(r4)
            java.lang.String r4 = r12.openAppKey
            r5.append(r4)
        L_0x01d0:
            java.lang.String r4 = r12.accessToken
            boolean r4 = mtopsdk.common.util.StringUtils.isNotBlank(r4)
            if (r4 == 0) goto L_0x01ec
            java.lang.String r4 = ";"
            r5.append(r4)
            java.lang.String r4 = "accesstoken"
            r5.append(r4)
            java.lang.String r4 = "="
            r5.append(r4)
            java.lang.String r4 = r12.accessToken
            r5.append(r4)
        L_0x01ec:
            java.lang.String r4 = "extdata"
            java.lang.String r5 = r5.toString()
            r13.put(r4, r5)
        L_0x01f5:
            java.lang.String r4 = r12.openBiz
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0252
            java.lang.String r4 = "open-biz"
            java.lang.String r5 = r12.openBiz
            r13.put(r4, r5)
            java.lang.String r4 = r12.miniAppKey
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0213
            java.lang.String r4 = "mini-appkey"
            java.lang.String r5 = r12.miniAppKey
            r13.put(r4, r5)
        L_0x0213:
            java.lang.String r4 = r12.reqAppKey
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0222
            java.lang.String r4 = "req-appkey"
            java.lang.String r5 = r12.requestSourceAppKey
            r13.put(r4, r5)
        L_0x0222:
            java.lang.String r4 = r12.openBizData
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x0231
            java.lang.String r4 = "open-biz-data"
            java.lang.String r5 = r12.openBizData
            r13.put(r4, r5)
        L_0x0231:
            java.lang.String r3 = r3.getInstanceId()
            java.lang.String r4 = r12.miniAppKey
            java.lang.String r3 = mtopsdk.common.util.StringUtils.concatStr(r3, r4)
            java.lang.String r4 = "accessToken"
            java.lang.String r3 = mtopsdk.xstate.XState.getValue(r3, r4)
            r12.accessToken = r3
            java.lang.String r3 = r12.accessToken
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x0252
            java.lang.String r3 = "accessToken"
            java.lang.String r4 = r12.accessToken
            r13.put(r3, r4)
        L_0x0252:
            java.util.HashMap r3 = new java.util.HashMap
            r3.<init>()
            java.lang.String r4 = "pageId"
            java.lang.String r5 = r12.pageUrl
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 == 0) goto L_0x0264
            java.lang.String r5 = ""
            goto L_0x0266
        L_0x0264:
            java.lang.String r5 = r12.pageUrl
        L_0x0266:
            r3.put(r4, r5)
            java.lang.String r4 = "pageName"
            java.lang.String r5 = r12.pageName
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 == 0) goto L_0x0276
            java.lang.String r5 = ""
            goto L_0x0278
        L_0x0276:
            java.lang.String r5 = r12.pageName
        L_0x0278:
            r3.put(r4, r5)
            mtopsdk.mtop.global.SwitchConfig r4 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            int r4 = r4.getUseSecurityAdapter()
            r5 = 4
            r4 = r4 & r5
            r16 = 0
            if (r4 != r5) goto L_0x03b7
            int r4 = r12.wuaFlag
            if (r4 >= 0) goto L_0x0295
            boolean r4 = r12.wuaRetry
            if (r4 == 0) goto L_0x0292
            goto L_0x0295
        L_0x0292:
            r17 = 0
            goto L_0x0297
        L_0x0295:
            r17 = 1
        L_0x0297:
            mtopsdk.mtop.util.MtopStatistics r4 = r2.stats
            long r18 = r4.currentTimeMillis()
            r4 = r10
            r5 = r13
            r6 = r3
            r7 = r14
            r8 = r15
            r1 = 1
            r9 = r17
            java.util.HashMap r4 = r4.getUnifiedSign(r5, r6, r7, r8, r9)
            mtopsdk.mtop.util.MtopStatistics r5 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r6 = r2.stats
            long r6 = r6.currentTimeMillis()
            long r6 = r6 - r18
            r5.computeSignTime = r6
            if (r4 == 0) goto L_0x03b8
            java.lang.String r5 = "x-sign"
            java.lang.Object r5 = r4.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            boolean r6 = mtopsdk.common.util.StringUtils.isBlank(r5)
            if (r6 == 0) goto L_0x02ea
            java.lang.String r0 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r1 = r2.seqNo
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "[buildParams]get sign failed empty output , apiKey="
            r2.append(r3)
            java.lang.String r3 = r11.getKey()
            r2.append(r3)
            java.lang.String r3 = ",authCode="
            r2.append(r3)
            r2.append(r15)
            java.lang.String r2 = r2.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r0, (java.lang.String) r1, (java.lang.String) r2)
            return r13
        L_0x02ea:
            java.lang.String r6 = "sign"
            r13.put(r6, r5)
            boolean r5 = r10 instanceof mtopsdk.security.LocalInnerSignImpl
            if (r5 != 0) goto L_0x0363
            if (r17 == 0) goto L_0x032c
            java.lang.String r5 = "wua"
            java.lang.Object r5 = r4.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "wua"
            r13.put(r6, r5)
            boolean r5 = mtopsdk.common.util.StringUtils.isBlank(r5)
            if (r5 == 0) goto L_0x032c
            java.lang.String r5 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r6 = r2.seqNo
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "[buildParams]get wua failed empty output , apiKey="
            r7.append(r8)
            java.lang.String r8 = r11.getKey()
            r7.append(r8)
            java.lang.String r8 = ",authCode="
            r7.append(r8)
            r7.append(r15)
            java.lang.String r7 = r7.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r5, (java.lang.String) r6, (java.lang.String) r7)
        L_0x032c:
            java.lang.String r5 = "x-mini-wua"
            java.lang.Object r5 = r4.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "x-mini-wua"
            r13.put(r6, r5)
            boolean r5 = mtopsdk.common.util.StringUtils.isBlank(r5)
            if (r5 == 0) goto L_0x0363
            java.lang.String r5 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r6 = r2.seqNo
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "[buildParams]get mini wua failed empty output , apiKey="
            r7.append(r8)
            java.lang.String r8 = r11.getKey()
            r7.append(r8)
            java.lang.String r8 = ",authCode="
            r7.append(r8)
            r7.append(r15)
            java.lang.String r7 = r7.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r5, (java.lang.String) r6, (java.lang.String) r7)
        L_0x0363:
            java.lang.String r5 = "x-umt"
            java.lang.Object r5 = r4.get(r5)
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r6 = "umt"
            r13.put(r6, r5)
            boolean r5 = mtopsdk.common.util.StringUtils.isBlank(r5)
            if (r5 == 0) goto L_0x039a
            java.lang.String r5 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r6 = r2.seqNo
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "[buildParams]get umt failed empty output , apiKey="
            r7.append(r8)
            java.lang.String r8 = r11.getKey()
            r7.append(r8)
            java.lang.String r8 = ",authCode="
            r7.append(r8)
            r7.append(r15)
            java.lang.String r7 = r7.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r5, (java.lang.String) r6, (java.lang.String) r7)
        L_0x039a:
            java.lang.String r5 = "x-sgext"
            java.lang.Object r4 = r4.get(r5)
            java.lang.String r4 = (java.lang.String) r4
            boolean r5 = mtopsdk.common.util.StringUtils.isNotBlank(r4)
            if (r5 == 0) goto L_0x03ad
            java.lang.String r5 = "x-sgext"
            r13.put(r5, r4)
        L_0x03ad:
            java.lang.String r4 = "pv"
            java.lang.String r5 = "6.3"
            r13.put(r4, r5)
            r16 = 1
            goto L_0x03b8
        L_0x03b7:
            r1 = 1
        L_0x03b8:
            if (r16 != 0) goto L_0x04d1
            java.lang.String r4 = "pv"
            java.lang.String r5 = "6.2"
            r13.put(r4, r5)
            mtopsdk.mtop.util.MtopStatistics r4 = r2.stats
            long r4 = r4.currentTimeMillis()
            java.lang.String r6 = r10.getMtopApiSign(r13, r14, r15)
            mtopsdk.mtop.util.MtopStatistics r7 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r8 = r2.stats
            long r8 = r8.currentTimeMillis()
            long r8 = r8 - r4
            r7.computeSignTime = r8
            boolean r4 = mtopsdk.common.util.StringUtils.isBlank(r6)
            if (r4 == 0) goto L_0x0410
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = 128(0x80, float:1.794E-43)
            r0.<init>(r1)
            java.lang.String r1 = "apiKey="
            r0.append(r1)
            java.lang.String r1 = r11.getKey()
            r0.append(r1)
            java.lang.String r1 = " call getMtopApiSign failed.[appKey="
            r0.append(r1)
            r0.append(r14)
            java.lang.String r1 = ", authCode="
            r0.append(r1)
            r0.append(r15)
            java.lang.String r1 = "]"
            r0.append(r1)
            java.lang.String r1 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r2 = r2.seqNo
            java.lang.String r0 = r0.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r1, (java.lang.String) r2, (java.lang.String) r0)
            return r13
        L_0x0410:
            java.lang.String r4 = "sign"
            r13.put(r4, r6)
            boolean r4 = r10 instanceof mtopsdk.security.LocalInnerSignImpl
            if (r4 != 0) goto L_0x04d1
            int r4 = r12.wuaFlag
            if (r4 >= 0) goto L_0x0421
            boolean r4 = r12.wuaRetry
            if (r4 == 0) goto L_0x0476
        L_0x0421:
            mtopsdk.mtop.util.MtopStatistics r4 = r2.stats
            long r4 = r4.currentTimeMillis()
            java.lang.String r7 = ""
            mtopsdk.mtop.global.SwitchConfig r8 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            int r8 = r8.getUseSecurityAdapter()
            r8 = r8 & r1
            if (r8 != r1) goto L_0x0438
            java.lang.String r7 = r10.getWua(r13, r14)
        L_0x0438:
            boolean r8 = mtopsdk.common.util.StringUtils.isBlank(r7)
            if (r8 == 0) goto L_0x0444
            int r7 = r12.wuaFlag
            java.lang.String r7 = r10.getAvmpSign(r6, r15, r7)
        L_0x0444:
            mtopsdk.mtop.util.MtopStatistics r6 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r8 = r2.stats
            long r8 = r8.currentTimeMillis()
            long r8 = r8 - r4
            r6.computeWuaTime = r8
            java.lang.String r4 = "wua"
            r13.put(r4, r7)
            boolean r4 = mtopsdk.common.util.StringUtils.isBlank(r7)
            if (r4 == 0) goto L_0x0476
            java.lang.String r4 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r5 = r2.seqNo
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = r11.getKey()
            r6.append(r7)
            java.lang.String r7 = " call getAvmpSign for wua fail."
            r6.append(r7)
            java.lang.String r6 = r6.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r4, (java.lang.String) r5, (java.lang.String) r6)
        L_0x0476:
            mtopsdk.mtop.util.MtopStatistics r4 = r2.stats
            long r16 = r4.currentTimeMillis()
            java.lang.String r4 = ""
            mtopsdk.mtop.global.SwitchConfig r5 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            int r5 = r5.getUseSecurityAdapter()
            r5 = r5 & r1
            if (r5 != r1) goto L_0x048d
            java.lang.String r4 = r10.getMiniWua(r13, r3)
        L_0x048d:
            boolean r1 = mtopsdk.common.util.StringUtils.isBlank(r4)
            if (r1 == 0) goto L_0x049e
            r9 = 8
            r4 = r10
            r5 = r0
            r6 = r14
            r7 = r15
            r8 = r3
            java.lang.String r4 = r4.getSecBodyDataEx(r5, r6, r7, r8, r9)
        L_0x049e:
            mtopsdk.mtop.util.MtopStatistics r0 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r1 = r2.stats
            long r5 = r1.currentTimeMillis()
            long r5 = r5 - r16
            r0.computeMiniWuaTime = r5
            java.lang.String r0 = "x-mini-wua"
            r13.put(r0, r4)
            boolean r0 = mtopsdk.common.util.StringUtils.isBlank(r4)
            if (r0 == 0) goto L_0x04d1
            java.lang.String r0 = "mtopsdk.InnerProtocolParamBuilderImpl"
            java.lang.String r1 = r2.seqNo
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = r11.getKey()
            r3.append(r4)
            java.lang.String r4 = " call getSecurityBodyDataEx for mini_wua failed."
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r0, (java.lang.String) r1, (java.lang.String) r3)
        L_0x04d1:
            r1 = r20
            r1.buildExtParams(r2, r13)
            mtopsdk.mtop.util.MtopStatistics r0 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r3 = r2.stats
            long r3 = r3.currentTimeMillis()
            r0.buildParamsEndTime = r3
            mtopsdk.mtop.util.MtopStatistics r0 = r2.stats
            mtopsdk.mtop.util.MtopStatistics r3 = r2.stats
            long r3 = r3.buildParamsEndTime
            mtopsdk.mtop.util.MtopStatistics r2 = r2.stats
            long r5 = r2.buildParamsStartTime
            long r3 = r3 - r5
            r0.buildParamsTime = r3
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.mtop.protocol.builder.impl.InnerProtocolParamBuilderImpl.buildParams(mtopsdk.framework.domain.MtopContext):java.util.Map");
    }

    private void buildExtParams(MtopContext mtopContext, Map<String, String> map) {
        MtopNetworkProp mtopNetworkProp = mtopContext.property;
        map.put("netType", XState.getValue("netType"));
        map.put(XStateConstants.KEY_NQ, XState.getValue(XStateConstants.KEY_NQ));
        if (map.get(XStateConstants.KEY_UMID_TOKEN) == null) {
            map.put(XStateConstants.KEY_UMID_TOKEN, XState.getValue(mtopContext.mtopInstance.getInstanceId(), XStateConstants.KEY_UMID_TOKEN));
        }
        String str = this.mtopConfig.appVersion;
        if (StringUtils.isNotBlank(str)) {
            map.put(HttpHeaderConstant.X_APP_VER, str);
        }
        String str2 = this.mtopConfig.xOrangeQ;
        if (StringUtils.isNotBlank(str2)) {
            map.put(HttpHeaderConstant.X_ORANGE_Q, str2);
        }
        map.put(HttpHeaderConstant.X_APP_CONF_V, String.valueOf(this.mtopConfig.xAppConfigVersion));
        String value = XState.getValue("ua");
        if (value != null) {
            map.put("user-agent", value);
        }
        map.put(HttpHeaderConstant.CLIENT_TRACE_ID, mtopNetworkProp.clientTraceId);
        map.put("f-refer", "mtop");
        if (mtopNetworkProp.netParam > 0) {
            JSONObject jSONObject = new JSONObject();
            if ((mtopNetworkProp.netParam & 1) != 0) {
                String str3 = NetworkStateReceiver.ssid;
                if (!TextUtils.isEmpty(str3)) {
                    try {
                        jSONObject.put(NetParam.NetParamKey.SSID, str3);
                    } catch (JSONException e) {
                        TBSdkLog.w(TAG, "set wifi ssid error.", (Throwable) e);
                    }
                }
            }
            if ((mtopNetworkProp.netParam & 2) != 0) {
                String str4 = NetworkStateReceiver.bssid;
                if (!TextUtils.isEmpty(str4)) {
                    try {
                        jSONObject.put(NetParam.NetParamKey.BSSID, str4);
                    } catch (JSONException e2) {
                        TBSdkLog.w(TAG, "set wifi bssid error.", (Throwable) e2);
                    }
                }
            }
            if (jSONObject.length() > 0) {
                map.put(HttpHeaderConstant.X_NETINFO, jSONObject.toString());
            }
        }
        if (mtopNetworkProp.pageName != null) {
            map.put(HttpHeaderConstant.X_PAGE_NAME, mtopNetworkProp.pageName);
        }
        if (mtopNetworkProp.pageUrl != null) {
            map.put(HttpHeaderConstant.X_PAGE_URL, mtopNetworkProp.pageUrl);
            String str5 = this.mtopConfig.mtopGlobalABTestParams.get(mtopNetworkProp.pageUrl);
            if (str5 != null) {
                map.put(HttpHeaderConstant.X_PAGE_MAB, str5);
            }
        }
    }
}
