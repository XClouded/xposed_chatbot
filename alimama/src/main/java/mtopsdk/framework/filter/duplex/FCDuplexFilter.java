package mtopsdk.framework.filter.duplex;

import androidx.annotation.NonNull;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.middletier.fc.FCAction;
import com.alibaba.wireless.security.open.middletier.fc.IFCActionCallback;
import com.alibaba.wireless.security.open.middletier.fc.IFCComponent;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.taobao.tao.remotebusiness.RequestPoolManager;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.filter.after.AntiAttackAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.antiattack.ApiLockHelper;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.security.util.SignConstants;

public class FCDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.FCDuplexFilter";
    private AntiAttackAfterFilter antiAttackAfterFilter = new AntiAttackAfterFilter();
    private FlowLimitDuplexFilter flowLimitDuplexFilter = new FlowLimitDuplexFilter();

    @NonNull
    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        if ((SwitchConfig.getInstance().getUseSecurityAdapter() & 2) != 2) {
            return doOldFCAndAntiFilter(mtopContext);
        }
        final MtopResponse mtopResponse = mtopContext.mtopResponse;
        int responseCode = mtopResponse.getResponseCode();
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, " [doAfter]response code " + responseCode);
        }
        Map<String, List<String>> headerFields = mtopResponse.getHeaderFields();
        String singleHeaderFieldByKey = HeaderHandlerUtil.getSingleHeaderFieldByKey(headerFields, SignConstants.BX_USE_SG);
        if (StringUtils.isNotBlank(singleHeaderFieldByKey) && !Boolean.parseBoolean(singleHeaderFieldByKey)) {
            return doOldFCAndAntiFilter(mtopContext);
        }
        if (!(mtopContext.mtopBuilder instanceof MtopBusiness)) {
            return doOldFCAndAntiFilter(mtopContext);
        }
        if (headerFields == null) {
            return FilterResult.CONTINUE;
        }
        try {
            HashMap hashMap = new HashMap(headerFields);
            IFCComponent iFCComponent = (IFCComponent) SecurityGuardManager.getInstance(mtopContext.mtopInstance.getMtopConfig().context).getInterface(IFCComponent.class);
            mtopContext.stats.fcProcessCheckStartTime = mtopContext.stats.currentTimeMillis();
            if (iFCComponent == null || !iFCComponent.needFCProcessOrNot(responseCode, hashMap, IFCComponent.ResponseHeaderType.KVL)) {
                mtopContext.stats.fcProcessCheckEndTime = mtopContext.stats.currentTimeMillis();
                return FilterResult.CONTINUE;
            }
            mtopContext.stats.fcProcessCheckEndTime = mtopContext.stats.currentTimeMillis();
            final MtopBuilder mtopBuilder = mtopContext.mtopBuilder;
            final Mtop mtop = mtopContext.mtopInstance;
            RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).addToRequestPool(mtop, "", (MtopBusiness) mtopBuilder);
            final MtopContext mtopContext2 = mtopContext;
            AnonymousClass1 r0 = new IFCActionCallback() {
                public void onPreAction(long j, boolean z) {
                    mtopContext2.stats.bxSessionId = String.valueOf(j);
                    mtopContext2.stats.bxUI = z;
                }

                public void onAction(long j, FCAction.FCMainAction fCMainAction, long j2, HashMap hashMap) {
                    final long j3 = j;
                    final FCAction.FCMainAction fCMainAction2 = fCMainAction;
                    final long j4 = j2;
                    final HashMap hashMap2 = hashMap;
                    MtopSDKThreadPoolExecutorFactory.submitCallbackTask(mtopContext2.seqNo != null ? mtopContext2.seqNo.hashCode() : hashCode(), new Runnable() {
                        public void run() {
                            TBSdkLog.e(FCDuplexFilter.TAG, " [IFCActionCallback] onAction: " + ("--->###sessionId = " + j3 + ", MainAction = " + fCMainAction2 + ", subAction = " + j4 + ", extraInfo = " + hashMap2.toString() + "### ") + mtopContext2.seqNo);
                            mtopContext2.stats.fcProcessCallbackTime = mtopContext2.stats.currentTimeMillis();
                            mtopContext2.stats.bxMainAction = fCMainAction2.ordinal();
                            mtopContext2.stats.bxSubAction = j4;
                            if (fCMainAction2 == FCAction.FCMainAction.RETRY) {
                                mtopContext2.stats.bxRetry = 1;
                                String str = (String) hashMap2.get(SignConstants.BX_RESEND);
                                if (StringUtils.isNotBlank(str)) {
                                    HashMap hashMap = new HashMap();
                                    try {
                                        hashMap.put(SignConstants.BX_RESEND, URLEncoder.encode(str, "utf-8"));
                                        mtopBuilder.headers(hashMap);
                                    } catch (UnsupportedEncodingException unused) {
                                        TBSdkLog.e(FCDuplexFilter.TAG, "[IFCActionCallback]urlEncode x-bx-resend=" + str + "error");
                                    }
                                }
                                if ((j4 & FCAction.FCSubAction.LOGIN.getValue()) > 0) {
                                    RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).removeRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                    String str2 = mtopBuilder.mtopProp.userInfo;
                                    RequestPoolManager.getPool(RequestPoolManager.Type.SESSION).addToRequestPool(mtop, str2, (MtopBusiness) mtopBuilder);
                                    RemoteLogin.login(mtop, str2, true, mtopBuilder);
                                } else if ((j4 & FCAction.FCSubAction.WUA.getValue()) > 0) {
                                    mtopContext2.property.wuaRetry = true;
                                    RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).retryRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                } else {
                                    RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).retryRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                }
                            } else if (fCMainAction2 != FCAction.FCMainAction.FAIL) {
                                RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).removeRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                                    TBSdkLog.w(FCDuplexFilter.TAG, mtopContext2.seqNo, "[IFCActionCallback][SUCCESS/CANCEL/TIMEOUT] execute FCDuplexFilter apiKey=" + mtopContext2.mtopRequest.getKey());
                                }
                                mtopContext2.mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_41X_ANTI_ATTACK);
                                mtopContext2.mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_41X_ANTI_ATTACK);
                                FilterUtils.handleExceptionCallBack(mtopContext2);
                            } else if ((j4 & FCAction.FCSubAction.LOGIN.getValue()) > 0) {
                                RemoteLogin.login(mtop, mtopBuilder.mtopProp.userInfo, true, mtopBuilder);
                                RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).removeRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_41X_ANTI_ATTACK);
                                mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_41X_ANTI_ATTACK);
                                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                                    TBSdkLog.w(FCDuplexFilter.TAG, mtopContext2.seqNo, "[IFCActionCallback] execute FCDuplexFilter apiKey=" + mtopContext2.mtopRequest.getKey());
                                }
                                FilterUtils.handleExceptionCallBack(mtopContext2);
                            } else if ((j4 & FCAction.FCSubAction.FL.getValue()) > 0) {
                                RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).removeRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                String key = mtopContext2.mtopRequest.getKey();
                                long longValue = ((Long) hashMap2.get("bx-sleep")).longValue();
                                ApiLockHelper.lock(key, SDKUtils.getCorrectionTime(), longValue);
                                mtopContext2.stats.bxSleep = longValue;
                                FilterUtils.parseRetCodeFromHeader(mtopResponse);
                                if (StringUtils.isBlank(mtopResponse.getRetCode())) {
                                    mtopContext2.mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_FLOW_LIMIT_LOCKED);
                                    mtopContext2.mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_FLOW_LIMIT_LOCKED);
                                }
                                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                                    TBSdkLog.w(FCDuplexFilter.TAG, mtopContext2.seqNo, "[IFCActionCallback] doAfter execute FlowLimitDuplexFilter apiKey=" + key + " ,retCode=" + mtopResponse.getRetCode());
                                }
                                FilterUtils.handleExceptionCallBack(mtopContext2);
                            } else {
                                RequestPoolManager.getPool(RequestPoolManager.Type.ANTI).removeRequest(mtop, "", (MtopBusiness) mtopBuilder);
                                mtopContext2.mtopResponse.setRetCode(ErrorConstant.ERRCODE_API_41X_ANTI_ATTACK);
                                mtopContext2.mtopResponse.setRetMsg(ErrorConstant.ERRMSG_API_41X_ANTI_ATTACK);
                                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.WarnEnable)) {
                                    TBSdkLog.w(FCDuplexFilter.TAG, mtopContext2.seqNo, "[IFCActionCallback][FAIL] execute FCDuplexFilter apiKey=" + mtopContext2.mtopRequest.getKey());
                                }
                                FilterUtils.handleExceptionCallBack(mtopContext2);
                            }
                        }
                    });
                }
            };
            mtopContext.stats.fcProcessStartTime = mtopContext.stats.currentTimeMillis();
            TBSdkLog.e(TAG, "[IFCActionCallback]start process fc ", mtopContext.seqNo);
            iFCComponent.processFCContent(responseCode, hashMap, r0, IFCComponent.ResponseHeaderType.KVL);
            return FilterResult.STOP;
        } catch (SecException e) {
            TBSdkLog.e(TAG, "[IFCActionCallback] fc component exception , err code = " + e.getErrorCode());
            return FilterResult.CONTINUE;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, "[IFCActionCallback] fc component exception , msg = " + e2.getMessage());
            return FilterResult.CONTINUE;
        }
    }

    private String doOldFCAndAntiFilter(MtopContext mtopContext) {
        if (this.flowLimitDuplexFilter == null || this.antiAttackAfterFilter == null) {
            TBSdkLog.i(TAG, " [doAfter]flowLimitDuplexFilter or antiAttackAfterFilter create fail ");
            return FilterResult.STOP;
        }
        TBSdkLog.e(TAG, " [doOldFCAndAntiFilter] use old to do flow control, " + mtopContext.seqNo);
        String doAfter = this.antiAttackAfterFilter.doAfter(mtopContext);
        return (doAfter == null || FilterResult.STOP.equals(doAfter)) ? doAfter : this.flowLimitDuplexFilter.doAfter(mtopContext);
    }

    public String doBefore(MtopContext mtopContext) {
        return this.flowLimitDuplexFilter != null ? this.flowLimitDuplexFilter.doBefore(mtopContext) : FilterResult.CONTINUE;
    }
}
