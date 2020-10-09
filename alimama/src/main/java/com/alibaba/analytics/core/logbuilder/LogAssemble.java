package com.alibaba.analytics.core.logbuilder;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import com.alibaba.analytics.AnalyticsImp;
import com.alibaba.analytics.core.Constants;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTSampleConfBiz;
import com.alibaba.analytics.core.device.Device;
import com.alibaba.analytics.core.device.DeviceInfo;
import com.alibaba.analytics.core.logbuilder.GoogleAdvertisingIdClient;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.model.LogConstant;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.model.UTMCLogFields;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.BuildCompatUtils;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MapUtils;
import com.alibaba.analytics.utils.PhoneInfoUtils;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.utils.UTMCDevice;
import com.alibaba.analytics.version.UTBuildInfo;
import com.taobao.accs.utl.UTMini;
import com.taobao.weex.common.Constants;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;

public class LogAssemble {
    private static final int LOG_MAX_LENGHTH = 40960;
    private static final int LOG_MAX_LENGHTH_APPMONITOR = 409600;
    private static final int LOG_TRUNC_SEND_LENGHTH = 1024;
    private static volatile String s_bssid;
    private static volatile String s_mac_str;

    private static String checkField(String str) {
        return StringUtils.isEmpty(str) ? "-" : str;
    }

    private static void copyIfLogMapDoesNotExist(String str, Map<String, String> map, Map<String, String> map2) {
        if (!map.containsKey(str) && map2.get(str) != null) {
            map.put(str, map2.get(str));
        }
    }

    private static void mergeLogMapAndDeviceInfoMap(Map<String, String> map, Map<String, String> map2) {
        map.put(LogField.BRAND.toString(), map2.get(LogField.BRAND.toString()));
        map.put(LogField.DEVICE_MODEL.toString(), map2.get(LogField.DEVICE_MODEL.toString()));
        map.put(LogField.RESOLUTION.toString(), map2.get(LogField.RESOLUTION.toString()));
        map.put(LogField.OS.toString(), map2.get(LogField.OS.toString()));
        map.put(LogField.OSVERSION.toString(), map2.get(LogField.OSVERSION.toString()));
        map.put(LogField.UTDID.toString(), map2.get(LogField.UTDID.toString()));
        copyIfLogMapDoesNotExist(LogField.IMEI.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.IMSI.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.APPVERSION.toString(), map, map2);
        copyIfLogMapDoesNotExist(UTMCLogFields.DEVICE_ID.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.LANGUAGE.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.ACCESS.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.ACCESS_SUBTYPE.toString(), map, map2);
        copyIfLogMapDoesNotExist(LogField.CARRIER.toString(), map, map2);
    }

