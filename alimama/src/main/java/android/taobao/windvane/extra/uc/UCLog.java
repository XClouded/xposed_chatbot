package android.taobao.windvane.extra.uc;

import android.taobao.windvane.util.TaoLog;

import com.uc.webview.export.internal.interfaces.INetLogger;

public class UCLog implements INetLogger {
    private static final int LEVEL_ERROR = 0;
    int level = 0;
    boolean mEnable = true;

    public void i(String str, String str2) {
    }

    public void w(String str, String str2) {
    }

    public void setLogLevel(int i) {
        this.level = i;
    }

    public int getLogLevel() {
        return this.level;
    }

    public void setEnable(boolean z) {
        this.mEnable = z;
    }

    public boolean getEnable() {
        return this.mEnable;
    }

    public void e(String str, String str2) {
        if ("cancel_log".equals(str)) {
            TaoLog.e("alinetwork", "cancel reason:" + str2);
        }
    }

    public void d(String str, String str2) {
        if ("cancel_log".equals(str)) {
            TaoLog.e("alinetwork", "cancel reason:" + str2);
        }
    }
}
