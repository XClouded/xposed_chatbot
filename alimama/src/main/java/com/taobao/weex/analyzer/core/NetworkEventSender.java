package com.taobao.weex.analyzer.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.VisibleForTesting;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.Map;

public class NetworkEventSender {
    public static final String ACTION_NETWORK_REPORTER = "action_network_reporter";
    public static final String INTENT_EXTRA_BODY = "body";
    public static final String INTENT_EXTRA_DESC = "desc";
    public static final String INTENT_EXTRA_TITLE = "title";
    public static final String INTENT_EXTRA_TYPE = "type";
    public static final String TYPE_REQUEST = "request";
    public static final String TYPE_RESPONSE = "response";
    private LocalBroadcastManager mSender;

    public NetworkEventSender(Context context) {
        this.mSender = LocalBroadcastManager.getInstance(context);
    }

    @VisibleForTesting
    NetworkEventSender(LocalBroadcastManager localBroadcastManager) {
        this.mSender = localBroadcastManager;
    }

    public void sendMessage(String str, String str2, String str3, String str4, Map<String, String> map) {
        if (this.mSender != null) {
            Intent intent = new Intent(ACTION_NETWORK_REPORTER);
            if (!TextUtils.isEmpty(str)) {
                intent.putExtra("type", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                intent.putExtra("title", str2);
            }
            if (!TextUtils.isEmpty(str3)) {
                intent.putExtra(INTENT_EXTRA_DESC, str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                intent.putExtra("body", str4);
            }
            if (map != null && !map.isEmpty()) {
                Bundle bundle = new Bundle();
                for (Map.Entry next : map.entrySet()) {
                    bundle.putString((String) next.getKey(), (String) next.getValue());
                }
                intent.putExtras(bundle);
            }
            this.mSender.sendBroadcast(intent);
        }
    }
}
