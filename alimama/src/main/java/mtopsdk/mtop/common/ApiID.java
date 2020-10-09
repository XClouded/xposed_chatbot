package mtopsdk.mtop.common;

import android.os.Handler;
import com.taobao.weex.el.parse.Operators;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.network.Call;

public class ApiID implements IMTOPDataObject {
    private static final String TAG = "mtopsdk.ApiID";
    private volatile Call call;
    private volatile boolean isCancelled = false;
    private MtopContext mtopContext;

    public ApiID(Call call2, MtopContext mtopContext2) {
        this.call = call2;
        this.mtopContext = mtopContext2;
    }

    public MtopContext getMtopContext() {
        return this.mtopContext;
    }

    public Call getCall() {
        return this.call;
    }

    public void setCall(Call call2) {
        this.call = call2;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public boolean cancelApiCall() {
        if (this.call != null) {
            this.call.cancel();
            this.isCancelled = true;
        }
        return true;
    }

    public ApiID retryApiCall(Handler handler) {
        if (this.mtopContext == null) {
            return null;
        }
        this.mtopContext.property.handler = handler;
        FilterManager filterManager = this.mtopContext.mtopInstance.getMtopConfig().filterManager;
        if (filterManager != null) {
            filterManager.start((String) null, this.mtopContext);
        }
        FilterUtils.checkFilterManager(filterManager, this.mtopContext);
        return new ApiID((Call) null, this.mtopContext);
    }

    public ApiID retryApiCall() {
        return retryApiCall((Handler) null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("ApiID [call=");
        sb.append(this.call);
        sb.append(", mtopContext=");
        sb.append(this.mtopContext);
        sb.append(Operators.ARRAY_END_STR);
        return sb.toString();
    }
}
