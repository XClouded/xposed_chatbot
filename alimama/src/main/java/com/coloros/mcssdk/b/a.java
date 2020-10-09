package com.coloros.mcssdk.b;

import android.content.Context;
import com.coloros.mcssdk.callback.MessageCallback;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.Message;

public final class a implements c {
    public final void a(Context context, Message message, MessageCallback messageCallback) {
        if (message != null && message.getType() == 4098) {
            AppMessage appMessage = (AppMessage) message;
            if (messageCallback != null) {
                messageCallback.processMessage(context, appMessage);
            }
        }
    }
}
