package anet.channel.statist;

import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnProtocol;
import anet.channel.strategy.StrategyResultParser;

@Monitor(module = "networkPrefer", monitorPoint = "horseRace")
public class HorseRaceStat extends StatObject {
    @Dimension
    public volatile String bssid = NetworkStatusHelper.getWifiBSSID();
    @Dimension
    public volatile int connErrorCode;
    @Dimension
    public volatile int connRet = 0;
    @Measure
    public volatile long connTime;
    @Dimension
    public volatile String host;
    @Dimension
    public volatile String ip;
    @Dimension
    public volatile String localIP;
    @Dimension
    public volatile String mnc = NetworkStatusHelper.getSimOp();
    @Dimension
    public volatile String nettype = NetworkStatusHelper.getNetworkSubType();
    @Dimension
    public volatile String path;
    @Dimension
    public volatile int pingSuccessCount;
    @Dimension
    public volatile int pingTimeoutCount;
    @Dimension
    public volatile int port;
    @Dimension
    public volatile String protocol;
    @Dimension
    public volatile int reqErrorCode;
    @Dimension
    public volatile int reqRet = 0;
    @Measure
    public volatile long reqTime;

    public HorseRaceStat(String str, StrategyResultParser.Strategy strategy) {
        this.host = str;
        this.ip = strategy.ip;
        this.port = strategy.aisles.port;
        this.protocol = ConnProtocol.valueOf(strategy.aisles).name;
        this.path = strategy.path;
    }
}
