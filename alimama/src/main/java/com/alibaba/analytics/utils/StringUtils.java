package com.alibaba.analytics.utils;

import android.text.TextUtils;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import mtopsdk.common.util.SymbolExpUtil;

public class StringUtils {
    public static String convertObjectToString(Object obj) {
        if (obj == null) {
            return "";
        }
        if (obj instanceof String) {
            return ((String) obj).toString();
        }
        if (obj instanceof Integer) {
            return "" + ((Integer) obj).intValue();
        } else if (obj instanceof Long) {
            return "" + ((Long) obj).longValue();
        } else if (obj instanceof Double) {
            return "" + ((Double) obj).doubleValue();
        } else if (obj instanceof Float) {
            return "" + ((Float) obj).floatValue();
        } else if (obj instanceof Short) {
            return "" + ((Short) obj).shortValue();
        } else if (obj instanceof Byte) {
            return "" + ((Byte) obj).byteValue();
        } else if (obj instanceof Boolean) {
            return ((Boolean) obj).toString();
        } else {
            if (obj instanceof Character) {
                return ((Character) obj).toString();
            }
            return obj.toString();
        }
    }

    public static int hashCode(String str) {
        if (str.length() <= 0) {
            return 0;
        }
        int i = 0;
        for (char c : str.toCharArray()) {
            i = (i * 31) + c;
        }
        return i;
    }

    public static String convertMapToString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (String next : map.keySet()) {
            String convertObjectToString = convertObjectToString(map.get(next));
            String convertObjectToString2 = convertObjectToString(next);
            if (!(convertObjectToString == null || convertObjectToString2 == null)) {
                if (z) {
                    stringBuffer.append(convertObjectToString2 + SymbolExpUtil.SYMBOL_EQUAL + convertObjectToString);
                    z = false;
                } else {
                    stringBuffer.append(",");
                    stringBuffer.append(convertObjectToString2 + SymbolExpUtil.SYMBOL_EQUAL + convertObjectToString);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String convertStringAToString(String... strArr) {
        if (strArr != null && strArr.length == 0) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        if (strArr != null && strArr.length > 0) {
            boolean z = false;
            for (int i = 0; i < strArr.length; i++) {
                if (!isEmpty(strArr[i])) {
                    if (z) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(strArr[i]);
                    z = true;
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String[] convertMapToStringA(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        for (String next : map.keySet()) {
            String str = map.get(next);
            String convertObjectToString = convertObjectToString(next);
            if (!(str == null || convertObjectToString == null)) {
                linkedList.add(convertObjectToString + SymbolExpUtil.SYMBOL_EQUAL + str);
            }
        }
        if (linkedList.size() <= 0) {
            return null;
        }
        String[] strArr = new String[linkedList.size()];
        for (int i = 0; i < linkedList.size(); i++) {
            strArr[i] = (String) linkedList.get(i);
        }
        return strArr;
    }

    public static String convertToPostString(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (String next : map.keySet()) {
            String convertObjectToString = convertObjectToString(map.get(next));
            String convertObjectToString2 = convertObjectToString(next);
            if (!(convertObjectToString == null || convertObjectToString2 == null)) {
                if (z) {
                    stringBuffer.append(convertObjectToString2 + SymbolExpUtil.SYMBOL_EQUAL + convertObjectToString);
                    z = false;
                } else {
                    stringBuffer.append("&");
                    stringBuffer.append(convertObjectToString2 + SymbolExpUtil.SYMBOL_EQUAL + convertObjectToString);
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String convertMapToStringWithUrlEncoder(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        boolean z = true;
        StringBuffer stringBuffer = new StringBuffer();
        for (String next : map.keySet()) {
            String convertObjectToString = convertObjectToString(map.get(next));
            String convertObjectToString2 = convertObjectToString(next);
            if (!(convertObjectToString == null || convertObjectToString2 == null)) {
                if (z) {
                    try {
                        stringBuffer.append(URLEncoder.encode(convertObjectToString2, "UTF-8") + SymbolExpUtil.SYMBOL_EQUAL + URLEncoder.encode(convertObjectToString, "UTF-8"));
                        z = false;
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        stringBuffer.append(",");
                        stringBuffer.append(URLEncoder.encode(convertObjectToString2, "UTF-8") + SymbolExpUtil.SYMBOL_EQUAL + URLEncoder.encode(convertObjectToString, "UTF-8"));
                    } catch (UnsupportedEncodingException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
        return stringBuffer.toString();
    }

    public static String transMapToString(Map<String, String> map) {
        String str;
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry next = it.next();
            stringBuffer.append(next.getKey().toString());
            stringBuffer.append(DXBindingXConstant.SINGLE_QUOTE);
            if (next.getValue() == null) {
                str = "";
            } else {
                str = next.getValue().toString();
            }
            stringBuffer.append(str);
            stringBuffer.append(it.hasNext() ? "^" : "");
        }
        return stringBuffer.toString();
    }

    public static Map<String, String> transStringToMap(String str) {
        HashMap hashMap = new HashMap();
        StringTokenizer stringTokenizer = new StringTokenizer(str, "^");
        while (stringTokenizer.hasMoreTokens()) {
            StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), DXBindingXConstant.SINGLE_QUOTE);
            hashMap.put(stringTokenizer2.nextToken(), stringTokenizer2.hasMoreTokens() ? stringTokenizer2.nextToken() : null);
        }
        return hashMap;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }
}
