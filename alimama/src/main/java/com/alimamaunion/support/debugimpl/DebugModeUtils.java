package com.alimamaunion.support.debugimpl;

import android.app.Activity;
import android.app.Application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebugModeUtils {
    private static String MTOP_DOWNGRADE = "MTOP_DOWNGRADE";
    private static String MTOP_ISOPEN = "IS_OPEN";
    private static List<Map<String, String>> deviceInfoListWrapper = new ArrayList();
    private static Map<String, String> deviceInfoMap = new HashMap();
    private static boolean isSwitch = true;

    public static void MtopDownGrade(Application application, boolean z) {
    }

    public static void getConfigInfo(Application application, Activity activity) {
    }

    public static void getConfigInfo(Application application, String str) {
    }

    public static boolean mtopDegrate(Application application) {
        return false;
    }

    public static void netMock(Activity activity, String str) {
    }

    public static void openChromeDebug(Activity activity) {
    }

    public static boolean setMtopDownGrade(boolean z) {
        return false;
    }

    public static void showCrashStack(Activity activity) {
    }

    public static void showDeviceInfo(Application application, Activity activity) {
    }

    private static void showDialog(List<Map<String, String>> list, Activity activity) {
    }

    public static void showUserInfo(Activity activity) {
    }

    public static List<Map<String, String>> getDeviceInfo(Application application) {
        return new ArrayList();
    }

    public static List<Map<String, String>> getUserInfo() {
        return new ArrayList();
    }
}
