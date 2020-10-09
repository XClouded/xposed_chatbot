package anet.channel.strategy;

import android.text.TextUtils;
import anet.channel.strategy.StrategyResultParser;
import java.io.Serializable;

class IPConnStrategy implements IConnStrategy, Serializable {
    public static final int SOURCE_AMDC = 0;
    public static final int SOURCE_CUSTOMIZED = 2;
    public static final int SOURCE_LOCAL_DNS = 1;
    public static final int TYPE_IP_TO_HOST = -1;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_STATIC_BANDWITDH = 0;
    private static final long serialVersionUID = -2492035520806142510L;
    public volatile int cto;
    public volatile int heartbeat;
    public final String ip;
    volatile int ipSource = 1;
    volatile int ipType = 1;
    transient boolean isToRemove;
    public final int port;
    public final ConnProtocol protocol;
    public volatile int retry;
    public volatile int rto;

    static IPConnStrategy create(String str, StrategyResultParser.Aisles aisles) {
        ConnProtocol valueOf = ConnProtocol.valueOf(aisles);
        if (valueOf == null) {
            return null;
        }
        return create(str, aisles.port, valueOf, aisles.cto, aisles.rto, aisles.retry, aisles.heartbeat);
    }

    static IPConnStrategy create(String str, int i, ConnProtocol connProtocol, int i2, int i3, int i4, int i5) {
        if (TextUtils.isEmpty(str) || connProtocol == null || i <= 0) {
            return null;
        }
        return new IPConnStrategy(str, i, connProtocol, i2, i3, i4, i5);
    }

    private IPConnStrategy(String str, int i, ConnProtocol connProtocol, int i2, int i3, int i4, int i5) {
        this.ip = str;
        this.port = i;
        this.protocol = connProtocol;
        this.cto = i2;
        this.rto = i3;
        this.retry = i4;
        this.heartbeat = i5;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append('{');
        sb.append(this.ip);
        if (this.ipType == 0) {
            sb.append("(*)");
        }
        sb.append(' ');
        sb.append(this.port);
        sb.append(' ');
        sb.append(this.protocol);
        sb.append('}');
        return sb.toString();
    }

    public String getIp() {
        return this.ip;
    }

    public int getIpType() {
        return this.ipType;
    }

    public int getIpSource() {
        return this.ipSource;
    }

    public int getPort() {
        return this.port;
    }

    public ConnProtocol getProtocol() {
        return this.protocol;
    }

    public int getConnectionTimeout() {
        return this.cto;
    }

    public int getReadTimeout() {
        return this.rto;
    }

    public int getRetryTimes() {
        return this.retry;
    }

    public int getHeartbeat() {
        return this.heartbeat;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof IPConnStrategy)) {
            return false;
        }
        IPConnStrategy iPConnStrategy = (IPConnStrategy) obj;
        if (this.port != iPConnStrategy.port || !this.ip.equals(iPConnStrategy.ip) || !this.protocol.equals(iPConnStrategy.protocol)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ((((527 + this.ip.hashCode()) * 31) + this.port) * 31) + this.protocol.hashCode();
    }

    public int getUniqueId() {
        return hashCode();
    }
}
