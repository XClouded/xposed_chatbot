package android.taobao.windvane.connect;

import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.IApiAdapter;

import com.alibaba.android.bindingx.core.internal.BindingXConstants;

public class CdnApiAdapter implements IApiAdapter {
    private ApiRequest request;

    public String formatBody(ApiRequest apiRequest) {
        return "";
    }

    public String formatUrl(ApiRequest apiRequest) {
        if (apiRequest == null) {
            return "";
        }
        this.request = apiRequest;
        return wrapUrl(GlobalConfig.getCdnConfigUrlPre());
    }

    private String wrapUrl(String str) {
        if (str == null || str.length() <= 0) {
            return "";
        }
        Uri.Builder buildUpon = Uri.parse(str).buildUpon();
        buildUpon.appendPath(this.request.getParam("biztype"));
        buildUpon.appendPath("windvane");
        buildUpon.appendPath(BindingXConstants.KEY_CONFIG);
        if (this.request.getParam("api").contains("h5-apps.json")) {
            buildUpon.appendPath(this.request.getParam("wvgroupID"));
            buildUpon.appendPath(this.request.getParam("wvgroupVersion"));
        }
        StringBuilder sb = new StringBuilder();
        sb.append(GlobalConfig.getInstance().getAppKey());
        sb.append("-");
        sb.append(GlobalConfig.getInstance().getTtid());
        sb.append("-");
        sb.append(GlobalConfig.VERSION);
        int size = this.request.getDataParams().size();
        for (int i = 0; i < size; i++) {
            sb.append("-");
            sb.append(this.request.getDataParam(String.valueOf(i)));
        }
        buildUpon.appendPath(sb.toString());
        if (this.request.getParam("api").contains("h5-apps.json")) {
            buildUpon.appendPath(this.request.getParam("ABT"));
        }
        buildUpon.appendPath(this.request.getParam("api"));
        return buildUpon.toString();
    }
}
