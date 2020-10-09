package android.taobao.windvane.packageapp.monitor;

import android.taobao.windvane.util.TaoLog;

public class GlobalInfoMonitor {
    public static void error(int i, String str) {
        TaoLog.e("WVPackageApp", "failed to install app. error_type : " + i + " error_message : " + str);
    }
}
