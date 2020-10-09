package com.taobao.accs.ut.statistics;

import com.alimama.moon.dao.SettingManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import com.taobao.android.dinamicx.bindingx.DXBindingXConstant;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class ReceiveMsgStat implements UTInterface {
    private static final String TAG = "ReceiveMessage";
    private final String PAGE_NAME = "receiveMessage";
    public String dataId;
    public String dataLen;
    public String deviceId;
    private boolean isCommitted = false;
    public String messageType;
    public String receiveDate;
    public boolean repeat = false;
    public String serviceId;
    public String toBzDate;
    public String userId;

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
                } catch (Throwable th) {
                    th = th;
                    str = null;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str2, (String) null, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
                try {
                    hashMap.put("device_id", this.deviceId);
                    hashMap.put("data_id", this.dataId);
                    hashMap.put("receive_date", this.receiveDate);
                    hashMap.put("to_bz_date", this.toBzDate);
                    hashMap.put("service_id", this.serviceId);
                    hashMap.put("data_length", this.dataLen);
                    hashMap.put("msg_type", this.messageType);
                    hashMap.put(DXBindingXConstant.REPEAT, this.repeat ? Constants.Name.Y : "n");
                    hashMap.put(SettingManager.USERID, this.userId);
                    UTMini.getInstance().commitEvent(66001, "receiveMessage", (Object) str2, (Object) null, (Object) str, (Map<String, String>) hashMap);
                } catch (Throwable th2) {
                    th = th2;
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
