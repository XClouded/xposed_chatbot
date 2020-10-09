package com.taobao.accs.ut.statistics;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UTMini;
import com.taobao.weex.el.parse.Operators;
import java.util.HashMap;
import java.util.Map;

public class MonitorStatistic implements UTInterface {
    private static final long COMMIT_INTERVAL = 1200000;
    private static final String PAGE = "MONITOR";
    private static final String TAG = "MonitorStatistic";
    public int connType;
    private long lastCommitTime = 0;
    public int messageNum = 0;
    public boolean networkAvailable;
    public String proxy;
    public long startServiceTime;
    public int status;
    public boolean tcpConnected = false;
    public boolean threadIsalive;
    public int unHandleMessageNum = 0;
    public String url;

    public void commitUT() {
        String str;
        String str2;
        String str3;
        long currentTimeMillis = System.currentTimeMillis();
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "commitUT interval:" + (currentTimeMillis - this.lastCommitTime) + " interval1:" + (currentTimeMillis - this.startServiceTime), new Object[0]);
        }
        if (currentTimeMillis - this.lastCommitTime > COMMIT_INTERVAL && currentTimeMillis - this.startServiceTime > 60000) {
            HashMap hashMap = new HashMap();
            try {
                str3 = String.valueOf(this.messageNum);
                try {
                    str2 = String.valueOf(this.unHandleMessageNum);
                } catch (Throwable th) {
                    th = th;
                    str2 = null;
                    str = str2;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str3, str2, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
                try {
                    str = String.valueOf(Constants.SDK_VERSION_CODE);
                } catch (Throwable th2) {
                    th = th2;
                    str = null;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str3, str2, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
                try {
                    hashMap.put("connStatus", String.valueOf(this.status));
                    hashMap.put("connType", String.valueOf(this.connType));
                    hashMap.put("tcpConnected", String.valueOf(this.tcpConnected));
                    hashMap.put("proxy", String.valueOf(this.proxy));
                    hashMap.put("startServiceTime", String.valueOf(this.startServiceTime));
                    hashMap.put("commitTime", String.valueOf(currentTimeMillis));
                    hashMap.put("networkAvailable", String.valueOf(this.networkAvailable));
                    hashMap.put("threadIsalive", String.valueOf(this.threadIsalive));
                    hashMap.put("url", this.url);
                    if (ALog.isPrintLog(ALog.Level.D)) {
                        ALog.d(TAG, UTMini.getCommitInfo(66001, str3, str2, str, (Map<String, String>) hashMap), new Object[0]);
                    }
                    UTMini.getInstance().commitEvent(66001, PAGE, (Object) str3, (Object) str2, (Object) str, (Map<String, String>) hashMap);
                    this.lastCommitTime = currentTimeMillis;
                } catch (Throwable th3) {
                    th = th3;
                    ALog.d(TAG, UTMini.getCommitInfo(66001, str3, str2, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
                }
            } catch (Throwable th4) {
                th = th4;
                str3 = null;
                str2 = null;
                str = str2;
                ALog.d(TAG, UTMini.getCommitInfo(66001, str3, str2, str, (Map<String, String>) hashMap) + Operators.SPACE_STR + th.toString(), new Object[0]);
            }
        }
    }
}
