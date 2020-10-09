package com.taobao.accs.internal;

import androidx.annotation.Keep;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.IConnectionService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.data.Message;
import com.taobao.accs.net.BaseConnection;
import com.taobao.accs.net.InAppConnection;

@Keep
public class ConnectionServiceImpl implements IConnectionService {
    private static final String TAG = "ConnectionServiceImpl";
    private BaseConnection connection;

    public ConnectionServiceImpl(AccsClientConfig accsClientConfig) {
        AccsClientConfig.setAccsConfig(accsClientConfig.getConfigEnv(), accsClientConfig);
        this.connection = ElectionServiceImpl.getConnection(GlobalClientInfo.mContext, accsClientConfig.getTag(), true, 0);
    }

    public ConnectionServiceImpl(String str) {
        this.connection = new InAppConnection(GlobalClientInfo.mContext, 1, str);
    }

    public void start() {
        this.connection.start();
    }

    public void ping(boolean z, boolean z2) {
        this.connection.ping(z, z2);
    }

    public void close() {
        this.connection.close();
    }

    public boolean cancel(String str) {
        return this.connection.cancel(str);
    }

    public String getTag() {
        return this.connection.getTag();
    }

    public String getConfigTag() {
        return this.connection.mConfigTag;
    }

    public void startChannelService() {
        this.connection.startChannelService();
    }

    public String getAppkey() {
        return this.connection.getAppkey();
    }

    public void onResult(Message message, int i) {
        this.connection.onResult(message, i);
    }

    public String getHost(String str) {
        return this.connection.getHost(str);
    }

    public String getAppSecret() {
        return this.connection.mConfig.getAppSecret();
    }

    public void setTTid(String str) {
        this.connection.mTtid = str;
    }

    public void setAppkey(String str) {
        this.connection.mAppkey = str;
    }

    public void send(Message message, boolean z) {
        this.connection.send(message, z);
    }

    public void updateConfig(AccsClientConfig accsClientConfig) {
        if (this.connection instanceof InAppConnection) {
            ((InAppConnection) this.connection).updateConfig(accsClientConfig);
        }
    }

    public boolean isAppBinded(String str) {
        return this.connection.getClientManager().isAppBinded(str);
    }

    public boolean isAppUnbinded(String str) {
        return this.connection.getClientManager().isAppUnbinded(str);
    }

    public boolean isUserBinded(String str, String str2) {
        return this.connection.getClientManager().isUserBinded(str, str2);
    }

    public void sendMessage(Message message) {
        this.connection.sendMessage(message, true);
    }

    public void setForeBackState(int i) {
        this.connection.setForeBackState(i);
    }

    public String getStoreId() {
        return this.connection.mConfig.getStoreId();
    }
}
