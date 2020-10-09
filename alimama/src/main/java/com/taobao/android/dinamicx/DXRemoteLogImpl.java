package com.taobao.android.dinamicx;

import com.taobao.android.AliLogInterface;
import com.taobao.android.AliLogServiceFetcher;
import com.taobao.android.dinamicx.log.IDXRemoteDebugLog;

public class DXRemoteLogImpl implements IDXRemoteDebugLog {
    private AliLogInterface logService = AliLogServiceFetcher.getLogService();

    public void logv(String str, String... strArr) {
        if (this.logService != null) {
            this.logService.logv(str, strArr);
        }
    }

    public void logd(String str, String... strArr) {
        if (this.logService != null) {
            this.logService.logd(str, strArr);
        }
    }

    public void logi(String str, String... strArr) {
        if (this.logService != null) {
            this.logService.logi(str, strArr);
        }
    }

    public void logw(String str, String... strArr) {
        if (this.logService != null) {
            this.logService.logw(str, strArr);
        }
    }

    public void loge(String str, String... strArr) {
        if (this.logService != null) {
            this.logService.loge(str, strArr);
        }
    }

    public void logv(String str, String str2) {
        if (this.logService != null) {
            this.logService.logv(str, str2);
        }
    }

    public void logd(String str, String str2) {
        if (this.logService != null) {
            this.logService.logd(str, str2);
        }
    }

    public void logi(String str, String str2) {
        if (this.logService != null) {
            this.logService.logi(str, str2);
        }
    }

    public void logw(String str, String str2) {
        if (this.logService != null) {
            this.logService.logw(str, str2);
        }
    }

    public void loge(String str, String str2) {
        if (this.logService != null) {
            this.logService.loge(str, str2);
        }
    }

    public void logv(String str, String str2, String str3) {
        if (this.logService != null) {
            this.logService.logv(str, str2, str3);
        }
    }

    public void logd(String str, String str2, String str3) {
        if (this.logService != null) {
            this.logService.logd(str, str2, str3);
        }
    }

    public void logi(String str, String str2, String str3) {
        if (this.logService != null) {
            this.logService.logi(str, str2, str3);
        }
    }

    public void logw(String str, String str2, String str3) {
        if (this.logService != null) {
            this.logService.logw(str, str2, str3);
        }
    }

    public void loge(String str, String str2, String str3) {
        if (this.logService != null) {
            this.logService.loge(str, str2, str3);
        }
    }

    public void logw(String str, String str2, Throwable th) {
        if (this.logService != null) {
            this.logService.logw(str, str2, th);
        }
    }

    public void loge(String str, String str2, Throwable th) {
        if (this.logService != null) {
            this.logService.loge(str, str2, th);
        }
    }
}
