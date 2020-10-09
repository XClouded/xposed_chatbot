package mtopsdk.framework.filter.after;

import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.network.AbstractCallImpl;

public class NetworkErrorAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.NetworkErrorAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse = mtopContext.mtopResponse;
        if (mtopResponse.getResponseCode() >= 0) {
            return FilterResult.CONTINUE;
        }
        if (mtopContext.apiId == null || mtopContext.apiId.getCall() == null || !(mtopContext.apiId.getCall() instanceof AbstractCallImpl) || !((AbstractCallImpl) mtopContext.apiId.getCall()).isNoNetworkError(mtopResponse.getResponseCode())) {
            mtopResponse.setRetCode("ANDROID_SYS_NETWORK_ERROR");
            mtopResponse.setRetMsg("网络错误");
        } else {
            mtopResponse.setRetCode(ErrorConstant.ERRCODE_NO_NETWORK);
            mtopResponse.setRetMsg(ErrorConstant.ERRMSG_NO_NETWORK);
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
            StringBuilder sb = new StringBuilder(128);
            sb.append("api=");
            sb.append(mtopResponse.getApi());
            sb.append(",v=");
            sb.append(mtopResponse.getV());
            sb.append(",retCode =");
            sb.append(mtopResponse.getRetCode());
            sb.append(",responseCode =");
            sb.append(mtopResponse.getResponseCode());
            sb.append(",responseHeader=");
            sb.append(mtopResponse.getHeaderFields());
            TBSdkLog.e(TAG, mtopContext.seqNo, sb.toString());
        }
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
