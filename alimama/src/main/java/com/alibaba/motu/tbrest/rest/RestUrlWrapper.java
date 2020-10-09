package com.alibaba.motu.tbrest.rest;

import android.content.Context;
import com.alibaba.motu.tbrest.utils.MD5Utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

public class RestUrlWrapper {
    public static final String FIELD_APPKEY = "appkey";
    public static final String FIELD_APPVERSION = "app_version";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_PLATFORM = "platform";
    public static final String FIELD_SDK_VERSION = "sdk_version";
    public static final String FIELD_T = "t";
    public static final String FIELD_UTDID = "utdid";
    public static final String FIELD_V = "v";
    static boolean enableSecuritySDK = false;
    static Context mContext;

    public static void enableSecuritySDK() {
        enableSecuritySDK = true;
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    public static String getSignedTransferUrl(String str, Map<String, Object> map, Map<String, Object> map2, Context context, String str2, String str3, String str4, String str5, String str6, String str7) throws Exception {
        Map<String, Object> map3 = map2;
        String str8 = "";
        if (map3 != null && map2.size() > 0) {
            Set<String> keySet = map2.keySet();
            String[] strArr = new String[keySet.size()];
            keySet.toArray(strArr);
            for (String str9 : RestKeyArraySorter.getInstance().sortResourcesList(strArr, true)) {
                str8 = str8 + str9 + MD5Utils.getMd5Hex((byte[]) map3.get(str9));
            }
        }
        try {
            return _wrapUrl(str, (String) null, (String) null, str8, context, str2, str3, str4, str5, str6, str7);
        } catch (Exception unused) {
            return _wrapUrl(RestConstants.getTransferUrl(), (String) null, (String) null, str8, context, str2, str3, str4, str5, str6, str7);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x007a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String _wrapUrl(java.lang.String r4, java.lang.String r5, java.lang.String r6, java.lang.String r7, android.content.Context r8, java.lang.String r9, java.lang.String r10, java.lang.String r11, java.lang.String r12, java.lang.String r13, java.lang.String r14) throws java.lang.Exception {
        /*
            java.lang.String r8 = "4.1.0"
            java.lang.String r13 = "3.0"
            long r0 = java.lang.System.currentTimeMillis()
            java.lang.String r0 = java.lang.String.valueOf(r0)
            java.lang.String r1 = ""
            java.lang.String r2 = ""
            boolean r3 = enableSecuritySDK
            if (r3 == 0) goto L_0x0072
            android.content.Context r3 = mContext
            if (r3 == 0) goto L_0x0072
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x006c }
            r3.<init>()     // Catch:{ Exception -> 0x006c }
            r3.append(r9)     // Catch:{ Exception -> 0x006c }
            r3.append(r10)     // Catch:{ Exception -> 0x006c }
            r3.append(r11)     // Catch:{ Exception -> 0x006c }
            r3.append(r12)     // Catch:{ Exception -> 0x006c }
            r3.append(r8)     // Catch:{ Exception -> 0x006c }
            r3.append(r14)     // Catch:{ Exception -> 0x006c }
            r3.append(r0)     // Catch:{ Exception -> 0x006c }
            r3.append(r13)     // Catch:{ Exception -> 0x006c }
            r3.append(r1)     // Catch:{ Exception -> 0x006c }
            if (r6 != 0) goto L_0x003c
            java.lang.String r6 = ""
        L_0x003c:
            r3.append(r6)     // Catch:{ Exception -> 0x006c }
            if (r7 != 0) goto L_0x0043
            java.lang.String r7 = ""
        L_0x0043:
            r3.append(r7)     // Catch:{ Exception -> 0x006c }
            java.lang.String r6 = r3.toString()     // Catch:{ Exception -> 0x006c }
            byte[] r6 = r6.getBytes()     // Catch:{ Exception -> 0x006c }
            java.lang.String r6 = com.alibaba.motu.tbrest.utils.MD5Utils.getMd5Hex(r6)     // Catch:{ Exception -> 0x006c }
            com.alibaba.motu.tbrest.rest.RestSecuritySDKRequestAuthentication r7 = new com.alibaba.motu.tbrest.rest.RestSecuritySDKRequestAuthentication     // Catch:{ Exception -> 0x006c }
            android.content.Context r3 = mContext     // Catch:{ Exception -> 0x006c }
            r7.<init>(r3, r9)     // Catch:{ Exception -> 0x006c }
            java.lang.String r6 = r7.getSign(r6)     // Catch:{ Exception -> 0x006c }
            boolean r7 = com.alibaba.motu.tbrest.utils.StringUtils.isNotBlank(r8)     // Catch:{ Exception -> 0x0068 }
            if (r7 == 0) goto L_0x0066
            java.lang.String r7 = "1"
            r1 = r7
        L_0x0066:
            r2 = r6
            goto L_0x0072
        L_0x0068:
            r7 = move-exception
            r2 = r6
            r6 = r7
            goto L_0x006d
        L_0x006c:
            r6 = move-exception
        L_0x006d:
            java.lang.String r7 = "security sdk signed"
            com.alibaba.motu.tbrest.utils.LogUtil.w(r7, r6)
        L_0x0072:
            java.lang.String r6 = ""
            boolean r7 = com.alibaba.motu.tbrest.utils.StringUtils.isEmpty(r5)
            if (r7 != 0) goto L_0x008b
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            r6.append(r5)
            java.lang.String r5 = "&"
            r6.append(r5)
            java.lang.String r6 = r6.toString()
        L_0x008b:
            java.lang.String r5 = "%s?%sak=%s&av=%s&c=%s&v=%s&s=%s&d=%s&sv=%s&p=%s&t=%s&u=%s&is=%s"
            r7 = 13
            java.lang.Object[] r7 = new java.lang.Object[r7]
            r3 = 0
            r7[r3] = r4
            r4 = 1
            r7[r4] = r6
            r4 = 2
            java.lang.String r6 = _getEncoded(r9)
            r7[r4] = r6
            r4 = 3
            java.lang.String r6 = _getEncoded(r11)
            r7[r4] = r6
            r4 = 4
            java.lang.String r6 = _getEncoded(r10)
            r7[r4] = r6
            r4 = 5
            java.lang.String r6 = _getEncoded(r13)
            r7[r4] = r6
            r4 = 6
            java.lang.String r6 = _getEncoded(r2)
            r7[r4] = r6
            r4 = 7
            java.lang.String r6 = _getEncoded(r14)
            r7[r4] = r6
            r4 = 8
            r7[r4] = r8
            r4 = 9
            r7[r4] = r12
            r4 = 10
            r7[r4] = r0
            r4 = 11
            java.lang.String r6 = ""
            r7[r4] = r6
            r4 = 12
            r7[r4] = r1
            java.lang.String r4 = java.lang.String.format(r5, r7)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.rest.RestUrlWrapper._wrapUrl(java.lang.String, java.lang.String, java.lang.String, java.lang.String, android.content.Context, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    private static String _getEncoded(String str) {
        if (str == null) {
            return "";
        }
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }
}
