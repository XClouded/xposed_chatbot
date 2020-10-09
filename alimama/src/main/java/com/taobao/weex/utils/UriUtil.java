package com.taobao.weex.utils;

import android.net.Uri;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.ranger.api.RangerInitializer;
import com.taobao.weex.constant.Constants;
import java.util.Set;

public class UriUtil {
    private static final String TAG = "TBWXNavPreProcessor";

    public static Uri addScheme(@NonNull Uri uri) {
        if (!TextUtils.isEmpty(uri.getScheme())) {
            return uri;
        }
        Uri.Builder buildUpon = uri.buildUpon();
        if (uri.getHost() == null) {
            String uri2 = uri.toString();
            buildUpon = Uri.parse("//" + uri2).buildUpon();
        }
        buildUpon.scheme("http");
        return buildUpon.build();
    }

    public static Uri getBundleUri(@NonNull Uri uri) {
        if (uri.getBooleanQueryParameter(Constants.WH_WX, false)) {
            return uri;
        }
        String queryParameter = uri.getQueryParameter(Constants.WX_TPL);
        WXLogUtils.d(TAG, "origin WX_TPL:" + queryParameter);
        if (TextUtils.isEmpty(queryParameter)) {
            return null;
        }
        String aBTestUrl = RangerInitializer.getABTestUrl(queryParameter);
        if (!TextUtils.isEmpty(aBTestUrl)) {
            queryParameter = aBTestUrl;
        }
        WXLogUtils.d(TAG, "AB_TEST WX_TPL:" + queryParameter);
        Uri parse = Uri.parse(queryParameter);
        if (parse == null) {
            return null;
        }
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        Uri.Builder buildUpon = parse.buildUpon();
        for (String next : queryParameterNames) {
            if (!TextUtils.equals(next, Constants.WX_TPL)) {
                buildUpon.appendQueryParameter(next, uri.getQueryParameter(next));
            }
        }
        return buildUpon.build();
    }
}
