package com.alimama.moon.push;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.model.PushMsg;
import com.alimama.moon.model.PushMsgExts;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.taobao.agoo.BaseNotifyClickActivity;
import com.taobao.agoo.TaobaoRegister;
import org.android.agoo.common.AgooConstants;

public class ThirdPushRedirectActivity extends BaseNotifyClickActivity {
    private static final String TAG = "ThirdPushRedirectActivity";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MoonComponentManager.getInstance().getPageRouter().onCreate(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        MoonComponentManager.getInstance().getPageRouter().onPause(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        MoonComponentManager.getInstance().getPageRouter().onResume(this);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        MoonComponentManager.getInstance().getPageRouter().onStop(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        MoonComponentManager.getInstance().getPageRouter().onDestroy(this);
    }

    public void onMessage(Intent intent) {
        try {
            String stringExtra = intent.getStringExtra("body");
            String stringExtra2 = intent.getStringExtra("id");
            String stringExtra3 = intent.getStringExtra(AgooConstants.MESSAGE_TASK_ID);
            PushMsg pushMsg = (PushMsg) JSON.parseObject(stringExtra, PushMsg.class);
            pushMsg.setMessageId(stringExtra2);
            String stringExtra4 = intent.getStringExtra(AgooConstants.MESSAGE_EXT);
            pushMsg.setTaskId(stringExtra3);
            if (pushMsg == null) {
                NewMonitorLogger.Agoo.thirdPushNotShow(TAG, "pushMsg is null");
                return;
            }
            PushMsgExts exts = pushMsg.getExts();
            if (exts == null) {
                NewMonitorLogger.Agoo.thirdPushNotShow(TAG, pushMsg.toString());
                return;
            }
            String url = exts.getUrl();
            TaobaoRegister.clickMessage(this, stringExtra2, stringExtra4);
            if (TextUtils.isEmpty(url)) {
                url = "unionApp://ws-home";
            }
            MoonComponentManager.getInstance().getPageRouter().gotoPage(url);
            finish();
        } catch (Exception e) {
            NewMonitorLogger.Agoo.thirdPushCrash(TAG, e.toString());
        }
    }
}
