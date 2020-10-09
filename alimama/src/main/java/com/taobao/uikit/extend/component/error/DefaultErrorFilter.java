package com.taobao.uikit.extend.component.error;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.taobao.uikit.extend.R;
import org.json.JSONException;
import org.json.JSONObject;

public class DefaultErrorFilter extends AbsErrorFilter {
    Context context;
    JSONObject filterRule;

    public DefaultErrorFilter(@NonNull Context context2, @NonNull String str) {
        this.context = context2;
        try {
            this.filterRule = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String filterTitle(@NonNull Error error, CharSequence charSequence) {
        String charSequence2 = TextUtils.isEmpty(charSequence) ? "" : charSequence.toString();
        String codeToTitleKey = codeToTitleKey(error.responseCode, String.valueOf(error.errorCode));
        if (TextUtils.isEmpty(codeToTitleKey)) {
            codeToTitleKey = codeToTitleKey(error.responseCode, error.errorCode);
        }
        String optString = this.filterRule.optString(codeToTitleKey);
        return TextUtils.isEmpty(optString) ? charSequence2 : optString;
    }

    public String filterSubTitle(@NonNull Error error, CharSequence charSequence) {
        String charSequence2 = TextUtils.isEmpty(charSequence) ? "" : charSequence.toString();
        String codeToSubTitleKey = codeToSubTitleKey(error.responseCode, String.valueOf(error.errorCode));
        if (TextUtils.isEmpty(codeToSubTitleKey)) {
            codeToSubTitleKey = codeToSubTitleKey(error.responseCode, error.errorCode);
        }
        String optString = this.filterRule.optString(codeToSubTitleKey);
        return TextUtils.isEmpty(optString) ? charSequence2 : optString;
    }

    public int filterIcon(@NonNull Error error) {
        if (isNetworkError(error.errorCode)) {
            return R.drawable.uik_error_icon;
        }
        if (isLimitError(error.responseCode, error.errorCode)) {
            return R.drawable.uik_limit_error_icon;
        }
        if (isSysError(error.responseCode, error.errorCode)) {
            return R.drawable.uik_sys_error_icon;
        }
        return -1;
    }

    private String codeToTitleKey(int i, @NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (isNetworkError(str)) {
            return "errorview_networkerror_title";
        }
        if (isLimitError(i, str)) {
            return "errorview_limit_error_title";
        }
        return isSysError(i, str) ? "errorview_sys_error_title" : "";
    }

    private String codeToSubTitleKey(int i, @NonNull String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        if (isNetworkError(str)) {
            return "errorview_networkerror_subtitle";
        }
        if (isLimitError(i, str)) {
            return "errorview_limit_error_subtitle";
        }
        return isSysError(i, str) ? "errorview_sys_error_subtitle" : "";
    }
}
