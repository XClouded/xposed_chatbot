package com.alibaba.aliweex.plugin;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.alibaba.aliweex.plugin.MtopHandler;
import com.alibaba.aliweex.utils.WXPrefetchConstant;
import com.alibaba.aliweex.utils.WXPrefetchUtil;
import com.alibaba.android.prefetchx.core.file.WXFilePrefetchModule;
import com.alibaba.fastjson.JSON;
import com.taobao.android.dinamic.DinamicConstant;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import com.taobao.weaver.prefetch.PrefetchDataCallback;
import com.taobao.weaver.prefetch.PrefetchDataResponse;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXThread;
import com.taobao.weex.utils.WXLogUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MtopPreloader {
    public static String GEO_LATITUDE = "$_geo_latitude_$";
    public static String GEO_LONGITUDE = "$_geo_longitude_$";
    public static String STATUS_GOT_RESPONSE = "got_response";
    public static String STATUS_GOT_RESPONSE_FAIL = "got_response_fail";
    public static String STATUS_INIT = "init";
    public static String STATUS_KEY = "mtop_prefetch_status";
    public static String STATUS_QUEUE = "resquesting";
    public static String STATUS_SAVED_TO_STORAGE = "saved_to_storage";
    public static String STATUS_SAVED_TO_STORAGE_FAIL = "saved_to_storage_fail";
    public static String TAG = "MtopPreloader";
    public static volatile String lastGeoLatitude = "";
    public static volatile String lastGeolongitude = "";
    /* access modifiers changed from: private */
    public static volatile long lastRefreshTimeOfGeoInfo;

    private MtopPreloader() {
    }

    public static boolean isLogin() {
        return RemoteLogin.isSessionValid();
    }

    public static String preload(@Nullable String str, WXSDKInstance wXSDKInstance) {
        return preload(str, wXSDKInstance, (PrefetchDataCallback) null);
    }

    public static String preload(@Nullable String str, WXSDKInstance wXSDKInstance, @Nullable PrefetchDataCallback prefetchDataCallback) {
        String str2;
        if (!WXPrefetchUtil.allowPreload()) {
            WXLogUtils.d(TAG, "preload is disabled");
            return str;
        } else if (TextUtils.isEmpty(str)) {
            return str;
        } else {
            List<String> allowWhiteUrlList = WXPrefetchUtil.getAllowWhiteUrlList();
            boolean z = false;
            if (allowWhiteUrlList != null && allowWhiteUrlList.size() > 0) {
                Iterator<String> it = allowWhiteUrlList.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (str.contains(it.next())) {
                            z = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (z) {
                if (str.contains("?")) {
                    str = str.replaceFirst("\\?", "?data_prefetch=true&");
                } else {
                    str = str + "?" + "data_prefetch" + "=true";
                }
            }
            Uri parse = Uri.parse(str);
            if (!parse.isHierarchical()) {
                return str;
            }
            String queryParameter = parse.getQueryParameter("wh_prefetch");
            String queryParameter2 = parse.getQueryParameter("wh_needlogin");
            String queryParameter3 = parse.getQueryParameter("wh_prefetch_id");
            String queryParameter4 = parse.getQueryParameter("data_prefetch");
            String queryParameter5 = parse.getQueryParameter(WXPrefetchConstant.KEY_MTOP_PREFETCH);
            String queryParameter6 = parse.getQueryParameter("mtop_prefetch_enable");
            String queryParameter7 = parse.getQueryParameter(WXPrefetchConstant.KEY_MTOP_PREFETCH_ID);
            String queryParameter8 = parse.getQueryParameter(WXPrefetchConstant.KEY_NEED_LOGIN);
            String queryParameter9 = parse.getQueryParameter(WXPrefetchConstant.KEY_SUPPORT_GEO_REPLACE);
            if ((!TextUtils.isEmpty(queryParameter2) && queryParameter2.equals("1")) || (!TextUtils.isEmpty(queryParameter8) && (queryParameter8.equals("1") || queryParameter8.equals("true")))) {
                if (isLogin()) {
                    str = str.replaceAll("wh_needlogin=1", "");
                } else {
                    WXPrefetchUtil.commitFail(WXPrefetchConstant.NEED_LOGIN_ERROR, "user not login exception");
                    return str;
                }
            }
            if (TextUtils.isEmpty(queryParameter4) && TextUtils.isEmpty(queryParameter) && TextUtils.isEmpty(queryParameter3) && TextUtils.isEmpty(queryParameter5) && TextUtils.isEmpty(queryParameter6) && TextUtils.isEmpty(queryParameter7)) {
                return str;
            }
            if ("true".equals(queryParameter9)) {
                refreshGeoInfo(wXSDKInstance.getContext());
            }
            if (str.contains(GEO_LONGITUDE) && !TextUtils.isEmpty(lastGeolongitude)) {
                str = str.replaceFirst(GEO_LONGITUDE, lastGeolongitude);
            }
            if (str.contains(GEO_LATITUDE) && !TextUtils.isEmpty(lastGeoLatitude)) {
                str = str.replaceFirst(GEO_LATITUDE, lastGeoLatitude);
            }
            Map<String, String> generatePrefetchString = generatePrefetchString(str);
            if (generatePrefetchString == null) {
                return str;
            }
            String str3 = generatePrefetchString.get(WXFilePrefetchModule.PREFETCH_MODULE_NAME);
            boolean equals = Boolean.TRUE.toString().equals(generatePrefetchString.get("keyIsMtopPrefetch"));
            WXPrefetchUtil.saveStatusToStorage(STATUS_INIT, str3);
            String mtopApiAndParams = WXPrefetchUtil.getMtopApiAndParams(wXSDKInstance, str3);
            if (mtopApiAndParams == null) {
                return str;
            }
            if (equals) {
                str2 = WXPrefetchUtil.replaceUrlParameter(str, WXPrefetchConstant.KEY_MTOP_PREFETCH, str3);
            } else {
                str2 = WXPrefetchUtil.replaceUrlParameter(str, "wh_prefetch", str3);
            }
            sendMtopRequestData(wXSDKInstance, mtopApiAndParams, str3, prefetchDataCallback);
            WXPrefetchUtil.saveStatusToStorage(STATUS_QUEUE, str3);
            return str2;
        }
    }

    public static Map<String, String> generatePrefetchString(String str) {
        Uri parse = Uri.parse(str);
        String queryParameter = parse.getQueryParameter("wh_prefetch");
        String queryParameter2 = parse.getQueryParameter("wh_prefetch_id");
        String queryParameter3 = parse.getQueryParameter("data_prefetch");
        String queryParameter4 = parse.getQueryParameter(WXPrefetchConstant.KEY_MTOP_PREFETCH);
        String queryParameter5 = parse.getQueryParameter("mtop_prefetch_enable");
        String queryParameter6 = parse.getQueryParameter(WXPrefetchConstant.KEY_MTOP_PREFETCH_ID);
        HashMap hashMap = new HashMap();
        String realMtopApi = (TextUtils.isEmpty(queryParameter3) || !queryParameter3.equals("true")) ? null : getRealMtopApi(str);
        if (TextUtils.isEmpty(queryParameter5) || !queryParameter5.equals("true")) {
            if (TextUtils.isEmpty(queryParameter)) {
                if (!TextUtils.isEmpty(queryParameter4)) {
                    hashMap.put("keyIsMtopPrefetch", "true");
                    queryParameter = replaceDynamicData(queryParameter4, WXPrefetchUtil.getParams(str));
                } else if (!TextUtils.isEmpty(queryParameter2)) {
                    String mtopApiFromZcache = WXPrefetchUtil.getMtopApiFromZcache(queryParameter2);
                    if (TextUtils.isEmpty(mtopApiFromZcache)) {
                        WXPrefetchUtil.commitFail(WXPrefetchConstant.ZIP_PACKAGE_CACHE, "package cache get error ");
                        return null;
                    }
                    queryParameter = replaceDynamicData(mtopApiFromZcache, WXPrefetchUtil.getParams(str));
                } else if (!TextUtils.isEmpty(queryParameter6)) {
                    String mtopApiFromZcache2 = WXPrefetchUtil.getMtopApiFromZcache(queryParameter6);
                    if (TextUtils.isEmpty(mtopApiFromZcache2)) {
                        WXPrefetchUtil.commitFail(WXPrefetchConstant.ZIP_PACKAGE_CACHE, "package cache get error by mtop_prefetch_id");
                        return null;
                    }
                    hashMap.put("keyIsMtopPrefetch", "true");
                    queryParameter = replaceDynamicData(mtopApiFromZcache2, WXPrefetchUtil.getParams(str));
                }
            }
            hashMap.put(WXFilePrefetchModule.PREFETCH_MODULE_NAME, queryParameter);
            return hashMap;
        }
        try {
            String realPrefetchIdUrl = WXPrefetchUtil.getRealPrefetchIdUrl(str);
            if (realPrefetchIdUrl.endsWith("\\")) {
                realPrefetchIdUrl = realPrefetchIdUrl.substring(0, realPrefetchIdUrl.length() - 1);
            }
            String mtopApiFromZcache3 = WXPrefetchUtil.getMtopApiFromZcache(realPrefetchIdUrl);
            if (TextUtils.isEmpty(mtopApiFromZcache3)) {
                WXPrefetchUtil.commitFail(WXPrefetchConstant.ZIP_PACKAGE_CACHE, "package cache get error by mtop_prefetch_enable at " + str);
                return null;
            }
            hashMap.put("keyIsMtopPrefetch", "true");
            queryParameter = replaceDynamicData(mtopApiFromZcache3, WXPrefetchUtil.getParams(str));
            hashMap.put(WXFilePrefetchModule.PREFETCH_MODULE_NAME, queryParameter);
            return hashMap;
        } catch (Exception e) {
            WXPrefetchUtil.commitFail(WXPrefetchConstant.JSON_PRASE_FAILED_ERROR, e.getMessage());
        }
        queryParameter = realMtopApi;
        hashMap.put(WXFilePrefetchModule.PREFETCH_MODULE_NAME, queryParameter);
        return hashMap;
    }

    public static void sendMtopRequestData(final WXSDKInstance wXSDKInstance, String str, final String str2, final PrefetchDataCallback prefetchDataCallback) {
        MtopHandler.send(str, new MtopHandler.MtopFinshCallback() {
            public void onSuccess(String str) {
                WXPrefetchUtil.saveStatusToStorage(MtopPreloader.STATUS_GOT_RESPONSE, str2);
                WXPrefetchUtil.handResultsSuccess(wXSDKInstance, str2, str);
                if (prefetchDataCallback != null) {
                    PrefetchDataResponse prefetchDataResponse = new PrefetchDataResponse();
                    prefetchDataResponse.data = JSON.parseObject(str);
                    prefetchDataCallback.onComplete(prefetchDataResponse);
                }
            }

            public void onError(String str) {
                WXPrefetchUtil.saveStatusToStorage(MtopPreloader.STATUS_GOT_RESPONSE_FAIL, str2);
                WXPrefetchUtil.handResultsFail(wXSDKInstance, str2, "-1", str);
                if (str != null) {
                    WXPrefetchUtil.commitFail(WXPrefetchConstant.MTOP_QUERY_ERROR, "received mtop failed. params is " + str2 + "error message is" + str);
                } else {
                    WXPrefetchUtil.commitFail(WXPrefetchConstant.MTOP_QUERY_ERROR, "system error");
                }
                String str2 = MtopPreloader.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("received mtop failed. params is ");
                sb.append(str2);
                sb.append(",error msg is ");
                sb.append(str != null ? str : "system error");
                WXLogUtils.d(str2, sb.toString());
                if (prefetchDataCallback != null) {
                    prefetchDataCallback.onError("500", str);
                }
            }
        });
    }

    public static String getRealMtopApi(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Uri parse = Uri.parse(str);
        if (!parse.isHierarchical()) {
            return null;
        }
        String queryParameter = parse.getQueryParameter("data_prefetch");
        if (!TextUtils.isEmpty(queryParameter) && queryParameter.equals("true")) {
            String realPrefetchIdUrl = WXPrefetchUtil.getRealPrefetchIdUrl(str);
            try {
                if (realPrefetchIdUrl.endsWith("\\")) {
                    realPrefetchIdUrl = realPrefetchIdUrl.substring(0, realPrefetchIdUrl.length() - 1);
                }
                return replaceDynamicData(WXPrefetchUtil.getMtopApiFromZcache(realPrefetchIdUrl), WXPrefetchUtil.getParams(str));
            } catch (Exception e) {
                WXPrefetchUtil.commitFail(WXPrefetchConstant.JSON_PRASE_FAILED_ERROR, e.getMessage());
            }
        }
        return "";
    }

    public static String replaceDynamicData(String str, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (map == null || map.size() == 0) {
            return str;
        }
        String matchReplace = matchReplace("(\\$).*?(\\$)", map, str, "\\$", 0);
        if (!TextUtils.isEmpty(matchReplace)) {
            matchReplace = matchReplace("(#).*?(#)", map, matchReplace, "#", 1);
        }
        return !TextUtils.isEmpty(matchReplace) ? matchReplace("(@).*?(@)", map, matchReplace, DinamicConstant.DINAMIC_PREFIX_AT, 2) : matchReplace;
    }

    private static String matchReplace(String str, Map<String, String> map, String str2, String str3, int i) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return "";
        }
        Matcher matcher = Pattern.compile(str).matcher(str2);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            String str4 = "";
            if (matcher.group() != null) {
                str4 = matcher.group().replaceAll(str3, "");
            }
            String str5 = map.get(Uri.decode(str4));
            if (i == 0) {
                str5 = Uri.decode(str5);
            } else if (i == 2) {
                str5 = Uri.encode(str5);
            }
            if (!TextUtils.isEmpty(str5)) {
                matcher.appendReplacement(stringBuffer, str5);
            } else {
                matcher.appendReplacement(stringBuffer, "");
            }
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    static void refreshGeoInfo(Context context) {
        if (SystemClock.uptimeMillis() - lastRefreshTimeOfGeoInfo >= 3600000 && ActivityCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            MtopPrefetchLocationListener mtopPrefetchLocationListener = new MtopPrefetchLocationListener(context, locationManager);
            if (locationManager.getAllProviders() != null && locationManager.getAllProviders().contains("network")) {
                locationManager.requestLocationUpdates("network", (long) 20000, (float) 5, mtopPrefetchLocationListener);
            }
            if (locationManager.getAllProviders() != null && locationManager.getAllProviders().contains("gps")) {
                locationManager.requestLocationUpdates("gps", (long) 20000, (float) 5, mtopPrefetchLocationListener);
            }
        }
    }

    static class MtopPrefetchLocationListener implements LocationListener, Handler.Callback {
        private static final int TIME_OUT_WHAT = 3235841;
        private Context mContext;
        /* access modifiers changed from: private */
        public Handler mHandler = new Handler(this);
        private LocationManager mLocationManager;

        public MtopPrefetchLocationListener(Context context, LocationManager locationManager) {
            this.mContext = context;
            this.mLocationManager = locationManager;
            this.mHandler.post(WXThread.secure((Runnable) new Runnable() {
                public void run() {
                    MtopPrefetchLocationListener.this.mHandler.sendEmptyMessageDelayed(MtopPrefetchLocationListener.TIME_OUT_WHAT, 10000);
                }
            }));
        }

        public void onLocationChanged(Location location) {
            this.mHandler.removeMessages(TIME_OUT_WHAT);
            if (location != null) {
                MtopPreloader.lastGeolongitude = String.valueOf(location.getLongitude());
                MtopPreloader.lastGeoLatitude = String.valueOf(location.getLatitude());
                long unused = MtopPreloader.lastRefreshTimeOfGeoInfo = SystemClock.uptimeMillis();
                this.mLocationManager.removeUpdates(this);
            }
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            String str2 = MtopPreloader.TAG;
            WXLogUtils.i(str2, "into--[onStatusChanged] provider111:" + str + " status:" + i);
        }

        public void onProviderEnabled(String str) {
            String str2 = MtopPreloader.TAG;
            WXLogUtils.i(str2, "into--[onProviderEnabled] provider111:" + str);
        }

        public void onProviderDisabled(String str) {
            String str2 = MtopPreloader.TAG;
            WXLogUtils.i(str2, "into--[onProviderDisabled] provider111:" + str);
            this.mLocationManager.removeUpdates(this);
        }

        public boolean handleMessage(Message message) {
            try {
                if (message.what == TIME_OUT_WHAT) {
                    WXLogUtils.d(MtopPreloader.TAG, "into--[handleMessage] Location Time Out!");
                    if (this.mContext != null) {
                        if (this.mLocationManager != null) {
                            this.mLocationManager.removeUpdates(this);
                            return true;
                        }
                    }
                    return false;
                }
            } catch (Throwable unused) {
            }
            return false;
        }
    }
}
