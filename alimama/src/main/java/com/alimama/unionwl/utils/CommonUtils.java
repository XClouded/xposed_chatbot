package com.alimama.unionwl.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CommonUtils {
    public static final String DOUBLE_SLASH = "//";
    public static final String HTTP_PRE = "http:";

    public static String join(Map<String, String> map, String str, String str2) {
        if (map == null) {
            return "";
        }
        ArrayList arrayList = new ArrayList();
        for (Map.Entry next : map.entrySet()) {
            arrayList.add(((String) next.getKey()) + str + ((String) next.getValue()));
        }
        return TextUtils.join(str2, arrayList);
    }

    public static String imageUrl(String str) {
        if (TextUtils.isEmpty(str) || !str.startsWith("//")) {
            return str;
        }
        return HTTP_PRE + str;
    }

    public static long getSafeLongValue(String str) {
        return getSafeLongValue(str, 0);
    }

    public static long getSafeLongValue(String str, long j) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return j;
        }
    }

    public static int getSafeIntValue(String str) {
        return getSafeIntValue(str, 0);
    }

    public static int getSafeIntValue(String str, int i) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            return i;
        }
    }

    public static double getSafeDoubleValue(String str) {
        return getSafeDoubleValue(str, 0.0d);
    }

    public static double getSafeDoubleValue(String str, double d) {
        try {
            return Double.valueOf(str).doubleValue();
        } catch (Exception unused) {
            return d;
        }
    }

    public static String getSimpleDoubleStr(String str) {
        int indexOf = str.indexOf(".");
        if (indexOf < 0) {
            return str;
        }
        String substring = str.substring(0, indexOf);
        String substring2 = str.substring(indexOf + 1);
        while (substring2.endsWith("0")) {
            substring2 = substring2.substring(0, substring2.length() - 1);
        }
        if (TextUtils.isEmpty(substring2)) {
            return substring;
        }
        return substring + "." + substring2;
    }

    public static long yuanText2fen(String str) {
        double safeDoubleValue = getSafeDoubleValue(str, -1.0d);
        if (safeDoubleValue >= 0.0d) {
            safeDoubleValue *= 100.0d;
        }
        return Math.round(safeDoubleValue);
    }

    public static String addSpm(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !Uri.parse(str).isHierarchical()) {
            return str;
        }
        Uri parse = Uri.parse(str);
        String query = parse.getQuery();
        String queryParameter = parse.getQueryParameter("spm");
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(query) ? "?" : "&");
        sb.append(str2);
        String sb2 = sb.toString();
        if (!TextUtils.isEmpty(queryParameter)) {
            sb2 = "";
        }
        return str + sb2;
    }

    public static String safeEncode(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str2;
        }
    }

    public static String safeDecode(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str2;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (!(connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null)) {
                for (NetworkInfo state : allNetworkInfo) {
                    if (state.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception unused) {
            return false;
        }
    }

    public static String readAssertStr(Context context, String str) {
        ByteArrayOutputStream readAssertByte = readAssertByte(context, str);
        return readAssertByte != null ? new String(readAssertByte.toByteArray()) : "";
    }

    public static ByteArrayOutputStream readAssertByte(Context context, String str) {
        try {
            if (str.startsWith(File.separator)) {
                str = str.substring(File.separator.length());
            }
            DataInputStream dataInputStream = new DataInputStream(context.getAssets().open(str));
            byte[] bArr = new byte[dataInputStream.available()];
            dataInputStream.readFully(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bArr);
            dataInputStream.close();
            return byteArrayOutputStream;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String compress(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(str.getBytes("UTF-8"));
            gZIPOutputStream.close();
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (IOException unused) {
            return "";
        }
    }

    public static String decompress(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            GZIPInputStream gZIPInputStream = new GZIPInputStream(new ByteArrayInputStream(Base64.decode(str, 0)));
            StringBuilder sb = new StringBuilder();
            while (true) {
                int read = gZIPInputStream.read();
                if (read != -1) {
                    sb.append(read);
                } else {
                    gZIPInputStream.close();
                    return sb.toString();
                }
            }
        } catch (IOException unused) {
            return "";
        }
    }
}
