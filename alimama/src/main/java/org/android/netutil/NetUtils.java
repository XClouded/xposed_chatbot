package org.android.netutil;

import androidx.annotation.Keep;
import java.util.concurrent.Future;

@Keep
public class NetUtils {
    private static native long native_CreateAndRegister(long j, NetListener netListener);

    private static native String native_GetDefaultGateway(String str);

    private static native void native_UnRegister(long j);

    public static boolean registerNetListener(NetListener netListener) {
        if (netListener == null) {
            return false;
        }
        netListener.native_ptr = native_CreateAndRegister(netListener.netListenerType.getValue(), netListener);
        return netListener.native_ptr != 0;
    }

    public static void unRegisterNetListener(NetListener netListener) {
        if (netListener != null && netListener.native_ptr != 0) {
            native_UnRegister(netListener.native_ptr);
        }
    }

    public static String getDefaultGateway(String str) {
        return native_GetDefaultGateway(str);
    }

    public static String getPreferNextHop(String str) {
        return getPreferNextHop(str, 1);
    }

    public static String getPreferNextHop(String str, int i) {
        Future<PingResponse> launch = new PingTask(str, 0, 1, 0, i).launch();
        if (launch == null) {
            return null;
        }
        try {
            PingResponse pingResponse = launch.get();
            if (pingResponse != null) {
                return pingResponse.getLastHopIPStr();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
