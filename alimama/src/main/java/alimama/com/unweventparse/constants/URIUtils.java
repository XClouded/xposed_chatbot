package alimama.com.unweventparse.constants;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;

public class URIUtils {
    public static Bundle paramsToBundle(Uri uri) {
        Bundle bundle = new Bundle();
        if (uri == null) {
            return bundle;
        }
        for (String next : uri.getQueryParameterNames()) {
            bundle.putString(next, uri.getQueryParameter(next));
        }
        return bundle;
    }

    public static JSONObject paramsToSafeJson(Uri uri) {
        JSONObject jSONObject = new JSONObject();
        if (uri == null) {
            return jSONObject;
        }
        for (String next : uri.getQueryParameterNames()) {
            if (uri.getQueryParameter(next) != null) {
                jSONObject.put(next, (Object) uri.getQueryParameter(next));
            }
        }
        return jSONObject;
    }

    public static String getSchema(Uri uri) {
        return uri == null ? "" : uri.getScheme();
    }

    public static boolean isMatchSchema(Uri uri, @NonNull String str) {
        if (uri == null) {
            return false;
        }
        return TextUtils.equals(uri.getScheme(), str);
    }

    public static boolean isMatchHost(Uri uri, @NonNull String str) {
        if (uri == null) {
            return false;
        }
        return TextUtils.equals(uri.getHost(), str);
    }
}
