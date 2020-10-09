package com.taobao.weex.analyzer.core.logcat.ats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ATSMessageReceiver {
    static final String ACTION_ATS_MESSAGE = "action_ats_message";
    private InnerReceiver mInnerReceiver;
    private OnReceiveATSMsgListener mListener = null;
    private LocalBroadcastManager mLocalBroadcastManager;

    interface OnReceiveATSMsgListener {
        void onMessageReceived(ATSMessage aTSMessage);
    }

    private ATSMessageReceiver(Context context) {
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(context.getApplicationContext());
    }

    public static ATSMessageReceiver createInstance(@NonNull Context context, @NonNull OnReceiveATSMsgListener onReceiveATSMsgListener) {
        ATSMessageReceiver aTSMessageReceiver = new ATSMessageReceiver(context);
        aTSMessageReceiver.setListener(onReceiveATSMsgListener);
        return aTSMessageReceiver;
    }

    private void setListener(@NonNull OnReceiveATSMsgListener onReceiveATSMsgListener) {
        this.mListener = onReceiveATSMsgListener;
        this.mInnerReceiver = new InnerReceiver(this.mListener);
        this.mLocalBroadcastManager.registerReceiver(this.mInnerReceiver, new IntentFilter(ACTION_ATS_MESSAGE));
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
        private final OnReceiveATSMsgListener listener;

        public InnerReceiver(OnReceiveATSMsgListener onReceiveATSMsgListener) {
            this.listener = onReceiveATSMsgListener;
        }

        public void onReceive(Context context, Intent intent) {
            if (intent != null && ATSMessageReceiver.ACTION_ATS_MESSAGE.equals(intent.getAction())) {
                String stringExtra = intent.getStringExtra("state");
                String stringExtra2 = intent.getStringExtra("message");
                int intExtra = intent.getIntExtra("count", 0);
                if (this.listener != null) {
                    ATSMessage aTSMessage = new ATSMessage(stringExtra, stringExtra2);
                    aTSMessage.count = intExtra;
                    this.listener.onMessageReceived(aTSMessage);
                }
            }
        }
    }

    static class ATSMessage {
        int count;
        String message;
        String state;

        ATSMessage(String str, String str2) {
            this.state = str;
            this.message = str2;
        }

        /* access modifiers changed from: package-private */
        public void setCount(int i) {
            this.count = i;
        }

        public String toString() {
            return "ATSMessage{state='" + this.state + '\'' + ", message='" + this.message + '\'' + '}';
        }
    }
}
