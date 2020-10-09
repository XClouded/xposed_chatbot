package com.vivo.push.sdk;

import android.content.Context;
import com.vivo.push.LocalAliasTagsManager;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.model.UnvarnishedMessage;
import java.util.List;

public abstract class PushMessageReceiver extends BasePushMessageReceiver implements LocalAliasTagsManager.LocalMessageCallback {
    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
        LocalAliasTagsManager.getInstance(context).onReceiverMsg(unvarnishedMessage, this);
    }

    public boolean onNotificationMessageArrived(Context context, UPSNotificationMessage uPSNotificationMessage) {
        return LocalAliasTagsManager.getInstance(context).onReceiverNotification(uPSNotificationMessage, this);
    }

    public void onDelAlias(Context context, int i, List<String> list, List<String> list2, String str) {
        LocalAliasTagsManager.getInstance(context).onDelAlias(list, str);
    }

    public void onDelTags(Context context, int i, List<String> list, List<String> list2, String str) {
        LocalAliasTagsManager.getInstance(context).onDelTags(list, str);
    }

    public void onSetAlias(Context context, int i, List<String> list, List<String> list2, String str) {
        LocalAliasTagsManager.getInstance(context).onSetAlias(list, str);
    }

    public void onSetTags(Context context, int i, List<String> list, List<String> list2, String str) {
        LocalAliasTagsManager.getInstance(context).onSetTags(list, str);
    }
}
