package anet.channel.entity;

import anet.channel.strategy.IConnStrategy;
import com.taobao.weex.el.parse.Operators;

public class ConnInfo {
    private String host;
    public int maxRetryTime = 0;
    public int retryTime = 0;
    private String seq;
    public final IConnStrategy strategy;

    public ConnInfo(String str, String str2, IConnStrategy iConnStrategy) {
        this.strategy = iConnStrategy;
        this.host = str;
        this.seq = str2;
    }

    public String getIp() {
        if (this.strategy != null) {
            return this.strategy.getIp();
        }
        return null;
    }

    public int getPort() {
        if (this.strategy != null) {
            return this.strategy.getPort();
        }
        return 0;
    }

    public ConnType getConnType() {
        if (this.strategy != null) {
            return ConnType.valueOf(this.strategy.getProtocol());
        }
        return ConnType.HTTP;
    }

    public int getConnectionTimeout() {
        if (this.strategy == null || this.strategy.getConnectionTimeout() == 0) {
            return 20000;
        }
        return this.strategy.getConnectionTimeout();
    }

    public int getReadTimeout() {
        if (this.strategy == null || this.strategy.getReadTimeout() == 0) {
            return 20000;
        }
        return this.strategy.getReadTimeout();
    }

    public String getHost() {
        return this.host;
    }

    public int getHeartbeat() {
        if (this.strategy != null) {
            return this.strategy.getHeartbeat();
        }
        return 45000;
    }

    public String getSeq() {
        return this.seq;
    }

    public String toString() {
        return "ConnInfo [ip=" + getIp() + ",port=" + getPort() + ",type=" + getConnType() + ",hb" + getHeartbeat() + Operators.ARRAY_END_STR;
    }
}
