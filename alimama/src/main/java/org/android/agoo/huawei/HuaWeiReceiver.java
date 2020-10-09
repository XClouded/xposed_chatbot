package org.android.agoo.huawei;

import android.content.Context;
import android.os.Bundle;
import com.huawei.hms.support.api.push.PushReceiver;

public class HuaWeiReceiver extends PushReceiver {
    public void onEvent(Context context, PushReceiver.Event event, Bundle bundle) {
    }

    public boolean onPushMsg(Context context, byte[] bArr, Bundle bundle) {
        return true;
    }

    public void onPushState(Context context, boolean z) {
    }

    public void onToken(Context context, String str, Bundle bundle) {
    }
}
