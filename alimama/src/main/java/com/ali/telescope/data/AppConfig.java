package com.ali.telescope.data;

import android.text.TextUtils;
import com.ali.telescope.data.beans.AppParam;
import java.util.ArrayList;

public class AppConfig {
    public static String appKey;
    public static ArrayList<String> bootActivityNameList = new ArrayList<>();
    public static String channel = null;
    public static String imei = null;
    public static String imsi = null;
    public static Boolean isAliyunos = false;
    public static String packageName;
    public static String utdid;
    public static String versionName;

    public static void init(AppParam appParam) {
        if (!TextUtils.isEmpty(appParam.appKey)) {
            appKey = appParam.appKey;
            versionName = appParam.versionName;
            packageName = appParam.packageName;
            isAliyunos = appParam.isAliyunos;
            utdid = appParam.utdid;
            imsi = AppParam.imsi;
            imei = AppParam.imei;
            channel = AppParam.channel;
            return;
        }
        throw new RuntimeException("AppParam Error : appKey is necessary!");
    }
}
