package com.taobao.uikit.extend.component.error;

import androidx.annotation.NonNull;
import mtopsdk.mtop.util.ErrorConstant;

public abstract class AbsErrorFilter {
    static final String KEY_ERRORVIEW_LIMIT_ERROR_SUBTITLE = "errorview_limit_error_subtitle";
    static final String KEY_ERRORVIEW_LIMIT_ERROR_TITLE = "errorview_limit_error_title";
    static final String KEY_ERRORVIEW_NETWORKERROR_SUBTITLE = "errorview_networkerror_subtitle";
    static final String KEY_ERRORVIEW_NETWORKERROR_TITLE = "errorview_networkerror_title";
    static final String KEY_ERRORVIEW_SYS_ERROR_SUBTITLE = "errorview_sys_error_subtitle";
    static final String KEY_ERRORVIEW_SYS_ERROR_TITLE = "errorview_sys_error_title";

    public abstract int filterIcon(@NonNull Error error);

    public abstract String filterSubTitle(@NonNull Error error, CharSequence charSequence);

    public abstract String filterTitle(@NonNull Error error, CharSequence charSequence);

    /* access modifiers changed from: protected */
    public boolean isHttpError(int i) {
        return i == 204 || i == 302 || i == 400 || i == 401 || i == 403 || i == 404 || i == 502 || i == 503 || i == 504 || i == 500 || i == 499 || i == 599;
    }

    /* access modifiers changed from: protected */
    public boolean isNetworkError(@NonNull String str) {
        return str.equals(ErrorConstant.ERRCODE_NO_NETWORK) || str.equals("ANDROID_SYS_NETWORK_ERROR");
    }

    /* access modifiers changed from: protected */
    public boolean isSysError(int i, @NonNull String str) {
        return str.startsWith("FAIL_SYS") || str.equals("FAIL") || str.equals("UNKNOWN_ERROR") || str.equals(ErrorConstant.ERRCODE_SYSTEM_ERROR) || str.equals("UNKNOWN_FAIL_CODE") || str.startsWith("ANDROID_SYS") || isHttpError(i);
    }

    /* access modifiers changed from: protected */
    public boolean isLimitError(int i, String str) {
        return i == 419 || i == 420 || isLimitErrorByRetCode(str);
    }

    private boolean isLimitErrorByRetCode(String str) {
        return ErrorConstant.ERRCODE_API_FLOW_LIMIT_LOCKED.equals(str);
    }
}
