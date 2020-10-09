package anet.channel.heartbeat;

public class HeartbeatManager {
    public static IHeartbeat getDefaultHeartbeat() {
        return new DefaultHeartbeatImpl();
    }

    public static IHeartbeat getDefaultBackgroundAccsHeartbeat() {
        return new DefaultBgAccsHeartbeatImpl();
    }
}
