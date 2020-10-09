package com.taobao.orange;

import android.content.Context;
import com.taobao.orange.OConstant;
import com.taobao.orange.impl.HurlNetConnection;
import com.taobao.orange.impl.TBNetConnection;
import com.taobao.orange.inner.INetConnection;
import com.taobao.orange.util.OLog;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalOrange {
    static final String TAG = "GlobalOrange";
    public static String ackHost = null;
    public static Set<String> ackVips = Collections.synchronizedSet(new HashSet());
    public static String appKey = "";
    public static String appSecret = "";
    public static String appVersion = "";
    public static String authCode = "";
    public static Context context = null;
    public static String dcHost = null;
    public static Set<String> dcVips = Collections.synchronizedSet(new HashSet());
    public static String deviceId = "";
    public static volatile int downgrade = 0;
    public static OConstant.ENV env = OConstant.ENV.ONLINE;
    static volatile boolean fallbackAvoid = false;
    public static AtomicInteger indexContinueFailsNum = new AtomicInteger(0);
    public static volatile OConstant.UPDMODE indexUpdMode = OConstant.UPDMODE.O_XMD;
    public static volatile boolean isMainProcess = true;
    public static volatile boolean isTaobaoPackage = false;
    public static Class<? extends INetConnection> netConnection;
    public static volatile Set<String> probeHosts = Collections.synchronizedSet(new HashSet());
    public static volatile long randomDelayAckInterval = 10;
    public static volatile boolean reportUpdateAck = false;
    public static volatile String reqOrangeHeader;
    public static volatile int reqRetryNum = 3;
    public static volatile long reqTimestampOffset = 0;
    public static volatile String schema = "https";
    public static boolean statUsedConfig = false;
    public static String userId = "";

    static {
        try {
            Class.forName(OConstant.REFLECT_NETWORKSDK);
            netConnection = TBNetConnection.class;
        } catch (ClassNotFoundException unused) {
            netConnection = HurlNetConnection.class;
            OLog.w(TAG, "init not found networksdk", new Object[0]);
        }
    }
}
