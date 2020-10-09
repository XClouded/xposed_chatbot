package com.alibaba.ha.adapter.service.telescope;

import com.ali.telescope.api.Telescope;
import com.ali.telescope.data.AppConfig;
import com.ali.telescope.interfaces.OnAccurateBootListener;
import com.ali.telescope.interfaces.TelescopeErrReporter;
import com.ali.telescope.interfaces.TelescopeEventData;
import com.taobao.onlinemonitor.OnLineMonitorApp;
import java.lang.reflect.Method;

public class TelescopeService {
    public static Method mAnetworkEnd = null;
    public static Method mAnetworkStart = null;
    public static boolean sIsInTaobao = true;
    public static boolean sSdCardEnable;

    public void setBootPath(String[] strArr, long j) {
        if (strArr != null) {
            for (String split : strArr) {
                String[] split2 = split.split("\\.");
                int length = split2.length;
                if (length > 1) {
                    addBootActivityName(split2[length - 1]);
                }
            }
        }
        OnLineMonitorApp.setBootInfo(strArr, j);
    }

    private void addBootActivityName(String str) {
        AppConfig.bootActivityNameList.add(str);
    }

    public void addOnAccurateBootListener(OnAccurateBootListener onAccurateBootListener) {
        Telescope.addOnAccurateBootListener(onAccurateBootListener);
    }

    public void addTelescopeErrorReporter(TelescopeErrReporter telescopeErrReporter) {
        Telescope.addTelescopeErrorReporter(telescopeErrReporter);
    }

    public void addTelescopeDataListener(TelescopeEventData telescopeEventData) {
        Telescope.addTelescopeEventDataListener(telescopeEventData);
    }
}
