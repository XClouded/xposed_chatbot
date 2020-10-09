package com.huawei.hms.api;

import android.os.Handler;
import android.os.Message;
import com.huawei.hms.support.log.a;

/* compiled from: BindingFailedResolution */
class b implements Handler.Callback {
    final /* synthetic */ BindingFailedResolution a;

    b(BindingFailedResolution bindingFailedResolution) {
        this.a = bindingFailedResolution;
    }

    public boolean handleMessage(Message message) {
        if (message == null || message.what != 2) {
            return false;
        }
        a.d("BindingFailedResolution", "In connect, bind core try timeout");
        this.a.a(false);
        return true;
    }
}
