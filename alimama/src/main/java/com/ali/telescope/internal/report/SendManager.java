package com.ali.telescope.internal.report;

import android.content.Context;
import android.util.Log;
import com.ali.telescope.interfaces.TelescopeEventData;
import com.alibaba.motu.tbrest.SendService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendManager {
    public static byte SEND_TOOL_SWITCH = 1;
    private static List<TelescopeEventData> dataListeners = new ArrayList();
    private static long s_session_start_timestamp = System.currentTimeMillis();

    public static void initRestAPI(Context context) {
    }

    public static void addListener(TelescopeEventData telescopeEventData) {
        if (telescopeEventData != null) {
            dataListeners.add(telescopeEventData);
        }
    }

    public static boolean sendReport(String str) {
        if (SEND_TOOL_SWITCH == 0) {
            Log.w("Telescope.SendManager", "send report error,us send tool switch is 0");
            return false;
        } else if (SEND_TOOL_SWITCH != 1) {
            return false;
        } else {
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            Integer num = 61004;
            Boolean sendRequest = SendService.getInstance().sendRequest((String) null, valueOf.longValue(), "ALIHA", num.intValue(), "telescope", str, (Object) null, (Map<String, String>) null);
            if (sendRequest.booleanValue()) {
                for (TelescopeEventData onListener : dataListeners) {
                    onListener.onListener(valueOf.longValue(), "ALIHA", num.intValue(), "telescope", str, (Object) null, (Map<String, String>) null);
                }
            }
            return sendRequest.booleanValue();
        }
    }
}
