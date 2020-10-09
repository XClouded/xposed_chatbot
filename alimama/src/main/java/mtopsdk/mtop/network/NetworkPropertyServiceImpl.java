package mtopsdk.mtop.network;

import anet.channel.GlobalAppRuntimeInfo;
import mtopsdk.common.util.TBSdkLog;

public class NetworkPropertyServiceImpl implements NetworkPropertyService {
    private static final String TAG = "mtopsdk.NetworkPropertyServiceImpl";

    public void setUserId(String str) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setUserId] set NetworkProperty UserId =" + str);
        }
        GlobalAppRuntimeInfo.setUserId(str);
    }

    public void setTtid(String str) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, "[setTtid] set NetworkProperty ttid =" + str);
        }
        GlobalAppRuntimeInfo.setTtid(str);
    }
}
