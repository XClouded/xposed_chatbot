package com.ali.ha.fulltrace;

import com.ali.ha.fulltrace.logger.Logger;
import com.alibaba.motu.tbrest.SendService;
import java.util.Map;

public class SendManager {
    private static final String TAG = "SendManager";
    private static final String arg1 = "AliHA";
    private static final Integer eventId = 61004;
    private static final String host = null;

    public static boolean send(String str, String str2) {
        try {
            Boolean sendRequest = SendService.getInstance().sendRequest(host, System.currentTimeMillis(), (String) null, eventId.intValue(), arg1, str2, str, (Map<String, String>) null);
            if (sendRequest.booleanValue()) {
                Logger.i(TAG, "send success");
            } else {
                Logger.w(TAG, "send failure");
            }
            return sendRequest.booleanValue();
        } catch (Throwable th) {
            Logger.throwException(th);
            return false;
        }
    }
}
