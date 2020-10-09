package mtopsdk.framework.filter.before;

import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.global.SwitchConfig;
import mtopsdk.mtop.util.ErrorConstant;

public class CheckRequestParamBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.CheckRequestParamBeforeFilter";

    public String getName() {
        return TAG;
    }

    public String doBefore(MtopContext mtopContext) {
        return checkRequiredParam(mtopContext) ? FilterResult.CONTINUE : FilterResult.STOP;
    }

    private boolean checkRequiredParam(MtopContext mtopContext) {
        MtopResponse mtopResponse;
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopNetworkProp mtopNetworkProp = mtopContext.property;
        String str = mtopContext.seqNo;
        String str2 = null;
        if (mtopRequest == null) {
            str2 = "mtopRequest is invalid.mtopRequest=null";
            mtopResponse = new MtopResponse(ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, str2);
        } else if (!mtopRequest.isLegalRequest()) {
            str2 = "mtopRequest is invalid. " + mtopRequest.toString();
            mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, str2);
        } else if (mtopNetworkProp == null) {
            str2 = "MtopNetworkProp is invalid.property=null";
            mtopResponse = new MtopResponse(mtopRequest.getApiName(), mtopRequest.getVersion(), ErrorConstant.ERRCODE_MTOPCONTEXT_INIT_ERROR, str2);
        } else {
            mtopResponse = null;
        }
        mtopContext.mtopResponse = mtopResponse;
        if (StringUtils.isNotBlank(str2) && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
            TBSdkLog.e(TAG, str, "[checkRequiredParam]" + str2);
        }
        if (mtopRequest != null && TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, str, "[checkRequiredParam]" + mtopRequest.toString());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        if (!SwitchConfig.getInstance().isGlobalSpdySslSwitchOpen()) {
            TBSdkLog.w(TAG, str, "[checkRequiredParam]MTOP SSL switch is false");
            mtopContext.property.protocol = ProtocolEnum.HTTP;
        }
        return mtopResponse == null;
    }
}
