package com.taobao.weex.analyzer.core.debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.taobao.weex.analyzer.core.NetworkEventSender;
import com.taobao.weex.analyzer.core.debug.DebugController;

class MDSDebugMessageReceiver {
    static final String ACTION_DEBUG_MESSAGE = "action_debug_message";
    private InnerReceiver mInnerReceiver;
    private OnReceiveDebugMsgListener mListener;
    private LocalBroadcastManager mLocalBroadcastManager;

    interface OnReceiveDebugMsgListener {
        void onMessageReceived(DebugController.ConnectionInfo connectionInfo);
    }

    private MDSDebugMessageReceiver(@NonNull Context context) {
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    private void setListener(@NonNull OnReceiveDebugMsgListener onReceiveDebugMsgListener) {
        this.mListener = onReceiveDebugMsgListener;
        this.mInnerReceiver = new InnerReceiver(this.mListener);
        this.mLocalBroadcastManager.registerReceiver(this.mInnerReceiver, new IntentFilter(ACTION_DEBUG_MESSAGE));
    }

    static MDSDebugMessageReceiver createInstance(@NonNull Context context, @NonNull OnReceiveDebugMsgListener onReceiveDebugMsgListener) {
        MDSDebugMessageReceiver mDSDebugMessageReceiver = new MDSDebugMessageReceiver(context);
        mDSDebugMessageReceiver.setListener(onReceiveDebugMsgListener);
        return mDSDebugMessageReceiver;
    }

    public void destroy() {
        if (!(this.mInnerReceiver == null || this.mLocalBroadcastManager == null)) {
            this.mLocalBroadcastManager.unregisterReceiver(this.mInnerReceiver);
            this.mInnerReceiver = null;
            this.mLocalBroadcastManager = null;
        }
        this.mListener = null;
    }

    static class InnerReceiver extends BroadcastReceiver {
        private final OnReceiveDebugMsgListener listener;

        public InnerReceiver(OnReceiveDebugMsgListener onReceiveDebugMsgListener) {
            this.listener = onReceiveDebugMsgListener;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && MDSDebugMessageReceiver.ACTION_DEBUG_MESSAGE.equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("message");
                String stringExtra2 = intent.getStringExtra(NetworkEventSender.INTENT_EXTRA_DESC);
                int intExtra = intent.getIntExtra("state", 0);
                if (this.listener != null) {
                    this.listener.onMessageReceived(new DebugController.ConnectionInfo(stringExtra, stringExtra2, intExtra));
                }
            }
        }
    }
}
