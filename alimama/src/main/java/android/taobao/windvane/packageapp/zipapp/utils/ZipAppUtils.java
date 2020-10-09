package android.taobao.windvane.packageapp.zipapp.utils;

import android.content.Context;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.taobao.windvane.packageapp.WVPackageAppPrefixesConfig;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.ZipAppFileManager;
import android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.data.AppResConfig;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.webview.WVWrapWebResourceResponse;
import android.text.TextUtils;
import android.webkit.WebResourceResponse;

import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheManager;
import com.taobao.zcache.model.ZCacheResourceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ZipAppUtils {
    private static final String SPNAME = "WVpackageApp";
    private static final String TAG = "ZipAppUtils";
    public static String ZIP_APP_PATH = "app";

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00d3  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00ec A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String parseUrlSuffix(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r7, java.lang.String r8) {
        /*
            r0 = 0
            if (r8 == 0) goto L_0x00ed
            if (r7 != 0) goto L_0x0007
            goto L_0x00ed
        L_0x0007:
            java.lang.String r1 = "http:"
            java.lang.String r2 = ""
            java.lang.String r1 = r8.replaceFirst(r1, r2)
            java.lang.String r2 = "https:"
            java.lang.String r3 = ""
            java.lang.String r1 = r1.replaceFirst(r2, r3)
            java.lang.String r2 = r7.mappingUrl
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 == 0) goto L_0x006a
            android.taobao.windvane.monitor.WVPackageMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r1 == 0) goto L_0x0069
            long r1 = r7.installedSeq
            r3 = 0
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r5 == 0) goto L_0x0069
            android.taobao.windvane.monitor.WVPackageMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r7.name
            r2.append(r3)
            java.lang.String r3 = "-0"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            long r4 = r7.s
            r3.append(r4)
            java.lang.String r4 = "/"
            r3.append(r4)
            long r4 = r7.installedSeq
            r3.append(r4)
            java.lang.String r7 = ":"
            r3.append(r7)
            r3.append(r8)
            java.lang.String r7 = r3.toString()
            java.lang.String r8 = "13"
            r1.commitPackageVisitError(r2, r7, r8)
        L_0x0069:
            return r0
        L_0x006a:
            java.util.ArrayList<java.lang.String> r2 = r7.folders
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L_0x00a5
            java.util.ArrayList<java.lang.String> r2 = r7.folders
            int r2 = r2.size()
            if (r2 != 0) goto L_0x0079
            goto L_0x00a5
        L_0x0079:
            r2 = 0
        L_0x007a:
            java.util.ArrayList<java.lang.String> r5 = r7.folders
            int r5 = r5.size()
            if (r2 >= r5) goto L_0x00d0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r6 = r7.mappingUrl
            r5.append(r6)
            java.util.ArrayList<java.lang.String> r6 = r7.folders
            java.lang.Object r6 = r6.get(r2)
            java.lang.String r6 = (java.lang.String) r6
            r5.append(r6)
            java.lang.String r5 = r5.toString()
            boolean r5 = r1.startsWith(r5)
            if (r5 == 0) goto L_0x00a2
            goto L_0x00d1
        L_0x00a2:
            int r2 = r2 + 1
            goto L_0x007a
        L_0x00a5:
            java.lang.String r2 = r7.mappingUrl
            boolean r1 = r1.startsWith(r2)
            if (r1 == 0) goto L_0x00ae
            goto L_0x00d1
        L_0x00ae:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r1 == 0) goto L_0x00d0
            android.taobao.windvane.monitor.WVPackageMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = r7.name
            r2.append(r4)
            java.lang.String r4 = "-0"
            r2.append(r4)
            java.lang.String r2 = r2.toString()
            java.lang.String r4 = "14"
            r1.commitPackageVisitError(r2, r8, r4)
        L_0x00d0:
            r4 = 0
        L_0x00d1:
            if (r4 == 0) goto L_0x00ec
            android.net.Uri r0 = android.net.Uri.parse(r8)
            r0.getPath()
            java.lang.String r0 = r7.mappingUrl
            int r0 = r8.indexOf(r0)
            java.lang.String r7 = r7.mappingUrl
            int r7 = r7.length()
            int r0 = r0 + r7
            java.lang.String r7 = r8.substring(r0)
            return r7
        L_0x00ec:
            return r0
        L_0x00ed:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.parseUrlSuffix(android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo, java.lang.String):java.lang.String");
    }

    public static String parseZcacheMap2String(Hashtable<String, ArrayList<String>> hashtable) {
        return new JSONObject(hashtable).toString();
    }

    public static AppResConfig parseAppResConfig(String str, boolean z) {
        try {
            ConfigDataUtils.ConfigData parseConfig = ConfigDataUtils.parseConfig(str, z, true);
            if (parseConfig == null) {
                return null;
            }
            JSONObject jSONObject = new JSONObject(str);
            AppResConfig appResConfig = new AppResConfig();
            appResConfig.tk = parseConfig.tk;
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONObject jSONObject2 = jSONObject.getJSONObject(next);
                if (!(next == null || jSONObject2 == null)) {
                    appResConfig.getClass();
                    AppResConfig.FileInfo fileInfo = new AppResConfig.FileInfo();
                    fileInfo.path = next;
                    fileInfo.v = jSONObject2.getString("v");
                    fileInfo.url = jSONObject2.getString("url");
                    fileInfo.headers = jSONObject2.optJSONObject("header");
                    appResConfig.mResfileMap.put(next, fileInfo);
                }
            }
            return appResConfig;
        } catch (Exception e) {
            TaoLog.e(TAG, "parseAppResConfig Exception:" + e.getMessage());
            return null;
        }
    }

    public static Hashtable<String, ArrayList<String>> parseZcacheConfig(String str) {
        Hashtable<String, ArrayList<String>> hashtable = new Hashtable<>();
        if (str == null) {
            return hashtable;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONArray optJSONArray = jSONObject.optJSONArray(next);
                if (optJSONArray != null) {
                    ArrayList arrayList = new ArrayList();
                    int length = optJSONArray.length();
                    for (int i = 0; i < length; i++) {
                        arrayList.add(optJSONArray.getString(i));
                    }
                    hashtable.put(next, arrayList);
                }
            }
        } catch (Exception unused) {
        }
        return hashtable;
    }

    public static Hashtable<String, Hashtable<String, String>> parsePrefixes(String str) {
        Hashtable<String, Hashtable<String, String>> hashtable = new Hashtable<>();
        int i = 0;
        if (str == null) {
            WVPackageAppPrefixesConfig.getInstance().updateCount = 0;
            return hashtable;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                JSONObject optJSONObject = jSONObject.optJSONObject(next);
                if (optJSONObject != null) {
                    Hashtable hashtable2 = new Hashtable();
                    Iterator<String> keys2 = optJSONObject.keys();
                    while (keys2.hasNext()) {
                        String next2 = keys2.next();
                        hashtable2.put(next2, optJSONObject.getString(next2));
                        i++;
                    }
                    hashtable.put(next, hashtable2);
                }
            }
        } catch (Exception e) {
            TaoLog.e(TAG, "parse prefixes Exception:" + e.getMessage());
        }
        WVPackageAppPrefixesConfig.getInstance().updateCount = i;
        return hashtable;
    }

    public static synchronized String parseGlobalConfig2String(ZipGlobalConfig zipGlobalConfig) {
        String jSONObject;
        synchronized (ZipAppUtils.class) {
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("v", zipGlobalConfig.v);
                jSONObject2.put(UploadQueueMgr.MSGTYPE_INTERVAL, zipGlobalConfig.i);
                JSONObject jSONObject3 = new JSONObject();
                Map<String, ZipAppInfo> appsTable = zipGlobalConfig.getAppsTable();
                for (String next : appsTable.keySet()) {
                    ZipAppInfo zipAppInfo = appsTable.get(next);
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("v", zipAppInfo.v);
                    jSONObject4.put("f", zipAppInfo.f);
                    jSONObject4.put("z", zipAppInfo.z);
                    jSONObject4.put("s", zipAppInfo.s);
                    jSONObject4.put("t", zipAppInfo.t);
                    jSONObject4.put("status", zipAppInfo.status);
                    jSONObject4.put("mappingUrl", zipAppInfo.mappingUrl);
                    jSONObject4.put("installedSeq", zipAppInfo.installedSeq);
                    jSONObject4.put("installedVersion", zipAppInfo.installedVersion);
                    jSONObject4.put("isOptional", zipAppInfo.isOptional);
                    jSONObject4.put("isPreViewApp", zipAppInfo.isPreViewApp);
                    jSONObject4.put("name", zipAppInfo.name);
                    jSONObject4.put("folders", zipAppInfo.folders);
                    jSONObject3.put(next, jSONObject4);
                }
                jSONObject2.put("apps", jSONObject3);
                jSONObject = jSONObject2.toString();
                if (TaoLog.getLogStatus()) {
                    TaoLog.v("parseGlobalConfig2String", jSONObject);
                }
            } catch (Exception e) {
                TaoLog.w(TAG, "Exception on parseConfig", e, new Object[0]);
                return null;
            }
        }
        return jSONObject;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004c, code lost:
        r2 = r1.next();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig parseString2GlobalConfig(java.lang.String r12) {
        /*
            boolean r0 = android.taobao.windvane.util.TaoLog.getLogStatus()
            if (r0 == 0) goto L_0x000b
            java.lang.String r0 = "parseString2GlobalConfig"
            android.taobao.windvane.util.TaoLog.v(r0, r12)
        L_0x000b:
            android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig r0 = new android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig
            r0.<init>()
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x018c }
            r1.<init>(r12)     // Catch:{ Exception -> 0x018c }
            java.lang.String r12 = "v"
            java.lang.String r2 = ""
            java.lang.String r12 = r1.optString(r12, r2)     // Catch:{ Exception -> 0x018c }
            boolean r2 = android.text.TextUtils.isEmpty(r12)     // Catch:{ Exception -> 0x018c }
            if (r2 != 0) goto L_0x0187
            r0.v = r12     // Catch:{ Exception -> 0x018c }
            java.lang.String r12 = "i"
            java.lang.String r2 = "0"
            java.lang.String r12 = r1.optString(r12, r2)     // Catch:{ Exception -> 0x018c }
            r0.i = r12     // Catch:{ Exception -> 0x018c }
            java.lang.String r12 = "zcache"
            org.json.JSONObject r12 = r1.optJSONObject(r12)     // Catch:{ Exception -> 0x018c }
            if (r12 == 0) goto L_0x003c
            java.lang.String r12 = "0"
            r0.v = r12     // Catch:{ Exception -> 0x018c }
            return r0
        L_0x003c:
            java.lang.String r12 = "apps"
            org.json.JSONObject r12 = r1.optJSONObject(r12)     // Catch:{ Exception -> 0x018c }
            java.util.Iterator r1 = r12.keys()     // Catch:{ Exception -> 0x018c }
        L_0x0046:
            boolean r2 = r1.hasNext()     // Catch:{ Exception -> 0x018c }
            if (r2 == 0) goto L_0x018c
            java.lang.Object r2 = r1.next()     // Catch:{ Exception -> 0x018c }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ Exception -> 0x018c }
            org.json.JSONObject r3 = r12.getJSONObject(r2)     // Catch:{ Exception -> 0x018c }
            if (r3 != 0) goto L_0x0059
            return r0
        L_0x0059:
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r4 = new android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo     // Catch:{ Exception -> 0x018c }
            r4.<init>()     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "f"
            r6 = 5
            long r8 = r3.optLong(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.f = r8     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "v"
            java.lang.String r8 = ""
            java.lang.String r5 = r3.optString(r5, r8)     // Catch:{ Exception -> 0x018c }
            r4.v = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "s"
            r8 = 0
            long r10 = r3.optLong(r5, r8)     // Catch:{ Exception -> 0x018c }
            r4.s = r10     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "t"
            long r5 = r3.optLong(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.t = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "z"
            java.lang.String r6 = ""
            java.lang.String r5 = r3.optString(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.z = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "isOptional"
            r6 = 0
            boolean r5 = r3.optBoolean(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.isOptional = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "isPreViewApp"
            boolean r5 = r3.optBoolean(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.isPreViewApp = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "installedSeq"
            long r7 = r3.optLong(r5, r8)     // Catch:{ Exception -> 0x018c }
            r4.installedSeq = r7     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "installedVersion"
            java.lang.String r7 = "0.0"
            java.lang.String r5 = r3.optString(r5, r7)     // Catch:{ Exception -> 0x018c }
            r4.installedVersion = r5     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "status"
            int r5 = r3.optInt(r5, r6)     // Catch:{ Exception -> 0x018c }
            r4.status = r5     // Catch:{ Exception -> 0x018c }
            r4.name = r2     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "folders"
            java.lang.String r7 = ""
            java.lang.String r5 = r3.optString(r5, r7)     // Catch:{ Exception -> 0x018c }
            r7 = 1
            boolean r8 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x010b }
            if (r8 != 0) goto L_0x0121
            int r8 = r5.length()     // Catch:{ Exception -> 0x010b }
            r9 = 3
            if (r8 <= r9) goto L_0x0121
            java.lang.String r8 = "]"
            int r8 = r5.lastIndexOf(r8)     // Catch:{ Exception -> 0x010b }
            java.lang.String r8 = r5.substring(r7, r8)     // Catch:{ Exception -> 0x010b }
            java.lang.String r9 = ", "
            java.lang.String[] r8 = r8.split(r9)     // Catch:{ Exception -> 0x010b }
            java.util.ArrayList r9 = new java.util.ArrayList     // Catch:{ Exception -> 0x010b }
            java.util.List r8 = java.util.Arrays.asList(r8)     // Catch:{ Exception -> 0x010b }
            r9.<init>(r8)     // Catch:{ Exception -> 0x010b }
            r4.folders = r9     // Catch:{ Exception -> 0x010b }
            java.lang.String r8 = "Folders"
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x010b }
            r9.<init>()     // Catch:{ Exception -> 0x010b }
            java.lang.String r10 = "new folder for "
            r9.append(r10)     // Catch:{ Exception -> 0x010b }
            r9.append(r2)     // Catch:{ Exception -> 0x010b }
            java.lang.String r10 = ": "
            r9.append(r10)     // Catch:{ Exception -> 0x010b }
            r9.append(r5)     // Catch:{ Exception -> 0x010b }
            java.lang.String r5 = r9.toString()     // Catch:{ Exception -> 0x010b }
            android.taobao.windvane.util.TaoLog.e(r8, r5)     // Catch:{ Exception -> 0x010b }
            goto L_0x0121
        L_0x010b:
            java.lang.String r5 = "ZipAppUtils"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018c }
            r8.<init>()     // Catch:{ Exception -> 0x018c }
            java.lang.String r9 = "failed to parse folders : "
            r8.append(r9)     // Catch:{ Exception -> 0x018c }
            r8.append(r2)     // Catch:{ Exception -> 0x018c }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x018c }
            android.taobao.windvane.util.TaoLog.e(r5, r8)     // Catch:{ Exception -> 0x018c }
        L_0x0121:
            java.lang.String r5 = "mappingUrl"
            java.lang.String r8 = ""
            java.lang.String r3 = r3.optString(r5, r8)     // Catch:{ Exception -> 0x018c }
            r4.mappingUrl = r3     // Catch:{ Exception -> 0x018c }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r3 = r4.getAppType()     // Catch:{ Exception -> 0x018c }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r5 = android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_ZCACHE2     // Catch:{ Exception -> 0x018c }
            if (r3 != r5) goto L_0x014a
            java.lang.String r3 = r4.mappingUrl     // Catch:{ Exception -> 0x018c }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x018c }
            if (r3 != 0) goto L_0x0147
            java.util.ArrayList<java.lang.String> r3 = r4.folders     // Catch:{ Exception -> 0x018c }
            if (r3 == 0) goto L_0x0147
            java.util.ArrayList<java.lang.String> r3 = r4.folders     // Catch:{ Exception -> 0x018c }
            int r3 = r3.size()     // Catch:{ Exception -> 0x018c }
            if (r3 != 0) goto L_0x014a
        L_0x0147:
            android.taobao.windvane.packageapp.zipapp.ZipAppManager.parseUrlMappingInfo(r4, r6, r7)     // Catch:{ Exception -> 0x018c }
        L_0x014a:
            java.lang.String r3 = r4.mappingUrl     // Catch:{ Exception -> 0x018c }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x018c }
            if (r3 == 0) goto L_0x0182
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r3 = r4.getAppType()     // Catch:{ Exception -> 0x018c }
            android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum r5 = android.taobao.windvane.packageapp.zipapp.data.ZipAppTypeEnum.ZIP_APP_TYPE_PACKAGEAPP     // Catch:{ Exception -> 0x018c }
            if (r3 != r5) goto L_0x0182
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x018c }
            r3.<init>()     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "//h5."
            r3.append(r5)     // Catch:{ Exception -> 0x018c }
            android.taobao.windvane.config.EnvEnum r5 = android.taobao.windvane.config.GlobalConfig.env     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = r5.getValue()     // Catch:{ Exception -> 0x018c }
            r3.append(r5)     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = ".taobao.com/app/"
            r3.append(r5)     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = r4.name     // Catch:{ Exception -> 0x018c }
            r3.append(r5)     // Catch:{ Exception -> 0x018c }
            java.lang.String r5 = "/"
            r3.append(r5)     // Catch:{ Exception -> 0x018c }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x018c }
            r4.mappingUrl = r3     // Catch:{ Exception -> 0x018c }
        L_0x0182:
            r0.putAppInfo2Table(r2, r4)     // Catch:{ Exception -> 0x018c }
            goto L_0x0046
        L_0x0187:
            java.lang.String r12 = ""
            r0.v = r12     // Catch:{ Exception -> 0x018c }
            return r0
        L_0x018c:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.parseString2GlobalConfig(java.lang.String):android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig");
    }

    public static boolean isNeedPreInstall(Context context) {
        String stringVal = ConfigStorage.getStringVal(SPNAME, "wvttid", "");
        String ttid = GlobalConfig.getInstance().getTtid();
        boolean z = ttid != null && !stringVal.equals(ttid);
        if (!z) {
            return z;
        }
        ConfigStorage.putStringVal(SPNAME, "wvttid", ttid);
        return true;
    }

    public static String getLocPathByUrl(String str, boolean z) {
        if (GlobalConfig.context == null) {
            TaoLog.e(TAG, "WindVane is not init");
            return null;
        } else if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            TaoLog.i(TAG, "packageApp is closed");
            return null;
        } else {
            String force2HttpUrl = WVUrlUtil.force2HttpUrl(WVUrlUtil.removeQueryParam(str));
            ZipAppInfo appInfoByUrl = WVPackageAppRuntime.getAppInfoByUrl(force2HttpUrl);
            if (appInfoByUrl != null) {
                if (!z || appInfoByUrl.installedSeq == appInfoByUrl.s) {
                    String parseUrlSuffix = parseUrlSuffix(appInfoByUrl, force2HttpUrl);
                    if (parseUrlSuffix != null) {
                        return ZipAppFileManager.getInstance().getZipResAbsolutePath(appInfoByUrl, parseUrlSuffix, false);
                    }
                } else {
                    TaoLog.i(TAG, force2HttpUrl + " is not installed newest app");
                    return null;
                }
            }
            ZipGlobalConfig.CacheFileData isZcacheUrl = ConfigManager.getLocGlobalConfig().isZcacheUrl(force2HttpUrl);
            if (isZcacheUrl != null) {
                return isZcacheUrl.path;
            }
            return null;
        }
    }

    public static String getLocPathByUrl(String str) {
        return getLocPathByUrl(str, false);
    }

    public static String getStreamByUrl(String str) {
        InputStream inputStreamByUrl = getInputStreamByUrl(str);
        if (inputStreamByUrl == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStreamByUrl.read(bArr);
                if (read == -1) {
                    return byteArrayOutputStream.toString("UTF-8");
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (Exception unused) {
            return null;
        }
    }

    public static InputStream getInputStreamByUrl(String str) {
        if (WVCommonConfig.commonConfig.packageAppStatus == 0) {
            TaoLog.i(TAG, "packageApp is closed");
            return null;
        }
        String force2HttpUrl = WVUrlUtil.force2HttpUrl(WVUrlUtil.removeQueryParam(str));
        ZCacheConfigManager.getInstance().triggerZCacheConfig();
        if ("3".equals(GlobalConfig.zType)) {
            ZCacheResourceResponse zCacheResource = ZCacheManager.instance().getZCacheResource(force2HttpUrl);
            StringBuilder sb = new StringBuilder();
            sb.append("getInputStreamByUrl，url=[");
            sb.append(force2HttpUrl);
            sb.append("], with response=[");
            sb.append(zCacheResource != null && zCacheResource.isSuccess);
            sb.append(Operators.ARRAY_END_STR);
            TaoLog.i("ZCache", sb.toString());
            if (zCacheResource == null || !zCacheResource.isSuccess) {
                return null;
            }
            return zCacheResource.inputStream;
        }
        ZipAppInfo appInfoByUrl = WVPackageAppRuntime.getAppInfoByUrl(force2HttpUrl);
        WVWrapWebResourceResponse wrapResourceResponse = appInfoByUrl != null ? WVPackageAppRuntime.getWrapResourceResponse(force2HttpUrl, appInfoByUrl) : null;
        if (wrapResourceResponse != null) {
            return wrapResourceResponse.mInputStream;
        }
        ZipGlobalConfig.CacheFileData isZcacheUrl = ConfigManager.getLocGlobalConfig().isZcacheUrl(force2HttpUrl);
        if (isZcacheUrl != null) {
            wrapResourceResponse = WVPackageAppRuntime.getWrapResourceResponse(force2HttpUrl, isZcacheUrl);
        }
        if (wrapResourceResponse != null) {
            return wrapResourceResponse.mInputStream;
        }
        try {
            WebResourceResponse makeComboRes = WVPackageAppRuntime.makeComboRes(force2HttpUrl, (ComboInfo) null, new HashMap());
            if (makeComboRes != null) {
                return makeComboRes.getData();
            }
        } catch (Exception unused) {
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:28:0x00af  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String getStreamByUrl(java.lang.String r6, java.lang.String r7) {
        /*
            android.app.Application r0 = android.taobao.windvane.config.GlobalConfig.context
            r1 = 0
            if (r0 == 0) goto L_0x00b7
            if (r6 == 0) goto L_0x00b7
            if (r7 != 0) goto L_0x000b
            goto L_0x00b7
        L_0x000b:
            java.lang.String r7 = android.taobao.windvane.util.WVUrlUtil.removeQueryParam(r7)
            java.lang.String r7 = android.taobao.windvane.util.WVUrlUtil.force2HttpUrl(r7)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r2 = 128(0x80, float:1.794E-43)
            r0.<init>(r2)
            android.app.Application r2 = android.taobao.windvane.config.GlobalConfig.context
            java.io.File r2 = r2.getFilesDir()
            java.lang.String r2 = r2.getAbsolutePath()
            r0.append(r2)
            java.lang.String r2 = java.io.File.separator
            r0.append(r2)
            java.lang.String r2 = android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants.ZIPAPP_ROOT_APPS_DIR
            r0.append(r2)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = java.io.File.separator
            r2.append(r3)
            r2.append(r6)
            java.lang.String r2 = r2.toString()
            r0.append(r2)
            java.io.File r2 = new java.io.File
            java.lang.String r3 = r0.toString()
            r2.<init>(r3)
            java.lang.String[] r2 = r2.list()
            if (r2 != 0) goto L_0x0055
            return r1
        L_0x0055:
            java.lang.String r3 = java.io.File.separator
            r0.append(r3)
            r3 = 0
            r2 = r2[r3]
            r0.append(r2)
            boolean r2 = r7.contains(r6)
            if (r2 == 0) goto L_0x0074
            int r2 = r7.indexOf(r6)
            int r3 = r6.length()
            int r2 = r2 + r3
            java.lang.String r2 = r7.substring(r2)
            goto L_0x0075
        L_0x0074:
            r2 = r1
        L_0x0075:
            r0.append(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x007f
            return r1
        L_0x007f:
            java.lang.String r0 = r0.toString()
            byte[] r0 = android.taobao.windvane.file.FileAccesser.read((java.lang.String) r0)
            java.lang.String r2 = new java.lang.String     // Catch:{ Exception -> 0x00a8 }
            java.lang.String r3 = "UTF-8"
            r2.<init>(r0, r3)     // Catch:{ Exception -> 0x00a8 }
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x00a6 }
            if (r0 == 0) goto L_0x00b6
            android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo r0 = android.taobao.windvane.packageapp.WVPackageAppRuntime.getAppInfoByUrl(r7)     // Catch:{ Exception -> 0x00a6 }
            if (r0 == 0) goto L_0x00b6
            android.taobao.windvane.monitor.WVPackageMonitorInterface r1 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()     // Catch:{ Exception -> 0x00a6 }
            java.lang.String r3 = r0.name     // Catch:{ Exception -> 0x00a6 }
            long r4 = r0.installedSeq     // Catch:{ Exception -> 0x00a6 }
            r1.commitPackageVisitSuccess(r3, r4)     // Catch:{ Exception -> 0x00a6 }
            goto L_0x00b6
        L_0x00a6:
            goto L_0x00a9
        L_0x00a8:
            r2 = r1
        L_0x00a9:
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            if (r0 == 0) goto L_0x00b6
            android.taobao.windvane.monitor.WVPackageMonitorInterface r0 = android.taobao.windvane.monitor.WVMonitorService.getPackageMonitorInterface()
            r0.commitPackageWarning(r6, r7)
        L_0x00b6:
            return r2
        L_0x00b7:
            java.lang.String r6 = "ZipAppUtils"
            java.lang.String r7 = "WindVane is not init or param is null"
            android.taobao.windvane.util.TaoLog.e(r6, r7)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils.getStreamByUrl(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean savaZcacheMapToLoc(Hashtable<String, ArrayList<String>> hashtable) {
        if (hashtable != null) {
            try {
                String parseZcacheMap2String = parseZcacheMap2String(hashtable);
                TaoLog.d(TAG, "ZcacheMap : " + parseZcacheMap2String);
                return ZipAppFileManager.getInstance().saveZcacheConfig(parseZcacheMap2String.getBytes(), false);
            } catch (Exception e) {
                e.printStackTrace();
                TaoLog.e(TAG, "Zcache 本地配置保存异常失败:" + e.toString());
            }
        }
        return false;
    }

    public static Map<String, Object> toMap(JSONObject jSONObject) throws JSONException {
        HashMap hashMap = new HashMap();
        Iterator<String> keys = jSONObject.keys();
        while (keys.hasNext()) {
            String next = keys.next();
            Object obj = jSONObject.get(next);
            if (obj instanceof JSONArray) {
                obj = toList((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = toMap((JSONObject) obj);
            }
            hashMap.put(next, obj);
        }
        return hashMap;
    }

    public static List<Object> toList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object obj = jSONArray.get(i);
            if (obj instanceof JSONArray) {
                obj = toList((JSONArray) obj);
            } else if (obj instanceof JSONObject) {
                obj = toMap((JSONObject) obj);
            }
            arrayList.add(obj);
        }
        return arrayList;
    }

    public static String getErrorCode(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case SecExceptionCode.SEC_ERROR_SIMULATORDETECT_UNSUPPORTED /*1598*/:
                if (str.equals("20")) {
                    c = 0;
                    break;
                }
                break;
            case 1599:
                if (str.equals("21")) {
                    c = 2;
                    break;
                }
                break;
            case SecExceptionCode.SEC_ERROR_SAFETOKEN /*1600*/:
                if (str.equals("22")) {
                    c = 3;
                    break;
                }
                break;
            case SecExceptionCode.SEC_ERROR_SAFETOKEN_DATA_FILE_MISMATCH /*1602*/:
                if (str.equals("24")) {
                    c = 1;
                    break;
                }
                break;
            case SecExceptionCode.SEC_ERROR_SAFETOKEN_NO_DATA_FILE /*1603*/:
                if (str.equals(WVPackageMonitorInterface.ZIP_REMOVED_BY_CONFIG)) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "404";
            case 1:
                return "405";
            case 2:
                return "406";
            case 3:
                return "408";
            case 4:
                return "409";
            default:
                return null;
        }
    }
}
