package com.vivo.push.sdk;

import android.content.Context;
import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.model.UnvarnishedMessage;
import java.util.List;

public abstract class OpenClientPushMessageReceiver extends BasePushMessageReceiver {
    public final void onDelAlias(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public final void onDelTags(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public final boolean onNotificationMessageArrived(Context context, UPSNotificationMessage uPSNotificationMessage) {
        return false;
    }

    public final void onSetAlias(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public final void onSetTags(Context context, int i, List<String> list, List<String> list2, String str) {
    }

    public void onTransmissionMessage(Context context, UnvarnishedMessage unvarnishedMessage) {
    }

    public final void onBind(Context context, int i, String str) {
        super.onBind(context, i, str);
    }

    public final void onUnBind(Context context, int i, String str) {
        super.onUnBind(context, i, str);
    }

    public final void onLog(Context context, String str, int i, boolean z) {
        super.onLog(context, str, i, z);
    }

    public final boolean isAllowNet(Context context) {
        return super.isAllowNet(context);
    }

    public final void onListTags(Context context, int i, List<String> list, String str) {
        super.onListTags(context, i, list, str);
    }

    public final void onPublish(Context context, int i, String str) {
        super.onPublish(context, i, str);
    }
}
