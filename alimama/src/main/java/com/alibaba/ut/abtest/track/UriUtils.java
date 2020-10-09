package com.alibaba.ut.abtest.track;

import android.net.Uri;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.taobao.android.dinamicx.template.utils.DXTemplateNamePathUtil;
import com.taobao.weex.el.parse.Operators;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class UriUtils {
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final String PARAMETER_SEPARATOR = "&";
    private static final String TAG = "UriUtils";

    public static URI createURI(String str, String str2, int i, String str3, String str4, String str5) throws URISyntaxException {
        StringBuilder sb = new StringBuilder();
        if (str2 != null) {
            if (str != null) {
                sb.append(str);
                sb.append(HttpConstant.SCHEME_SPLIT);
            } else {
                sb.append("//");
            }
            sb.append(str2);
            if (i > 0) {
                sb.append(Operators.CONDITION_IF_MIDDLE);
                sb.append(i);
            }
        }
        if (str3 == null || !str3.startsWith("/")) {
            sb.append(DXTemplateNamePathUtil.DIR);
        }
        if (str3 != null) {
            sb.append(str3);
        }
        if (str4 != null) {
            sb.append(Operators.CONDITION_IF);
            sb.append(str4);
        }
        if (str5 != null) {
            sb.append('#');
            sb.append(str5);
        }
        return new URI(sb.toString());
    }

    public static LinkedHashMap<String, String> getQueryParameters(Uri uri) {
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        for (String next : queryParameterNames) {
            linkedHashMap.put(next, uri.getQueryParameter(next));
        }
        return linkedHashMap;
    }

    public static LinkedHashMap<String, String> mergeParameters(LinkedHashMap<String, String> linkedHashMap, Uri uri) {
        LinkedHashMap<String, String> linkedHashMap2 = new LinkedHashMap<>();
        if (linkedHashMap != null) {
            linkedHashMap2.putAll(linkedHashMap);
        }
        LinkedHashMap<String, String> queryParameters = getQueryParameters(uri);
        if (queryParameters != null && !queryParameters.isEmpty()) {
            linkedHashMap2.putAll(queryParameters);
        }
        return linkedHashMap2;
    }

    public static String formatQueryParameters(Map<String, String> map, String str) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : map.entrySet()) {
            String encode = encode((String) next.getKey(), str);
            String str2 = (String) next.getValue();
            String encode2 = str2 != null ? encode(str2, str) : "";
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(encode);
            sb.append("=");
            sb.append(encode2);
        }
        return sb.toString();
    }

    public static Map<String, String> parse(URI uri, String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        String rawQuery = uri.getRawQuery();
        if (rawQuery != null && rawQuery.length() > 0) {
            parse(linkedHashMap, new Scanner(rawQuery), str);
        }
        return linkedHashMap;
    }

    public static void parse(Map<String, String> map, Scanner scanner, String str) {
        int indexOf;
        scanner.useDelimiter("&");
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (!TextUtils.isEmpty(next) && (indexOf = next.indexOf("=")) != -1) {
                String substring = next.substring(0, indexOf);
                String substring2 = next.substring(indexOf + 1);
                if (!TextUtils.isEmpty(substring)) {
                    map.put(decode(substring, str), decode(substring2, str));
                }
            }
        }
    }

    private static String decode(String str, String str2) {
        if (str2 == null) {
            str2 = "UTF-8";
        }
        try {
            return URLDecoder.decode(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String encode(String str, String str2) {
        if (str2 == null) {
            str2 = "UTF-8";
        }
        try {
            return URLEncoder.encode(str, str2);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Uri parseURI(String str, boolean z) {
        if (z) {
            try {
                str = correctURI(str);
            } catch (Exception unused) {
                return null;
            }
        }
        return Uri.parse(str);
    }

    public static Uri parseURI(String str) {
        return parseURI(str, true);
    }

    public static boolean equalsExperimentURI(Uri uri, Uri uri2) {
        if (uri == uri2) {
            return true;
        }
        if (uri == null || uri2 == null || !equalsScheme(getScheme(uri, "http"), getScheme(uri2, "http")) || !TextUtils.equals(uri.getAuthority(), uri2.getAuthority()) || !TextUtils.equals(uri.getPath(), uri2.getPath())) {
            return false;
        }
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (!uri2.getQueryParameterNames().containsAll(queryParameterNames)) {
            return false;
        }
        for (String next : queryParameterNames) {
            String queryParameter = uri.getQueryParameter(next);
            if (!TextUtils.isEmpty(queryParameter) && !TextUtils.equals(queryParameter, uri2.getQueryParameter(next))) {
                return false;
            }
        }
        return true;
    }

    private static String getScheme(Uri uri, String str) {
        if (uri == null) {
            return str;
        }
        String scheme = uri.getScheme();
        return TextUtils.isEmpty(scheme) ? str : scheme;
    }

    private static boolean equalsScheme(String str, String str2) {
        if (TextUtils.equals(str, str2)) {
            return true;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !str.startsWith("http") || !str2.startsWith("http")) {
            return false;
        }
        return true;
    }

    private static String correctURI(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(":");
        if (str.charAt(indexOf + 1) == '/' && str.charAt(indexOf + 2) == '/') {
            return str;
        }
        return "//" + str;
    }
}
