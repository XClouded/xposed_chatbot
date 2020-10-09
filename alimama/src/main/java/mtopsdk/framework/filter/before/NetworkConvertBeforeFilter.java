package mtopsdk.framework.filter.before;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.protocol.converter.INetworkConverter;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.network.domain.Request;

public class NetworkConvertBeforeFilter implements IBeforeFilter {
    private static final String TAG = "mtopsdk.NetworkConvertBeforeFilter";
    private INetworkConverter networkConverter;

    public String getName() {
        return TAG;
    }

    public NetworkConvertBeforeFilter(@NonNull INetworkConverter iNetworkConverter) {
        this.networkConverter = iNetworkConverter;
    }

    public String doBefore(MtopContext mtopContext) {
        Request convert = this.networkConverter.convert(mtopContext);
        convert.fullTraceId = mtopContext.stats.fullTraceId;
        String launchInfoValue = mtopContext.stats.launchInfoValue();
        if (!TextUtils.isEmpty(launchInfoValue)) {
            convert.headers.put(HttpHeaderConstant.LAUNCH_INFO_KEY, launchInfoValue);
        }
        mtopContext.networkRequest = convert;
        mtopContext.stats.url = convert.url;
        if (convert != null) {
            return FilterResult.CONTINUE;
        }
        mtopContext.mtopResponse = new MtopResponse(mtopContext.mtopRequest.getApiName(), mtopContext.mtopRequest.getVersion(), ErrorConstant.ERRCODE_NETWORK_REQUEST_CONVERT_ERROR, ErrorConstant.ERRMSG_NETWORK_REQUEST_CONVERT_ERROR);
        FilterUtils.handleExceptionCallBack(mtopContext);
        return FilterResult.STOP;
    }
}
