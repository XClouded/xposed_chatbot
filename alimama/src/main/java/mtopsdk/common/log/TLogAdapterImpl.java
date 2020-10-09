package mtopsdk.common.log;

import com.taobao.tlog.adapter.AdapterForTLog;

public class TLogAdapterImpl implements LogAdapter {
    public String getLogLevel() {
        return AdapterForTLog.getLogLevel();
    }

    public void traceLog(String str, String str2) {
        AdapterForTLog.traceLog(str, str2);
    }

    public void printLog(int i, String str, String str2, Throwable th) {
        if (i == 4) {
            AdapterForTLog.logi(str, str2);
        } else if (i == 8) {
            AdapterForTLog.logw(str, str2, th);
        } else if (i != 16) {
            switch (i) {
                case 1:
                    AdapterForTLog.logv(str, str2);
                    return;
                case 2:
                    AdapterForTLog.logd(str, str2);
                    return;
                default:
                    return;
            }
        } else {
            loge(str, str2, th);
        }
    }

    private void loge(String str, String str2, Throwable th) {
        if (th == null) {
            AdapterForTLog.loge(str, str2);
        } else {
            AdapterForTLog.loge(str, str2, th);
        }
    }
}
