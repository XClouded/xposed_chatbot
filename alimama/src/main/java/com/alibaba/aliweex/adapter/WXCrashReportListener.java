package com.alibaba.aliweex.adapter;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WXCrashReportListener implements IUTCrashCaughtListener {
    private String mCrashUrl = "";
    private String mCurInstanceId = "";
    private String mExceedLimitComponentInfo = "";

    public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(this.mCrashUrl)) {
            hashMap.put(WXEnvironment.WEEX_CURRENT_KEY, this.mCrashUrl);
        }
        setCurCrashComponentExceedGPULimit();
        if (!TextUtils.isEmpty(this.mExceedLimitComponentInfo)) {
            hashMap.put("wx_exceed_limit_component_info", this.mExceedLimitComponentInfo);
        }
        return hashMap;
    }

    private void setCurCrashComponentExceedGPULimit() {
        List<JSONObject> componentsExceedGPULimit;
        if (!TextUtils.isEmpty(this.mCurInstanceId) && (componentsExceedGPULimit = WXSDKManager.getInstance().getSDKInstance(this.mCurInstanceId).getComponentsExceedGPULimit()) != null && !componentsExceedGPULimit.isEmpty()) {
            this.mExceedLimitComponentInfo = componentsExceedGPULimit.toString();
        }
    }

    public void setCurCrashUrl(String str) {
        this.mCrashUrl = str;
    }

    public void setCurInstanceId(String str) {
        this.mCurInstanceId = str;
    }
}
