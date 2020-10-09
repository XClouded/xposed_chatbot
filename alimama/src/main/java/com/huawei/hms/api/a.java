package com.huawei.hms.api;

import android.os.Handler;
import android.os.Message;

/* compiled from: BindingFailedResolution */
class a implements Handler.Callback {
    final /* synthetic */ BindingFailedResolution a;

    a(BindingFailedResolution bindingFailedResolution) {
        this.a = bindingFailedResolution;
    }

    public boolean handleMessage(Message message) {
        if (message == null || message.what != 3) {
            return false;
        }
        this.a.a(8);
        return true;
    }
}
