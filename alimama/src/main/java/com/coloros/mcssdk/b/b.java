package com.coloros.mcssdk.b;

import android.content.Context;
import com.coloros.mcssdk.callback.MessageCallback;
import com.coloros.mcssdk.mode.CommandMessage;
import com.coloros.mcssdk.mode.Message;

public final class b implements c {
    public final void a(Context context, Message message, MessageCallback messageCallback) {
        if (message != null && message.getType() == 4105) {
            CommandMessage commandMessage = (CommandMessage) message;
            if (messageCallback != null) {
                messageCallback.processMessage(context, commandMessage);
            }
        }
    }
}
