package android.taobao.windvane.util;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.text.TextUtils;

import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.vessel.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import mtopsdk.common.util.SymbolExpUtil;

@SuppressLint({"DefaultLocale"})
public class WVUrlUtil {
    private static Map<String, String> mimeTypes = new HashMap();

    static {
        for (MimeTypeEnum mimeTypeEnum : MimeTypeEnum.values()) {
            mimeTypes.put(mimeTypeEnum.getSuffix(), mimeTypeEnum.getMimeType());
        }
    }

    public static boolean isRes(String str) {
        String suffix = getSuffix(str);
        return MimeTypeEnum.JS.getSuffix().equals(suffix) || MimeTypeEnum.CSS.getSuffix().equals(suffix);
    }

    public static boolean shouldTryABTest(String str) {
        Uri parse = Uri.parse(str);
        if (!parse.isHierarchical()) {
            return false;
        }
        String path = parse.getPath();
        if (path.endsWith(".htm") || path.endsWith(".html") || path.endsWith(".js")) {
            return true;
        }
        return false;
    }

    public static boolean isImg(String str) {
        return getMimeType(str).startsWith("image");
    }

    public static boolean isHtml(String str) {
        String path;
        if (!TextUtils.isEmpty(str) && !str.contains("??") && (path = Uri.parse(str).getPath()) != null) {
            if (path.endsWith("." + MimeTypeEnum.HTM.getSuffix())) {
                return true;
            }
            if (path.endsWith("." + MimeTypeEnum.HTML.getSuffix()) || TextUtils.isEmpty(path) || "/".equals(path)) {
                return true;
            }
        }
        return false;
    }

    public static String addParam(String str, String str2, String str3) {
        if (str == null || TextUtils.isEmpty(str2)) {
            return str;
        }
        Uri parse = Uri.parse(str);
        if (parse.getQueryParameter(str2) != null) {
            return str;
        }
        Uri.Builder buildUpon = parse.buildUpon();
        buildUpon.appendQueryParameter(str2, str3);
        return buildUpon.toString();
    }

    public static Map<String, String> getParamMap(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        int indexOf = str.indexOf("?");
        if (indexOf != -1) {
            String substring = str.substring(indexOf + 1);
            if (substring.contains("#")) {
                substring = substring.substring(0, substring.indexOf("#"));
            }
            for (String split : substring.split("&")) {
                String[] split2 = split.split(SymbolExpUtil.SYMBOL_EQUAL);
                if (split2.length < 2) {
                    hashMap.put(split2[0], "");
                } else {
                    hashMap.put(split2[0], split2[1]);
                }
            }
        }
        return hashMap;
    }

    public static String getParamVal(String str, String str2) {
        if (str == null || str2 == null) {
            return null;
        }
        return Uri.parse(str).getQueryParameter(str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0013, code lost:
        r0 = (r2 = android.net.Uri.parse(r2).getPath()).lastIndexOf(".");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getSuffix(java.lang.String r2) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r2)
            if (r0 == 0) goto L_0x0009
            java.lang.String r2 = ""
            return r2
        L_0x0009:
            android.net.Uri r2 = android.net.Uri.parse(r2)
            java.lang.String r2 = r2.getPath()
            if (r2 == 0) goto L_0x0023
            java.lang.String r0 = "."
            int r0 = r2.lastIndexOf(r0)
            r1 = -1
            if (r0 == r1) goto L_0x0023
            int r0 = r0 + 1
            java.lang.String r2 = r2.substring(r0)
            return r2
        L_0x0023:
            java.lang.String r2 = ""
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.WVUrlUtil.getSuffix(java.lang.String):java.lang.String");
    }

    public static String getMimeType(String str) {
        String str2 = mimeTypes.get(getSuffix(str));
        return str2 == null ? "" : str2;
    }

    public static String getMimeTypeExtra(String str) {
        if (str.contains("??")) {
            str = str.replaceFirst("\\?\\?", "");
        }
        return getMimeType(str);
    }

    public static boolean isCommonUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.toLowerCase().startsWith("http://") || str.toLowerCase().startsWith("https://")) {
            return true;
        }
        return false;
    }

    public static String force2HttpUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str.replaceAll("^((?i)https:)?//", "http://");
    }

    public static String removeQueryParam(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int qureyIndex = getQureyIndex(str);
        if (qureyIndex != -1) {
            return str.substring(0, qureyIndex);
        }
        return str.indexOf("#") > 0 ? str.substring(0, str.indexOf("#")) : str;
    }

    public static String rebuildWVurl(String str, String str2) {
        StringBuilder sb = new StringBuilder(str2);
        char lastChar = getLastChar(str2);
        if (!('?' == lastChar || '&' == lastChar)) {
            if (str2.contains("?")) {
                sb.append("&");
            } else {
                sb.append("?");
            }
        }
        if ('?' != getLastChar(str) && str.contains("?")) {
            String[] split = str.split("\\?");
            for (int i = 1; i < split.length; i++) {
                if (split[i] != null) {
                    sb.append(split[i]);
                }
                if (i != split.length - 1) {
                    sb.append("?");
                }
            }
        }
        return sb.toString();
    }

    private static char getLastChar(String str) {
        return str.charAt(str.length() - 1);
    }

    public static int getQureyIndex(String str) {
        int indexOf;
        int length = str.length();
        int i = 0;
        while (true) {
            indexOf = str.indexOf("?", i);
            if (indexOf == -1) {
                return -1;
            }
            int i2 = indexOf + 1;
            if (i2 >= length || str.charAt(i2) != '?') {
                return indexOf;
            }
            i = indexOf + 2;
        }
        return indexOf;
    }

    public static String[] parseCombo(String str) {
        String removeQueryParam = removeQueryParam(str);
        int indexOf = removeQueryParam.indexOf("??");
        if (-1 == indexOf) {
            return null;
        }
        return removeQueryParam.substring(indexOf + 2).split("\\,");
    }

    public static String removeScheme(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        String str2 = null;
        if (str.startsWith(CommonUtils.HTTP_PRE)) {
            str2 = str.replace(CommonUtils.HTTP_PRE, "");
        }
        if (str.startsWith(Utils.HTTPS_SCHEMA)) {
            str2 = str.replace(Utils.HTTPS_SCHEMA, "");
        }
        return TextUtils.isEmpty(str2) ? str : str2;
    }

    public static String removeHashCode(String str) {
        return (!TextUtils.isEmpty(str) && str.indexOf("#") != -1) ? str.substring(0, str.indexOf("#")) : str;
    }
}
