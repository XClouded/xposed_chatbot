package com.taobao.accs.utl;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.os.Process;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import anet.channel.util.HMacUtil;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.client.AdapterGlobalClientInfo;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.android.spdy.SpdyProtocol;

public class UtilityImpl {
    public static final String NET_TYPE_2G = "2g";
    public static final String NET_TYPE_3G = "3g";
    public static final String NET_TYPE_4G = "4g";
    public static final String NET_TYPE_UNKNOWN = "unknown";
    public static final String NET_TYPE_WIFI = "wifi";
    private static final String SSL_TIKET_KEY = "accs_ssl_ticket_key";
    private static final String SSL_TIKET_KEY2 = "accs_ssl_key2_";
    private static final String TAG = "UtilityImpl";
    public static final int TNET_FILE_NUM = 5;
    public static final int TNET_FILE_SIZE = 5242880;
    private static final byte[] mLock = new byte[0];

    public static String getProxyHost(Context context) {
        String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 4).getString(Constants.KEY_PROXY_HOST, (String) null);
        if (!TextUtils.isEmpty(string)) {
            return string;
        }
        String proxyIp = getProxyIp();
        if (TextUtils.isEmpty(proxyIp)) {
            return null;
        }
        return proxyIp;
    }

    public static int getProxyPort(Context context) {
        int i = context.getSharedPreferences(Constants.SP_FILE_NAME, 4).getInt(Constants.KEY_PROXY_PORT, -1);
        if (i > 0) {
            return i;
        }
        if (getProxyHost(context) == null) {
            return -1;
        }
        try {
            return getProxyPort();
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public static boolean appVersionChanged(Context context) {
        int i;
        String str;
        synchronized (mLock) {
            PackageInfo packageInfo = GlobalClientInfo.getInstance(context).getPackageInfo();
            int i2 = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getInt(Constants.KEY_APP_VERSION_CODE, -1);
            String string = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).getString(Constants.KEY_APP_VERSION_NAME, "");
            if (packageInfo != null) {
                i = packageInfo.versionCode;
                str = packageInfo.versionName;
            } else {
                str = null;
                i = 0;
            }
            if (i2 == i) {
                if (string.equals(str)) {
                    return false;
                }
            }
            saveAppVersion(context);
            ALog.i(TAG, "appVersionChanged", "oldV", Integer.valueOf(i2), "nowV", Integer.valueOf(i), "oldN", string, "nowN", str);
            return true;
        }
    }

    private static void saveAppVersion(Context context) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
            edit.putInt(Constants.KEY_APP_VERSION_CODE, GlobalClientInfo.getInstance(context).getPackageInfo().versionCode);
            edit.putString(Constants.KEY_APP_VERSION_NAME, GlobalClientInfo.getInstance(context).getPackageInfo().versionName);
            edit.apply();
        } catch (Throwable th) {
            ALog.e(TAG, "saveAppVersion", th, new Object[0]);
        }
    }

    public static void focusDisableService(Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.putBoolean(Constants.KEY_FOUCE_DISABLE, true);
                edit.apply();
                disableService(context);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "focusDisableService", th, new Object[0]);
        }
    }

    public static void focusEnableService(Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.putBoolean(Constants.KEY_FOUCE_DISABLE, false);
                edit.apply();
                enableService(context);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "focusEnableService", th, new Object[0]);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v9, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: boolean} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v13, resolved type: android.content.Context} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v14, resolved type: boolean} */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0014, code lost:
        r4 = r4;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean getFocusDisableStatus(android.content.Context r4) {
        /*
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            byte[] r1 = mLock     // Catch:{ Exception -> 0x001d }
            monitor-enter(r1)     // Catch:{ Exception -> 0x001d }
            java.lang.String r2 = "ACCS_SDK"
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r2, r0)     // Catch:{ all -> 0x0015 }
            java.lang.String r2 = "fouce_disable"
            boolean r4 = r4.getBoolean(r2, r0)     // Catch:{ all -> 0x0015 }
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            goto L_0x0028
        L_0x0015:
            r2 = move-exception
            r4 = 0
        L_0x0017:
            monitor-exit(r1)     // Catch:{ all -> 0x001b }
            throw r2     // Catch:{ Exception -> 0x0019 }
        L_0x0019:
            r1 = move-exception
            goto L_0x001f
        L_0x001b:
            r2 = move-exception
            goto L_0x0017
        L_0x001d:
            r1 = move-exception
            r4 = 0
        L_0x001f:
            java.lang.String r2 = "UtilityImpl"
            java.lang.String r3 = "getFocusDisableStatus"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r2, r3, r1, r0)
        L_0x0028:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.UtilityImpl.getFocusDisableStatus(android.content.Context):boolean");
    }

    public static boolean getServiceEnabled(Context context) {
        try {
            if (context.getPackageManager().getServiceInfo(new ComponentName(context, AdapterUtilityImpl.channelService), 128).enabled) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e(TAG, getStackMsg(e), new Object[0]);
        }
    }

    public static boolean getAgooServiceEnabled(Context context) {
        ComponentName componentName = new ComponentName(context, AdapterGlobalClientInfo.getAgooCustomServiceName(context.getPackageName()));
        PackageManager packageManager = context.getPackageManager();
        try {
            if (componentName.getPackageName().equals(Operators.AND_NOT)) {
                ALog.e(TAG, "getAgooServiceEnabled,exception,comptName.getPackageName()=" + componentName.getPackageName(), new Object[0]);
                return false;
            } else if (packageManager.getServiceInfo(componentName, 128).enabled) {
                return true;
            } else {
                return false;
            }
        } catch (Exception unused) {
        }
    }

    public static void enableService(Context context) {
        ComponentName componentName = new ComponentName(context, AdapterUtilityImpl.channelService);
        ALog.d(TAG, "enableService", "comptName", componentName);
        try {
            context.getPackageManager().setComponentEnabledSetting(componentName, 1, 1);
        } catch (Exception e) {
            ALog.w(TAG, "enableService", e, new Object[0]);
        }
    }

    public static void disableService(Context context) {
        ComponentName componentName = new ComponentName(context, AdapterUtilityImpl.channelService);
        PackageManager packageManager = context.getPackageManager();
        try {
            ALog.d(TAG, "disableService,comptName=" + componentName.toString(), new Object[0]);
            if (packageManager.getServiceInfo(componentName, 128).enabled) {
                packageManager.setComponentEnabledSetting(componentName, 2, 1);
                killService(context);
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
    }

    public static void killService(Context context) {
        try {
            int myUid = Process.myUid();
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null) {
                for (ActivityManager.RunningAppProcessInfo next : activityManager.getRunningAppProcesses()) {
                    if (next.uid == myUid) {
                        if (!TextUtils.isEmpty(AdapterGlobalClientInfo.mChannelProcessName) && AdapterGlobalClientInfo.mChannelProcessName.equals(next.processName)) {
                            ALog.e(TAG, "killService", "processName", next.processName);
                            Process.killProcess(next.pid);
                            return;
                        } else if (next.processName.endsWith(":channel")) {
                            ALog.e(TAG, "killService", "processName", next.processName);
                            Process.killProcess(next.pid);
                            return;
                        }
                    }
                }
                ALog.e(TAG, "kill nothing", new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "killService", th, new Object[0]);
        }
    }

    public static String getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return "unknown";
        }
        if (activeNetworkInfo.getType() == 1) {
            return "wifi";
        }
        String subtypeName = activeNetworkInfo.getSubtypeName();
        if (!TextUtils.isEmpty(subtypeName)) {
            return subtypeName.replaceAll(Operators.SPACE_STR, "");
        }
        return "";
    }

    public static String getNetworkTypeExt(Context context) {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo == null) {
                return "unknown";
            }
            if (activeNetworkInfo.getType() == 1) {
                return "wifi";
            }
            switch (activeNetworkInfo.getSubtype()) {
                case 1:
                case 2:
                case 4:
                case 7:
                case 11:
                    return "2g";
                case 3:
                case 5:
                case 6:
                case 8:
                case 9:
                case 10:
                case 12:
                case 14:
                case 15:
                    return "3g";
                case 13:
                    return "4g";
                default:
                    String subtypeName = activeNetworkInfo.getSubtypeName();
                    if (TextUtils.isEmpty(subtypeName)) {
                        return "unknown";
                    }
                    if (subtypeName.equalsIgnoreCase("td-scdma") || subtypeName.equalsIgnoreCase("td_scdma")) {
                        return "3g";
                    }
                    return subtypeName.equalsIgnoreCase("tds_hsdpa") ? "3g" : "unknown";
            }
        } catch (Exception e) {
            ALog.e(TAG, "getNetworkTypeExt", e, new Object[0]);
            return null;
        }
    }

    public static String getImsi(Context context) {
        try {
            return ((TelephonyManager) context.getSystemService("phone")).getSubscriberId();
        } catch (Throwable unused) {
            return null;
        }
    }

    public static String formatDay(long j) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Long.valueOf(j));
        } catch (Throwable th) {
            ALog.e(TAG, "formatDay", th, new Object[0]);
            return "";
        }
    }

    public static boolean isForeground(Context context) {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            String packageName = GlobalClientInfo.getInstance(context).getActivityManager().getRunningTasks(1).get(0).topActivity.getPackageName();
            if (!TextUtils.isEmpty(packageName) && packageName.equals(context.getPackageName())) {
                return true;
            }
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(TAG, "isForeground time " + (System.currentTimeMillis() - currentTimeMillis), new Object[0]);
            }
            return false;
        } catch (Throwable th) {
            ALog.e(TAG, "isForeground error ", th, new Object[0]);
        }
    }

    private static boolean isSecurityOff(String str) {
        int i;
        AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
        if (configByTag == null) {
            i = 0;
        } else {
            i = configByTag.getSecurity();
        }
        if (i == 2) {
            return true;
        }
        return false;
    }

    public static String getAppsign(Context context, String str, String str2, String str3, String str4) {
        String signRequest;
        if (TextUtils.isEmpty(str)) {
            ALog.e(TAG, "getAppsign appkey null", new Object[0]);
            return null;
        }
        try {
            if (!isSecurityOff(str4)) {
                SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
                if (instance != null) {
                    ALog.d(TAG, "getAppsign SecurityGuardManager not null!", new Object[0]);
                    ISecureSignatureComponent secureSignatureComp = instance.getSecureSignatureComp();
                    SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
                    securityGuardParamContext.appKey = str;
                    Map<String, String> map = securityGuardParamContext.paramMap;
                    map.put("INPUT", str3 + str);
                    securityGuardParamContext.requestType = 3;
                    AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str4);
                    signRequest = secureSignatureComp.signRequest(securityGuardParamContext, configByTag != null ? configByTag.getAuthCode() : null);
                } else {
                    ALog.d(TAG, "getAppsign SecurityGuardManager is null", new Object[0]);
                    return null;
                }
            } else if (!TextUtils.isEmpty(str2)) {
                byte[] bytes = str2.getBytes();
                signRequest = HMacUtil.hmacSha1Hex(bytes, (str + str3).getBytes());
            } else {
                ALog.e(TAG, "getAppsign secret null", new Object[0]);
                return null;
            }
            return signRequest;
        } catch (Throwable th) {
            ALog.e(TAG, "getAppsign", th, new Object[0]);
            return null;
        }
    }

    public static byte[] staticBinarySafeDecryptNoB64(Context context, String str, String str2, byte[] bArr) {
        IStaticDataEncryptComponent staticDataEncryptComp;
        byte[] bArr2 = null;
        if (isSecurityOff(str)) {
            return null;
        }
        if (context == null || bArr == null) {
            ALog.e(TAG, "staticBinarySafeDecryptNoB64 input null!", new Object[0]);
            return null;
        }
        try {
            AccsClientConfig configByTag = AccsClientConfig.getConfigByTag(str);
            String authCode = configByTag != null ? configByTag.getAuthCode() : null;
            ALog.i(TAG, "staticBinarySafeDecryptNoB64", "appkey", str2, "authcode", authCode);
            SecurityGuardManager instance = SecurityGuardManager.getInstance(context);
            if (!(instance == null || (staticDataEncryptComp = instance.getStaticDataEncryptComp()) == null)) {
                bArr2 = staticDataEncryptComp.staticBinarySafeDecryptNoB64(16, SpdyProtocol.TNET_PUBKEY_SG_KEY, bArr, authCode);
            }
            if (bArr2 == null) {
                ALog.e(TAG, "staticBinarySafeDecryptNoB64 null", new Object[0]);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "staticBinarySafeDecryptNoB64", th, new Object[0]);
        }
        return bArr2;
    }

    public static int SecurityGuardPutSslTicket2(Context context, String str, String str2, String str3, byte[] bArr) {
        SecurityGuardManager instance;
        IDynamicDataStoreComponent dynamicDataStoreComp;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || context == null || bArr == null) {
            return -1;
        }
        try {
            if (isSecurityOff(str) || (instance = SecurityGuardManager.getInstance(context)) == null || (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) == null) {
                return -1;
            }
            return dynamicDataStoreComp.putByteArray(SSL_TIKET_KEY2 + str3, bArr);
        } catch (Throwable th) {
            ALog.e(TAG, "SecurityGuardPutSslTicket2", th, new Object[0]);
            return -1;
        }
    }

    public static byte[] SecurityGuardGetSslTicket2(Context context, String str, String str2, String str3) {
        SecurityGuardManager instance;
        IDynamicDataStoreComponent dynamicDataStoreComp;
        if (context == null || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str2)) {
            ALog.i(TAG, "get sslticket input null", new Object[0]);
            return null;
        }
        try {
            if (isSecurityOff(str) || (instance = SecurityGuardManager.getInstance(context)) == null || (dynamicDataStoreComp = instance.getDynamicDataStoreComp()) == null) {
                return null;
            }
            return dynamicDataStoreComp.getByteArray(SSL_TIKET_KEY2 + str3);
        } catch (Throwable th) {
            ALog.e(TAG, "SecurityGuardGetSslTicket2", th, new Object[0]);
            return null;
        }
    }

    public static boolean isNetworkConnected(Context context) {
        if (context == null) {
            return false;
        }
        try {
            NetworkInfo activeNetworkInfo = GlobalClientInfo.getInstance(context).getConnectivityManager().getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getDeviceId(Context context) {
        return AdapterUtilityImpl.getDeviceId(context);
    }

    public static boolean isServiceRunning(Context context, String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo next : ((ActivityManager) context.getSystemService("activity")).getRunningServices(30)) {
            if (next.service.getPackageName().equals(str) && next.service.getClassName().equals(AdapterUtilityImpl.channelService)) {
                return true;
            }
        }
        return false;
    }

    public static boolean packageExist(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            ALog.e(TAG, "package not exist", "pkg", str);
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        r3 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean utdidChanged(java.lang.String r3, android.content.Context r4) {
        /*
            r0 = 0
            byte[] r1 = mLock     // Catch:{ Throwable -> 0x0022 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0022 }
            java.lang.String r2 = getDeviceId(r4)     // Catch:{ all -> 0x001a }
            android.content.SharedPreferences r3 = r4.getSharedPreferences(r3, r0)     // Catch:{ all -> 0x001a }
            java.lang.String r4 = "utdid"
            java.lang.String r3 = r3.getString(r4, r2)     // Catch:{ all -> 0x001a }
            boolean r3 = r3.equals(r2)     // Catch:{ all -> 0x001a }
            r3 = r3 ^ 1
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            goto L_0x002d
        L_0x001a:
            r4 = move-exception
            r3 = 0
        L_0x001c:
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            throw r4     // Catch:{ Throwable -> 0x001e }
        L_0x001e:
            r4 = move-exception
            goto L_0x0024
        L_0x0020:
            r4 = move-exception
            goto L_0x001c
        L_0x0022:
            r4 = move-exception
            r3 = 0
        L_0x0024:
            java.lang.String r1 = "UtilityImpl"
            java.lang.String r2 = "utdidChanged"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r1, r2, r4, r0)
        L_0x002d:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.UtilityImpl.utdidChanged(java.lang.String, android.content.Context):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        r3 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean notificationStateChanged(java.lang.String r3, android.content.Context r4) {
        /*
            r0 = 0
            byte[] r1 = mLock     // Catch:{ Throwable -> 0x0022 }
            monitor-enter(r1)     // Catch:{ Throwable -> 0x0022 }
            java.lang.String r2 = isNotificationEnabled(r4)     // Catch:{ all -> 0x001a }
            android.content.SharedPreferences r3 = r4.getSharedPreferences(r3, r0)     // Catch:{ all -> 0x001a }
            java.lang.String r4 = "notification_state"
            java.lang.String r3 = r3.getString(r4, r2)     // Catch:{ all -> 0x001a }
            boolean r3 = r3.equals(r2)     // Catch:{ all -> 0x001a }
            r3 = r3 ^ 1
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            goto L_0x002d
        L_0x001a:
            r4 = move-exception
            r3 = 0
        L_0x001c:
            monitor-exit(r1)     // Catch:{ all -> 0x0020 }
            throw r4     // Catch:{ Throwable -> 0x001e }
        L_0x001e:
            r4 = move-exception
            goto L_0x0024
        L_0x0020:
            r4 = move-exception
            goto L_0x001c
        L_0x0022:
            r4 = move-exception
            r3 = 0
        L_0x0024:
            java.lang.String r1 = "UtilityImpl"
            java.lang.String r2 = "notificationStateChanged"
            java.lang.Object[] r0 = new java.lang.Object[r0]
            com.taobao.accs.utl.ALog.e(r1, r2, r4, r0)
        L_0x002d:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.UtilityImpl.notificationStateChanged(java.lang.String, android.content.Context):boolean");
    }

    public static void saveUtdid(String str, Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences.Editor edit = context.getSharedPreferences(str, 0).edit();
                edit.putString("utdid", getDeviceId(context));
                edit.apply();
            }
        } catch (Throwable th) {
            ALog.e(TAG, "saveUtdid", th, new Object[0]);
        }
    }

    public static String getUtdid(String str, Context context) {
        String string;
        try {
            synchronized (mLock) {
                string = context.getSharedPreferences(str, 0).getString("utdid", getDeviceId(context));
            }
            return string;
        } catch (Throwable th) {
            ALog.e(TAG, "getUtdid", th, new Object[0]);
            return "";
        }
    }

    public static String getOperator(Context context) {
        String imsi = getImsi(context);
        if (imsi == null || imsi.length() <= 5) {
            return BuildConfig.buildJavascriptFrameworkVersion;
        }
        return imsi.substring(0, 5);
    }

    public static void setServiceTime(Context context, String str, long j) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_CHANNEL_FILE_NAME, 0).edit();
            edit.putLong(str, j);
            edit.apply();
            ALog.d(TAG, "setServiceTime:" + j, new Object[0]);
        } catch (Throwable th) {
            ALog.e(TAG, "setServiceTime:", th, new Object[0]);
        }
    }

    public static void saveNotificationState(Context context, String str, String str2) {
        try {
            SharedPreferences.Editor edit = context.getSharedPreferences(str, 4).edit();
            edit.putString(Constants.SP_KEY_NOTIFICATION_STATE, str2);
            edit.apply();
        } catch (Exception e) {
            ALog.e(TAG, "saveNotificationState fail", e, new Object[0]);
        }
    }

    public static long getServiceAliveTime(Context context) {
        long j;
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_CHANNEL_FILE_NAME, 0);
            long j2 = sharedPreferences.getLong(Constants.SP_KEY_SERVICE_START, 0);
            j = j2 > 0 ? sharedPreferences.getLong(Constants.SP_KEY_SERVICE_END, 0) - j2 : 0;
            try {
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putLong(Constants.SP_KEY_SERVICE_START, 0);
                edit.putLong(Constants.SP_KEY_SERVICE_END, 0);
                edit.apply();
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            j = 0;
            ALog.e(TAG, "getServiceAliveTime:", th, new Object[0]);
            return j;
        }
        return j;
    }

    public static String getAppVersion(Context context) {
        try {
            return GlobalClientInfo.getInstance(context).getPackageInfo().versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isMainProcess(Context context) {
        return AdapterUtilityImpl.isMainProcess(context);
    }

    public static int getByteLen(String str) {
        if (str == null) {
            return 0;
        }
        try {
            return str.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getStackMsg(Throwable th) {
        return AdapterUtilityImpl.getStackMsg(th);
    }

    public static void storeCookie(Context context, String str) {
        try {
            if (!TextUtils.isEmpty(str)) {
                GlobalClientInfo.mCookieSec = str;
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_COOKIE_FILE_NAME, 0).edit();
                edit.putString(Constants.SP_KEY_COOKIE_SEC, str);
                edit.apply();
            }
        } catch (Exception e) {
            ALog.e(TAG, "storeCookie fail", e, new Object[0]);
        }
    }

    public static String restoreCookie(Context context) {
        try {
            return context.getSharedPreferences(Constants.SP_COOKIE_FILE_NAME, 4).getString(Constants.SP_KEY_COOKIE_SEC, (String) null);
        } catch (Exception e) {
            ALog.e(TAG, "reStoreCookie fail", e, new Object[0]);
            return null;
        }
    }

    public static void clearCookie(Context context) {
        try {
            GlobalClientInfo.mCookieSec = null;
            SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_COOKIE_FILE_NAME, 0).edit();
            edit.clear();
            edit.apply();
        } catch (Throwable th) {
            ALog.e(TAG, "clearCookie fail", th, new Object[0]);
        }
    }

    public static long getUsableSpace() {
        return AdapterUtilityImpl.getUsableSpace();
    }

    public static String getProxyIp() {
        if (Build.VERSION.SDK_INT < 11) {
            return Proxy.getDefaultHost();
        }
        return System.getProperty("http.proxyHost");
    }

    public static int getProxyPort() {
        if (Build.VERSION.SDK_INT < 11) {
            return Proxy.getDefaultPort();
        }
        try {
            return Integer.parseInt(System.getProperty("http.proxyPort"));
        } catch (NumberFormatException unused) {
            return -1;
        }
    }

    public static String getProxy() {
        String str = getProxyIp() + ":" + getProxyPort();
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "getProxy:" + str, new Object[0]);
        }
        return str;
    }

    public static String isNotificationEnabled(Context context) {
        return AdapterUtilityImpl.isNotificationEnabled(context);
    }

    public static String getTnetLogFilePath(Context context, String str) {
        try {
            File externalFilesDir = context.getExternalFilesDir("tnetlogs");
            if (externalFilesDir == null || !externalFilesDir.exists() || !externalFilesDir.canWrite()) {
                externalFilesDir = context.getDir(TLogInitializer.DEFAULT_DIR, 0);
            }
            ALog.d(TAG, "getTnetLogFilePath :" + externalFilesDir, new Object[0]);
            return externalFilesDir + "/" + str.toLowerCase();
        } catch (Throwable th) {
            ALog.e(TAG, "getTnetLogFilePath", th, new Object[0]);
            return null;
        }
    }

    public static String int2String(int i) {
        try {
            return String.valueOf(i);
        } catch (Exception e) {
            ALog.e(TAG, "int2String", e, new Object[0]);
            return null;
        }
    }

    public static int String2Int(String str) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception e) {
            ALog.e(TAG, "String2Int", e, new Object[0]);
            return 0;
        }
    }

    public static String[] getAppkey(Context context) {
        String[] appkey;
        synchronized (mLock) {
            appkey = ACCSManager.getAppkey(context);
        }
        return appkey;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x004c, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveAppKey(android.content.Context r3, java.lang.String r4) {
        /*
            byte[] r0 = mLock     // Catch:{ Throwable -> 0x0050 }
            monitor-enter(r0)     // Catch:{ Throwable -> 0x0050 }
            java.lang.String r1 = "ACCS_SDK"
            r2 = 0
            android.content.SharedPreferences r3 = r3.getSharedPreferences(r1, r2)     // Catch:{ all -> 0x004d }
            java.lang.String r1 = "appkey"
            java.lang.String r2 = ""
            java.lang.String r1 = r3.getString(r1, r2)     // Catch:{ all -> 0x004d }
            boolean r2 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x004d }
            if (r2 != 0) goto L_0x004b
            boolean r2 = r1.equals(r4)     // Catch:{ all -> 0x004d }
            if (r2 != 0) goto L_0x004b
            boolean r2 = r1.contains(r4)     // Catch:{ all -> 0x004d }
            if (r2 == 0) goto L_0x0025
            goto L_0x004b
        L_0x0025:
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x004d }
            if (r2 == 0) goto L_0x002c
            goto L_0x003d
        L_0x002c:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x004d }
            r2.<init>(r1)     // Catch:{ all -> 0x004d }
            java.lang.String r1 = "|"
            r2.append(r1)     // Catch:{ all -> 0x004d }
            r2.append(r4)     // Catch:{ all -> 0x004d }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x004d }
        L_0x003d:
            android.content.SharedPreferences$Editor r3 = r3.edit()     // Catch:{ all -> 0x004d }
            java.lang.String r1 = "appkey"
            r3.putString(r1, r4)     // Catch:{ all -> 0x004d }
            r3.apply()     // Catch:{ all -> 0x004d }
            monitor-exit(r0)     // Catch:{ all -> 0x004d }
            goto L_0x0054
        L_0x004b:
            monitor-exit(r0)     // Catch:{ all -> 0x004d }
            return
        L_0x004d:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x004d }
            throw r3     // Catch:{ Throwable -> 0x0050 }
        L_0x0050:
            r3 = move-exception
            r3.printStackTrace()
        L_0x0054:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.utl.UtilityImpl.saveAppKey(android.content.Context, java.lang.String):void");
    }

    public static void clearSharePreferences(Context context) {
        try {
            synchronized (mLock) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SP_FILE_NAME, 0);
                String string = sharedPreferences.getString("appkey", (String) null);
                String string2 = sharedPreferences.getString("app_sercet", (String) null);
                String string3 = sharedPreferences.getString(Constants.KEY_PROXY_HOST, (String) null);
                int i = sharedPreferences.getInt(Constants.KEY_PROXY_PORT, -1);
                int i2 = sharedPreferences.getInt("version", -1);
                int i3 = sharedPreferences.getInt(Constants.SP_KEY_DEBUG_MODE, 0);
                SharedPreferences.Editor edit = context.getSharedPreferences(Constants.SP_FILE_NAME, 0).edit();
                edit.clear();
                if (!TextUtils.isEmpty(string)) {
                    edit.putString("appkey", string);
                }
                if (!TextUtils.isEmpty(string2)) {
                    edit.putString("app_sercet", string2);
                }
                if (!TextUtils.isEmpty(string3)) {
                    edit.putString(Constants.KEY_PROXY_HOST, string3);
                }
                if (i > 0) {
                    edit.putInt(Constants.KEY_PROXY_PORT, i);
                }
                if (i2 > 0) {
                    edit.putInt("version", i2);
                }
                if (i3 == 2 || i3 == 1) {
                    edit.putInt(Constants.SP_KEY_DEBUG_MODE, i3);
                }
                edit.apply();
            }
        } catch (Throwable th) {
            ALog.e(TAG, "clearSharePreferences", th, new Object[0]);
        }
    }

    public static String getEmuiVersion() {
        Class[] clsArr = {String.class};
        Object[] objArr = {"ro.build.version.emui"};
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            String str = (String) cls.getDeclaredMethod("get", clsArr).invoke(cls, objArr);
            ALog.d(TAG, "getEmuiVersion", "result", str);
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
            return "";
        } catch (Exception e) {
            ALog.e(TAG, "getEmuiVersion", e, new Object[0]);
        }
    }

    public static final Map<String, String> getHeader(Map<String, List<String>> map) {
        HashMap hashMap = new HashMap();
        try {
            for (Map.Entry next : map.entrySet()) {
                String str = (String) next.getKey();
                if (!TextUtils.isEmpty(str)) {
                    String list2String = list2String((List) next.getValue());
                    if (!TextUtils.isEmpty(list2String)) {
                        if (!str.startsWith(":")) {
                            str = str.toLowerCase(Locale.US);
                        }
                        hashMap.put(str, list2String);
                    }
                }
            }
        } catch (Throwable unused) {
        }
        return hashMap;
    }

    public static final String list2String(List<String> list) {
        StringBuffer stringBuffer = new StringBuffer();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            stringBuffer.append(list.get(i));
            if (i < size - 1) {
                stringBuffer.append(",");
            }
        }
        return stringBuffer.toString();
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException unused) {
            return str;
        }
    }

    public static boolean isSameDay(long j, long j2) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date(j));
        Calendar instance2 = Calendar.getInstance();
        instance2.setTime(new Date(j2));
        if (instance.get(0) == instance2.get(0) && instance.get(1) == instance2.get(1) && instance.get(6) == instance2.get(6)) {
            return true;
        }
        return false;
    }
}
