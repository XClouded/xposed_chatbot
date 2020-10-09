package com.taobao.weex.analyzer.core.inspector.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import java.util.HashMap;
import java.util.Map;

public class NetworkEventInspector {
    private CoreMessageReceiver mCoreMessageReceiver;
    private OnMessageReceivedListener mListener;
    private LocalBroadcastManager mLocalBroadcastManager;

    public interface OnMessageReceivedListener {
        void onMessageReceived(MessageBean messageBean);
    }

    private NetworkEventInspector(@NonNull Context context) {
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private NetworkEventInspector(@NonNull LocalBroadcastManager localBroadcastManager) {
        this.mLocalBroadcastManager = localBroadcastManager;
    }

    private void setOnMessageReceivedListener(@NonNull OnMessageReceivedListener onMessageReceivedListener) {
        this.mListener = onMessageReceivedListener;
        this.mCoreMessageReceiver = new CoreMessageReceiver(this.mListener);
        this.mLocalBroadcastManager.registerReceiver(this.mCoreMessageReceiver, new IntentFilter(NetworkEventSender.ACTION_NETWORK_REPORTER));
    }

    public static NetworkEventInspector createInstance(@NonNull Context context, @NonNull OnMessageReceivedListener onMessageReceivedListener) {
        NetworkEventInspector networkEventInspector = new NetworkEventInspector(context);
        networkEventInspector.setOnMessageReceivedListener(onMessageReceivedListener);
        return networkEventInspector;
    }

    @VisibleForTesting
    @NonNull
    static NetworkEventInspector createInstance(@NonNull LocalBroadcastManager localBroadcastManager, @NonNull OnMessageReceivedListener onMessageReceivedListener) {
        NetworkEventInspector networkEventInspector = new NetworkEventInspector(localBroadcastManager);
        networkEventInspector.setOnMessageReceivedListener(onMessageReceivedListener);
        return networkEventInspector;
    }

    public void destroy() {
        if (!(this.mCoreMessageReceiver == null || this.mLocalBroadcastManager == null)) {
            this.mLocalBroadcastManager.unregisterReceiver(this.mCoreMessageReceiver);
            this.mCoreMessageReceiver = null;
            this.mLocalBroadcastManager = null;
        }
        this.mListener = null;
    }

    static class CoreMessageReceiver extends BroadcastReceiver {
        OnMessageReceivedListener listener;

        CoreMessageReceiver(@NonNull OnMessageReceivedListener onMessageReceivedListener) {
            this.listener = onMessageReceivedListener;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(NetworkEventSender.ACTION_NETWORK_REPORTER)) {
                String stringExtra = intent.getStringExtra("type");
                String stringExtra2 = intent.getStringExtra("title");
                String stringExtra3 = intent.getStringExtra(NetworkEventSender.INTENT_EXTRA_DESC);
                String stringExtra4 = intent.getStringExtra("body");
                Bundle extras = intent.getExtras();
                HashMap hashMap = null;
                if (extras != null) {
                    hashMap = new HashMap();
                    for (String str : extras.keySet()) {
                        if (!"type".equals(str) && !NetworkEventSender.INTENT_EXTRA_DESC.equals(str) && !"title".equals(str) && !"body".equals(str)) {
                            hashMap.put(str, extras.getString(str));
                        }
                    }
                }
                MessageBean messageBean = new MessageBean(stringExtra, stringExtra2, stringExtra3, hashMap, stringExtra4);
                try {
                    if (!TextUtils.isEmpty(messageBean.body)) {
                        messageBean.content = JSON.parseObject(messageBean.body.trim());
                    }
                } catch (Exception unused) {
                }
                if (this.listener != null) {
                    this.listener.onMessageReceived(messageBean);
                }
            }
        }
    }

    public static class MessageBean {
        @JSONField(serialize = false)
        public String body;
        public JSONObject content;
        public String desc;
        @JSONField(serialize = false)
        public Map<String, String> extendProps;
        public String title;
        public String type;

        public MessageBean(String str, String str2, String str3, Map<String, String> map, String str4) {
            this.type = str;
            this.title = str2;
            this.desc = str3;
            this.extendProps = map;
            this.body = str4;
        }
    }
}
