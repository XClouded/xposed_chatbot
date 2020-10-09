package com.taobao.accs;

import androidx.annotation.Keep;
import com.taobao.accs.base.AccsDataListener;
import com.taobao.aipc.annotation.type.ClassName;

@ClassName("com.taobao.accs.client.GlobalClientInfo")
@Keep
public interface IGlobalClientInfoService {
    void registerRemoteListener(String str, AccsDataListener accsDataListener);

    void registerRemoteService(String str, String str2);

    void setRemoteAgooAppReceiver(IAgooAppReceiver iAgooAppReceiver);

    void setRemoteAppReceiver(String str, IAppReceiver iAppReceiver);

    void unregisterRemoteListener(String str);

    void unregisterRemoteService(String str);
}