    public static String assemble(Map<String, String> map) {
        String str;
        String str2;
        StringBuilder sb;
        if (map != null && map.size() > 0) {
            Context context = Variables.getInstance().getContext();
            if (context == null && AnalyticsImp.getApplication() != null) {
                context = AnalyticsImp.getApplication().getApplicationContext();
            }
            DeviceInfo device = Device.getDevice(context);
            if (device != null) {
                str = device.getImei();
                str2 = device.getImsi();
            } else {
                str2 = null;
                str = null;
            }
            if (str != null && str2 != null && map.get(LogField.IMEI.toString()) == null && map.get(LogField.IMSI.toString()) == null) {
                map.put(LogField.IMEI.toString(), str);
                map.put(LogField.IMSI.toString(), str2);
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getUsernick())) {
                map.put(LogField.USERNICK.toString(), Variables.getInstance().getUsernick());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getLongLoginUsernick())) {
                map.put(LogField.LL_USERNICK.toString(), Variables.getInstance().getLongLoginUsernick());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getUserid())) {
                map.put(LogField.USERID.toString(), Variables.getInstance().getUserid());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getLongLoingUserid())) {
                map.put(LogField.LL_USERID.toString(), Variables.getInstance().getLongLoingUserid());
            }
            if (!map.containsKey(LogField.SDKVERSION.toString())) {
                map.put(LogField.SDKVERSION.toString(), UTBuildInfo.getInstance().getFullSDKVersion());
            }
            if (!map.containsKey(LogField.APPKEY.toString())) {
                map.put(LogField.APPKEY.toString(), Variables.getInstance().getAppkey());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getChannel())) {
                map.put(LogField.CHANNEL.toString(), Variables.getInstance().getChannel());
            }
            if (!StringUtils.isEmpty(Variables.getInstance().getAppVersion())) {
                map.put(LogField.APPVERSION.toString(), Variables.getInstance().getAppVersion());
            }
            if (map.containsKey(LogField.RECORD_TIMESTAMP.toString())) {
                map.put(LogField.RECORD_TIMESTAMP.toString(), "" + TimeStampAdjustMgr.getInstance().getCurrentMils(map.get(LogField.RECORD_TIMESTAMP.toString())));
            } else {
                map.put(LogField.RECORD_TIMESTAMP.toString(), "" + TimeStampAdjustMgr.getInstance().getCurrentMils());
            }
            if (!map.containsKey(LogField.START_SESSION_TIMESTAMP.toString())) {
                map.put(LogField.START_SESSION_TIMESTAMP.toString(), "" + SessionTimeAndIndexMgr.getInstance().getSessionTimestamp());
            }
            if (!map.containsKey(LogField.SDKTYPE.toString())) {
                map.put(LogField.SDKTYPE.toString(), Constants.getSdkType());
            }
            map.put(LogField.RESERVE5.toString(), Reserve5Helper.getReserve(context));
            Map<String, String> deviceInfo = UTMCDevice.getDeviceInfo(context);
            if (deviceInfo != null) {
                mergeLogMapAndDeviceInfoMap(map, deviceInfo);
                if (map.containsKey(UTMCLogFields.ALIYUN_PLATFORM_FLAG.toString())) {
                    map.put(LogField.OS.toString(), Constants.Name.Y);
                }
                String str3 = map.get(LogField.RESERVES.toString());
                if (!StringUtils.isEmpty(str3)) {
                    sb = new StringBuilder(str3);
                } else {
                    sb = new StringBuilder(100);
                }
                if (!BuildCompatUtils.isAtLeastQ()) {
                    if (StringUtils.isEmpty(s_mac_str) && context != null) {
                        try {
                            if (context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE") == 0) {
                                s_mac_str = NetworkUtil.getWifiAddress(context);
                            }
                        } catch (Exception unused) {
                        }
                    }
                    if (s_mac_str != null) {
                        if (sb.length() > 0) {
                            sb.append(",_mac=");
                            sb.append(s_mac_str);
                        } else {
                            sb.append("_mac=");
                            sb.append(s_mac_str);
                        }
                        map.remove("_mac");
                    }
                    if (s_bssid == null) {
                        String bssID = getBssID(context);
                        if (bssID == null) {
                            s_bssid = "";
                        } else {
                            s_bssid = bssID;
                        }
                    }
                    if (!StringUtils.isEmpty(s_bssid)) {
                        if (sb.length() > 0) {
                            sb.append(",_bssid=");
                            sb.append(s_bssid);
                        } else {
                            sb.append("_bssid=");
                            sb.append(s_bssid);
                        }
                    }
                    String imeiBySystem = PhoneInfoUtils.getImeiBySystem(context);
                    if (!TextUtils.isEmpty(imeiBySystem)) {
                        if (sb.length() > 0) {
                            sb.append(",_ie=");
                            sb.append(imeiBySystem);
                        } else {
                            sb.append("_ie=");
                            sb.append(imeiBySystem);
                        }
                    }
                    String imsiBySystem = PhoneInfoUtils.getImsiBySystem(context);
                    if (!TextUtils.isEmpty(imsiBySystem)) {
                        if (sb.length() > 0) {
                            sb.append(",_is=");
                            sb.append(imsiBySystem);
                        } else {
                            sb.append("_is=");
                            sb.append(imsiBySystem);
                        }
                    }
                }
                if (NetworkUtil.isDevice5GSupported(context)) {
                    if (sb.length() > 0) {
                        sb.append(",_spt5g=1");
                    } else {
                        sb.append("_spt5g=1");
                    }
                } else if (sb.length() > 0) {
                    sb.append(",_spt5g=0");
                } else {
                    sb.append("_spt5g=0");
                }
                GoogleAdvertisingIdClient.AdInfo adInfo = GoogleAdvertisingIdClient.getAdInfo();
                if (adInfo != null) {
                    if (!adInfo.isLimitAdTrackingEnabled()) {
                        if (sb.length() > 0) {
                            sb.append(",_glat=0");
                        } else {
                            sb.append("_glat=0");
                        }
                        String id = adInfo.getId();
                        if (!TextUtils.isEmpty(id)) {
                            if (sb.length() > 0) {
                                sb.append(",gps_adid=");
                                sb.append(id);
                            } else {
                                sb.append("gps_adid=");
                                sb.append(id);
                            }
                        }
                    } else if (sb.length() > 0) {
                        sb.append(",_glat=1");
                    } else {
                        sb.append("_glat=1");
                    }
                }
                String str4 = map.get(UTMCLogFields.DEVICE_ID.toString());
                if (str4 != null) {
                    if (sb.length() > 0) {
                        sb.append(",_did=");
                        sb.append(str4);
                    } else {
                        sb.append("_did=");
                        sb.append(str4);
                    }
                    map.remove(UTMCLogFields.DEVICE_ID.toString());
                }
                String securityToken = LogAssembleHelper.getSecurityToken(context);
                if (securityToken != null) {
                    if (map.containsKey(LogField.UTDID.toString()) && securityToken.equals(map.get(LogField.UTDID.toString()))) {
                        securityToken = "utdid";
                    }
                    if (sb.length() > 0) {
                        sb.append(",_umid=");
                        sb.append(securityToken);
                    } else {
                        sb.append("_umid=");
                        sb.append(securityToken);
                    }
                }
                if (Variables.getInstance().isDebugPackage()) {
                    String packageBuildId = Variables.getInstance().getPackageBuildId();
                    if (!TextUtils.isEmpty(packageBuildId)) {
                        if (sb.length() > 0) {
                            sb.append(",_buildid=");
                            sb.append(packageBuildId);
                        } else {
                            sb.append("_buildid=");
                            sb.append(packageBuildId);
                        }
                    }
                }
                if (sb.length() > 0) {
                    sb.append(",_timeAdjust=");
                    sb.append(TimeStampAdjustMgr.getInstance().getAdjustFlag() ? "1" : "0");
                } else {
                    sb.append("_timeAdjust=");
                    sb.append(TimeStampAdjustMgr.getInstance().getAdjustFlag() ? "1" : "0");
                }
                String str5 = map.get(LogField.APPKEY.toString());
                String appkey = Variables.getInstance().getAppkey();
                if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(appkey) && !appkey.equalsIgnoreCase(str5)) {
                    if (sb.length() > 0) {
                        sb.append(",_mak=");
                        sb.append(appkey);
                    } else {
                        sb.append("_mak=");
                        sb.append(appkey);
                    }
                }
                String str6 = UTMCDevice.isPad(Variables.getInstance().getContext()) ? "1" : "0";
                if (sb.length() > 0) {
                    sb.append(",_pad=");
                    sb.append(str6);
                } else {
                    sb.append("_pad=");
                    sb.append(str6);
                }
                String channle2ForPreLoadApk = AppInfoUtil.getChannle2ForPreLoadApk(context);
                if (!TextUtils.isEmpty(channle2ForPreLoadApk)) {
                    if (sb.length() > 0) {
                        sb.append(",_channel2=");
                        sb.append(channle2ForPreLoadApk);
                    } else {
                        sb.append("_channel2=");
                        sb.append(channle2ForPreLoadApk);
                    }
                }
                String openid = Variables.getInstance().getOpenid();
                if (!StringUtils.isEmpty(openid)) {
                    if (sb.length() > 0) {
                        sb.append(",_openid=");
                        sb.append(openid);
                    } else {
                        sb.append("_openid=");
                        sb.append(openid);
                    }
                }
                String str7 = map.get(LogConstant.UTPVID_T);
                if (!TextUtils.isEmpty(str7)) {
                    if (sb.length() > 0) {
                        sb.append(",_t=");
                        sb.append(str7);
                    } else {
                        sb.append("_t=");
                        sb.append(str7);
                    }
                    map.remove(LogConstant.UTPVID_T);
                }
                Map<String, String> sessionProperties = Variables.getInstance().getSessionProperties();
                if (sessionProperties != null && sessionProperties.size() > 0) {
                    String convertMapToStringWithUrlEncoder = StringUtils.convertMapToStringWithUrlEncoder(sessionProperties);
                    if (!StringUtils.isEmpty(convertMapToStringWithUrlEncoder)) {
                        if (sb.length() > 0) {
                            sb.append(",");
                            sb.append(convertMapToStringWithUrlEncoder);
                        } else {
                            sb.append(convertMapToStringWithUrlEncoder);
                        }
                    }
                }
                map.put(LogField.RESERVES.toString(), sb.toString());
                return assembleWithFullFields(map);
            }
        }
        return null;
    }

    public static String assembleWithFullFields(Map<String, String> map) {
        boolean z;
        String str;
        LogField logField;
        Map<String, String> checkMapFields = FieldCheck.checkMapFields(map);
        boolean z2 = truncLog(checkMapFields, LogField.ARG3.toString()) || (truncLog(checkMapFields, LogField.ARG2.toString()) || truncLog(checkMapFields, LogField.ARG1.toString()));
        StringBuffer stringBuffer = new StringBuffer();
        LogField[] values = LogField.values();
        int length = values.length;
        int i = 0;
        while (true) {
            String str2 = null;
            if (i >= length || (logField = values[i]) == LogField.ARGS) {
                StringBuffer stringBuffer2 = new StringBuffer();
            } else {
                if (checkMapFields.containsKey(logField.toString())) {
                    str2 = StringUtils.convertObjectToString(checkMapFields.get(logField.toString()));
                    checkMapFields.remove(logField.toString());
                }
                stringBuffer.append(checkField(str2));
                stringBuffer.append("||");
                i++;
            }
        }
        StringBuffer stringBuffer22 = new StringBuffer();
        if (checkMapFields.containsKey(LogField.ARGS.toString())) {
            stringBuffer22.append(checkField(StringUtils.convertObjectToString(checkMapFields.get(LogField.ARGS.toString()))));
            checkMapFields.remove(LogField.ARGS.toString());
            z = false;
        } else {
            z = true;
        }
        for (String next : checkMapFields.keySet()) {
            String convertObjectToString = checkMapFields.containsKey(next) ? StringUtils.convertObjectToString(checkMapFields.get(next)) : null;
            if (z) {
                if ("StackTrace".equals(next)) {
                    stringBuffer22.append("StackTrace=====>");
                    stringBuffer22.append(convertObjectToString);
                } else {
                    stringBuffer22.append(checkField(next));
                    stringBuffer22.append(SymbolExpUtil.SYMBOL_EQUAL);
                    stringBuffer22.append(convertObjectToString);
                }
                z = false;
            } else if ("StackTrace".equals(next)) {
                stringBuffer22.append(",");
                stringBuffer22.append("StackTrace=====>");
                stringBuffer22.append(convertObjectToString);
            } else {
                stringBuffer22.append(",");
                stringBuffer22.append(checkField(next));
                stringBuffer22.append(SymbolExpUtil.SYMBOL_EQUAL);
                stringBuffer22.append(convertObjectToString);
            }
        }
        int length2 = stringBuffer22.length();
        if (length2 < 1) {
            str = "-";
        } else if (length2 > LOG_MAX_LENGHTH) {
            if (!isAppMonitor(map.get(LogField.EVENTID.toString()))) {
                Logger.e("LogAssemble", "truncLog field", LogField.ARGS.toString(), "length", Integer.valueOf(length2));
                str = stringBuffer22.substring(0, LOG_MAX_LENGHTH);
            } else if (length2 > LOG_MAX_LENGHTH_APPMONITOR) {
                Logger.e("LogAssemble", "truncLog field", LogField.ARGS.toString(), "length", Integer.valueOf(length2));
                str = stringBuffer22.substring(0, LOG_MAX_LENGHTH_APPMONITOR);
            } else {
                str = stringBuffer22.toString();
            }
            z2 = true;
        } else {
            str = stringBuffer22.toString();
        }
        stringBuffer.append(str);
        String stringBuffer3 = stringBuffer.toString();
        if (z2) {
            sendTruncLogEvent(stringBuffer3);
        }
        return stringBuffer3;
    }

    private static boolean isAppMonitor(String str) {
        return "65501".equalsIgnoreCase(str) || "65502".equalsIgnoreCase(str) || "65503".equalsIgnoreCase(str);
    }

    public static Map<String, String> disassemble(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        HashMap hashMap = new HashMap();
        String[] lSplitResult = getLSplitResult(str, 34);
        if (lSplitResult != null && lSplitResult.length > 0) {
            int i = 0;
            for (LogField logField : LogField.values()) {
                if (i < lSplitResult.length && lSplitResult[i] != null) {
                    hashMap.put(logField.toString(), lSplitResult[i]);
                }
                i++;
            }
        }
        return hashMap;
    }

    private static String[] getLSplitResult(String str, int i) {
        String[] strArr = new String[i];
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= strArr.length - 1) {
                break;
            }
            int indexOf = str.indexOf("||", i3);
            if (indexOf == -1) {
                strArr[i2] = str.substring(i3);
                break;
            }
            strArr[i2] = str.substring(i3, indexOf);
            i3 = indexOf + 2;
            i2++;
        }
        strArr[i - 1] = str.substring(i3);
        return strArr;
    }

    private static String getBssID(Context context) {
        try {
            return ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getBSSID();
        } catch (Throwable unused) {
            return null;
        }
    }

    public static int getEventId(Map<String, String> map) {
        try {
            return Integer.parseInt(map.get(LogField.EVENTID.toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String assemble(String str, String str2, String str3, String str4, String str5, Map<String, String> map, String str6, String str7) {
        HashMap hashMap = new HashMap();
        if (map != null) {
            hashMap.putAll(map);
        }
        if (!TextUtils.isEmpty(str)) {
            hashMap.put(LogField.PAGE.toString(), str);
        }
        hashMap.put(LogField.EVENTID.toString(), str2);
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put(LogField.ARG1.toString(), str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            hashMap.put(LogField.ARG2.toString(), str4);
        }
        if (!TextUtils.isEmpty(str5)) {
            hashMap.put(LogField.ARG3.toString(), str5);
        }
        if (!TextUtils.isEmpty(str7)) {
            hashMap.put(LogField.RECORD_TIMESTAMP.toString(), str7);
        }
        if (!TextUtils.isEmpty(str6)) {
            hashMap.put(LogField.RESERVE3.toString(), str6);
        }
        return assemble(hashMap);
    }

    private static boolean truncLog(Map<String, String> map, String str) {
        String str2 = map.get(str);
        if (TextUtils.isEmpty(str2) || str2.length() <= LOG_MAX_LENGHTH) {
            return false;
        }
        Logger.e("LogAssemble truncLog", "field", str, "length", Integer.valueOf(str2.length()));
        map.put(str, str2.substring(0, LOG_MAX_LENGHTH));
        return true;
    }

    private static void sendTruncLogEvent(String str) {
        if (!UTSampleConfBiz.getInstance().isSampleSuccess(UTMini.EVENTID_AGOO, "TRUNC_LOG")) {
            Logger.d("sendTruncLogEvent", "TRUNC_LOG is discarded!");
            return;
        }
        Map<String, String> disassemble = disassemble(str);
        if (disassemble != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("PN", disassemble.get(LogField.PAGE.toString()));
            hashMap.put("EID", disassemble.get(LogField.EVENTID.toString()));
            hashMap.put("A1", getSubString(disassemble.get(LogField.ARG1.toString())));
            hashMap.put("A2", getSubString(disassemble.get(LogField.ARG2.toString())));
            hashMap.put("A3", getSubString(disassemble.get(LogField.ARG3.toString())));
            hashMap.put("AS", getSubString(disassemble.get(LogField.ARGS.toString())));
            hashMap.put("R3", disassemble.get(LogField.RESERVE3.toString()));
            LogStoreMgr.getInstance().add(new Log("UT_ANALYTICS", "19999", "TRUNC_LOG", "", "", MapUtils.convertToUrlEncodedMap(hashMap)));
        }
    }

    private static String getSubString(String str) {
        return (TextUtils.isEmpty(str) || str.length() <= 1024) ? str : str.substring(0, 1024);
    }
}
