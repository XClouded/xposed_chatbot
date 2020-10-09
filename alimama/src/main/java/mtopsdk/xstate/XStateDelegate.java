package mtopsdk.xstate;

import android.content.Context;
import android.content.IntentFilter;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.xstate.network.NetworkStateReceiver;

public class XStateDelegate {
    private static final String TAG = "mtopsdk.XStateDelegate";
    private static Context context = null;
    private static volatile boolean isInit = false;
    private static NetworkStateReceiver netReceiver;
    private static ConcurrentHashMap<String, String> stateIDs;

    public static Context getContext() {
        return context;
    }

    private static void checkInit(Context context2) {
        try {
            if (isInit) {
                return;
            }
            if (context2 == null) {
                TBSdkLog.e(TAG, "[checkInit]parameter context for init(Context context) is null.");
                return;
            }
            if (stateIDs == null) {
                stateIDs = new ConcurrentHashMap<>();
            }
            context = context2;
            if (netReceiver == null) {
                netReceiver = new NetworkStateReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                context2.registerReceiver(netReceiver, intentFilter);
            }
            isInit = true;
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, "[checkInit] init XState OK,isInit=" + isInit);
            }
        } catch (Throwable th) {
            TBSdkLog.e(TAG, "[checkInit] checkInit error --" + th.toString());
        }
    }

    public static void init(Context context2) {
        if (!isInit) {
            checkInit(context2);
        }
    }

    public static void unInit() {
        if (isInit) {
            try {
                if (isInit) {
                    if (stateIDs != null) {
                        stateIDs.clear();
                        stateIDs = null;
                    }
                    if (context == null) {
                        TBSdkLog.e(TAG, "[unInit] context in Class XState is null.");
                        return;
                    }
                    try {
                        if (netReceiver != null) {
                            context.unregisterReceiver(netReceiver);
                            netReceiver = null;
                        }
                    } catch (Throwable th) {
                        TBSdkLog.e(TAG, "[unRegisterReceive]unRegisterReceive failed", th);
                    }
                    isInit = false;
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "[unInit] unInit XState OK,isInit=" + isInit);
                    }
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[unInit] unInit error --" + e.toString());
            }
        }
    }

    public static String getValue(String str) {
        if (stateIDs == null || str == null) {
            return null;
        }
        return stateIDs.get(str);
    }

    public static void setValue(String str, String str2) {
        if (stateIDs != null && str != null && str2 != null) {
            stateIDs.put(str, str2);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, "[setValue]set  XStateID succeed," + str + SymbolExpUtil.SYMBOL_EQUAL + str2);
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[setValue]set  XStateID failed,key=" + str + ",value=" + str2);
        }
    }

    public static String removeKey(String str) {
        if (stateIDs == null || str == null) {
            return null;
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "remove XState key=" + str);
        }
        return stateIDs.remove(str);
    }
}
