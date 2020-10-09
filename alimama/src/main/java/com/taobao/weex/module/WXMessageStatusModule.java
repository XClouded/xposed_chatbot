package com.taobao.weex.module;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSONObject;
import com.taobao.accs.common.Constants;
import com.taobao.uikit.actionbar.R;
import com.taobao.uikit.actionbar.TBPublicMenu;
import com.taobao.uikit.actionbar.TBPublicMenuItem;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.common.WXModule;
import java.util.HashMap;
import org.android.agoo.vivo.VivoBadgeReceiver;

public class WXMessageStatusModule extends WXModule implements Destroyable {
    /* access modifiers changed from: private */
    public String ACTION_MPM_MESSAGE_BOX_UNREAD = VivoBadgeReceiver.ACTION_MPM_MESSAGE_BOX_UNREAD;
    private BroadcastReceiver mMessageChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), WXMessageStatusModule.this.ACTION_MPM_MESSAGE_BOX_UNREAD) && WXBridgeManager.getInstance() != null) {
                WXBridgeManager.getInstance().post(new Runnable() {
                    public void run() {
                        if (WXMessageStatusModule.this.mWXSDKInstance != null) {
                            WXMessageStatusModule.this.mWXSDKInstance.fireGlobalEventCallback("messageCountChanged", new HashMap());
                        }
                    }
                });
            }
        }
    };

    public WXMessageStatusModule() {
        LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).registerReceiver(this.mMessageChangeReceiver, new IntentFilter(this.ACTION_MPM_MESSAGE_BOX_UNREAD));
    }

    @JSMethod(uiThread = false)
    public void getOverflowStatus(JSCallback jSCallback) {
        TBPublicMenuItem publicMenu = TBPublicMenu.getPublicMenu(R.id.uik_menu_wangxin);
        if (publicMenu != null) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(Constants.KEY_MODE, (Object) publicMenu.getMessageMode());
            jSONObject.put("value", (Object) publicMenu.getMessage());
            jSCallback.invoke(jSONObject);
        }
    }

    public void destroy() {
        LocalBroadcastManager.getInstance(WXEnvironment.getApplication()).unregisterReceiver(this.mMessageChangeReceiver);
    }
}
