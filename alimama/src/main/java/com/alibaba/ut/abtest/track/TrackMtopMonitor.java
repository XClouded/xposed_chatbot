package com.alibaba.ut.abtest.track;

import android.text.TextUtils;
import com.alibaba.ut.abtest.UTABTest;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import java.util.HashMap;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.mtop.stat.IMtopMonitor;

public class TrackMtopMonitor implements IMtopMonitor {
    private static final String TAG = "TrackMtopMonitor";

    public void onCommit(String str, HashMap<String, String> hashMap) {
        LogUtils.logDAndReport(TAG, "接收到MTOP响应信息, type=" + str + ", data=" + hashMap);
        try {
            if (TextUtils.equals(IMtopMonitor.MtopMonitorType.TYPE_RESPONSE, str) && hashMap != null) {
                String str2 = hashMap.get(HttpHeaderConstant.X_AB);
                if (!TextUtils.isEmpty(str2)) {
                    UTABTest.activateServer(str2);
                    return;
                }
                String str3 = hashMap.get("mtop-x-ali-ab");
                if (!TextUtils.isEmpty(str3)) {
                    UTABTest.activateServer(str3);
                }
            }
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }
}
