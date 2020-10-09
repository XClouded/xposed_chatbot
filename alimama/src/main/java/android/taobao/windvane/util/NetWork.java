package android.taobao.windvane.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.taobao.windvane.config.GlobalConfig;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.taobao.weex.el.parse.Operators;

import org.apache.http.HttpHost;

import java.util.HashMap;
import java.util.Map;

public class NetWork {
    public static final int CHINA_MOBILE = 1;
    public static final int CHINA_TELECOM = 3;
    public static final int CHINA_UNICOM = 2;
    public static final String CONN_TYPE_GPRS = "gprs";
    public static final String CONN_TYPE_NONE = "none";
    public static final String CONN_TYPE_WIFI = "wifi";
    public static final int SIM_NO = -1;
    public static final int SIM_OK = 0;
    public static final int SIM_UNKNOW = -2;
    private static BroadcastReceiver connChangerRvr = null;
    public static boolean proxy = false;

    public static boolean isAvailable(Context context) {
        return getNetworkType(context) >= 0;
    }

    public static int getNetworkType(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null || !activeNetworkInfo.isConnectedOrConnecting()) {
            return -9;
        }
        return activeNetworkInfo.getType();
    }

    public static String getNetworkInfo(Context context) {
        NetworkInfo activeNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (activeNetworkInfo = connectivityManager.getActiveNetworkInfo()) == null) {
            return null;
        }
        NetworkInfo.State state = activeNetworkInfo.getState();
        if (state != NetworkInfo.State.CONNECTED && state != NetworkInfo.State.CONNECTING) {
            return null;
        }
        return activeNetworkInfo.getTypeName() + Operators.SPACE_STR + activeNetworkInfo.getSubtypeName() + activeNetworkInfo.getExtraInfo();
    }

    public static int getSimState(Context context) {
        int simState = ((TelephonyManager) context.getSystemService("phone")).getSimState();
        if (simState == 5) {
            return 0;
        }
        return simState == 1 ? -1 : -2;
    }

    public static int getNSP(Context context) {
        if (getSimState(context) != 0) {
            return -1;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        String replaceAll = telephonyManager.getNetworkOperatorName().replaceAll(Operators.SPACE_STR, "");
        String networkOperator = telephonyManager.getNetworkOperator();
        TaoLog.d("NSP: ", "operator = " + replaceAll + "  type = " + networkOperator);
        if ((replaceAll != null && !"".equals("")) || networkOperator == null) {
            networkOperator = replaceAll;
        }
        if (networkOperator == null || networkOperator.length() == 0) {
            return -2;
        }
        if (networkOperator.compareToIgnoreCase("中国移动") == 0 || networkOperator.compareToIgnoreCase("CMCC") == 0 || networkOperator.compareToIgnoreCase("ChinaMobile") == 0 || networkOperator.compareToIgnoreCase("46000") == 0) {
            return 1;
        }
        if (networkOperator.compareToIgnoreCase("中国电信") == 0 || networkOperator.compareToIgnoreCase("ChinaTelecom") == 0 || networkOperator.compareToIgnoreCase("46003") == 0 || networkOperator.compareToIgnoreCase("ChinaTelcom") == 0 || networkOperator.compareToIgnoreCase("460003") == 0) {
            return 3;
        }
        if (networkOperator.compareToIgnoreCase("中国联通") == 0 || networkOperator.compareToIgnoreCase("ChinaUnicom") == 0 || networkOperator.compareToIgnoreCase("46001") == 0 || networkOperator.compareToIgnoreCase("CU-GSM") == 0 || networkOperator.compareToIgnoreCase("CHN-CUGSM") == 0 || networkOperator.compareToIgnoreCase("CHNUnicom") == 0) {
            return 2;
        }
        String imsi = PhoneInfo.getImsi(context);
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            return 1;
        }
        if (imsi.startsWith("46001")) {
            return 2;
        }
        if (imsi.startsWith("46003")) {
            return 3;
        }
        return -2;
    }

    public static void unRegNetWorkRev(Context context) {
        setProxy((String) null, (String) null);
        try {
            if (connChangerRvr != null) {
                context.unregisterReceiver(connChangerRvr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setProxy(String str, String str2) {
        if (str == null || str.length() == 0) {
            System.getProperties().put("proxySet", "false");
            proxy = false;
            return;
        }
        proxy = true;
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyHost", str);
        if (str2 == null || str2.length() <= 0) {
            System.getProperties().put("proxyPort", "80");
        } else {
            System.getProperties().put("proxyPort", str2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a6, code lost:
        if (r10 != null) goto L_0x00b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b6, code lost:
        if (r10 != null) goto L_0x00b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b8, code lost:
        r10.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bb, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00b1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.HashMap<java.lang.String, java.lang.String> getProxyInfo(android.content.Context r10, android.net.Uri r11) {
        /*
            java.lang.String r0 = getNetworkInfo(r10)
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            r2 = 0
            if (r0 != 0) goto L_0x000d
            return r2
        L_0x000d:
            java.lang.String r3 = "getProxyInfo"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "current network:"
            r4.append(r5)
            r4.append(r0)
            java.lang.String r4 = r4.toString()
            android.taobao.windvane.util.TaoLog.d(r3, r4)
            java.lang.String r3 = "WIFI"
            boolean r3 = r0.contains(r3)
            if (r3 != 0) goto L_0x00bc
            java.lang.String r3 = "MOBILE UMTS"
            int r3 = r0.compareToIgnoreCase(r3)
            if (r3 != 0) goto L_0x0035
            goto L_0x00bc
        L_0x0035:
            android.content.ContentResolver r4 = r10.getContentResolver()     // Catch:{ Exception -> 0x00b5, all -> 0x00ad }
            r6 = 0
            java.lang.String r7 = "mcc =?"
            r10 = 1
            java.lang.String[] r8 = new java.lang.String[r10]     // Catch:{ Exception -> 0x00b5, all -> 0x00ad }
            r10 = 0
            java.lang.String r3 = "460"
            r8[r10] = r3     // Catch:{ Exception -> 0x00b5, all -> 0x00ad }
            r9 = 0
            r5 = r11
            android.database.Cursor r10 = r4.query(r5, r6, r7, r8, r9)     // Catch:{ Exception -> 0x00b5, all -> 0x00ad }
            boolean r11 = r10.moveToFirst()     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            if (r11 == 0) goto L_0x00a6
        L_0x0050:
            int r11 = r10.getCount()     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            if (r11 <= 0) goto L_0x00a0
            java.lang.String r11 = "host"
            java.lang.String r3 = "proxy"
            int r3 = r10.getColumnIndex(r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r3 = r10.getString(r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            r1.put(r11, r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r11 = "port"
            java.lang.String r3 = "port"
            int r3 = r10.getColumnIndex(r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r3 = r10.getString(r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            r1.put(r11, r3)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r11 = "apn"
            int r11 = r10.getColumnIndex(r11)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r11 = r10.getString(r11)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r3 = "getProxyInfo"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            r4.<init>()     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r5 = "apn:"
            r4.append(r5)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            r4.append(r11)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            java.lang.String r4 = r4.toString()     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            android.taobao.windvane.util.TaoLog.d(r3, r4)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            boolean r11 = r0.contains(r11)     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            if (r11 == 0) goto L_0x00a0
            if (r10 == 0) goto L_0x009f
            r10.close()
        L_0x009f:
            return r1
        L_0x00a0:
            boolean r11 = r10.moveToNext()     // Catch:{ Exception -> 0x00ab, all -> 0x00a9 }
            if (r11 != 0) goto L_0x0050
        L_0x00a6:
            if (r10 == 0) goto L_0x00bb
            goto L_0x00b8
        L_0x00a9:
            r11 = move-exception
            goto L_0x00af
        L_0x00ab:
            goto L_0x00b6
        L_0x00ad:
            r11 = move-exception
            r10 = r2
        L_0x00af:
            if (r10 == 0) goto L_0x00b4
            r10.close()
        L_0x00b4:
            throw r11
        L_0x00b5:
            r10 = r2
        L_0x00b6:
            if (r10 == 0) goto L_0x00bb
        L_0x00b8:
            r10.close()
        L_0x00bb:
            return r2
        L_0x00bc:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.util.NetWork.getProxyInfo(android.content.Context, android.net.Uri):java.util.HashMap");
    }

    public static HttpHost getHttpsProxyInfo(Context context) {
        NetworkInfo networkInfo;
        if (Build.VERSION.SDK_INT < 11) {
            try {
                networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            } catch (Exception e) {
                e.printStackTrace();
                networkInfo = null;
            }
            if (networkInfo == null || !networkInfo.isAvailable() || networkInfo.getType() != 0) {
                return null;
            }
            String defaultHost = Proxy.getDefaultHost();
            int defaultPort = Proxy.getDefaultPort();
            if (defaultHost != null) {
                return new HttpHost(defaultHost, defaultPort);
            }
            return null;
        }
        String property = System.getProperty("https.proxyHost");
        String property2 = System.getProperty("https.proxyPort");
        if (!TextUtils.isEmpty(property)) {
            return new HttpHost(property, Integer.parseInt(property2));
        }
        return null;
    }

    public static String getNetConnType(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            TaoLog.w("Network", "can not get Context.CONNECTIVITY_SERVICE");
            return "none";
        }
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        if (networkInfo != null) {
            if (NetworkInfo.State.CONNECTED == networkInfo.getState()) {
                return "wifi";
            }
        } else {
            TaoLog.w("Network", "can not get ConnectivityManager.TYPE_WIFI");
        }
        NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(0);
        if (networkInfo2 != null) {
            if (NetworkInfo.State.CONNECTED == networkInfo2.getState()) {
                return CONN_TYPE_GPRS;
            }
            return "none";
        }
        TaoLog.w("Network", "can not get ConnectivityManager.TYPE_MOBILE");
        return "none";
    }

    public static Map<String, String> getNetWorkSubTypeMap(Context context) {
        NetworkInfo networkInfo;
        String str;
        HashMap hashMap = new HashMap(2);
        String str2 = "";
        try {
            networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Throwable th) {
            str2 = th.getMessage();
            networkInfo = null;
        }
        if (networkInfo == null) {
            hashMap.put("message", str2);
            hashMap.put("type", "NONE");
            return hashMap;
        } else if (networkInfo.getType() == 1) {
            hashMap.put("message", str2);
            hashMap.put("type", "WIFI");
            return hashMap;
        } else {
            switch (networkInfo.getSubtype()) {
                case 1:
                    str2 = "GPRS";
                    str = "2G";
                    break;
                case 2:
                    str2 = "EDGE";
                    str = "2G";
                    break;
                case 3:
                    str2 = "UMTS";
                    str = "3G";
                    break;
                case 4:
                    str2 = "CDMA";
                    str = "2G";
                    break;
                case 5:
                    str2 = "EVDO_0";
                    str = "3G";
                    break;
                case 6:
                    str2 = "EVDO_A";
                    str = "3G";
                    break;
                case 7:
                    str2 = "1xRTT";
                    str = "2G";
                    break;
                case 8:
                    str2 = "HSDPA";
                    str = "3G";
                    break;
                case 9:
                    str2 = "HSUPA";
                    str = "3G";
                    break;
                case 10:
                    str2 = "HSPA";
                    str = "3G";
                    break;
                case 11:
                    str2 = "IDEN";
                    str = "2G";
                    break;
                case 12:
                    str2 = "EVDO_B";
                    str = "3G";
                    break;
                case 13:
                    str2 = "LTE";
                    str = "4G";
                    break;
                case 14:
                    str2 = "EHRPD";
                    str = "3G";
                    break;
                case 15:
                    str2 = "HSPAP";
                    str = "3G";
                    break;
                default:
                    str = "UNKNOWN";
                    break;
            }
            hashMap.put("message", str2);
            hashMap.put("type", str);
            return hashMap;
        }
    }

    public static boolean isWiFiActive() {
        return isWiFiActive(GlobalConfig.context);
    }

    public static boolean isConnectionInexpensive() {
        return isWiFiActive() || isEthernetActive(GlobalConfig.context);
    }

    public static boolean isEthernetActive(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return getNetworkType(context) == 9;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean isWiFiActive(Context context) {
        if (context == null) {
            return false;
        }
        try {
            return getNetworkType(context) == 1;
        } catch (Exception unused) {
            return false;
        }
    }
}
