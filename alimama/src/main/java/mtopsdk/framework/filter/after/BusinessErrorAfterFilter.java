package mtopsdk.framework.filter.after;

import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.util.ErrorConstant;

public class BusinessErrorAfterFilter implements IAfterFilter {
    private static final String TAG = "mtopsdk.BusinessErrorAfterFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        MtopResponse mtopResponse;
        MtopResponse mtopResponse2 = mtopContext.mtopResponse;
        if (304 == mtopResponse2.getResponseCode() && mtopContext.responseSource != null && (mtopResponse = mtopContext.responseSource.cacheResponse) != null) {
            mtopContext.mtopResponse = mtopResponse;
            FilterUtils.handleExceptionCallBack(mtopContext);
            return FilterResult.STOP;
        } else if (mtopResponse2.getBytedata() == null) {
            mtopResponse2.setRetCode(ErrorConstant.ERRCODE_JSONDATA_BLANK);
            mtopResponse2.setRetMsg(ErrorConstant.ERRMSG_JSONDATA_BLANK);
            FilterUtils.handleExceptionCallBack(mtopContext);
            return FilterResult.STOP;
        } else {
            FilterUtils.parseRetCodeFromHeader(mtopResponse2);
            return FilterResult.CONTINUE;
        }
    }
}
