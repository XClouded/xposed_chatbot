package com.alibaba.taffy.core.util.net;

import android.os.Bundle;
import com.alibaba.taffy.core.util.lang.BundleUtil;
import com.alibaba.taffy.core.util.lang.MapUtil;
import com.alibaba.taffy.core.util.lang.StringUtil;
import com.taobao.weex.el.parse.Operators;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class URLEncodedUtil {
    private static final String NAME_VALUE_SEPARATOR = "=";
    private static final char[] QP_SEPS = {'&', ';'};
    private static final char QP_SEP_A = '&';
    private static final String QP_SEP_PATTERN = (Operators.ARRAY_START_STR + new String(QP_SEPS) + Operators.ARRAY_END_STR);
    private static final char QP_SEP_S = ';';

    public static String encode(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return URLEncoder.encode(str, str2);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String decode(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return URLDecoder.decode(str, str2);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String format(Map<String, String> map, String str) {
        return format(map, '&', str);
    }

    public static String format(Map<String, String> map, char c, String str) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry next : map.entrySet()) {
            String encode = encode((String) next.getKey(), str);
            String encode2 = encode((String) next.getValue(), str);
            if (sb.length() > 0) {
                sb.append(c);
            }
            if (StringUtil.isNotEmpty(encode)) {
                sb.append(encode);
                sb.append("=");
                if (encode2 != null) {
                    sb.append(encode2);
                }
            }
        }
        return sb.toString();
    }

    public static String format(Bundle bundle, String str) {
        return format(bundle, '&', str);
    }

    public static String format(Bundle bundle, char c, String str) {
        StringBuilder sb = new StringBuilder();
        for (String str2 : bundle.keySet()) {
            String encode = encode(str2, str);
            String encode2 = encode(bundle.getString(str2), str);
            if (sb.length() > 0) {
                sb.append(c);
            }
            if (StringUtil.isNotEmpty(encode)) {
                sb.append(encode);
                sb.append("=");
                if (encode2 != null) {
                    sb.append(encode2);
                }
            }
        }
        return sb.toString();
    }

    public static String encodeURL(URI uri, Bundle bundle, String str) {
        return encodeURL(uri.toString(), bundle, str);
    }

    public static String encodeURL(String str, Bundle bundle, String str2) {
        if (!StringUtil.isNotBlank(str) || !BundleUtil.isNotEmpty(bundle)) {
            return str;
        }
        URLParseBundleInfo parseAllByBundle = parseAllByBundle(str, str2);
        StringBuilder sb = new StringBuilder(parseAllByBundle.base);
        Bundle merge = BundleUtil.merge(parseAllByBundle.param, bundle);
        sb.append("?");
        sb.append(format(merge, str2));
        if (BundleUtil.isNotEmpty(parseAllByBundle.fragment)) {
            sb.append("#");
            sb.append(format(parseAllByBundle.fragment, str2));
        }
        return sb.toString();
    }

    public static String encodeURL(URI uri, Map<String, String> map, String str) {
        return encodeURL(uri.toString(), map, str);
    }

    public static String encodeURL(String str, Map<String, String> map, String str2) {
        if (!StringUtil.isNotBlank(str) || !MapUtil.isNotEmpty(map)) {
            return str;
        }
        URLParseInfo parseAll = parseAll(str, str2);
        StringBuilder sb = new StringBuilder(parseAll.base);
        Map<String, String> mergeToFirst = MapUtil.mergeToFirst(parseAll.param, map);
        sb.append("?");
        sb.append(format(mergeToFirst, str2));
        if (MapUtil.isNotEmpty(parseAll.fragment)) {
            sb.append("#");
            sb.append(format(parseAll.fragment, str2));
        }
        return sb.toString();
    }

    public static URLParseInfo parseAll(URI uri, String str) {
        return parseAll(uri.toString(), str);
    }

    public static URLParseInfo parseAll(String str, String str2) {
        URLParseInfo uRLParseInfo = new URLParseInfo();
        if (str != null && str.length() > 0) {
            String[] split = StringUtil.split(str, "#");
            if (split.length > 0) {
                Scanner scanner = new Scanner(split[0]);
                scanner.useDelimiter("\\?");
                if (scanner.hasNext()) {
                    uRLParseInfo.base = scanner.next();
                    if (scanner.hasNext()) {
                        scanner.skip("\\?");
                    }
                    parse(uRLParseInfo.param, scanner, QP_SEP_PATTERN, str2);
                }
            }
            if (split.length > 1) {
                parse(uRLParseInfo.fragment, new Scanner(split[1]), QP_SEP_PATTERN, str2);
            }
        }
        return uRLParseInfo;
    }

    public static Map<String, String> parse(URI uri, String str) {
        return parse(uri.getRawQuery(), str);
    }

    public static Map<String, String> parse(String str, String str2) {
        if (str == null || str.length() <= 0) {
            return Collections.emptyMap();
        }
        HashMap hashMap = new HashMap();
        parse(hashMap, new Scanner(str), QP_SEP_PATTERN, str2);
        return hashMap;
    }

    private static void parse(Map<String, String> map, Scanner scanner, String str, String str2) {
        String str3;
        String str4;
        scanner.useDelimiter(str);
        while (scanner.hasNext()) {
            try {
                String next = scanner.next();
                int indexOf = next.indexOf("=");
                if (indexOf != -1) {
                    str3 = URLDecoder.decode(next.substring(0, indexOf).trim(), str2);
                    str4 = URLDecoder.decode(next.substring(indexOf + 1).trim(), str2);
                } else {
                    String decode = URLDecoder.decode(next.trim(), str2);
                    str4 = null;
                    str3 = decode;
                }
                map.put(str3, str4);
            } catch (Exception unused) {
                return;
            }
        }
    }

    public static URLParseBundleInfo parseAllByBundle(URI uri, String str) {
        return parseAllByBundle(uri.toString(), str);
    }

    public static URLParseBundleInfo parseAllByBundle(String str, String str2) {
        URLParseBundleInfo uRLParseBundleInfo = new URLParseBundleInfo();
        if (str != null && str.length() > 0) {
            String[] split = StringUtil.split(str, "#");
            if (split.length > 0) {
                Scanner scanner = new Scanner(split[0]);
                scanner.useDelimiter("\\?");
                if (scanner.hasNext()) {
                    uRLParseBundleInfo.base = scanner.next();
                    if (scanner.hasNext()) {
                        scanner.skip("\\?");
                    }
                    parseByBundle(uRLParseBundleInfo.param, scanner, QP_SEP_PATTERN, str2);
                }
            }
            if (split.length > 1) {
                parseByBundle(uRLParseBundleInfo.fragment, new Scanner(split[1]), QP_SEP_PATTERN, str2);
            }
        }
        return uRLParseBundleInfo;
    }

    public static Bundle parseByBundle(URI uri, String str) {
        return parseByBundle(uri.getRawQuery(), str);
    }

    public static Bundle parseByBundle(String str, String str2) {
        if (str == null || str.length() <= 0) {
            return new Bundle();
        }
        Bundle bundle = new Bundle();
        parseByBundle(bundle, new Scanner(str), QP_SEP_PATTERN, str2);
        return bundle;
    }

    private static void parseByBundle(Bundle bundle, Scanner scanner, String str, String str2) {
        String str3;
        String str4;
        scanner.useDelimiter(str);
        while (scanner.hasNext()) {
            try {
                String next = scanner.next();
                int indexOf = next.indexOf("=");
                if (indexOf != -1) {
                    str3 = URLDecoder.decode(next.substring(0, indexOf).trim(), str2);
                    str4 = URLDecoder.decode(next.substring(indexOf + 1).trim(), str2);
                } else {
                    String decode = URLDecoder.decode(next.trim(), str2);
                    str4 = null;
                    str3 = decode;
                }
                bundle.putString(str3, str4);
            } catch (Exception unused) {
                return;
            }
        }
    }
}
