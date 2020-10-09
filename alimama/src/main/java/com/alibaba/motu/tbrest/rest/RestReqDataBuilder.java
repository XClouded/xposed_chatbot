package com.alibaba.motu.tbrest.rest;

import android.content.Context;
import android.os.Build;
import com.alibaba.analytics.core.Constants;
import com.alibaba.motu.tbrest.SendService;
import com.alibaba.motu.tbrest.utils.DeviceUtils;
import com.alibaba.motu.tbrest.utils.LogUtil;
import com.alibaba.motu.tbrest.utils.StringUtils;
import com.taobao.weex.common.Constants;
import com.taobao.weex.utils.tools.TimeCalculator;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class RestReqDataBuilder {
    private static long s_session_start_timestamp = System.currentTimeMillis();

    private static String _fixVariableValue(String str) {
        if (StringUtils.isBlank(str)) {
            return "-";
        }
        if (str == null || "".equals(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str.length());
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (!(charArray[i] == 10 || charArray[i] == 13 || charArray[i] == 9 || charArray[i] == '|')) {
                sb.append(charArray[i]);
            }
        }
        return sb.toString();
    }

    public static String buildRequestData(long j, String str, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return buildRequestData(SendService.getInstance().appKey, j, str, i, obj, obj2, obj3, map);
    }

    public static String buildRequestData(String str, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        long j2;
        String str3 = null;
        if (i == 0) {
            return null;
        }
        try {
            String utdid = DeviceUtils.getUtdid(SendService.getInstance().context);
            if (utdid == null) {
                LogUtil.e("get utdid failure, so build report failure, now return");
                return null;
            }
            String[] networkType = DeviceUtils.getNetworkType(SendService.getInstance().context);
            String str4 = networkType[0];
            if (networkType.length > 1 && str4 != null && !"Wi-Fi".equals(str4)) {
                str3 = networkType[1];
            }
            if (j > 0) {
                j2 = j;
            } else {
                j2 = System.currentTimeMillis();
            }
            String str5 = "" + j2;
            String _fixVariableValue = _fixVariableValue(str2);
            String _fixVariableValue2 = _fixVariableValue(String.valueOf(i));
            String _fixVariableValue3 = _fixVariableValue(StringUtils.convertObjectToString(obj));
            String _fixVariableValue4 = _fixVariableValue(StringUtils.convertObjectToString(obj2));
            String _fixVariableValue5 = _fixVariableValue(StringUtils.convertObjectToString(obj3));
            String _fixVariableValue6 = _fixVariableValue(StringUtils.convertMapToString(map));
            String _fixVariableValue7 = _fixVariableValue(DeviceUtils.getImei(SendService.getInstance().context));
            String _fixVariableValue8 = _fixVariableValue(DeviceUtils.getImsi(SendService.getInstance().context));
            String _fixVariableValue9 = _fixVariableValue(Build.BRAND);
            _fixVariableValue(DeviceUtils.getCpuName());
            _fixVariableValue(_fixVariableValue7);
            String _fixVariableValue10 = _fixVariableValue(Build.MODEL);
            String _fixVariableValue11 = _fixVariableValue(DeviceUtils.getResolution());
            String _fixVariableValue12 = _fixVariableValue(DeviceUtils.getCarrier(SendService.getInstance().context));
            String _fixVariableValue13 = _fixVariableValue(str4);
            String _fixVariableValue14 = _fixVariableValue(str3);
            String str6 = _fixVariableValue6;
            String _fixVariableValue15 = _fixVariableValue(str);
            String str7 = _fixVariableValue5;
            String _fixVariableValue16 = _fixVariableValue(SendService.getInstance().appVersion);
            String str8 = _fixVariableValue4;
            String _fixVariableValue17 = _fixVariableValue(SendService.getInstance().channel);
            String str9 = _fixVariableValue3;
            String _fixVariableValue18 = _fixVariableValue(SendService.getInstance().userNick);
            String str10 = _fixVariableValue2;
            String _fixVariableValue19 = _fixVariableValue(SendService.getInstance().userNick);
            String str11 = _fixVariableValue;
            String _fixVariableValue20 = _fixVariableValue(DeviceUtils.getCountry());
            String str12 = str5;
            String _fixVariableValue21 = _fixVariableValue(DeviceUtils.getLanguage());
            String str13 = SendService.getInstance().appId;
            String str14 = "a";
            String str15 = _fixVariableValue19;
            String _fixVariableValue22 = _fixVariableValue(Build.VERSION.RELEASE);
            Object obj4 = Constants.SDK_TYPE;
            Object obj5 = "1.0";
            StringBuilder sb = new StringBuilder();
            String str16 = _fixVariableValue18;
            sb.append("");
            String str17 = _fixVariableValue16;
            String str18 = _fixVariableValue15;
            sb.append(s_session_start_timestamp);
            sb.toString();
            String _fixVariableValue23 = _fixVariableValue(utdid);
            StringBuilder sb2 = new StringBuilder();
            Object obj6 = "-";
            sb2.append("_app_build_id=");
            sb2.append(SendService.getInstance().buildId);
            String _fixVariableValue24 = _fixVariableValue(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            Object obj7 = "-";
            sb3.append("country=");
            sb3.append(_fixVariableValue20);
            String _fixVariableValue25 = _fixVariableValue(_fixVariableValue(sb3.toString()) + "," + _fixVariableValue24);
            StringUtils.isBlank("");
            if (str13 != null && str13.contains("aliyunos")) {
                str14 = Constants.Name.Y;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(RestFieldsScheme.IMEI.toString(), _fixVariableValue7);
            hashMap.put(RestFieldsScheme.IMSI.toString(), _fixVariableValue8);
            hashMap.put(RestFieldsScheme.BRAND.toString(), _fixVariableValue9);
            hashMap.put(RestFieldsScheme.DEVICE_MODEL.toString(), _fixVariableValue10);
            hashMap.put(RestFieldsScheme.RESOLUTION.toString(), _fixVariableValue11);
            hashMap.put(RestFieldsScheme.CARRIER.toString(), _fixVariableValue12);
            hashMap.put(RestFieldsScheme.ACCESS.toString(), _fixVariableValue13);
            hashMap.put(RestFieldsScheme.ACCESS_SUBTYPE.toString(), _fixVariableValue14);
            hashMap.put(RestFieldsScheme.CHANNEL.toString(), _fixVariableValue17);
            hashMap.put(RestFieldsScheme.APPKEY.toString(), str18);
            hashMap.put(RestFieldsScheme.APPVERSION.toString(), str17);
            hashMap.put(RestFieldsScheme.LL_USERNICK.toString(), str16);
            hashMap.put(RestFieldsScheme.USERNICK.toString(), str15);
            hashMap.put(RestFieldsScheme.LL_USERID.toString(), "-");
            hashMap.put(RestFieldsScheme.USERID.toString(), "-");
            hashMap.put(RestFieldsScheme.LANGUAGE.toString(), _fixVariableValue21);
            hashMap.put(RestFieldsScheme.OS.toString(), str14);
            hashMap.put(RestFieldsScheme.OSVERSION.toString(), _fixVariableValue22);
            hashMap.put(RestFieldsScheme.SDKVERSION.toString(), obj5);
            hashMap.put(RestFieldsScheme.START_SESSION_TIMESTAMP.toString(), "" + s_session_start_timestamp);
            hashMap.put(RestFieldsScheme.UTDID.toString(), _fixVariableValue23);
            hashMap.put(RestFieldsScheme.SDKTYPE.toString(), obj4);
            hashMap.put(RestFieldsScheme.RESERVE2.toString(), _fixVariableValue23);
            hashMap.put(RestFieldsScheme.RESERVE3.toString(), "-");
            hashMap.put(RestFieldsScheme.RESERVE4.toString(), obj7);
            hashMap.put(RestFieldsScheme.RESERVE5.toString(), obj6);
            hashMap.put(RestFieldsScheme.RESERVES.toString(), _fixVariableValue25);
            hashMap.put(RestFieldsScheme.RECORD_TIMESTAMP.toString(), str12);
            hashMap.put(RestFieldsScheme.PAGE.toString(), str11);
            hashMap.put(RestFieldsScheme.EVENTID.toString(), str10);
            hashMap.put(RestFieldsScheme.ARG1.toString(), str9);
            hashMap.put(RestFieldsScheme.ARG2.toString(), str8);
            hashMap.put(RestFieldsScheme.ARG3.toString(), str7);
            hashMap.put(RestFieldsScheme.ARGS.toString(), str6);
            return assembleWithFullFields(hashMap);
        } catch (Exception e) {
            LogUtil.e("UTRestAPI buildTracePostReqDataObj catch!", e);
            return "";
        }
    }

    public static String assembleWithFullFields(Map<String, String> map) {
        boolean z;
        RestFieldsScheme restFieldsScheme;
        StringBuffer stringBuffer = new StringBuffer();
        RestFieldsScheme[] values = RestFieldsScheme.values();
        int length = values.length;
        int i = 0;
        while (true) {
            String str = null;
            if (i < length && (restFieldsScheme = values[i]) != RestFieldsScheme.ARGS) {
                if (map.containsKey(restFieldsScheme.toString())) {
                    str = StringUtils.convertObjectToString(map.get(restFieldsScheme.toString()));
                    map.remove(restFieldsScheme.toString());
                }
                stringBuffer.append(_fixVariableValue(str));
                stringBuffer.append("||");
                i++;
            }
        }
        if (map.containsKey(RestFieldsScheme.ARGS.toString())) {
            stringBuffer.append(_fixVariableValue(StringUtils.convertObjectToString(map.get(RestFieldsScheme.ARGS.toString()))));
            map.remove(RestFieldsScheme.ARGS.toString());
            z = false;
        } else {
            z = true;
        }
        for (String next : map.keySet()) {
            String convertObjectToString = map.containsKey(next) ? StringUtils.convertObjectToString(map.get(next)) : null;
            if (z) {
                if ("StackTrace".equals(next)) {
                    stringBuffer.append("StackTrace=====>");
                    stringBuffer.append(convertObjectToString);
                } else {
                    stringBuffer.append(_fixVariableValue(next));
                    stringBuffer.append(SymbolExpUtil.SYMBOL_EQUAL);
                    stringBuffer.append(convertObjectToString);
                }
                z = false;
            } else if ("StackTrace".equals(next)) {
                stringBuffer.append(",");
                stringBuffer.append("StackTrace=====>");
                stringBuffer.append(convertObjectToString);
            } else {
                stringBuffer.append(",");
                stringBuffer.append(_fixVariableValue(next));
                stringBuffer.append(SymbolExpUtil.SYMBOL_EQUAL);
                stringBuffer.append(convertObjectToString);
            }
        }
        String stringBuffer2 = stringBuffer.toString();
        if (StringUtils.isEmpty(stringBuffer2) || !stringBuffer2.endsWith("||")) {
            return stringBuffer2;
        }
        return stringBuffer2 + "-";
    }

    public static Map<String, Object> buildPostRequestMap(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("stm_x", str);
        return buildPostRequestMap((Map<String, String>) hashMap);
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0062 A[SYNTHETIC, Splitter:B:25:0x0062] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x0067 A[Catch:{ Exception -> 0x006c }] */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0017 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, java.lang.Object> buildPostRequestMap(java.util.Map<java.lang.String, java.lang.String> r8) {
        /*
            r0 = 0
            if (r8 == 0) goto L_0x0073
            int r1 = r8.size()
            if (r1 > 0) goto L_0x000a
            goto L_0x0073
        L_0x000a:
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Exception -> 0x006c }
            r1.<init>()     // Catch:{ Exception -> 0x006c }
            java.util.Set r2 = r8.keySet()     // Catch:{ Exception -> 0x006c }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ Exception -> 0x006c }
        L_0x0017:
            boolean r3 = r2.hasNext()     // Catch:{ Exception -> 0x006c }
            if (r3 == 0) goto L_0x006b
            java.lang.Object r3 = r2.next()     // Catch:{ Exception -> 0x006c }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x006c }
            java.lang.Object r4 = r8.get(r3)     // Catch:{ Exception -> 0x006c }
            java.lang.String r4 = (java.lang.String) r4     // Catch:{ Exception -> 0x006c }
            boolean r5 = com.alibaba.motu.tbrest.utils.StringUtils.isEmpty(r3)     // Catch:{ Exception -> 0x006c }
            if (r5 != 0) goto L_0x0017
            boolean r5 = com.alibaba.motu.tbrest.utils.StringUtils.isEmpty(r4)     // Catch:{ Exception -> 0x006c }
            if (r5 != 0) goto L_0x0017
            java.io.ByteArrayOutputStream r5 = new java.io.ByteArrayOutputStream     // Catch:{ IOException -> 0x005e }
            r5.<init>()     // Catch:{ IOException -> 0x005e }
            java.util.zip.GZIPOutputStream r6 = new java.util.zip.GZIPOutputStream     // Catch:{ IOException -> 0x005c }
            r6.<init>(r5)     // Catch:{ IOException -> 0x005c }
            java.lang.String r7 = "UTF-8"
            byte[] r4 = r4.getBytes(r7)     // Catch:{ IOException -> 0x005a }
            r6.write(r4)     // Catch:{ IOException -> 0x005a }
            r6.flush()     // Catch:{ IOException -> 0x005a }
            r6.close()     // Catch:{ IOException -> 0x005a }
            byte[] r4 = r5.toByteArray()     // Catch:{ IOException -> 0x005a }
            byte[] r4 = com.alibaba.motu.tbrest.utils.RC4.rc4(r4)     // Catch:{ IOException -> 0x005a }
            r1.put(r3, r4)     // Catch:{ IOException -> 0x005a }
            goto L_0x0017
        L_0x005a:
            goto L_0x0060
        L_0x005c:
            r6 = r0
            goto L_0x0060
        L_0x005e:
            r5 = r0
            r6 = r5
        L_0x0060:
            if (r6 == 0) goto L_0x0065
            r6.close()     // Catch:{ Exception -> 0x006c }
        L_0x0065:
            if (r5 == 0) goto L_0x0017
            r5.close()     // Catch:{ Exception -> 0x006c }
            goto L_0x0017
        L_0x006b:
            return r1
        L_0x006c:
            r8 = move-exception
            java.lang.String r1 = "buildPostRequestMap"
            com.alibaba.motu.tbrest.utils.LogUtil.e(r1, r8)
            return r0
        L_0x0073:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.motu.tbrest.rest.RestReqDataBuilder.buildPostRequestMap(java.util.Map):java.util.Map");
    }

    public static RestReqDataBuildResult buildMonkeyPostReqDataObj(String str, Context context, long j, String str2, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        return buildMonkeyPostReqDataObj(SendService.getInstance().appKey, str, context, j, str2, i, obj, obj2, obj3, map);
    }

    public static RestReqDataBuildResult buildMonkeyPostReqDataObj(String str, String str2, Context context, long j, String str3, int i, Object obj, Object obj2, Object obj3, Map<String, String> map) {
        long j2;
        String str4;
        String str5;
        if (i == 0) {
            return null;
        }
        try {
            String utdid = DeviceUtils.getUtdid(SendService.getInstance().context);
            if (utdid == null) {
                LogUtil.e("get utdid failure, so build report failure, now return");
                return null;
            }
            String[] networkType = DeviceUtils.getNetworkType(SendService.getInstance().context);
            String str6 = networkType[0];
            String str7 = (networkType.length <= 1 || str6 == null || "Wi-Fi".equals(str6)) ? null : networkType[1];
            if (j > 0) {
                j2 = j;
            } else {
                j2 = System.currentTimeMillis();
            }
            String str8 = "" + j2;
            String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(j2));
            String _fixVariableValue = _fixVariableValue(str3);
            String _fixVariableValue2 = _fixVariableValue(String.valueOf(i));
            String _fixVariableValue3 = _fixVariableValue(StringUtils.convertObjectToString(obj));
            String _fixVariableValue4 = _fixVariableValue(StringUtils.convertObjectToString(obj2));
            String _fixVariableValue5 = _fixVariableValue(StringUtils.convertObjectToString(obj3));
            String _fixVariableValue6 = _fixVariableValue(StringUtils.convertMapToString(map));
            String _fixVariableValue7 = _fixVariableValue(DeviceUtils.getImei(SendService.getInstance().context));
            String _fixVariableValue8 = _fixVariableValue(DeviceUtils.getImsi(SendService.getInstance().context));
            String _fixVariableValue9 = _fixVariableValue(Build.BRAND);
            String _fixVariableValue10 = _fixVariableValue(DeviceUtils.getCpuName());
            String str9 = _fixVariableValue6;
            String _fixVariableValue11 = _fixVariableValue(_fixVariableValue7);
            String str10 = _fixVariableValue5;
            String _fixVariableValue12 = _fixVariableValue(Build.MODEL);
            String str11 = _fixVariableValue4;
            String _fixVariableValue13 = _fixVariableValue(DeviceUtils.getResolution());
            String str12 = _fixVariableValue3;
            String _fixVariableValue14 = _fixVariableValue(DeviceUtils.getCarrier(SendService.getInstance().context));
            String _fixVariableValue15 = _fixVariableValue(str6);
            String _fixVariableValue16 = _fixVariableValue(str7);
            String str13 = _fixVariableValue2;
            String _fixVariableValue17 = _fixVariableValue(str);
            String str14 = _fixVariableValue;
            String _fixVariableValue18 = _fixVariableValue(SendService.getInstance().appVersion);
            String str15 = str8;
            String _fixVariableValue19 = _fixVariableValue(SendService.getInstance().channel);
            String str16 = format;
            String _fixVariableValue20 = _fixVariableValue(SendService.getInstance().userNick);
            String _fixVariableValue21 = _fixVariableValue(SendService.getInstance().userNick);
            String str17 = "-";
            String _fixVariableValue22 = _fixVariableValue(DeviceUtils.getCountry());
            String _fixVariableValue23 = _fixVariableValue(DeviceUtils.getLanguage());
            String str18 = SendService.getInstance().appId;
            if (str18 != null) {
                str4 = _fixVariableValue18;
                if (str18.contains("aliyunos")) {
                    str5 = "aliyunos";
                    String _fixVariableValue24 = _fixVariableValue(Build.VERSION.RELEASE);
                    String str19 = com.alibaba.analytics.core.Constants.SDK_TYPE;
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    String str20 = _fixVariableValue16;
                    sb.append(s_session_start_timestamp);
                    String sb2 = sb.toString();
                    String _fixVariableValue25 = _fixVariableValue(utdid);
                    StringUtils.isBlank("");
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append("5.0.1");
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue7);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue8);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue9);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue10);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue11);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue12);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue13);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue14);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue15);
                    stringBuffer.append("||");
                    stringBuffer.append(str20);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue19);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue17);
                    stringBuffer.append("||");
                    String str21 = str4;
                    stringBuffer.append(str21);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue20);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue21);
                    stringBuffer.append("||");
                    stringBuffer.append(str17);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue22);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue23);
                    stringBuffer.append("||");
                    String str22 = str5;
                    stringBuffer.append(str22);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue24);
                    stringBuffer.append("||");
                    stringBuffer.append(str19);
                    stringBuffer.append("||");
                    stringBuffer.append("1.0");
                    stringBuffer.append("||");
                    stringBuffer.append(sb2);
                    stringBuffer.append("||");
                    stringBuffer.append(_fixVariableValue25);
                    stringBuffer.append("||");
                    stringBuffer.append("-");
                    stringBuffer.append("||");
                    stringBuffer.append("-");
                    stringBuffer.append("||");
                    stringBuffer.append("-");
                    stringBuffer.append("||");
                    stringBuffer.append("-");
                    stringBuffer.append("||");
                    stringBuffer.append(str16);
                    stringBuffer.append("||");
                    stringBuffer.append(str15);
                    stringBuffer.append("||");
                    stringBuffer.append(str14);
                    stringBuffer.append("||");
                    stringBuffer.append(str13);
                    stringBuffer.append("||");
                    stringBuffer.append(str12);
                    stringBuffer.append("||");
                    stringBuffer.append(str11);
                    stringBuffer.append("||");
                    stringBuffer.append(str10);
                    stringBuffer.append("||");
                    stringBuffer.append(str9);
                    String stringBuffer2 = stringBuffer.toString();
                    HashMap hashMap = new HashMap();
                    hashMap.put("stm_x", stringBuffer2.getBytes());
                    RestReqDataBuildResult restReqDataBuildResult = new RestReqDataBuildResult();
                    restReqDataBuildResult.setReqUrl(RestUrlWrapper.getSignedTransferUrl(str2, (Map<String, Object>) null, hashMap, context, _fixVariableValue17, _fixVariableValue19, str21, str22, "", _fixVariableValue25));
                    restReqDataBuildResult.setPostReqData(hashMap);
                    return restReqDataBuildResult;
                }
            } else {
                str4 = _fixVariableValue18;
            }
            str5 = TimeCalculator.PLATFORM_ANDROID;
            String _fixVariableValue242 = _fixVariableValue(Build.VERSION.RELEASE);
            String str192 = com.alibaba.analytics.core.Constants.SDK_TYPE;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("");
            String str202 = _fixVariableValue16;
            sb3.append(s_session_start_timestamp);
            String sb22 = sb3.toString();
            String _fixVariableValue252 = _fixVariableValue(utdid);
            StringUtils.isBlank("");
            StringBuffer stringBuffer3 = new StringBuffer();
            stringBuffer3.append("5.0.1");
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue7);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue8);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue9);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue10);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue11);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue12);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue13);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue14);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue15);
            stringBuffer3.append("||");
            stringBuffer3.append(str202);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue19);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue17);
            stringBuffer3.append("||");
            String str212 = str4;
            stringBuffer3.append(str212);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue20);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue21);
            stringBuffer3.append("||");
            stringBuffer3.append(str17);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue22);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue23);
            stringBuffer3.append("||");
            String str222 = str5;
            stringBuffer3.append(str222);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue242);
            stringBuffer3.append("||");
            stringBuffer3.append(str192);
            stringBuffer3.append("||");
            stringBuffer3.append("1.0");
            stringBuffer3.append("||");
            stringBuffer3.append(sb22);
            stringBuffer3.append("||");
            stringBuffer3.append(_fixVariableValue252);
            stringBuffer3.append("||");
            stringBuffer3.append("-");
            stringBuffer3.append("||");
            stringBuffer3.append("-");
            stringBuffer3.append("||");
            stringBuffer3.append("-");
            stringBuffer3.append("||");
            stringBuffer3.append("-");
            stringBuffer3.append("||");
            stringBuffer3.append(str16);
            stringBuffer3.append("||");
            stringBuffer3.append(str15);
            stringBuffer3.append("||");
            stringBuffer3.append(str14);
            stringBuffer3.append("||");
            stringBuffer3.append(str13);
            stringBuffer3.append("||");
            stringBuffer3.append(str12);
            stringBuffer3.append("||");
            stringBuffer3.append(str11);
            stringBuffer3.append("||");
            stringBuffer3.append(str10);
            stringBuffer3.append("||");
            stringBuffer3.append(str9);
            String stringBuffer22 = stringBuffer3.toString();
            HashMap hashMap2 = new HashMap();
            hashMap2.put("stm_x", stringBuffer22.getBytes());
            RestReqDataBuildResult restReqDataBuildResult2 = new RestReqDataBuildResult();
            restReqDataBuildResult2.setReqUrl(RestUrlWrapper.getSignedTransferUrl(str2, (Map<String, Object>) null, hashMap2, context, _fixVariableValue17, _fixVariableValue19, str212, str222, "", _fixVariableValue252));
            restReqDataBuildResult2.setPostReqData(hashMap2);
            return restReqDataBuildResult2;
        } catch (Exception e) {
            LogUtil.e("UTRestAPI buildTracePostReqDataObj catch!", e);
            return null;
        }
    }
}
