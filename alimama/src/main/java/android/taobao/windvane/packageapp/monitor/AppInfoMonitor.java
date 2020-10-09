package android.taobao.windvane.packageapp.monitor;

import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.util.NetWork;
import android.text.TextUtils;

import java.util.Hashtable;
import java.util.Map;

public class AppInfoMonitor {
    private static boolean isFirstTime = true;
    private static Map<String, UtData> map = new Hashtable();
    private static long startTime = 0;

    public static void start(String str, int i) {
        UtData utData = new UtData();
        utData.download_start = System.currentTimeMillis();
        utData.update_type = i;
        if (!map.containsKey(str)) {
            utData.is_wifi = NetWork.isWiFiActive();
            utData.update_start_time = utData.download_start;
        }
        map.put(str, utData);
        if (isFirstTime) {
            startTime = System.currentTimeMillis() - WVPackageAppManager.getInstance().pkgInitTime;
        }
    }

    public static void download(String str) {
        UtData utData = map.get(str);
        if (utData != null) {
            utData.download_end = System.currentTimeMillis();
        }
    }

    public static void success(ZipAppInfo zipAppInfo) {
        UtData utData = map.get(zipAppInfo.getNameandVersion());
        if (utData != null) {
            utData.operate_end = System.currentTimeMillis();
            utData.success = true;
            upload(zipAppInfo, utData);
        }
    }

    public static void error(ZipAppInfo zipAppInfo, int i, String str) {
        UtData utData = map.get(zipAppInfo.getNameandVersion());
        if (utData != null) {
            utData.operate_end = System.currentTimeMillis();
            utData.success = false;
            utData.error_type = i;
            utData.error_message = str;
            upload(zipAppInfo, utData);
        }
        if (zipAppInfo.isInstantApp) {
            WVEventService.getInstance().onInstantEvent(6007, zipAppInfo.getZipUrl(), str, zipAppInfo.name);
            return;
        }
        WVEventService.getInstance().onEvent(6007, zipAppInfo.getZipUrl(), str, zipAppInfo.name);
    }

    public static void upload(ZipAppInfo zipAppInfo, UtData utData) {
        UtData utData2 = utData;
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            if (isFirstTime) {
                WVMonitorService.getPackageMonitorInterface().commitPackageUpdateStartInfo(startTime, System.currentTimeMillis() - WVPackageAppManager.getInstance().pkgInitTime);
                isFirstTime = false;
            }
            String nameandVersion = zipAppInfo.getNameandVersion();
            int indexOf = nameandVersion.indexOf(95);
            String substring = nameandVersion.substring(0, indexOf);
            String substring2 = nameandVersion.substring(indexOf + 1);
            String str = nameandVersion;
            WVMonitorService.getPackageMonitorInterface().packageApp(zipAppInfo, substring, substring2, String.valueOf(utData2.update_type), utData2.success, utData2.operate_end - utData2.download_start, utData2.download_end - utData2.download_start, utData2.error_type, utData2.error_message, utData2.is_wifi, utData2.update_start_time);
            if (!TextUtils.isEmpty(str) && map != null) {
                map.remove(str);
            }
        }
    }

    private static class UtData {
        public long download_end;
        public long download_start;
        public String error_message;
        public int error_type;
        public boolean is_wifi;
        public long operate_end;
        public boolean success;
        public long update_start_time;
        public int update_type;

        private UtData() {
        }
    }
}
