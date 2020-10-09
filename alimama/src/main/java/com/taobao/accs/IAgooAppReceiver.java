package com.taobao.accs;

import androidx.annotation.Keep;

@Keep
public interface IAgooAppReceiver extends IAppReceiverV1 {
    String getAppkey();
}
