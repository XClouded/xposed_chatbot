package com.coloros.mcssdk.b;

import android.content.Context;
import com.coloros.mcssdk.callback.MessageCallback;
import com.coloros.mcssdk.mode.Message;
import com.coloros.mcssdk.mode.SptDataMessage;

public final class d implements c {
    public final void a(Context context, Message message, MessageCallback messageCallback) {
        if (message != null && message.getType() == 4103) {
            SptDataMessage sptDataMessage = (SptDataMessage) message;
            if (messageCallback != null) {
                messageCallback.processMessage(context, sptDataMessage);
            }
        }
    }
}
