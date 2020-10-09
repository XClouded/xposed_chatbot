package com.alibaba.ut.abtest.pipeline.encoder;

import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.Base64;
import com.alibaba.ut.abtest.UTABVersion;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.util.JsonUtil;
import com.alibaba.ut.abtest.internal.util.SystemInformation;
import com.alibaba.ut.abtest.pipeline.Request;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public final class ProtocolEncoder {
    private static final String SECRET = "51734f6a783d4d4e6152405f413f68684552807b888d8163927b5280838d648d";

    public String encode(HttpURLConnection httpURLConnection, Request request) throws Exception {
        String str = "";
        if (!(request.getParams() == null || request.getParams().getValue() == null)) {
            Object value = request.getParams().getValue();
            if (value instanceof Map) {
                str = JsonUtil.toJson((Map<String, ?>) (Map) request.getParams().getValue());
            } else if (value instanceof List) {
                str = JsonUtil.toJson((List) request.getParams().getValue());
            }
        }
        String encodeToString = Base64.encodeToString(RC4.rc4(str.getBytes(ABConstants.BasicConstants.DEFAULT_CHARSET)), 2);
        httpURLConnection.setRequestProperty("ab-sign", HmacUtils.hmacMd5Hex(SECRET, encodeToString));
        httpURLConnection.setRequestProperty("ab-client-version", UTABVersion.VERSION_NAME);
        httpURLConnection.setRequestProperty("app-key", ClientVariables.getInstance().getAppKey());
        httpURLConnection.setRequestProperty("app-version", SystemInformation.getInstance().getAppVersionName());
        return URLEncoder.encode(encodeToString, ABConstants.BasicConstants.DEFAULT_CHARSET.name());
    }
}
