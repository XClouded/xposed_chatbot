package com.taobao.login4android.jsbridge;

import android.content.ContextWrapper;
import android.os.Build;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVResult;
import android.taobao.windvane.jsbridge.WindVaneInterface;
import android.text.TextUtils;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.utils.NetworkUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import java.util.HashMap;
import org.json.JSONException;
import org.json.JSONObject;

public class SecurityGuardBridge extends WVApiPlugin {
    private static final String Tag = "Login.SecurityGuardBridge";

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if ("getSecurityGuardEncryptedDevAndEnvInfo".equals(str)) {
            getEncryptedDevAndEnvInfo(wVCallBackContext, str2);
            return true;
        } else if (!"getDeviceInfo".equals(str)) {
            return false;
        } else {
            getInfo(wVCallBackContext, str2);
            return true;
        }
    }

    @WindVaneInterface
    private void getInfo(WVCallBackContext wVCallBackContext, String str) {
        String deviceId = DataProviderFactory.getDataProvider().getDeviceId();
        String ttid = DataProviderFactory.getDataProvider().getTTID();
        String networkType = NetworkUtil.getNetworkType(this.mContext);
        HashMap hashMap = new HashMap();
        hashMap.put("deviceID", deviceId);
        hashMap.put("ttid", ttid);
        hashMap.put("network", networkType);
        hashMap.put("sdkversion", "" + Build.VERSION.SDK_INT);
        wVCallBackContext.success(JSON.toJSONString(hashMap));
    }

    private void getEncryptedDevAndEnvInfo(WVCallBackContext wVCallBackContext, String str) {
        String str2;
        try {
            str2 = new JSONObject(str).optString("key");
        } catch (JSONException e) {
            TLogAdapter.e(Tag, "json phrase error: " + e.getMessage());
            str2 = "";
        }
        if (TextUtils.isEmpty(str2)) {
            wVCallBackContext.error(WVResult.RET_PARAM_ERR);
            return;
        }
        try {
            String encryptedDevAndEnvInfo = SecurityGuardManager.getInstance(new ContextWrapper(this.mContext)).getDataCollectionComp().getEncryptedDevAndEnvInfo(16, str2);
            WVResult wVResult = new WVResult();
            wVResult.addData("encryptedInfo", encryptedDevAndEnvInfo);
            wVCallBackContext.success(wVResult);
        } catch (Exception e2) {
            TLogAdapter.e(Tag, "exec fail : " + e2.getMessage());
            wVCallBackContext.error();
        }
    }
}
