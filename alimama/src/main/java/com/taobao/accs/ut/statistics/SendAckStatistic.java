package com.taobao.accs.ut.statistics;

import com.ali.user.mobile.register.RegistConstants;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class SendAckStatistic implements UTInterface {
    private static final String TAG = "accs.SendAckStatistic";
    private final String PAGE_NAME = "sendAck";
    public String dataId;
    public String deviceId;
    public String failReason;
    private boolean isCommitted = false;
    public String sendTime;
    public String serviceId;
    public String sessionId;

    public void commitUT() {
        String str;
        String str2;
        if (!this.isCommitted) {
            this.isCommitted = true;
            HashMap hashMap = new HashMap();
            try {
                str2 = this.deviceId;
                try {
                    str = String.valueOf(Constants.SDK_VERSION_CODE);
                    try {
                        hashMap.put("device_id", this.deviceId);
                        hashMap.put(RegistConstants.REGISTER_SESSION_ID, this.sessionId);
                        hashMap.put("data_id", this.dataId);
                        hashMap.put("ack_date", this.sendTime);
                        hashMap.put("service_id", this.serviceId);
                        hashMap.put("fail_reasons", this.failReason);
                        UTMini.getInstance().commitEvent(66001, "sendAck", (Object) str2, (Object) null, (Object) str, (Map<String, String>) hashMap);
                    } catch (Throwable th) {
                        th = th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    str = null;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str2, (String) null, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
            } catch (Throwable th3) {
                th = th3;
                str2 = null;
                str = null;
                ALog.d(TAG, UTMini.getCommitInfo(66001, str2, (String) null, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
            }
        }
    }
}
