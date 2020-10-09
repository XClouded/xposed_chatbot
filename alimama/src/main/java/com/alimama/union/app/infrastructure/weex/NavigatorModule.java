package com.alimama.union.app.infrastructure.weex;

import android.content.Context;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alimama.moon.R;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.appfram.navigator.WXNavigatorModule;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.utils.WXLogUtils;
import org.json.JSONObject;

public class NavigatorModule extends WXNavigatorModule {
    @JSMethod
    public void push(String str, JSCallback jSCallback) {
        try {
            String optString = new JSONObject(str).optString("url", "");
            if (TextUtils.isEmpty(optString)) {
                return;
            }
            if (WeexUtils.startTargetActivity(this.mWXSDKInstance.getContext(), optString)) {
                jSCallback.invoke("WX_SUCCESS");
            } else {
                jSCallback.invoke("WX_FAILED");
            }
        } catch (Exception e) {
            WXLogUtils.eTag("Navigator", e);
            jSCallback.invoke("WX_FAILED");
        }
    }

    @JSMethod
    public void setNavBarHidden(String str, String str2) {
        String str3 = "WX_FAILED";
        try {
            if (changeVisibilityOfActionBar(this.mWXSDKInstance.getContext(), JSON.parseObject(str).getInteger("hidden").intValue())) {
                str3 = "WX_SUCCESS";
            }
        } catch (JSONException e) {
            WXLogUtils.e("Navigator", WXLogUtils.getStackTrace(e));
        }
        WXBridgeManager.getInstance().callback(this.mWXSDKInstance.getInstanceId(), str2, str3);
    }

    private boolean changeVisibilityOfActionBar(Context context, int i) {
        Toolbar toolbar = (Toolbar) ((AppCompatActivity) context).findViewById(R.id.toolbar);
        switch (i) {
            case 0:
                toolbar.setVisibility(0);
                return true;
            case 1:
                toolbar.setVisibility(8);
                return true;
            default:
                return false;
        }
    }
}
