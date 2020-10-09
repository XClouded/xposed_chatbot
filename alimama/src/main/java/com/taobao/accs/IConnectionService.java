package com.taobao.accs;

import androidx.annotation.Keep;
import com.taobao.accs.data.Message;
import com.taobao.aipc.annotation.method.OneWay;
import com.taobao.aipc.annotation.type.ClassName;

@ClassName("com.taobao.accs.internal.ConnectionServiceImpl")
@Keep
public interface IConnectionService {
    boolean cancel(String str);

    void close();

    String getAppSecret();

    String getAppkey();

    String getConfigTag();

    String getHost(String str);

    String getStoreId();

    String getTag();

    boolean isAppBinded(String str);

    boolean isAppUnbinded(String str);

    boolean isUserBinded(String str, String str2);

    void onResult(Message message, int i);

    @OneWay
    void ping(boolean z, boolean z2);

    @OneWay
    void send(Message message, boolean z);

    @OneWay
    void sendMessage(Message message);

    void setAppkey(String str);

    void setForeBackState(int i);

    void setTTid(String str);

    @OneWay
    void start();

    @OneWay
    void startChannelService();

    void updateConfig(AccsClientConfig accsClientConfig);
}
