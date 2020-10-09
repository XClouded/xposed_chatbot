package mtopsdk.framework.filter.after;

import androidx.annotation.NonNull;
import mtopsdk.framework.filter.IAfterFilter;

public class ErrorCodeMappingAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.ErrorCodeMappingAfterFilter";

    @NonNull
    public String getName() {
        return TAG;
    }

    /* JADX WARNING: Unknown top exception splitter block from list: {B:128:0x034a=Splitter:B:128:0x034a, B:177:0x04cd=Splitter:B:177:0x04cd} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String doAfter(mtopsdk.framework.domain.MtopContext r11) {
        /*
            r10 = this;
            java.lang.String r0 = r11.seqNo
            java.lang.String r1 = "CONTINUE"
            mtopsdk.mtop.global.SwitchConfig r2 = mtopsdk.mtop.global.SwitchConfig.getInstance()
            boolean r2 = r2.isGlobalErrorCodeMappingOpen()
            if (r2 != 0) goto L_0x0016
            java.lang.String r11 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r2 = "GlobalErrorCodeMappingOpen=false,Don't do ErrorCode Mapping."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r11, (java.lang.String) r0, (java.lang.String) r2)
            return r1
        L_0x0016:
            mtopsdk.mtop.domain.MtopResponse r2 = r11.mtopResponse
            boolean r3 = r2.isApiSuccess()
            if (r3 == 0) goto L_0x001f
            return r1
        L_0x001f:
            boolean r3 = r2.isNoNetwork()     // Catch:{ all -> 0x054b }
            r4 = 1
            if (r3 == 0) goto L_0x002a
            mtopsdk.mtop.util.MtopStatistics r3 = r11.stats     // Catch:{ all -> 0x054b }
            r3.isNoNetwork = r4     // Catch:{ all -> 0x054b }
        L_0x002a:
            boolean r3 = r2.isNetworkError()     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x00a9
            java.lang.String r3 = r2.getRetCode()     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.getMappingCodeByErrorCode(r3)     // Catch:{ all -> 0x054b }
            r2.mappingCodeSuffix = r3     // Catch:{ all -> 0x054b }
            int r3 = r2.getResponseCode()     // Catch:{ all -> 0x054b }
            java.lang.String r5 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.appendMappingCode(r3, r5)     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            java.util.Map<java.lang.String, java.lang.String> r3 = mtopsdk.mtop.global.SwitchConfig.errorMappingMsgMap     // Catch:{ all -> 0x054b }
            java.lang.String r5 = "NETWORK_ERROR_MAPPING"
            java.lang.Object r3 = r3.get(r5)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0053
            goto L_0x0055
        L_0x0053:
            java.lang.String r3 = "网络竟然崩溃了"
        L_0x0055:
            r2.setRetMsg(r3)     // Catch:{ all -> 0x054b }
            mtopsdk.mtop.util.MtopStatistics r3 = r11.stats     // Catch:{ all -> 0x054b }
            r3.retType = r4     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x00a8
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x00a3
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x00a5
        L_0x00a3:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x00a5:
            r11.onCommit(r2, r0)
        L_0x00a8:
            return r1
        L_0x00a9:
            mtopsdk.mtop.util.MtopStatistics r3 = r11.stats     // Catch:{ all -> 0x054b }
            r5 = 2
            r3.retType = r5     // Catch:{ all -> 0x054b }
            boolean r3 = r2.is41XResult()     // Catch:{ all -> 0x054b }
            if (r3 != 0) goto L_0x04cd
            boolean r3 = r2.isApiLockedResult()     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x00bc
            goto L_0x04cd
        L_0x00bc:
            boolean r3 = r2.isMtopServerError()     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0148
            java.lang.String r3 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            boolean r3 = mtopsdk.common.util.StringUtils.isBlank(r3)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x00dd
            java.lang.String r3 = r2.getRetCode()     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.getMappingCodeByErrorCode(r3)     // Catch:{ all -> 0x054b }
            boolean r4 = mtopsdk.common.util.StringUtils.isNotBlank(r3)     // Catch:{ all -> 0x054b }
            if (r4 == 0) goto L_0x00d9
            goto L_0x00db
        L_0x00d9:
            java.lang.String r3 = "ES00000"
        L_0x00db:
            r2.mappingCodeSuffix = r3     // Catch:{ all -> 0x054b }
        L_0x00dd:
            int r3 = r2.getResponseCode()     // Catch:{ all -> 0x054b }
            java.lang.String r4 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.appendMappingCode(r3, r4)     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            java.util.Map<java.lang.String, java.lang.String> r3 = mtopsdk.mtop.global.SwitchConfig.errorMappingMsgMap     // Catch:{ all -> 0x054b }
            java.lang.String r4 = "SERVICE_ERROR_MAPPING"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x00f6
            goto L_0x00f8
        L_0x00f6:
            java.lang.String r3 = "服务竟然出错了"
        L_0x00f8:
            r2.setRetMsg(r3)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x0147
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0142
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0144
        L_0x0142:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0144:
            r11.onCommit(r2, r0)
        L_0x0147:
            return r1
        L_0x0148:
            boolean r3 = r2.isMtopSdkError()     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x01d8
            java.lang.String r3 = r2.getRetCode()     // Catch:{ all -> 0x054b }
            java.lang.String r4 = mtopsdk.mtop.util.ErrorConstant.getMappingCodeByErrorCode(r3)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0162
            java.lang.String r5 = "ANDROID_SYS_GENERATE_MTOP_SIGN_ERROR"
            boolean r3 = r3.startsWith(r5)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0162
            java.lang.String r4 = "EC40002"
        L_0x0162:
            boolean r3 = mtopsdk.common.util.StringUtils.isNotBlank(r4)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0169
            goto L_0x016b
        L_0x0169:
            java.lang.String r4 = "EC00000"
        L_0x016b:
            r2.mappingCodeSuffix = r4     // Catch:{ all -> 0x054b }
            int r3 = r2.getResponseCode()     // Catch:{ all -> 0x054b }
            java.lang.String r4 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.appendMappingCode(r3, r4)     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            java.util.Map<java.lang.String, java.lang.String> r3 = mtopsdk.mtop.global.SwitchConfig.errorMappingMsgMap     // Catch:{ all -> 0x054b }
            java.lang.String r4 = "SERVICE_ERROR_MAPPING"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0186
            goto L_0x0188
        L_0x0186:
            java.lang.String r3 = "服务竟然出错了"
        L_0x0188:
            r2.setRetMsg(r3)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x01d7
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x01d2
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x01d4
        L_0x01d2:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x01d4:
            r11.onCommit(r2, r0)
        L_0x01d7:
            return r1
        L_0x01d8:
            mtopsdk.mtop.util.MtopStatistics r3 = r11.stats     // Catch:{ all -> 0x054b }
            r5 = 3
            r3.retType = r5     // Catch:{ all -> 0x054b }
            java.lang.String r3 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            boolean r3 = mtopsdk.common.util.StringUtils.isNotBlank(r3)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0236
            java.lang.String r3 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x0235
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0230
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0232
        L_0x0230:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0232:
            r11.onCommit(r2, r0)
        L_0x0235:
            return r1
        L_0x0236:
            java.lang.String r3 = r2.getRetCode()     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            boolean r5 = mtopsdk.common.util.StringUtils.isBlank(r3)     // Catch:{ all -> 0x054b }
            if (r5 == 0) goto L_0x028f
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x028e
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0289
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x028b
        L_0x0289:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x028b:
            r11.onCommit(r2, r0)
        L_0x028e:
            return r1
        L_0x028f:
            mtopsdk.mtop.global.SwitchConfig r5 = mtopsdk.mtop.global.SwitchConfig.getInstance()     // Catch:{ all -> 0x054b }
            boolean r5 = r5.isBizErrorCodeMappingOpen()     // Catch:{ all -> 0x054b }
            if (r5 != 0) goto L_0x02ed
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = "BizErrorCodeMappingOpen=false,Don't do BizErrorCode Mapping."
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x02ec
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x02e7
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x02e9
        L_0x02e7:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x02e9:
            r11.onCommit(r2, r0)
        L_0x02ec:
            return r1
        L_0x02ed:
            int r5 = r3.length()     // Catch:{ all -> 0x054b }
            r6 = 17
            if (r5 != r6) goto L_0x034a
            char r4 = r3.charAt(r4)     // Catch:{ all -> 0x054b }
            r5 = 45
            if (r4 != r5) goto L_0x034a
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x0349
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0344
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0346
        L_0x0344:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0346:
            r11.onCommit(r2, r0)
        L_0x0349:
            return r1
        L_0x034a:
            mtopsdk.mtop.global.SwitchConfig r4 = mtopsdk.mtop.global.SwitchConfig.getInstance()     // Catch:{ all -> 0x054b }
            java.util.Set<java.lang.String> r4 = r4.degradeBizErrorMappingApiSet     // Catch:{ all -> 0x054b }
            if (r4 == 0) goto L_0x03cf
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest     // Catch:{ all -> 0x054b }
            java.lang.String r4 = r4.getKey()     // Catch:{ all -> 0x054b }
            mtopsdk.mtop.global.SwitchConfig r5 = mtopsdk.mtop.global.SwitchConfig.getInstance()     // Catch:{ all -> 0x054b }
            java.util.Set<java.lang.String> r5 = r5.degradeBizErrorMappingApiSet     // Catch:{ all -> 0x054b }
            boolean r5 = r5.contains(r4)     // Catch:{ all -> 0x054b }
            if (r5 == 0) goto L_0x03cf
            mtopsdk.common.util.TBSdkLog$LogEnable r3 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ all -> 0x054b }
            boolean r3 = mtopsdk.common.util.TBSdkLog.isLogEnable(r3)     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x0382
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x054b }
            r5.<init>()     // Catch:{ all -> 0x054b }
            java.lang.String r6 = "apiKey in degradeBizErrorMappingApiSet,apiKey="
            r5.append(r6)     // Catch:{ all -> 0x054b }
            r5.append(r4)     // Catch:{ all -> 0x054b }
            java.lang.String r4 = r5.toString()     // Catch:{ all -> 0x054b }
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)     // Catch:{ all -> 0x054b }
        L_0x0382:
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x03ce
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x03c9
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x03cb
        L_0x03c9:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x03cb:
            r11.onCommit(r2, r0)
        L_0x03ce:
            return r1
        L_0x03cf:
            boolean r4 = mtopsdk.common.util.MtopUtils.isContainChineseCharacter(r3)     // Catch:{ Exception -> 0x0469 }
            if (r4 == 0) goto L_0x043c
            java.lang.String r4 = "TERR00000"
            r2.mappingCode = r4     // Catch:{ Exception -> 0x0469 }
            java.lang.String r4 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0469 }
            r5.<init>()     // Catch:{ Exception -> 0x0469 }
            java.lang.String r6 = "retCode contain chinese character,retCode="
            r5.append(r6)     // Catch:{ Exception -> 0x0469 }
            r5.append(r3)     // Catch:{ Exception -> 0x0469 }
            java.lang.String r5 = r5.toString()     // Catch:{ Exception -> 0x0469 }
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r4, (java.lang.String) r0, (java.lang.String) r5)     // Catch:{ Exception -> 0x0469 }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x043b
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0436
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0438
        L_0x0436:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0438:
            r11.onCommit(r2, r0)
        L_0x043b:
            return r1
        L_0x043c:
            java.lang.String r4 = mtopsdk.common.util.MtopUtils.caesarEncrypt(r3)     // Catch:{ Exception -> 0x0469 }
            boolean r5 = mtopsdk.common.util.StringUtils.isNotBlank(r4)     // Catch:{ Exception -> 0x0469 }
            if (r5 == 0) goto L_0x0480
            mtopsdk.mtop.global.SwitchConfig r5 = mtopsdk.mtop.global.SwitchConfig.getInstance()     // Catch:{ Exception -> 0x0469 }
            long r5 = r5.getGlobalBizErrorMappingCodeLength()     // Catch:{ Exception -> 0x0469 }
            int r7 = r4.length()     // Catch:{ Exception -> 0x0469 }
            long r7 = (long) r7     // Catch:{ Exception -> 0x0469 }
            int r9 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r9 <= 0) goto L_0x0466
            r7 = 0
            int r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
            if (r9 <= 0) goto L_0x0466
            r7 = 0
            int r5 = (int) r5     // Catch:{ Exception -> 0x0469 }
            java.lang.String r4 = r4.substring(r7, r5)     // Catch:{ Exception -> 0x0469 }
            r2.mappingCode = r4     // Catch:{ Exception -> 0x0469 }
            goto L_0x0480
        L_0x0466:
            r2.mappingCode = r4     // Catch:{ Exception -> 0x0469 }
            goto L_0x0480
        L_0x0469:
            r4 = move-exception
            java.lang.String r5 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x054b }
            r6.<init>()     // Catch:{ all -> 0x054b }
            java.lang.String r7 = "Mapping biz retCode to mappingCode error.retCode="
            r6.append(r7)     // Catch:{ all -> 0x054b }
            r6.append(r3)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = r6.toString()     // Catch:{ all -> 0x054b }
            mtopsdk.common.util.TBSdkLog.e(r5, r0, r3, r4)     // Catch:{ all -> 0x054b }
        L_0x0480:
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x04cc
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x04c7
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x04c9
        L_0x04c7:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x04c9:
            r11.onCommit(r2, r0)
        L_0x04cc:
            return r1
        L_0x04cd:
            java.lang.String r3 = r2.getRetCode()     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.getMappingCodeByErrorCode(r3)     // Catch:{ all -> 0x054b }
            boolean r4 = mtopsdk.common.util.StringUtils.isNotBlank(r3)     // Catch:{ all -> 0x054b }
            if (r4 == 0) goto L_0x04dc
            goto L_0x04de
        L_0x04dc:
            java.lang.String r3 = "ES00000"
        L_0x04de:
            r2.mappingCodeSuffix = r3     // Catch:{ all -> 0x054b }
            int r3 = r2.getResponseCode()     // Catch:{ all -> 0x054b }
            java.lang.String r4 = r2.mappingCodeSuffix     // Catch:{ all -> 0x054b }
            java.lang.String r3 = mtopsdk.mtop.util.ErrorConstant.appendMappingCode(r3, r4)     // Catch:{ all -> 0x054b }
            r2.mappingCode = r3     // Catch:{ all -> 0x054b }
            java.util.Map<java.lang.String, java.lang.String> r3 = mtopsdk.mtop.global.SwitchConfig.errorMappingMsgMap     // Catch:{ all -> 0x054b }
            java.lang.String r4 = "FLOW_LIMIT_ERROR_MAPPING"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x054b }
            if (r3 == 0) goto L_0x04f9
            goto L_0x04fb
        L_0x04f9:
            java.lang.String r3 = "前方拥挤，亲稍等再试试"
        L_0x04fb:
            r2.setRetMsg(r3)     // Catch:{ all -> 0x054b }
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x054a
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0545
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0547
        L_0x0545:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0547:
            r11.onCommit(r2, r0)
        L_0x054a:
            return r1
        L_0x054b:
            r1 = move-exception
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            java.lang.String r4 = r2.getResponseLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            java.lang.String r3 = "mtopsdk.ErrorCodeMappingAfterFilter"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r3, (java.lang.String) r0, (java.lang.String) r4)
            mtopsdk.mtop.stat.IMtopMonitor r0 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            if (r0 == 0) goto L_0x0598
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            java.lang.String r3 = "key_data_request"
            mtopsdk.mtop.domain.MtopRequest r4 = r11.mtopRequest
            java.lang.String r4 = r4.getRequestLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_response"
            java.lang.String r4 = r2.getResponseLog()
            r0.put(r3, r4)
            java.lang.String r3 = "key_data_seq"
            java.lang.String r11 = r11.seqNo
            r0.put(r3, r11)
            mtopsdk.mtop.stat.IMtopMonitor r11 = mtopsdk.mtop.stat.MtopMonitor.getInstance()
            java.util.Map r2 = r2.getHeaderFields()
            if (r2 == 0) goto L_0x0593
            java.lang.String r2 = "TYPE_ERROR_RESPONSE"
            goto L_0x0595
        L_0x0593:
            java.lang.String r2 = "TYPE_ERROR_REQUEST"
        L_0x0595:
            r11.onCommit(r2, r0)
        L_0x0598:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.after.ErrorCodeMappingAfterFilter.doAfter(mtopsdk.framework.domain.MtopContext):java.lang.String");
    }
}
