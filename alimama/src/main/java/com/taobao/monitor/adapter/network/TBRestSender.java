package com.taobao.monitor.adapter.network;

import com.alibaba.motu.tbrest.SendService;
import com.taobao.monitor.adapter.common.TBAPMConstants;
import com.taobao.monitor.common.ThreadUtils;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.network.INetworkSender;
import java.util.List;
import java.util.Map;

public class TBRestSender implements INetworkSender {
    private static final String SPLIT = "HA_APM_______HA_APM";
    private static final String TAG = "TBRestSender";
    private static final int TRY_COUNT = 2;
    private final String arg1 = "AliHAMonitor";
    private final Integer eventId = 61004;
    /* access modifiers changed from: private */
    public boolean hasDiskData = true;
    private final String host = null;
    private ILiteDb senderDb = new SenderLiteDb();

    public void send(final String str, final String str2) {
        if (TBAPMConstants.needUpdateData) {
            DataLoggerUtils.log(TAG, str, str2);
            ThreadUtils.start(new Runnable() {
                public void run() {
                    try {
                        Logger.i(TBRestSender.TAG, str2);
                        int i = 0;
                        boolean z = false;
                        while (true) {
                            int i2 = i + 1;
                            if (i >= 2) {
                                break;
                            }
                            z = TBRestSender.this.sendApmContent(str, str2);
                            if (z) {
                                Logger.i(TBRestSender.TAG, "send success" + i2);
                                break;
                            }
                            i = i2;
                        }
                        if (!z) {
                            TBRestSender.this.save2Disk(str, str2);
                            boolean unused = TBRestSender.this.hasDiskData = true;
                        }
                        if (z && TBRestSender.this.hasDiskData) {
                            TBRestSender.this.tryLastFailedSend();
                            boolean unused2 = TBRestSender.this.hasDiskData = false;
                        }
                    } catch (Throwable th) {
                        Logger.throwException(th);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public boolean sendApmContent(String str, String str2) {
        return SendService.getInstance().sendRequest(this.host, System.currentTimeMillis(), (String) null, this.eventId.intValue(), "AliHAMonitor", str2, str, (Map<String, String>) null).booleanValue();
    }

    /* access modifiers changed from: private */
    public void tryLastFailedSend() {
        List<String> select = this.senderDb.select();
        if (select != null) {
            for (String next : select) {
                if (next != null) {
                    String[] split = next.split(SPLIT);
                    if (split.length >= 2) {
                        sendApmContent(split[0], split[1]);
                    }
                }
            }
        }
        this.senderDb.delete();
    }

    /* access modifiers changed from: private */
    public void save2Disk(String str, String str2) {
        ILiteDb iLiteDb = this.senderDb;
        iLiteDb.insert(str + SPLIT + str2);
    }
}
