package com.taobao.accs.ut.statistics;

import com.alimama.moon.dao.SettingManager;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import com.taobao.weex.common.Constants;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class BindUserStatistic implements UTInterface {
    private static final String TAG = "accs.BindUserStatistic";
    private final String PAGE_NAME = "BindUser";
    public String deviceId;
    public String failReason;
    private boolean isCommitted = false;
    public boolean ret;
    public String time;
    public String userId;

    public void setFailReason(String str) {
        this.failReason = str;
    }

    public void setFailReason(int i) {
        if (i == 200) {
            return;
        }
        if (i != 300) {
            switch (i) {
                case -4:
                    setFailReason("msg too large");
                    return;
                case -3:
                    setFailReason("service not available");
                    return;
                case -2:
                    setFailReason("param error");
                    return;
                case -1:
                    setFailReason("network fail");
                    return;
                default:
                    setFailReason(String.valueOf(i));
                    return;
            }
        } else {
            setFailReason("app not bind");
        }
    }

    public void commitUT() {
        commit("BindUser");
    }

    private void commit(String str) {
        String str2;
        String str3;
        if (!this.isCommitted) {
            this.isCommitted = true;
            HashMap hashMap = new HashMap();
            try {
                str3 = this.deviceId;
                try {
                    str2 = String.valueOf(Constants.SDK_VERSION_CODE);
                } catch (Throwable th) {
                    th = th;
                    str2 = null;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str3, (String) null, str2, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
                try {
                    hashMap.put("device_id", this.deviceId);
                    hashMap.put("bind_date", this.time);
                    hashMap.put("ret", this.ret ? Constants.Name.Y : "n");
                    hashMap.put("fail_reasons", this.failReason);
                    hashMap.put(SettingManager.USERID, this.userId);
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        ALog.d(TAG, UTMini.getCommitInfo(66001, str3, (String) null, str2, (Map<String, String>) hashMap), new Object[0]);
                    }
                    UTMini.getInstance().commitEvent(66001, str, (Object) str3, (Object) null, (Object) str2, (Map<String, String>) hashMap);
                } catch (Throwable th2) {
                    th = th2;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str3, (String) null, str2, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
            } catch (Throwable th3) {
                th = th3;
                str3 = null;
                str2 = null;
                ALog.d(TAG, UTMini.getCommitInfo(66001, str3, (String) null, str2, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
            }
        }
    }
}
