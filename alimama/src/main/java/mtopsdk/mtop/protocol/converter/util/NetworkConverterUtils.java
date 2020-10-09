package mtopsdk.mtop.protocol.converter.util;

import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;

public class NetworkConverterUtils {
    private static final String TAG = "mtopsdk.NetworkConverterUtils";

    public static URL initUrl(String str, Map<String, String> map) {
        if (StringUtils.isBlank(str)) {
            TBSdkLog.e(TAG, "[initUrl]baseUrl is blank,initUrl error");
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder(str);
            if (map != null) {
                String createParamQueryStr = createParamQueryStr(map, "utf-8");
                if (StringUtils.isNotBlank(createParamQueryStr) && !str.contains("?")) {
                    sb.append("?");
                    sb.append(createParamQueryStr);
                }
            }
            return new URL(sb.toString());
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[initUrl] build fullUrl error", (Throwable) e);
            return null;
        }
    }

    public static String createParamQueryStr(Map<String, String> map, String str) {
        if (map == null) {
            return null;
        }
        if (StringUtils.isBlank(str)) {
            str = "utf-8";
        }
        StringBuilder sb = new StringBuilder(64);
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry next = it.next();
            try {
                String encode = next.getKey() != null ? URLEncoder.encode((String) next.getKey(), str) : null;
                String encode2 = next.getValue() != null ? URLEncoder.encode((String) next.getValue(), str) : null;
                sb.append(encode);
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append(encode2);
                if (it.hasNext()) {
                    sb.append("&");
                }
            } catch (Throwable th) {
                TBSdkLog.e(TAG, "[createParamQueryStr]getQueryStr error ---" + th.toString());
            }
        }
        return sb.toString();
    }
}
